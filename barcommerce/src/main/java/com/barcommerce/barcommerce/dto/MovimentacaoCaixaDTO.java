package com.barcommerce.barcommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Para enviar os dados de abertura/fechamento de caixa via API.
 */
public class MovimentacaoCaixaDTO {
    private Long id;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private LocalDateTime abertura;
    private LocalDateTime fechamento;

    // Construtor vazio
    public MovimentacaoCaixaDTO() {}

    /**
     * Construtor completo para facilitar a montagem via serviço.
     */
    public MovimentacaoCaixaDTO(Long id,
                                BigDecimal saldoInicial,
                                BigDecimal saldoFinal,
                                LocalDateTime abertura,
                                LocalDateTime fechamento) {
        this.id = id;
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }

    public BigDecimal getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }

    public LocalDateTime getAbertura() { return abertura; }
    public void setAbertura(LocalDateTime abertura) { this.abertura = abertura; }

    public LocalDateTime getFechamento() { return fechamento; }
    public void setFechamento(LocalDateTime fechamento) { this.fechamento = fechamento; }
}
