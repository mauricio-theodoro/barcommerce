package com.barcommerce.barcommerce.service;

import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;
import com.barcommerce.barcommerce.repository.CategoriaRepository;
import com.barcommerce.barcommerce.repository.ProdutoRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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
     * Lista todos os produtos.
     * - @Cacheable: armazena o resultado em cache sob a chave "produtos::all"
     */
    @Cacheable(value = "produtos", key = "'all'")
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo ID.
     * - @Cacheable: armazena cada produto em "produtos::id"
     */
    @Cacheable(value = "produtos", key = "#id")
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    /**
     * Cria um novo produto. Valida se a categoria existe.
     *
     * @param produto Entidade com dados a persistir (categoria com apenas ID).
     * @return Optional com o produto salvo, ou vazio se categoria inválida.
     */
    /**
     * Cria um novo produto.
     * - @CacheEvict(allEntries=true): limpa cache de listagens e buscas,
     *   garantindo que retornos reflitam o novo registro.
     */
    @CacheEvict(value = "produtos", allEntries = true)
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
    /**
     * Atualiza um produto existente.
     * - @CacheEvict(allEntries=true): invalidar cache de produtos
     */
    @CacheEvict(value = "produtos", allEntries = true)
    public Optional<Produto> atualizarProduto(Long id, Produto dadosAtualizados) {
        return produtoRepository.findById(id)
                .flatMap(produto -> {
                    produto.setNome(dadosAtualizados.getNome());
                    produto.setPreco(dadosAtualizados.getPreco());
                    produto.setEstoque(dadosAtualizados.getEstoque());
                    produto.setTipo(dadosAtualizados.getTipo());
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
     * - @CacheEvict(allEntries=true): limpa cache para refletir remoção.
     */
    @CacheEvict(value = "produtos", allEntries = true)
    public boolean deletarProduto(Long id) {
        return produtoRepository.findById(id)
                .map(p -> {
                    produtoRepository.delete(p);
                    return true;
                }).orElse(false);
    }

    /**
     * Salva qualquer alteração no produto (e.g. imagem).
     * - @CacheEvict: invalida cache para manter consistência.
     */
    @CacheEvict(value = "produtos", allEntries = true)
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
}
