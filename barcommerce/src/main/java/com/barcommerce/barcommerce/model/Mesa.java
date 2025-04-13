package com.barcommerce.barcommerce.model;

import com.barcommerce.barcommerce.enums.StatusMesa;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identificacao; // Ex: "Mesa 01"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMesa status = StatusMesa.LIVRE;

    @Column(nullable = false)
    private Integer capacidade; // Número de lugares
}