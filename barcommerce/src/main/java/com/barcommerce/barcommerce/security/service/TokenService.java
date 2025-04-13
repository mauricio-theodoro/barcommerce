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
 */
@Service
public class TokenService {

    private final SecretKey signingKey;
    private final long expirationMillis;

    public TokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // Garante ao menos 256 bits (32 bytes) para HS256
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "O jwt.secret deve ter pelo menos 32 bytes (256 bits). " +
                            "Gere um valor aleatório com 32+ bytes.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMillis;
    }

    /**
     * Gera um JWT contendo o email como subject e a role como claim.
     *
     * @param usuario o usuário autenticado
     * @return token JWT assinado
     */
    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("role", usuario.getRole().name())
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida um token JWT: verifica assinatura e expiração.
     *
     * @param token o JWT bruto
     * @return true se válido, false caso contrário
     */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // logger.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrai o subject (email) de um token válido.
     *
     * @param token o JWT bruto (assume que já foi validado)
     * @return o email do usuário
     * @throws JwtException se o token for inválido
     */
    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
