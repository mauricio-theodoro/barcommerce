// src/main/java/com/barcommerce/barcommerce/service/CategoriaService.java
package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.repository.CategoriaRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para operações de Categoria. 
 * Aplica cache para leituras e invalida em operações de escrita.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Lista páginas de categorias – resultado cacheado em "categorias::listar".
     */
    @Cacheable(cacheNames = "categorias", key = "'listar-'+#pageable.pageNumber+'-'+#pageable.pageSize")
    public Page<Categoria> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    /**
     * @param id identificador da categoria
     * @return categoria por ID (cache “categorias::[id]”)
     */
    @Cacheable(value = "categorias", key = "#id")
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Cria categoria e invalida caches de categoria.
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public Categoria criar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Atualiza categoria e invalida cache.
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public Optional<Categoria> atualizar(Long id, Categoria nova) {
        return categoriaRepository.findById(id)
                .map(cat -> {
                    cat.setNome(nova.getNome());
                    cat.setDescricao(nova.getDescricao());
                    return categoriaRepository.save(cat);
                });
    }

    /**
     * Deleta categoria e invalida cache.
     */
    @CacheEvict(value = "categorias", allEntries = true)
    public boolean deletar(Long id) {
        return categoriaRepository.findById(id)
                .map(cat -> {
                    categoriaRepository.delete(cat);
                    return true;
                })
                .orElse(false);
    }
}
