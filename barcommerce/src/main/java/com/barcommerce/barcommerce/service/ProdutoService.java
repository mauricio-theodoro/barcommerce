package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.repository.CategoriaRepository;
import com.barcommerce.barcommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que encapsula toda a lógica de negócio para Produto.
 * Responsável por CRUD, validações de categoria e salvamento de imagens.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Retorna todos os produtos cadastrados.
     */
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo ID.
     */
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /**
     * Cria um novo produto. Valida se a categoria existe.
     *
     * @param produto Entidade com dados a persistir (categoria com apenas ID).
     * @return Optional com o produto salvo, ou vazio se categoria inválida.
     */
    public Optional<Produto> criarProduto(Produto produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            return Optional.empty();
        }
        return categoriaRepository.findById(produto.getCategoria().getId())
                .map(categoria -> {
                    produto.setCategoria(categoria);
                    return produtoRepository.save(produto);
                });
    }

    /**
     * Atualiza um produto existente. Valida existência do produto e da categoria.
     *
     * @param id             ID do produto a atualizar.
     * @param dadosAtualizados Entidade com novos dados (categoria com apenas ID).
     * @return Optional com o produto atualizado, ou vazio se não encontrado ou categoria inválida.
     */
    public Optional<Produto> atualizarProduto(Long id, Produto dadosAtualizados) {
        return produtoRepository.findById(id)
                .flatMap(produto -> {
                    // Atualiza campos básicos
                    produto.setNome(dadosAtualizados.getNome());
                    produto.setPreco(dadosAtualizados.getPreco());
                    produto.setEstoque(dadosAtualizados.getEstoque());
                    produto.setTipo(dadosAtualizados.getTipo());

                    // Valida categoria
                    if (dadosAtualizados.getCategoria() == null
                            || dadosAtualizados.getCategoria().getId() == null) {
                        return Optional.empty();
                    }
                    return categoriaRepository.findById(dadosAtualizados.getCategoria().getId())
                            .map(categoria -> {
                                produto.setCategoria(categoria);
                                return produtoRepository.save(produto);
                            });
                });
    }

    /**
     * Deleta um produto pelo ID.
     *
     * @param id ID do produto a remover.
     * @return true se deletado, false se não encontrado.
     */
    public boolean deletarProduto(Long id) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return true;
                }).orElse(false);
    }

    /**
     * Salva qualquer alteração no produto (útil para atualizações parciais como imagem).
     *
     * @param produto Entidade a salvar.
     * @return Produto persistido.
     */
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
}
