package com.barcommerce.barcommerce.dto;

import java.math.BigDecimal;

public class FecharPedidoDTO {
    private String metodoPagamento;
    private BigDecimal valorPago;

    // getters e setters
    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }
}
