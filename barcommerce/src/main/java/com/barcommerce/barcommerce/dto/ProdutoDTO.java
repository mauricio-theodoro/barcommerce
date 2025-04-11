package com.barcommerce.barcommerce.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para a entidade Produto.
 *
 * <p>Contém todas as validações necessárias para garantir a integridade
 * dos dados antes de chegar na camada de persistência.</p>
 */
public class ProdutoDTO {

    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String nome;

    @Size(max = 500, message = "A descrição não pode ultrapassar 500 caracteres.")
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser positivo.")
    @Digits(integer = 5, fraction = 2, message = "O preço deve ter no máximo 5 dígitos inteiros e 2 decimais.")
    private BigDecimal preco;

    @NotNull(message = "O estoque é obrigatório.")
    @PositiveOrZero(message = "O estoque não pode ser negativo.")
    private Integer estoque;

    @NotBlank(message = "O tipo do produto é obrigatório.")
    private String tipo; // Será convertido para enum no mapper

    @URL(message = "A URL da imagem deve ser válida.")
    @Size(max = 512, message = "A URL da imagem não pode exceder 512 caracteres.")
    private String imagemUrl;

    @NotNull(message = "O status ativo/inativo é obrigatório.")
    private Boolean ativo = true;

    @NotNull(message = "A categoria é obrigatória.")
    private CategoriaRefDTO categoria;

    /**
     * DTO interno para referência à categoria
     */
    public static class CategoriaRefDTO {
        @NotNull(message = "O ID da categoria é obrigatório.")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public CategoriaRefDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaRefDTO categoria) {
        this.categoria = categoria;
    }
}