package com.barcommerce.barcommerce.dto;

import com.barcommerce.barcommerce.enums.MetodoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de requisição para confirmar um pagamento e, em sequência,
 * liberar a mesa correspondente.
 */
public class ConfirmacaoPagamentoDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotNull(message = "Método de pagamento é obrigatório")
    private MetodoPagamento metodoPagamento;

    /**
     * No caso de PIX ou cartão, o ID da transação retornado pelo gateway.
     * Para dinheiro, pode vir em branco ou um valor fixo (ex: "CASH").
     */
    private String idTransacao;

    @NotBlank(message = "DeviceId é obrigatório para validar quem libera a mesa")
    private String deviceId;

    // getters e setters

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
