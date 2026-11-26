package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "repuestos")
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El código del repuesto es obligatorio")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "El nombre del repuesto es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio base debe ser mayor a cero")
    @Column(name = "precio_base", nullable = false)
    private Double precioBase;
}