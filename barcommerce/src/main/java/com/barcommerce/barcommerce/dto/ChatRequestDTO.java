package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição ao chatbot.
 */
public class ChatRequestDTO {
    @NotBlank(message = "Mensagem não pode ser vazia")
    private String message;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}