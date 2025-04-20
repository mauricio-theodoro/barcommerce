package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.dto.RecomendacaoDTO;
import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.model.ItemPedido;
import com.barcommerce.barcommerce.model.Pedido;
import com.barcommerce.barcommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Gera recomendações com base no histórico de pedidos entregues de um cliente.
 */
@Service
public class RecomendacaoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRecomendacaoMlService clienteRecomendacaoMlService;

    public RecomendacaoService(PedidoRepository pedidoRepository,
                               ClienteRecomendacaoMlService clienteRecomendacaoMlService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRecomendacaoMlService = clienteRecomendacaoMlService;
    }

    /**
     * Retorna até `limit` itens mais consumidos pelo cliente.
     * @param clienteId ID do cliente
     * @param limit número máximo de recomendações
     */
    public List<RecomendacaoDTO> getRecommendationsForCliente(Long clienteId, int limit) {


        // 1) Tenta obter IDs via ML
        List<Long> mlIds = clienteRecomendacaoMlService.buscarIdsProdutosRecomendados(clienteId, limit);
        if (mlIds != null && !mlIds.isEmpty()) {
            // Constrói DTOs mínimos (freq não disponível no ML, usamos zero)
            return mlIds.stream()
                    .map(id -> new RecomendacaoDTO(id, null, 0))
                    .collect(Collectors.toList());
        }

        // 1) Buscar apenas pedidos entregues
        List<Pedido> pedidos = pedidoRepository
                .findByClienteIdAndStatus(clienteId, StatusPedido.ENTREGUE);

        // 2) Contabilizar quantidade consumida de cada produto
        Map<Long,Integer> contador = new HashMap<>();
        for (Pedido p : pedidos) {
            for (ItemPedido item : p.getItens()) {
                contador.merge(
                        item.getProduto().getId(),
                        item.getQuantidade(),
                        Integer::sum
                );
            }
        }

        // 3) Ordenar por frequência decrescente e mapear para DTO
        return contador.entrySet().stream()
                .sorted(Map.Entry.<Long,Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(e -> {
                    // Encontrar nome do produto a partir do histórico
                    var prod = pedidos.stream()
                            .flatMap(pr -> pr.getItens().stream())
                            .map(ItemPedido::getProduto)
                            .filter(p -> p.getId().equals(e.getKey()))
                            .findFirst().orElseThrow();
                    return new RecomendacaoDTO(
                            prod.getId(), prod.getNome(), e.getValue()
                    );
                })
                .collect(Collectors.toList());
    }
}
