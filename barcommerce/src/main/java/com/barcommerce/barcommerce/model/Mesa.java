package com.barcommerce.barcommerce.model;

import com.barcommerce.barcommerce.enums.StatusMesa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa uma mesa no restaurante/bar, incluindo coordenadas para validação de localização.
 */
@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Identificação da mesa é obrigatória.")
    @Column(nullable = false, unique = true)
    private String identificacao;

    @NotNull(message = "Capacidade da mesa é obrigatória.")
    @Column(nullable = false)
    private Integer capacidade;

    @NotNull(message = "Status da mesa é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMesa status = StatusMesa.LIVRE;

    /**
     * Latitude da mesa, em graus decimais.
     */
    @NotNull(message = "Latitude é obrigatória")
    @Column(nullable = false)
    private Double latitude;

    /**
     * Longitude da mesa, em graus decimais.
     */
    @NotNull(message = "Longitude é obrigatória")
    @Column(nullable = false)
    private Double longitude;


    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public StatusMesa getStatus() { return status; }
    public void setStatus(StatusMesa status) { this.status = status; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}