package com.barcommerce.barcommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para receber notificações de webhook do Mercado Pago.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MercadoPagoWebhookDTO {

    @NotNull(message = "Campo 'data' é obrigatório no payload do webhook")
    @Valid
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    /**
     * Estrutura interna que contém o ID da transação no Mercado Pago.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @NotNull(message = "Campo 'id' é obrigatório dentro de 'data'")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}