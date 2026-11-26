package com.damianpadillaproyectoweb.core_flotas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleAuditoriaItemDTO {
    private LocalDate fecha;
    private String repuesto;
    private int cantidad;
    private double costoBase;
    private double coberturaSeguro;
    private int porcentajeCobertura;
    private double penalizacionReincidencia;
    private double gastoRealInstitucion;
}