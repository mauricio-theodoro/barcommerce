package com.barcommerce.barcommerce.security.controller;

import com.barcommerce.barcommerce.security.dto.ErroDTO;
import com.barcommerce.barcommerce.security.dto.LoginDTO;
import com.barcommerce.barcommerce.security.dto.TokenDTO;
import com.barcommerce.barcommerce.security.dto.UsuarioDTO;
import com.barcommerce.barcommerce.security.enums.Role;
import com.barcommerce.barcommerce.security.model.Usuario;
import com.barcommerce.barcommerce.security.service.TokenService;
import com.barcommerce.barcommerce.security.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

/**
 * Controlador responsável pela autenticação (login) e registro de usuários.
 * Disponível publicamente sem necessidade de autenticação prévia.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    // Injeção de dependência via construtor
    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint de login de usuário.
     * Recebe email e senha, autentica o usuário e retorna um JWT para acesso futuro.
     *
     * @param loginDTO DTO contendo email e senha
     * @return TokenDTO com o JWT gerado
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Autentica o usuário e retorna um token JWT.")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        // Cria o token de autenticação com as credenciais informadas
        var authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getSenha());

        // Autentica o usuário
        Authentication auth = authenticationManager.authenticate(authToken);

        // Recupera os dados do usuário autenticado
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Gera o JWT baseado nas informações do usuário
        String jwt = tokenService.gerarToken(usuario);

        // Retorna o token no corpo da resposta
        return ResponseEntity.ok(new TokenDTO(jwt));
    }

    /**
     * Endpoint de registro de novo usuário.
     * Realiza validações e salva novo usuário no banco.
     *
     * @param dto DTO com dados de cadastro
     * @return DTO com dados do usuário criado (sem senha)
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema.")
    public ResponseEntity<?> register(@RequestBody @Valid UsuarioDTO dto) {
        // Verifica se as senhas coincidem
        if (!dto.getSenha().equals(dto.getSenhaConfirmacao())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroDTO("As senhas não coincidem."));
        }

        // Verifica se o email já está em uso
        if (usuarioService.buscarPorEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErroDTO("Já existe um usuário com esse e-mail."));
        }

        // Define role padrão, independente do que vier no DTO
        Role rolePadrao = Role.FUNCIONARIO; // ou Role.USER, dependendo do seu enum

        // Cria novo usuário
        Usuario novo = new Usuario(dto.getEmail(), dto.getSenha(), rolePadrao);

        // Salva no banco
        Usuario salvo = usuarioService.salvarUsuario(novo);

        URI location = URI.create("/api/auth/" + salvo.getId());

        // Retorna DTO com informações do usuário (sem senha)
        return ResponseEntity
                .created(location)
                .body(new UsuarioDTO(salvo));
    }

}
