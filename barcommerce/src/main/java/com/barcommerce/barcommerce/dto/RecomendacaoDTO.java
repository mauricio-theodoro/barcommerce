package com.barcommerce.barcommerce.dto;

/**
 * DTO para enviar recomendações de produtos ao cliente.
 */
public class RecomendacaoDTO {
    private Long produtoId;
    private String nome;
    private Integer frequencia;

    public RecomendacaoDTO(Long produtoId, String nome, Integer frequencia) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.frequencia = frequencia;
    }

    // —— Getters e Setters ——————————————————————————————————
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getFrequencia() { return frequencia; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }
}