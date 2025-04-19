package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.CartaoResponseDTO;
import com.barcommerce.barcommerce.dto.ConfirmacaoPagamentoDTO;
import com.barcommerce.barcommerce.dto.PagamentoCartaoDTO;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.service.MesaService;
import com.barcommerce.barcommerce.service.PagamentoService;
import com.barcommerce.barcommerce.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PedidoService pedidoService;
    private final MesaService mesaService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService, PedidoService pedidoService, MesaService mesaService) {
        this.pagamentoService = pagamentoService;
        this.pedidoService = pedidoService;
        this.mesaService =mesaService;
    }

    @PostMapping("/{pedidoId}/pix")
    public ResponseEntity<String> gerarQrCodePix(@PathVariable Long pedidoId) {
        // busca o pedido e lança 404 se não existir
        Pedido pedido = pedidoService.buscarPorId(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        // chama serviço para gerar o QR
        String qrCodeBase64 = pagamentoService.criarPagamentoPix(pedido);
        return ResponseEntity.ok(qrCodeBase64);
    }

    /**
     * Captura pagamento via cartão de crédito.
     */
    @PostMapping("/cartao")
    public ResponseEntity<CartaoResponseDTO> pagarComCartao(
            @Valid @RequestBody PagamentoCartaoDTO dto) {

        CartaoResponseDTO resp = pagamentoService.processarPagamentoCartao(dto);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarPagamento(
            @Valid @RequestBody ConfirmacaoPagamentoDTO dto) {

        // 1) Carrega e atualiza status do pedido
        Pedido pedido = pedidoService
                .buscarPorId(dto.getPedidoId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        pagamentoService.registrarConfirmacao(pedido, dto.getMetodoPagamento(), dto.getIdTransacao());

        // 2) Libera a mesa (só o anfitrião pode chamar)
        mesaService.liberarMesa(pedido.getMesa().getId(), dto.getDeviceId());

        return ResponseEntity.ok().build();
    }
}
