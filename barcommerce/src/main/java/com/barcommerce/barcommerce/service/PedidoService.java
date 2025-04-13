package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido criarPedido(Pedido pedido) {
        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(novoStatus);
            return pedidoRepository.save(pedido);
        }).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public void cancelarPedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}