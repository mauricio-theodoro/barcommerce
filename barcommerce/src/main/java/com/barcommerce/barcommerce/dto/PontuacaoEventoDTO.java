package com.barcommerce.barcommerce.dto;

import java.time.Instant;

/**
 * DTO para representar um evento de pontuação.
 * Evita expor diretamente entidades JPA, facilitando a serialização segura.
 */
public class PontuacaoEventoDTO {

    private Long id;
    private Long clienteId;
    private Integer pontos;
    private String tipo;
    private Instant timestamp;

    public PontuacaoEventoDTO(Long id, Long clienteId, Integer pontos, String tipo, Instant timestamp) {
        this.id = id;
        this.clienteId = clienteId;
        this.pontos = pontos;
        this.tipo = tipo;
        this.timestamp = timestamp;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Integer getPontos() {
        return pontos;
    }

    public String getTipo() {
        return tipo;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
