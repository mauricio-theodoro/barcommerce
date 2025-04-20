package com.barcommerce.barcommerce.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Representa o saldo de pontos de fidelidade de um Cliente.
 */
@Entity
@Table(name = "cliente_fidelidade")
public class ClienteFidelidade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relaciona 1‑para‑1 com Cliente */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    /** Total de pontos acumulados */
    @Column(nullable = false)
    private Integer pontos = 0;

    public ClienteFidelidade() {}

    public ClienteFidelidade(Cliente cliente) {
        this.cliente = cliente;
    }

    // getters & setters

    public Long getId() {
        return id;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public Integer getPontos() {
        return pontos;
    }
    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }
}
