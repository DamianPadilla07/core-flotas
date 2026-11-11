package com.damianpadillaproyectoweb.core_flotas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "polizas")
public class Poliza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El número de póliza es obligatorio")
    @Column(name = "numero_poliza", nullable = false, length = 50)
    private String numeroPoliza;

    @NotNull(message = "El monto de cobertura es obligatorio")
    @Column(name = "monto_cobertura", nullable = false)
    private Double montoCobertura;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDate fechaExpiracion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aseguradora", nullable = false)
    private Aseguradora aseguradora;
}