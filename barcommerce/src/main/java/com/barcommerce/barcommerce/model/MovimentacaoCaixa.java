package com.barcommerce.barcommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa a abertura e fechamento de um caixa.
 */
@Entity
@Table(name = "caixas")
public class MovimentacaoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Saldo no momento da abertura */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoInicial;

    /** Saldo após fechamento (saldoInicial + vendas) */
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoFinal;

    /** Data e hora da abertura */
    @Column(nullable = false)
    private LocalDateTime abertura;

    /** Data e hora do fechamento */
    @Column
    private LocalDateTime fechamento;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public LocalDateTime getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalDateTime abertura) {
        this.abertura = abertura;
    }

    public LocalDateTime getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalDateTime fechamento) {
        this.fechamento = fechamento;
    }
}
