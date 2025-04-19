package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.PedidoDTO;
import com.barcommerce.barcommerce.enums.StatusPagamento;
import com.barcommerce.barcommerce.mapper.PedidoMapper;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Lista todos os pedidos")
    @PreAuthorize("hasAnyRole('GERENTE','FUNCIONARIO')")
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        var list = pedidoService.listarTodos()
                .stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lista pedidos por status de pagamento")
    @GetMapping("/por-status-pagamento")
    public ResponseEntity<List<PedidoDTO>> listarPorStatusPagamento(
            @RequestParam StatusPagamento status) {
        var list = pedidoService.listarPorPagamento(status)
                .stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Cria um novo pedido")
    @PreAuthorize("hasAnyRole('GERENTE','FUNCIONARIO')")
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoDTO dto) {
        try {
            Pedido pedido = PedidoMapper.toEntity(dto);
            Pedido salvo = pedidoService.criarPedido(pedido);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(PedidoMapper.toDTO(salvo));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar pedido: " + e.getMessage());
        }
    }


    /** Fechar → FECHADO */
    @PutMapping("/{id}/fechar")
    public PedidoDTO fechar(@PathVariable Long id) {
        var p = pedidoService.fecharPedido(id);
        return PedidoMapper.toDTO(p);
    }



    @Operation(summary = "Atualiza status do pedido")
    @PreAuthorize("hasAnyRole('GERENTE','FUNCIONARIO')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {

        Pedido p = pedidoService.atualizarStatusPedido(id, status);
        return ResponseEntity.ok(PedidoMapper.toDTO(p));
    }


    @Operation(summary = "Cancela um pedido")
    @PreAuthorize("hasAnyRole('GERENTE','FUNCIONARIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
