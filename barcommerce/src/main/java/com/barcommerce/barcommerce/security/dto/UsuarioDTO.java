package com.barcommerce.barcommerce.security.dto;

import com.barcommerce.barcommerce.security.enums.Role;
import com.barcommerce.barcommerce.security.model.Usuario;
import jakarta.validation.constraints.*;

public class UsuarioDTO {

    @NotBlank
    @Email
    private String email;

    @Size(min = 6)
    @NotBlank
    private String senha;

    @Size(min = 6)
    @NotBlank
    private String senhaConfirmacao;

    // Role pode ser opcional agora, será definida no backend
    private Role role;

    // Construtor vazio
    public UsuarioDTO() {}

    // Construtor manual (sem senhaConfirmacao para evitar uso indevido)
    public UsuarioDTO(String email, String senha, Role role) {
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    // Construtor baseado em entidade Usuario (resposta)
    public UsuarioDTO(Usuario usuario) {
        this.email = usuario.getEmail();
        this.role = usuario.getRole();
        this.senha = null;
        this.senhaConfirmacao = null;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaConfirmacao() {
        return senhaConfirmacao;
    }

    public void setSenhaConfirmacao(String senhaConfirmacao) {
        this.senhaConfirmacao = senhaConfirmacao;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
