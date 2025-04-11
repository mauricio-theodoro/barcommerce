package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.eq;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.barcommerce.barcommerce.controller.ProdutoController;


import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("GET /api/produtos → retorna lista de produtos")
    void listarTodos_deveRetornarLista() throws Exception {
        Produto p1 = new Produto(1L, "Cerveja", new BigDecimal("5.00"), 10, null, null);
        Produto p2 = new Produto(2L, "Refrigerante", new BigDecimal("4.50"), 20, null, null);

        when(produtoService.listarTodos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].nome").value("Refrigerante"));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → quando existe retorna 200")
    void buscarPorId_quandoExiste_retorna200() throws Exception {
        Produto p = new Produto(1L, "Cerveja", new BigDecimal("5.00"), 10, null, null);
        when(produtoService.buscarPorId(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cerveja"));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → quando não existe retorna 404")
    void buscarPorId_quandoNaoExiste_retorna404() throws Exception {
        when(produtoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/produtos → cria com sucesso retorna 200")
    void criar_deveRetornar200_quandoSucesso() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1L);
        Produto novo = new Produto(null, "Cerveja", new BigDecimal("5.00"), 10, null, cat);
        Produto salvo = new Produto(1L, "Cerveja", new BigDecimal("5.00"), 10, null, cat);

        when(produtoService.criarProduto(any(Produto.class))).thenReturn(Optional.of(salvo));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cerveja"));
    }

    @Test
    @DisplayName("POST /api/produtos → falha quando categoria inválida retorna 400")
    void criar_quandoCategoriaInvalida_retorna400() throws Exception {
        Produto novo = new Produto(null, "Cerveja", new BigDecimal("5.00"), 10, null, null);

        when(produtoService.criarProduto(any(Produto.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → atualiza com sucesso retorna 200")
    void atualizar_deveRetornar200_quandoSucesso() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1L);
        Produto atualizado = new Produto(1L, "Cerveja Long Neck", new BigDecimal("6.00"), 15, null, cat);

        when(produtoService.atualizarProduto(eq(1L), any(Produto.class)))
                .thenReturn(Optional.of(atualizado));

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cerveja Long Neck"))
                .andExpect(jsonPath("$.preco").value(6.00))
                .andExpect(jsonPath("$.estoque").value(15));
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → falha quando categoria inválida retorna 400")
    void atualizar_quandoCategoriaInvalida_retorna400() throws Exception {
        Produto semCategoria = new Produto(null, "Cerveja", new BigDecimal("5.00"), 10, null, null);

        when(produtoService.atualizarProduto(eq(1L), any(Produto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(semCategoria)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → falha quando produto não existe retorna 404")
    void atualizar_quandoNaoExiste_retorna404() throws Exception {
        Categoria cat = new Categoria();
        cat.setId(1L);
        Produto produto = new Produto(null, "Cerveja", new BigDecimal("5.00"), 10, null, cat);

        when(produtoService.atualizarProduto(eq(99L), any(Produto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/produtos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → deleta com sucesso retorna 204")
    void deletar_deveRetornar204_quandoSucesso() throws Exception {
        when(produtoService.deletarProduto(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → falha quando não existe retorna 404")
    void deletar_quandoNaoExiste_retorna404() throws Exception {
        when(produtoService.deletarProduto(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }
}
