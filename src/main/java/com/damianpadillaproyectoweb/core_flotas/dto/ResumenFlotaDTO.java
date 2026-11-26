package com.damianpadillaproyectoweb.core_flotas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenFlotaDTO {
    private String institucionNombre;
    private int vehiculosEvaluados;
    private double ahorroPorSeguros;
    private double perdidasOperativas;
    private int cantidadRentables;
    private int cantidadObservacion;
    private int cantidadDarDeBaja;
}