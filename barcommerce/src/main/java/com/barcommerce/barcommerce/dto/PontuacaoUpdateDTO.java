package com.barcommerce.barcommerce.dto;

/**
 * DTO para enviar atualização de pontos via WebSocket.
 */
public class PontuacaoUpdateDTO {
    private Long clienteId;
    private Integer novosPontos;

    public PontuacaoUpdateDTO(Long clienteId, Integer novosPontos) {
        this.clienteId = clienteId;
        this.novosPontos = novosPontos;
    }
    public Long getClienteId() { return clienteId; }
    public Integer getNovosPontos() { return novosPontos; }
}