package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.*;

/**
 * DTO para transferência de dados de ItemPedido.
 */
public class ItemPedidoDTO {

    private Long id;

    @NotNull(message = "ID do produto é obrigatório.")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória.")
    @Positive(message = "Quantidade deve ser maior que zero.")
    private Integer quantidade;

    public ItemPedidoDTO() {
        // Construtor para desserialização
    }

    public ItemPedidoDTO(Long id, Long produtoId, Integer quantidade) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
