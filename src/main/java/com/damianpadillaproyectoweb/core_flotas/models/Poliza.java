package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "polizas")
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El número de póliza es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String numeroPoliza;

    @NotNull(message = "El monto de cobertura es obligatorio")
    @DecimalMin(value = "1.00", message = "El monto de cobertura debe ser mayor a 0")
    @Column(nullable = false)
    private Double montoCobertura;

    // RELACIÓN MUCHOS A UNO: Muchas pólizas pertenecen a una sola Aseguradora
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aseguradora", nullable = false)
    private Aseguradora aseguradora;
}