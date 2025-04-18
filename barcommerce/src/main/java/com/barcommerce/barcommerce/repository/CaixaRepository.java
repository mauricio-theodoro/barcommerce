package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.MovimentacaoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para persistência de MovimentacaoCaixa.
 */
@Repository
public interface CaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {

    /** Busca a última movimentação (maior abertura) */
    Optional<MovimentacaoCaixa> findTopByOrderByAberturaDesc();
}
