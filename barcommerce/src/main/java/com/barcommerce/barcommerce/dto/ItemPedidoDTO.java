package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

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

    private BigDecimal subtotal;

    public ItemPedidoDTO() {
        // Construtor para desserialização
    }

    public ItemPedidoDTO(Long id, Long produtoId, Integer quantidade, BigDecimal subtotal) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // embora nunca venha do front, o setter é necessário para o mapper
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
