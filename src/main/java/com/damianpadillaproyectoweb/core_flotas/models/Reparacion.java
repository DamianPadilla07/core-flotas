package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "reparaciones")
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "El vehículo es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @NotNull(message = "El jefe de taller responsable es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_jefe_taller", nullable = false)
    private Usuario jefeTaller;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoReparacion estado = EstadoReparacion.PENDIENTE;

    @OneToMany(mappedBy = "reparacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleReparacion> detalles = new ArrayList<>();
}