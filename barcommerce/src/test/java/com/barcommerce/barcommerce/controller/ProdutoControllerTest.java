package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.dto.ProdutoDTO.CategoriaRefDTO;
import com.barcommerce.barcommerce.enums.TipoProduto;
import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProdutoDTO dto;
    private Produto entity;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Categoria de teste
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Bebidas");

        // DTO de entrada
        dto = new ProdutoDTO();
        dto.setNome("Coca-Cola");
        dto.setDescricao("Refrigerante 2L");
        dto.setPreco(new BigDecimal("5.50"));
        dto.setEstoque(100);
        dto.setTipo(TipoProduto.BEBIDA.name());
        dto.setImagemUrl("http://exemplo.com/coca.jpg");
        dto.setAtivo(true);
        CategoriaRefDTO catRef = new CategoriaRefDTO();
        catRef.setId(1L);
        dto.setCategoria(catRef);

        // Entidade de saída
        entity = new Produto();
        entity.setId(10L);
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setEstoque(dto.getEstoque());
        entity.setTipo(TipoProduto.BEBIDA);
        entity.setImagemUrl(dto.getImagemUrl());
        entity.setAtivo(true);
        entity.setCategoria(categoria);
    }

    @Test
    @DisplayName("POST /api/produtos → cria produto e retorna 200")
    void criarProduto_sucesso() throws Exception {
        when(produtoService.criarProduto(any(Produto.class)))
                .thenReturn(Optional.of(entity));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Coca-Cola"))
                .andExpect(jsonPath("$.tipo").value("BEBIDA"))
                .andExpect(jsonPath("$.categoria.id").value(1));

        // Verifica que o serviço recebeu a entidade corretamente mapeada
        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoService).criarProduto(captor.capture());
        Produto capturado = captor.getValue();
        assertThat(capturado.getNome()).isEqualTo(dto.getNome());
        assertThat(capturado.getTipo()).isEqualTo(TipoProduto.BEBIDA);
        assertThat(capturado.getCategoria().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("POST /api/produtos → categoria inválida retorna 400")
    void criarProduto_categoriaInvalida() throws Exception {
        when(produtoService.criarProduto(any(Produto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Categoria inválida ou não informada."));
    }

    @Test
    @DisplayName("GET /api/produtos → lista produtos retorna 200")
    void listarProdutos() throws Exception {
        when(produtoService.listarTodos()).thenReturn(List.of(entity));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → produto existe retorna 200")
    void buscarPorId_existe() throws Exception {
        when(produtoService.buscarPorId(10L)).thenReturn(Optional.of(entity));

        mockMvc.perform(get("/api/produtos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Coca-Cola"));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → não existe retorna 404")
    void buscarPorId_naoExiste() throws Exception {
        when(produtoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → atualiza com sucesso retorna 200")
    void atualizarProduto_sucesso() throws Exception {
        when(produtoService.atualizarProduto(eq(10L), any(Produto.class)))
                .thenReturn(Optional.of(entity));

        mockMvc.perform(put("/api/produtos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Coca-Cola"));
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → falha retorna 404")
    void atualizarProduto_falha() throws Exception {
        when(produtoService.atualizarProduto(eq(10L), any(Produto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/produtos/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → deleta retorna 204")
    void deletarProduto_sucesso() throws Exception {
        when(produtoService.deletarProduto(10L)).thenReturn(true);

        mockMvc.perform(delete("/api/produtos/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → não existe retorna 404")
    void deletarProduto_naoExiste() throws Exception {
        when(produtoService.deletarProduto(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }
}
