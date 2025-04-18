// src/main/java/com/barcommerce/barcommerce/dto/ClienteDTO.java
package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para criação/atualização e exibição de Cliente.
 */
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome pode ter até 100 caracteres")
    private String nome;

    @NotBlank(message = "E‑mail é obrigatório")
    @Email(message = "E‑mail deve ser válido")
    @Size(max = 150, message = "E‑mail pode ter até 150 caracteres")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 20, message = "Telefone pode ter até 20 caracteres")
    private String telefone;

    public ClienteDTO() {}

    public ClienteDTO(Long id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
