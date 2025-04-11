package com.barcommerce.barcommerce.controller;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.enums.TipoProduto;
import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private Produto produtoValido;
    private Categoria categoriaValida;
    private ProdutoDTO produtoDTOValido;

    @BeforeEach
    void setUp() {
        categoriaValida = new Categoria();
        categoriaValida.setId(1L);
        categoriaValida.setNome("Bebidas");

        produtoValido = new Produto(1L, "Cerveja", "Cerveja artesanal",
                new BigDecimal("10.99"), 50,
                TipoProduto.BEBIDA, categoriaValida);

        produtoDTOValido = new ProdutoDTO();
        produtoDTOValido.setNome("Cerveja");
        produtoDTOValido.setDescricao("Cerveja artesanal");
        produtoDTOValido.setPreco(new BigDecimal("10.99"));
        produtoDTOValido.setEstoque(50);
        produtoDTOValido.setTipo("BEBIDA");
        produtoDTOValido.setAtivo(true);

        ProdutoDTO.CategoriaRefDTO categoriaRef = new ProdutoDTO.CategoriaRefDTO();
        categoriaRef.setId(1L);
        produtoDTOValido.setCategoria(categoriaRef);
    }

    @Test
    @DisplayName("GET /api/produtos → deve retornar lista de produtos com status 200")
    void listarTodos_deveRetornarListaDeProdutos() throws Exception {
        when(produtoService.listarTodos()).thenReturn(List.of(produtoValido));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Cerveja"))
                .andExpect(jsonPath("$[0].preco").value(10.99));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → quando existe deve retornar produto com status 200")
    void buscarPorId_quandoExiste_deveRetornarProduto() throws Exception {
        when(produtoService.buscarPorId(1L)).thenReturn(Optional.of(produtoValido));

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cerveja"));
    }

    @Test
    @DisplayName("GET /api/produtos/{id} → quando não existe deve retornar status 404")
    void buscarPorId_quandoNaoExiste_deveRetornarNotFound() throws Exception {
        when(produtoService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/produtos → quando válido deve criar produto e retornar status 201")
    void criar_quandoValido_deveRetornarProdutoCriado() throws Exception {
        when(produtoService.criarProduto(any(Produto.class))).thenReturn(produtoValido);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTOValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cerveja"));
    }

    @Test
    @DisplayName("POST /api/produtos → quando inválido deve retornar status 400")
    void criar_quandoInvalido_deveRetornarBadRequest() throws Exception {
        produtoDTOValido.setNome(null); // Tornando o DTO inválido

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTOValido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → quando válido deve atualizar produto e retornar status 200")
    void atualizar_quandoValido_deveRetornarProdutoAtualizado() throws Exception {
        when(produtoService.atualizarProduto(eq(1L), any(Produto.class))).thenReturn(produtoValido);

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTOValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Cerveja"));
    }

    @Test
    @DisplayName("PUT /api/produtos/{id} → quando não existe deve retornar status 404")
    void atualizar_quandoNaoExiste_deveRetornarNotFound() throws Exception {
        when(produtoService.atualizarProduto(eq(99L), any(Produto.class))).thenReturn(null);

        mockMvc.perform(put("/api/produtos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTOValido)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → quando existe deve retornar status 204")
    void deletar_quandoExiste_deveRetornarNoContent() throws Exception {
        doNothing().when(produtoService).deletarProduto(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/produtos/{id} → quando não existe deve retornar status 404")
    void deletar_quandoNaoExiste_deveRetornarNotFound() throws Exception {
        doThrow(new RuntimeException("Produto não encontrado"))
                .when(produtoService).deletarProduto(99L);

        mockMvc.perform(delete("/api/produtos/99"))
                .andExpect(status().isNotFound());
    }
}