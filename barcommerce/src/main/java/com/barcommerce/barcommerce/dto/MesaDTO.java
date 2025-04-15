package com.barcommerce.barcommerce.dto;

import com.barcommerce.barcommerce.enums.StatusMesa;
import jakarta.validation.constraints.*;

/**
 * DTO para transferência de dados de Mesa.
 */
public class MesaDTO {

    private Long id;

    @NotBlank(message = "Identificação da mesa é obrigatória.")
    private String identificacao;

    @NotNull(message = "Capacidade da mesa é obrigatória.")
    @Positive(message = "Capacidade deve ser maior que zero.")
    private Integer capacidade;

    private StatusMesa status;

    public MesaDTO() {
        // Construtor para desserialização
    }

    public MesaDTO(Long id, String identificacao, Integer capacidade, StatusMesa status) {
        this.id = id;
        this.identificacao = identificacao;
        this.capacidade = capacidade;
        this.status = status;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public StatusMesa getStatus() {
        return status;
    }

    public void setStatus(StatusMesa status) {
        this.status = status;
    }
}
