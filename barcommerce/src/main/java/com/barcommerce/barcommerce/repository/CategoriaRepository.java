package com.barcommerce.barcommerce.repository;

import com.barcommerce.barcommerce.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface responsável por acessar e manipular os dados da entidade Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Podemos adicionar métodos personalizados depois, se necessário.
}