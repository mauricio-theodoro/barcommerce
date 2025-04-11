package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.repository.CategoriaRepository;
import com.barcommerce.barcommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Optional<Produto> criarProduto(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            return Optional.empty();
        }

        Optional<Categoria> categoria = categoriaRepository.findById(produto.getCategoria().getId());
        if (categoria.isEmpty()) {
            return Optional.empty();
        }

        produto.setCategoria(categoria.get());
        Produto salvo = produtoRepository.save(produto);
        return Optional.of(salvo);
    }

    public Optional<Produto> atualizarProduto(Long id, Produto dadosAtualizados) {
        return produtoRepository.findById(id).map(produto -> {
            produto.setNome(dadosAtualizados.getNome());
            produto.setPreco(dadosAtualizados.getPreco());
            produto.setEstoque(dadosAtualizados.getEstoque());
            produto.setTipo(dadosAtualizados.getTipo());

            if (dadosAtualizados.getCategoria() == null || dadosAtualizados.getCategoria().getId() == null) {
                return null;
            }

            Optional<Categoria> categoria = categoriaRepository.findById(dadosAtualizados.getCategoria().getId());
            if (categoria.isEmpty()) {
                return null;
            }

            produto.setCategoria(categoria.get());
            return produtoRepository.save(produto);
        });
    }

    public boolean deletarProduto(Long id) {
        return produtoRepository.findById(id).map(produto -> {
            produtoRepository.delete(produto);
            return true;
        }).orElse(false);
    }
}
