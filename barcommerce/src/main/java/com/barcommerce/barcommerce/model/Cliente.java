package com.barcommerce.barcommerce.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

/**
 * Representa um cliente no sistema (consumidor que realiza pedidos).
 * Inclui credenciais (senha criptografada) e data de nascimento para
 * futura integração de fidelização.
 */
@Entity
@Table(
        name = "clientes",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class Cliente extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "E‑mail é obrigatório")
    @Email(message = "E‑mail deve ser válido")
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false, length = 20)
    private String telefone;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha; // armazenada criptografada (BCrypt)

    @Past(message = "Data de nascimento deve ser no passado")
    @Column(
            name = "data_nascimento",
            nullable = false,
            columnDefinition = "DATE DEFAULT '1970-01-01'"
    )
    private LocalDate dataNascimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(name = "pontos_fidelidade", nullable = false)
    private Integer pontosFidelidade = 0;  // inicia em zero


// getters/setters

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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    public @NotBlank(message = "Senha é obrigatória") String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank(message = "Senha é obrigatória") String senha) {
        this.senha = senha;
    }

    public @Past(message = "Data de nascimento deve ser no passado") LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(@Past(message = "Data de nascimento deve ser no passado") LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getPontosFidelidade() {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(Integer pontosFidelidade) {
        this.pontosFidelidade = pontosFidelidade;
    }
}
