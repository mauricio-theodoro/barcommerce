// UsuarioDTO.java
package com.barcommerce.barcommerce.security.dto;

import com.barcommerce.barcommerce.security.enums.Role;
import jakarta.validation.constraints.*;

public class UsuarioDTO {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min=6)
    private String senha;

    @NotNull
    private Role role;

    public UsuarioDTO() {}

    // <-- Adicione este construtor:
    public UsuarioDTO(String email, String senha, Role role) {
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 6) String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6) String senha) {
        this.senha = senha;
    }

    public @NotNull Role getRole() {
        return role;
    }

    public void setRole(@NotNull Role role) {
        this.role = role;
    }

}
