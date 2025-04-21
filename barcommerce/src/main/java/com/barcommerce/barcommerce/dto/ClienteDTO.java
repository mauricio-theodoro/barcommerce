// src/main/java/com/barcommerce/barcommerce/dto/ClienteDTO.java
package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO para criação, atualização e exibição de Cliente.
 * Inclui senha (para entrada) mas nunca a retorna na resposta.
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

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    public ClienteDTO() {}

    public ClienteDTO(Long id,
                      String nome,
                      String email,
                      String telefone,
                      String senha,
                      LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    // Getters & Setters

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

    /**
     * Senha em texto plano — criptografar na camada de serviço.
     */
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
