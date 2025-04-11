package com.barcommerce.barcommerce.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

/**
 * Representa uma categoria de produtos no sistema (ex: Bebidas, Comidas, etc).
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    /**
     * Uma categoria pode conter vários produtos.
     * mappedBy = "categoria" refere-se ao campo "categoria" na entidade Produto.
     * cascade = CascadeType.ALL permite salvar/atualizar/deletar em cascata.
     */
    @JsonManagedReference  // Evita recursão infinita ao serializar a lista de produtos
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos;

    // Construtor vazio (obrigatório para JPA)
    public Categoria() {
    }

    // Construtor com todos os campos
    public Categoria(Long id, String nome, String descricao, List<Produto> produtos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.produtos = produtos;
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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
