package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para receber os dados do pagamento por cartão.
 */
public class PagamentoCartaoDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotBlank(message = "Token do cartão é obrigatório")
    private String token;

    @NotBlank(message = "E‑mail do pagador é obrigatório")
    @Email(message = "E‑mail inválido")
    private String email;

    @Min(value = 1, message = "Parcelas deve ser pelo menos 1")
    @Max(value = 1, message ="Máximo de parcelas é 1")
    private int parcelas;

    @NotBlank(message = "Método de pagamento é obrigatório")
    private String metodoPagamento; // Ex: "visa"

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotBlank(message = "Identificação (CPF/CNPJ) é obrigatória")
    private String identificacao;

    @NotBlank(message = "Tipo de identificação é obrigatório")
    private String tipoIdentificacao; // "CPF" ou "CNPJ"

    // getters e setters…

    public @NotNull(message = "ID do pedido é obrigatório") Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(@NotNull(message = "ID do pedido é obrigatório") Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public @NotBlank(message = "Token do cartão é obrigatório") String getToken() {
        return token;
    }

    public void setToken(@NotBlank(message = "Token do cartão é obrigatório") String token) {
        this.token = token;
    }

    public @NotBlank(message = "E‑mail do pagador é obrigatório") @Email(message = "E‑mail inválido") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "E‑mail do pagador é obrigatório") @Email(message = "E‑mail inválido") String email) {
        this.email = email;
    }

    @Min(value = 1, message = "Parcelas deve ser pelo menos 1")
    @Max(value = 1, message = "Máximo de parcelas é 1")
    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(@Min(value = 1, message = "Parcelas deve ser pelo menos 1") @Max(value = 1, message = "Máximo de parcelas é 1") int parcelas) {
        this.parcelas = parcelas;
    }

    public @NotBlank(message = "Método de pagamento é obrigatório") String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(@NotBlank(message = "Método de pagamento é obrigatório") String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public @NotBlank(message = "Descrição é obrigatória") String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NotBlank(message = "Descrição é obrigatória") String descricao) {
        this.descricao = descricao;
    }

    public @NotNull(message = "Valor é obrigatório") @DecimalMin(value = "0.01", message = "Valor deve ser positivo") BigDecimal getValor() {
        return valor;
    }

    public void setValor(@NotNull(message = "Valor é obrigatório") @DecimalMin(value = "0.01", message = "Valor deve ser positivo") BigDecimal valor) {
        this.valor = valor;
    }

    public @NotBlank(message = "Identificação (CPF/CNPJ) é obrigatória") String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(@NotBlank(message = "Identificação (CPF/CNPJ) é obrigatória") String identificacao) {
        this.identificacao = identificacao;
    }

    public @NotBlank(message = "Tipo de identificação é obrigatório") String getTipoIdentificacao() {
        return tipoIdentificacao;
    }

    public void setTipoIdentificacao(@NotBlank(message = "Tipo de identificação é obrigatório") String tipoIdentificacao) {
        this.tipoIdentificacao = tipoIdentificacao;
    }
}
