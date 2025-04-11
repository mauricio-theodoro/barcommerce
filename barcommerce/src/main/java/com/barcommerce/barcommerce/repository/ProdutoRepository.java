package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface responsável por acessar e manipular os dados da entidade Produto.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    // Podemos buscar por nome, tipo, etc. no futuro com métodos customizados.
}