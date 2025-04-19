package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.CartaoResponseDTO;
import com.barcommerce.barcommerce.dto.MercadoPagoWebhookDTO;
import com.barcommerce.barcommerce.dto.PagamentoCartaoDTO;
import com.barcommerce.barcommerce.enums.MetodoPagamento;
import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.model.Pedido;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class PagamentoService {

    // Constantes configuráveis (idealmente em application.properties)
    @Value("${mercadopago.access.token}") // Injeta do YML
    private String mercadoPagoToken;

    @Value("${mercadopago.email.padrao}") // Adicione no YML
    private String emailPadrao;

    private  PaymentClient paymentClient;
    private final PedidoService pedidoService;

    public PagamentoService(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Remova as constantes estáticas
    private static final String METODO_PAGAMENTO_PIX = "pix";
    private static final int CASAS_DECIMAIS = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO = RoundingMode.HALF_UP;


    @PostConstruct
    public void init() {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoToken);
            this.paymentClient = new PaymentClient();
        } catch (Exception e) {
            throw new IllegalStateException("Falha na configuração do Mercado Pago: " + e.getMessage(), e);
        }
    }

    public String criarPagamentoPix(Pedido pedido) {
        validarPedido(pedido);
        BigDecimal total = obterTotalValidado(pedido);

        try {
            PaymentCreateRequest request = PaymentCreateRequest.builder()
                    .transactionAmount(converterParaBigDecimalSeguro(total))
                    .paymentMethodId(METODO_PAGAMENTO_PIX)
                    .description("Pedido #" + pedido.getId())
                    .payer(PaymentPayerRequest.builder()
                            .email(emailPadrao)
                            .build())
                    .build();

            Payment payment = paymentClient.create(request);
            return extrairQrCode(payment);

        } catch (MPApiException apiEx) {
            String errorDetails = apiEx.getApiResponse().getContent();
            throw new RuntimeException("Falha na API do Mercado Pago. Detalhes: " + errorDetails, apiEx);

        } catch (MPException ex) {
            throw new RuntimeException("Erro no SDK Mercado Pago: " + ex.getMessage(), ex);

        } catch (Exception ex) {
            throw new RuntimeException("Erro inesperado: " + ex.getMessage(), ex);
        }
    }

    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        if (pedido.getId() == null) {
            throw new IllegalArgumentException("ID do pedido é obrigatório");
        }
        if (pedido.getCliente() == null || pedido.getMesa() == null) {
            throw new IllegalArgumentException("Cliente e Mesa devem estar associados ao pedido");
        }
    }

    private BigDecimal obterTotalValidado(Pedido pedido) {
        return Optional.ofNullable(pedido.getTotal())
                .map(total -> total.setScale(CASAS_DECIMAIS, MODO_ARREDONDAMENTO))
                .orElseThrow(() -> new IllegalArgumentException("Valor total do pedido é obrigatório"));
    }

    private BigDecimal converterParaBigDecimalSeguro(BigDecimal valor) {
        try {
            return valor.setScale(CASAS_DECIMAIS, MODO_ARREDONDAMENTO);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Valor inválido: " + valor, e);
        }
    }

    private String extrairQrCode(Payment payment) {
        if (payment == null
                || payment.getPointOfInteraction() == null
                || payment.getPointOfInteraction().getTransactionData() == null) {
            throw new IllegalStateException("Resposta inválida do Mercado Pago");
        }

        String qrCode = payment.getPointOfInteraction()
                .getTransactionData()
                .getQrCodeBase64();

        if (qrCode == null || qrCode.isBlank()) {
            throw new IllegalStateException("QR Code não gerado pelo Mercado Pago");
        }

        return qrCode;
    }

    /**
     * Cria e captura o pagamento via cartão de crédito.
     *
     * @param dto Dados de pagamento vindos do front‑end (token, parcelas, identificação, etc)
     * @return CartaoResponseDTO contendo ID da transação e status final
     */
    public CartaoResponseDTO processarPagamentoCartao(PagamentoCartaoDTO dto) {
        // 1) Carrega o pedido e valida existência
        Pedido pedido = pedidoService.buscarPorId(dto.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // 2) Prepara valor com precisão
        BigDecimal valor = Optional.ofNullable(dto.getValor())
                .map(v -> v.setScale(CASAS_DECIMAIS, MODO_ARREDONDAMENTO))
                .orElseThrow(() -> new IllegalArgumentException("Valor do pagamento não informado"));

        try {
            // 3) Monta requisição ao Mercado Pago
            PaymentCreateRequest req = PaymentCreateRequest.builder()
                    .transactionAmount(valor)                           // <— usa BigDecimal
                    .paymentMethodId(dto.getMetodoPagamento())          // ex: "visa"
                    .token(dto.getToken())
                    .installments(dto.getParcelas())
                    .description(dto.getDescricao())
                    .payer(PaymentPayerRequest.builder()
                            .email(dto.getEmail())
                            .identification(IdentificationRequest.builder()
                                    .type(dto.getTipoIdentificacao())     // "CPF" ou "CNPJ"
                                    .number(dto.getIdentificacao())
                                    .build())
                            .build())
                    .build();

            // 4) Executa a chamada ao gateway
            Payment mp = paymentClient.create(req);

            // 5) Atualiza o pedido com as informações de pagamento
            pedido.setMetodoPagamento(MetodoPagamento.CARTAO_CREDITO);
            pedido.setStatusPagamento(StatusPagamento.valueOf(mp.getStatus().toUpperCase()));
            pedido.setIdTransacao(mp.getId().toString());

            // 6) Persiste somente as alterações de pagamento
            pedidoService.atualizarPagamento(pedido);

            // 7) Retorna ao front‑end apenas o necessário
            return new CartaoResponseDTO(
                    pedido.getIdTransacao(),
                    pedido.getStatusPagamento()
            );

        } catch (MPApiException apiEx) {
            throw new RuntimeException("Falha na API Mercado Pago: " +
                    apiEx.getApiResponse().getContent(), apiEx);

        } catch (MPException sdkEx) {
            throw new RuntimeException("SDK Mercado Pago: " + sdkEx.getMessage(), sdkEx);
        }
    }
    public void registrarConfirmacao(Pedido pedido,
                                     MetodoPagamento metodo,
                                     String idTransacao) {
        // atualiza dados de pagamento no pedido
        pedido.setMetodoPagamento(metodo);
        pedido.setStatusPagamento(StatusPagamento.APROVADO);
        pedido.setIdTransacao(idTransacao);
        pedidoService.atualizarPagamento(pedido);
    }

    /**
     * Trata notificação via webhook: consulta o gateway e atualiza status completo.
     */
    public void confirmarPorWebhook(Long pagamentoId) {
        try {
            // 1) Recupera dados do pagamento diretamente do Mercado Pago
            //    OBS: não existe PaymentGetRequest na SDK; get() aceita diretamente o id
            Payment mp = paymentClient.get(pagamentoId);

            if (mp == null) throw new IllegalStateException("Pagamento não encontrado no Mercado Pago: " + pagamentoId);


            // 2) Mapeia o status retornado pela API para o nosso enum interno
            StatusPagamento status =
                    switch (mp.getStatus()) {
                        case "approved"   -> StatusPagamento.APROVADO;
                        case "refunded"   -> StatusPagamento.REEMBOLSADO;
                        case "pending", "in_process" -> StatusPagamento.PENDENTE;
                        default           -> StatusPagamento.RECUSADO;
                    };

            // 3) Busca o pedido vinculando pelo idTransacao que gravamos
            Pedido pedido = pedidoService.buscarPorIdTransacao(mp.getId().toString())
                    .orElseThrow(() -> new IllegalStateException(
                            "Pedido não encontrado para transação: " + mp.getId()));

            // 4) Atualiza somente os campos de pagamento
            pedido.setStatusPagamento(status);
            pedido.setMetodoPagamento(
                    MetodoPagamento.valueOf(mp.getPaymentMethodId().toUpperCase())
            );

            // 5) Persiste a atualização
            pedidoService.atualizarPagamento(pedido);

        } catch (MPApiException apiEx) {
            throw new RuntimeException(
                    "Erro ao consultar pagamento no Mercado Pago: " +
                            apiEx.getApiResponse().getContent(), apiEx);
        } catch (MPException mpEx) {
            throw new RuntimeException("Erro no SDK Mercado Pago: " + mpEx.getMessage(), mpEx);
        }
    }
    /**
     * Após webhook de reembolso, atualiza o pedido para StatusPagamento.REEMBOLSADO.
     */
    public void confirmarReembolsoPorWebhook(Long pagamentoId) {
        try {
            Payment mp = paymentClient.get(pagamentoId);
            if (mp == null || !"refunded".equalsIgnoreCase(mp.getStatus())) {
                throw new IllegalStateException("Pagamento não está em estado " +
                        "de reembolso: " + Optional.ofNullable(mp).map(Payment::getStatus).orElse("nulo"));
            }
            String idTransacao = mp.getId().toString();
            Pedido pedido = pedidoService.buscarPorIdTransacao(idTransacao)
                    .orElseThrow(() -> new IllegalStateException(
                            "Pedido não encontrado para transação=" + idTransacao));
            pedido.setStatusPagamento(StatusPagamento.REEMBOLSADO);
            pedidoService.atualizarPagamento(pedido);
        } catch (MPApiException | MPException e) {
            throw new RuntimeException("Erro ao processar reembolso webhook: " + e.getMessage(), e);
        }
    }
}