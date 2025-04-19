package com.barcommerce.barcommerce.dto;

import com.barcommerce.barcommerce.enums.StatusPagamento;

/**
 * DTO de resposta após pagamento com cartão.
 */
public class CartaoResponseDTO {
    private String idTransacao;
    private StatusPagamento statusPagamento;

    public CartaoResponseDTO(String idTransacao, StatusPagamento statusPagamento) {
        this.idTransacao = idTransacao;
        this.statusPagamento = statusPagamento;
    }
    // getters…

    public String getIdTransacao() {
        return idTransacao;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }
}
