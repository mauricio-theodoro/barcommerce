package com.barcommerce.barcommerce.mapper;

import com.barcommerce.barcommerce.dto.ProdutoDTO;
import com.barcommerce.barcommerce.dto.ProdutoDTO.CategoriaRefDTO;
import com.barcommerce.barcommerce.enums.TipoProduto;
import com.barcommerce.barcommerce.model.Categoria;
import com.barcommerce.barcommerce.model.Produto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Mapper responsável pela conversão entre Entidade Produto e DTO
 *
 * <p>Implementa padrões robustos de conversão com tratamento de erros e validação</p>
 */
public class ProdutoMapper {

    private static final int PRECISAO_DECIMAL = 2;
    private static final RoundingMode MODO_ARREDONDAMENTO = RoundingMode.HALF_UP;

    /**
     * Converte DTO para Entidade (usado em operações de criação/atualização)
     *
     * @param dto Objeto de transferência de dados
     * @return Entidade Produto pronta para persistência
     * @throws IllegalArgumentException Se o tipo do produto for inválido
     */
    public static Produto toEntity(ProdutoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser nulo");
        }

        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());

        // Conversão segura do preço com arredondamento
        produto.setPreco(arredondarPreco(dto.getPreco()));

        produto.setEstoque(dto.getEstoque());
        produto.setImagemUrl(dto.getImagemUrl());
        produto.setAtivo(dto.getAtivo());

        // Conversão e validação do tipo do produto
        produto.setTipo(converterTipoProduto(dto.getTipo()));

        // Criação da relação com categoria
        produto.setCategoria(mapearCategoria(dto.getCategoria()));

        return produto;
    }

    /**
     * Converte Entidade para DTO (usado em operações de consulta)
     *
     * @param produto Entidade persistida
     * @return DTO pronto para serialização
     */
    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setEstoque(produto.getEstoque());
        dto.setImagemUrl(produto.getImagemUrl());
        dto.setAtivo(produto.getAtivo());
        dto.setTipo(produto.getTipo().name());

        // Mapeamento da categoria
        if (produto.getCategoria() != null) {
            CategoriaRefDTO categoriaRef = new CategoriaRefDTO();
            categoriaRef.setId(produto.getCategoria().getId());
            dto.setCategoria(categoriaRef);
        }

        return dto;
    }

    /**
     * Método privado para tratamento seguro do tipo de produto
     */
    private static TipoProduto converterTipoProduto(String tipo) {
        try {
            return TipoProduto.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Tipo de produto inválido: " + tipo +
                    ". Valores aceitos: " + java.util.Arrays.toString(TipoProduto.values()));
        }
    }

    /**
     * Método privado para mapeamento seguro da categoria
     */
    private static Categoria mapearCategoria(CategoriaRefDTO categoriaRef) {
        if (categoriaRef == null || categoriaRef.getId() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        Categoria categoria = new Categoria();
        categoria.setId(categoriaRef.getId());
        return categoria;
    }

    /**
     * Padroniza o arredondamento de valores monetários
     */
    private static BigDecimal arredondarPreco(BigDecimal preco) {
        return preco != null ?
                preco.setScale(PRECISAO_DECIMAL, MODO_ARREDONDAMENTO) :
                BigDecimal.ZERO.setScale(PRECISAO_DECIMAL);
    }
}