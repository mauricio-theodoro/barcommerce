package com.barcommerce.barcommerce.security.controller;

import com.barcommerce.barcommerce.security.dto.UsuarioDTO;
import com.barcommerce.barcommerce.security.model.Usuario;
import com.barcommerce.barcommerce.security.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável pelas operações do usuário autenticado.
 * Acessível por usuários com roles ROLE_USER e ROLE_ADMIN.
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Retorna os dados do usuário autenticado formatados com DTO.
     *
     * @param usuario Usuário autenticado via JWT
     * @return Dados do usuário
     */
    @GetMapping("/me")
    public ResponseEntity<?> obterUsuarioAutenticado(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        return ResponseEntity.ok(usuarioDTO);
    }
}
