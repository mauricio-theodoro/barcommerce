package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.MercadoPagoWebhookDTO;
import com.barcommerce.barcommerce.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para receber notificações de pagamento (webhooks).
 * Implementado com DTO fortemente tipado para maior robustez.
 */
@RestController
@RequestMapping("/api/webhooks/mercadopago")
@Validated
public class WebhookController {

    private final PagamentoService pagamentoService;

    public WebhookController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    /**
     * Endpoint para notificações do Mercado Pago.
     * Exemplo de payload:
     * {
     *   "data": { "id": 1234567890 },
     *   "type": "payment"
     * }
     *
     * @param dto DTO contendo estrutura do payload e ID da transação
     */
    @PostMapping("/mercadopago")
    @Operation(summary = "Recebe notificação de pagamento do Mercado Pago")
    public ResponseEntity<Void> receberNotificacao(
            @Valid @RequestBody MercadoPagoWebhookDTO dto) {

        // Extrai o ID da transação como Long
        Long pagamentoId = dto.getData().getId();

        // Delegação à camada de serviço para processar esse ID
        pagamentoService.confirmarPorWebhook(pagamentoId);

        // Retorna 200 OK para indicar recebimento bem‑sucedido
        return ResponseEntity.ok().build();
    }

    /**
     * Recebe notificação de pagamento (PIX/cartão capturado).
     */
    /**
     * Notificação específica de pagamento concluído.
     */
    @PostMapping("/pagamento")
    @Operation(summary = "Notificação de pagamento concluído")
    public ResponseEntity<Void> pagamentoRecebido(
            @Valid @RequestBody MercadoPagoWebhookDTO payload) {

        Long pagamentoId = payload.getData().getId();
        pagamentoService.confirmarPorWebhook(pagamentoId);
        return ResponseEntity.ok().build();
    }


    /**
     * Recebe notificação de reembolso.
     */
    @PostMapping("/reembolso")
    @Operation(summary = "Notificação de reembolso")
    public ResponseEntity<Void> reembolsoRecebido(@Valid @RequestBody MercadoPagoWebhookDTO payload) {
        pagamentoService.confirmarReembolsoPorWebhook(payload.getData().getId());
        return ResponseEntity.ok().build();
    }
}