package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.PontuacaoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PontuacaoEventoRepository extends JpaRepository<PontuacaoEvento, Long> {
    List<PontuacaoEvento> findByClienteIdOrderByTimestampDesc(Long clienteId);
}