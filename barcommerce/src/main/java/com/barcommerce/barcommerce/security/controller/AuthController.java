package com.barcommerce.barcommerce.security.controller;

import com.barcommerce.barcommerce.security.dto.LoginDTO;
import com.barcommerce.barcommerce.security.dto.TokenDTO;
import com.barcommerce.barcommerce.security.dto.UsuarioDTO;
import com.barcommerce.barcommerce.security.model.Usuario;
import com.barcommerce.barcommerce.security.service.TokenService;
import com.barcommerce.barcommerce.security.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Controlador para autenticação e registro de usuários.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint de login. Recebe credenciais e retorna um JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getSenha());

        Authentication auth = authenticationManager.authenticate(authToken);
        Usuario usuario = (Usuario) auth.getPrincipal();
        String jwt = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new TokenDTO(jwt));
    }

    /**
     * Endpoint de registro de novo usuário.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UsuarioDTO dto) {
        // Verifica se email já existe
        if (usuarioService.buscarPorEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Já existe usuário com esse email.");
        }

        // Cria entidade e persiste
        Usuario novo = new Usuario(dto.getEmail(), dto.getSenha(), dto.getRole());
        Usuario salvo = usuarioService.salvarUsuario(novo);

        // Retorna 201 Created com localização do recurso
        URI location = URI.create("/api/auth/" + salvo.getId());
        return ResponseEntity
                .created(location)
                .body(new UsuarioDTO(salvo.getEmail(), null, salvo.getRole()));
    }
}
