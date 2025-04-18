package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.model.Cliente;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;


    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido criarPedido(Pedido pedido, Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        pedido.setCliente(cliente);
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

    /**
     * Marca um pedido como FECHADO.
     */
    public Pedido fecharPedido(Long id) {
        Pedido p = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
        p.setStatus(StatusPedido.FECHADO);
        return pedidoRepository.save(p);
    }
}