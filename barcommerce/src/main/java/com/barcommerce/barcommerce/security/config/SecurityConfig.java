package com.barcommerce.barcommerce.security.config;

import com.barcommerce.barcommerce.security.filter.JwtAuthenticationFilter;
import com.barcommerce.barcommerce.security.service.TokenService;
import com.barcommerce.barcommerce.security.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenService tokenService;
    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(TokenService tokenService,
                          UsuarioDetailsService usuarioDetailsService) {
        this.tokenService = tokenService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthenticationFilter(tokenService, usuarioDetailsService);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1) public: autenticação de funcionários/admin
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        // 2) public: auto‑cadastro de cliente via QR
                        .requestMatchers(HttpMethod.POST, "/api/clientes").permitAll()
                        // 3) Swagger/OpenAPI
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // 4) gestão de mesas e pedidos: só GERENTE e FUNCIONARIO
                        .requestMatchers("/api/mesas/**", "/api/pedidos/**")
                        .hasAnyRole("GERENTE","FUNCIONARIO")

                        // 5) caixa: só GERENTE e ADMIN
                        .requestMatchers("/api/caixa/**")
                        .hasAnyRole("GERENTE","ADMIN")

                        // 6) clientes (listar/editar/remover): só ADMIN e GERENTE
                        .requestMatchers(HttpMethod.GET,    "/api/clientes/**").hasAnyRole("ADMIN","GERENTE")
                        .requestMatchers(HttpMethod.PUT,    "/api/clientes/**").hasAnyRole("ADMIN","GERENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasAnyRole("ADMIN","GERENTE")

                        // 7) dashboard: só ADMIN e GERENTE (via @PreAuthorize)
                        // .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN","GERENTE")

                        // 8) TODO: outras rotas devem pedir autenticação
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)))
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
