package com.barcommerce.barcommerce.model;

import com.barcommerce.barcommerce.enums.MetodoPagamento;
import com.barcommerce.barcommerce.enums.StatusPedido;
import com.barcommerce.barcommerce.enums.StatusPagamento; // Nova enumeração necessária
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa um pedido feito por uma mesa/cliente.
 * Controla status, pagamento e itens do pedido.
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ========== PAGAMENTO ========== //
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 20)
    private StatusPagamento statusPagamento = StatusPagamento.PENDENTE; // Valor padrão

    @Column(name = "id_transacao", length = 100)
    private String idTransacao; // ID da transação no gateway

    @Enumerated(EnumType.STRING) // Alterado para enumeração
    @Column(name = "metodo_pagamento", length = 20)
    private MetodoPagamento metodoPagamento;

    // ========== RELACIONAMENTOS ========== //
    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Mesa é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    // ========== ITENS E VALORES ========== //
    @NotNull(message = "Itens são obrigatórios")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    @NotNull(message = "Total não pode ser nulo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    // ========== CONTROLE TEMPORAL ========== //
    @NotNull(message = "Data/hora é obrigatória")
    @Column(name = "data_hora", nullable = false, updatable = false) // Não pode ser alterada
    private LocalDateTime dataHora;

    // ========== STATUS DO PEDIDO ========== //
    @NotNull(message = "Status do pedido é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.ABERTO;

    // ========== CALLBACKS JPA ========== //
    @PrePersist
    @PreUpdate
    public void calcularTotais() {
        this.dataHora = (this.dataHora == null) ? LocalDateTime.now() : this.dataHora;
        this.total = this.itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (itens != null) {
            for (ItemPedido item : itens) {
                if (item.getSubtotal() != null) {
                    total = total.add(item.getSubtotal());
                }
            }
        }
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    // ========== GETTERS/SETTERS ========== //
    // [Mantidos todos os existentes...]

    // Adicionar novos getters/setters
    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public void setDataHora(LocalDateTime dataHora) {  // Remova se não quiser permitir alteração
        this.dataHora = dataHora;
    }
}