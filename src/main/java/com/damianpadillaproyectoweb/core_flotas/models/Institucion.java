package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "instituciones")
public class Institucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El RUC es obligatorio")
    @Column(nullable = false, length = 20, unique = true)
    private String ruc;

    @NotBlank(message = "La Razón Social es obligatoria")
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;
}