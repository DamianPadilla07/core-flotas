package com.damianpadillaproyectoweb.core_flotas.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "detalles_reparacion")
public class DetalleReparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reparacion", nullable = false)
    private Reparacion reparacion;

    @NotNull(message = "El repuesto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_repuesto", nullable = false)
    private Repuesto repuesto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a cero")
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "cubierto_por_seguro", nullable = false)
    private Boolean cubiertoPorSeguro = false;
}