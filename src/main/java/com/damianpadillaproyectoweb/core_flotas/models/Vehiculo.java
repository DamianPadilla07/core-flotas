package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La placa es obligatoria")
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false, length = 50)
    private String modelo;

    // 🚨 REQUISITO 1: VALIDACIÓN EN BACK-END DEL DATO SENSIBLE DEL CORE
    @NotNull(message = "El umbral de depreciación es obligatorio")
    @DecimalMin(value = "1000.00", message = "El umbral de rentabilidad debe ser mayor o igual a $1,000 para evitar divisiones por cero en el cálculo del Core")
    @Column(name = "umbral_depreciacion", nullable = false)
    private Double umbralDepreciacion;

    // REQUISITO 2: RELACIÓN CON PÓLIZA (Seleccionada dinámicamente)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_poliza", nullable = false)
    private Poliza poliza;
}