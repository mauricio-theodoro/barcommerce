package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.enums.StatusMesa;
import com.barcommerce.barcommerce.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    boolean existsByIdentificacao(String identificacao);
    List<Mesa> findByStatus(StatusMesa status);
}