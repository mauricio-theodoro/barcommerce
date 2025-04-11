package com.barcommerce.barcommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.barcommerce.barcommerce.enums.TipoProduto;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade que representa um produto comercializado no bar.
 *
 * <p>Mapeada para a tabela 'produtos' no banco de dados com todas as
 * configurações necessárias para operações JPA.</p>
 */
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoProduto tipo;

    @Column(name = "imagem_url", length = 512)
    private String imagemUrl;

    @Column(nullable = false)
    private Boolean ativo = true;

    /**
     * Relacionamento Many-to-One entre Produto e Categoria.
     * A categoria de cada produto é representada por uma chave estrangeira.
     */
    @JsonBackReference  // Evita recursão infinita ao serializar a categoria associada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Construtores

    // Construtor padrão necessário para JPA
    public Produto() {
    }

    /**
     * Construtor completo para criação de produto.
     */
    public Produto(Long id, String nome, String descricao, BigDecimal preco,
                   Integer estoque, TipoProduto tipo, String imagemUrl,
                   Boolean ativo, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.tipo = tipo;
        this.imagemUrl = imagemUrl;
        this.ativo = ativo;
        this.categoria = categoria;
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

    public TipoProduto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProduto tipo) {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Métodos utilitários
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", preco=" + preco +
                '}';
    }
}
