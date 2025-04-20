package com.barcommerce.barcommerce.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Registra histórico de alterações de pontos de fidelidade.
 */
@Entity
@Table(name = "pontuacao_eventos")
public class PontuacaoEvento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Integer pontosAlterados;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false, updatable = false)
    private Instant timestamp = Instant.now();

    // Construtores, getters e setters
    public PontuacaoEvento() {}
    public PontuacaoEvento(Cliente cliente, Integer pontosAlterados, String motivo) {
        this.cliente = cliente;
        this.pontosAlterados = pontosAlterados;
        this.motivo = motivo;
    }
    public Long getId() {
        return id;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public Integer getPontosAlterados() {
        return pontosAlterados;
    }
    public String getMotivo() {
        return motivo;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
}
