package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.ItemPedidoDTO;
import com.barcommerce.barcommerce.model.ItemPedido;
import com.barcommerce.barcommerce.model.Produto;

/**
 * Mapper responsável por converter entre ItemPedido (entidade) e ItemPedidoDTO.
 * <p>O subtotal é calculado automaticamente na entidade via @PrePersist/@PreUpdate,
 * portanto não o mapeamos manualmente aqui.</p>
 */
public class ItemPedidoMapper {

    /**
     * Converte DTO → Entidade.
     * @param dto o DTO com id, produtoId e quantidade
     * @return ItemPedido pronto para ser persistido (subtotal calculado pela entidade)
     */
    public static ItemPedido toEntity(ItemPedidoDTO dto) {
        ItemPedido item = new ItemPedido();
        item.setId(dto.getId());

        // Vincula apenas o ID do produto, o setter ajusta prezzoUnitario
        Produto produto = new Produto();
        produto.setId(dto.getProdutoId());
        item.setProduto(produto);

        // Quantidade; subtotal e precoUnitario serão calculados no entity
        item.setQuantidade(dto.getQuantidade());
        return item;
    }

    /**
     * Converte Entidade → DTO.
     * @param entity o ItemPedido já persistido (com subtotal calculado)
     * @return ItemPedidoDTO com os campos necessários para exibição
     */
    public static ItemPedidoDTO toDTO(ItemPedido entity) {
        ItemPedidoDTO dto = new ItemPedidoDTO();
        dto.setId(entity.getId());
        dto.setProdutoId(entity.getProduto().getId());
        dto.setQuantidade(entity.getQuantidade());
        dto.setSubtotal(entity.getSubtotal()); // aqui liberamos a leitura do subtotal calculado
        return dto;
    }
}
