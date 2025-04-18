package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.model.*;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço responsável pelas operações relacionadas a pedidos.
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final MesaService mesaService;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteService clienteService,
                         MesaService mesaService,
                         ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.mesaService = mesaService;
        this.produtoService = produtoService;
    }

    /**
     * Lista todos os pedidos cadastrados.
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * Cria um novo pedido, vinculando cliente e mesa válidos.
     * Validações de integridade são aplicadas antes do salvamento.
     *
     * @param
     * @return Pedido persistido
     */
    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        Long clienteId = pedido.getCliente().getId();
        Cliente cliente = clienteService
                .buscarPorId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Long mesaId = pedido.getMesa().getId();
        Mesa mesa = mesaService
                .buscarPorId(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada"));

        pedido.setCliente(cliente);
        pedido.setMesa(mesa);

        if (pedido.getStatus() == null) {
            pedido.setStatus(StatusPedido.ABERTO);
        }

        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedido(pedido);

                Produto produto = produtoService
                        .buscarPorId(item.getProduto().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

                item.setProduto(produto);
                item.setQuantidade(item.getQuantidade());
                item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }

        pedido.calcularTotal(); // Atualiza total do pedido baseado nos itens

        return pedidoRepository.save(pedido);
    }


    /**
     * Atualiza o status de um pedido existente.
     *
     * @param id ID do pedido
     * @param novoStatus Novo status
     * @return Pedido atualizado
     */
    @Transactional
    public Pedido atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(novoStatus);
            return pedidoRepository.save(pedido);
        }).orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    /**
     * Cancela (remove) um pedido existente.
     *
     * @param id ID do pedido a ser cancelado
     */
    @Transactional
    public void cancelarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    /**
     * Marca um pedido como FECHADO.
     *
     * @param id ID do pedido
     * @return Pedido com status atualizado
     */
    @Transactional
    public Pedido fecharPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.FECHADO);
        return pedidoRepository.save(pedido);
    }
}
