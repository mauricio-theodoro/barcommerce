package com.barcommerce.barcommerce.security.service;

import com.barcommerce.barcommerce.security.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Configurado para trabalhar com roles SEM o prefixo "ROLE_".
 */
@Service
public class TokenService {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {

        // Validação da chave secreta (mínimo 256 bits)
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "A chave JWT deve ter pelo menos 32 bytes (256 bits) para segurança adequada.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis;
    }

    /**
     * Gera um token JWT com email (subject) e role (claim).
     * Role é armazenada SEM o prefixo "ROLE_".
     */
    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("role", usuario.getRole().name()) // Exemplo: "ADMIN"
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida a assinatura e a expiração do token.
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrai o email do usuário (subject) do token.
     */
    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extrai a role do token (sem prefixo "ROLE_").
     */
    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}