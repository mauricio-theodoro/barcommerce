package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações de Categoria.
 * Delegando toda a lógica de negócio para {@link CategoriaService}
 * e mantendo endpoints RESTful bem definidos.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    /**
     * Lista todas as categorias disponíveis.
     * (Cacheada via service, key = "categorias::all")
     */
    @Operation(summary = "Lista todas as categorias")
    @GetMapping
    public ResponseEntity<Page<Categoria>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pg = PageRequest.of(page, size, Sort.by("nome"));
        return ResponseEntity.ok(service.listar(pg));
    }

    /**
     * Busca uma categoria por ID.
     * (Cacheada via service, key = "categorias::[id]")
     */
    @Operation(summary = "Busca categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria uma nova categoria.
     * Invalida cache de listagem e buscas anteriores.
     */
    @Operation(summary = "Cria nova categoria")
    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {
        Categoria criada = service.criar(categoria);
        return ResponseEntity.ok(criada);
    }

    /**
     * Atualiza uma categoria existente.
     * Invalida cache de listagem e buscas anteriores.
     */
    @Operation(summary = "Atualiza categoria existente")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(
            @PathVariable Long id,
            @RequestBody Categoria dados) {

        return service.atualizar(id, dados)
                .map(atual -> ResponseEntity.ok(atual))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deleta uma categoria.
     * Invalida cache de listagem e buscas anteriores.
     */
    @Operation(summary = "Remove categoria por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean excluiu = service.deletar(id);
        return excluiu
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
