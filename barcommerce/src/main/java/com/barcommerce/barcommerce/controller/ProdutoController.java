package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.mapper.ProdutoMapper;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoDTO> dtos = produtoService.listarTodos()
                .stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        Optional<ProdutoDTO> dto = produtoService.buscarPorId(id)
                .map(ProdutoMapper::toDTO);
        return dto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ProdutoDTO dto) {
        Produto produto = ProdutoMapper.toEntity(dto);
        return produtoService.criarProduto(produto)
                .map(ProdutoMapper::toDTO)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body("Categoria inválida ou não informada."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        Produto produto = ProdutoMapper.toEntity(dto);
        return produtoService.atualizarProduto(id, produto)
                .map(ProdutoMapper::toDTO)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return produtoService.deletarProduto(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
