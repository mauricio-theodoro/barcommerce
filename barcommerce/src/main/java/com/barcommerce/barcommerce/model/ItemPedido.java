package com.barcommerce.barcommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Representa um item dentro de um pedido.
 */
@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotNull(message = "Produto é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "Quantidade é obrigatória.")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    /**
     * Antes de persistir ou atualizar, define precoUnitario e subtotal.
     */
    @PrePersist
    @PreUpdate
    public void preCalcular() {
        if (produto != null && produto.getPreco() != null && quantidade != null) {
            this.precoUnitario = produto.getPreco();
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        } else {
            this.subtotal = BigDecimal.ZERO; // Evita erro de null
        }
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() { return produto; }

    public void setProduto(Produto produto) {
        this.produto = produto;
        if (produto != null) {
            this.precoUnitario = produto.getPreco();
        }
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}
