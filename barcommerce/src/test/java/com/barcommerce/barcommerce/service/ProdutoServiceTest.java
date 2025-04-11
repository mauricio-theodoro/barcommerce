package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.repository.CategoriaRepository;
import com.barcommerce.barcommerce.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    private ProdutoRepository produtoRepository;
    private CategoriaRepository categoriaRepository;
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        produtoRepository = mock(ProdutoRepository.class);
        categoriaRepository = mock(CategoriaRepository.class);
        produtoService = new ProdutoService(produtoRepository, categoriaRepository);
    }

    @Test
    void deveListarTodosProdutos() {
        List<Produto> produtos = List.of(new Produto(), new Produto());
        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> resultado = produtoService.listarTodos();

        assertEquals(2, resultado.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setId(1L);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void deveCriarProdutoComCategoriaValida() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        Produto produto = new Produto();
        produto.setCategoria(categoria);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(produto)).thenReturn(produto);

        Optional<Produto> resultado = produtoService.criarProduto(produto);

        assertTrue(resultado.isPresent());
        verify(produtoRepository).save(produto);
    }

    @Test
    void naoDeveCriarProdutoComCategoriaInvalida() {
        Produto produto = new Produto();
        produto.setCategoria(new Categoria()); // id null

        Optional<Produto> resultado = produtoService.criarProduto(produto);

        assertTrue(resultado.isEmpty());
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void deveDeletarProdutoExistente() {
        Produto produto = new Produto();
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        boolean deletado = produtoService.deletarProduto(1L);

        assertTrue(deletado);
        verify(produtoRepository).delete(produto);
    }

    @Test
    void naoDeveDeletarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        boolean deletado = produtoService.deletarProduto(1L);

        assertFalse(deletado);
        verify(produtoRepository, never()).delete(any());
    }
}
