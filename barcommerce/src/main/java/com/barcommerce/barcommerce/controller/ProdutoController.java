package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.mapper.ProdutoMapper;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.service.FileStorageService;
import com.barcommerce.barcommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para operações com Produto.
 * Inclui CRUD padrão e upload de imagem protegido por role ADMIN.
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final FileStorageService storageService;

    public ProdutoController(ProdutoService produtoService,
                             FileStorageService storageService) {
        this.produtoService = produtoService;
        this.storageService = storageService;
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

    // ———————————————
    // Novo endpoint de upload de imagem
    // ———————————————

    @Operation(summary = "Faz upload de imagem para produto")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{id}/imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoDTO> uploadImagem(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo vazio");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas imagens são permitidas");
        }

        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        try {
            String url = storageService.store(file);
            produto.setImagemUrl(url);
            Produto atualizado = produtoService.salvar(produto);
            return ResponseEntity.ok(ProdutoMapper.toDTO(atualizado));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno: " + e.getMessage(), e);
        }
    }
}
