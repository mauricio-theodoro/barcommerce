// src/main/java/com/barcommerce/barcommerce/repository/ClienteRepository.java
package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
    Optional<Cliente> findByEmail(String email);
}
