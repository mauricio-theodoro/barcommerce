package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.ClienteFidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteFidelidadeRepository extends JpaRepository<ClienteFidelidade, Long> {
    Optional<ClienteFidelidade> findByClienteId(Long clienteId);
}
