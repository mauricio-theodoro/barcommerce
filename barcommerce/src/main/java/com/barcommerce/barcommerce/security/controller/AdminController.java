package com.barcommerce.barcommerce.security.controller;

import com.barcommerce.barcommerce.security.dto.UsuarioDTO;
import com.barcommerce.barcommerce.security.enums.Role;
import com.barcommerce.barcommerce.security.model.Usuario;
import com.barcommerce.barcommerce.security.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para operações administrativas.
 * Requer autenticação JWT e role ADMIN para acesso.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth") // Integração com Swagger
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos os usuários cadastrados (senhas omitidas).
     */
    @GetMapping("/usuarios")
    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários com dados básicos.")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        List<UsuarioDTO> dtos = usuarios.stream()
                .map(UsuarioDTO::new) // ← uso do construtor com null automático na senha
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Atualiza a role de um usuário específico.
     * @param id ID do usuário
     * @param novaRole Nova role (ADMIN, FUNCIONARIO, etc.)
     */
    @Operation(summary = "Atualizar role", description = "Modifica a role de um usuário existente.")
    @PutMapping("/usuarios/{id}/role")
    public ResponseEntity<UsuarioDTO> atualizarRoleUsuario(
            @PathVariable Long id,
            @RequestParam Role novaRole) {
        Usuario usuarioAtualizado = usuarioService.atualizarRoleUsuario(id, novaRole);
        return ResponseEntity.ok(new UsuarioDTO(
                usuarioAtualizado.getEmail(),
                null,
                usuarioAtualizado.getRole()
        ));
    }

    /**
     * Remove um usuário do sistema.
     * @param id ID do usuário a ser removido
     */

    @Operation(summary = "Deletar usuário", description = "Remove um usuário permanentemente.")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}