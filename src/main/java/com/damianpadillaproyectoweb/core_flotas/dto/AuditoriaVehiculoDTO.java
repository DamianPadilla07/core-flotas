package com.damianpadillaproyectoweb.core_flotas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaVehiculoDTO {
    private Integer vehiculoId;
    private String placa;
    private String modelo;
    private String institucion;
    private String aseguradora;
    private String numeroPoliza;
    private LocalDate fechaExpiracion;
    private long diasParaVencer;
    private double umbralDepreciacion;
    private double montoCobertura;
    private double brechaCobertura;
    private double ratioCobertura;
    private String nivelRiesgo;
    private String dictamen;
}