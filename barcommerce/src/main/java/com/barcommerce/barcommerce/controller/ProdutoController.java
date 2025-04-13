package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.mapper.ProdutoMapper;
import com.barcommerce.barcommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Lista todos os produtos")
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoDTO> dtos = produtoService.listarTodos()
                .stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Busca um produto pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id) {

        return produtoService.buscarPorId(id)
                .map(ProdutoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo produto")
    @PostMapping
    public ResponseEntity<?> criar(
            @Parameter(description = "Dados do produto a criar", required = true)
            @Valid @RequestBody ProdutoDTO dto) {

        return produtoService.criarProduto(ProdutoMapper.toEntity(dto))
                .map(ProdutoMapper::toDTO)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest()
                        .body("Categoria inválida ou não informada."));
    }

    @Operation(summary = "Atualiza um produto existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados para atualização", required = true)
            @Valid @RequestBody ProdutoDTO dto) {

        return produtoService.atualizarProduto(id, ProdutoMapper.toEntity(dto))
                .map(ProdutoMapper::toDTO)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um produto pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do produto", required = true)
            @PathVariable Long id) {

        return produtoService.deletarProduto(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
