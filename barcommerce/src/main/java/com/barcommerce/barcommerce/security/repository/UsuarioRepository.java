package com.barcommerce.barcommerce.security.repository;

import com.barcommerce.barcommerce.security.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário pelo email (único para login)
    Optional<Usuario> findByEmail(String email);

    // Verifica se já existe usuário com esse email (útil para registro)
    boolean existsByEmail(String email);
}
