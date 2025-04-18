package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.PedidoDTO;
import com.barcommerce.barcommerce.mapper.PedidoMapper;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> criar(@Valid @RequestBody PedidoDTO dto) {
        Pedido pedido = PedidoMapper.toEntity(dto);
        Long clienteId = dto.getClienteId(); // garante que o ID do cliente está vindo no DTO
        Pedido salvo = pedidoService.criarPedido(pedido, clienteId);
        return ResponseEntity.ok(PedidoMapper.toDTO(salvo));
    }

    /** Fechar → FECHADO */
    @PutMapping("/{id}/fechar")
    public PedidoDTO fechar(@PathVariable Long id) {
        var p = pedidoService.fecharPedido(id);
        return PedidoMapper.toDTO(p);
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        return pedidoService.atualizarStatusPedido(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
