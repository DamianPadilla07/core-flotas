package com.damianpadillaproyectoweb.core_flotas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenFlotaDTO {
    private int totalVehiculos;
    private int optimos;
    private int moderados;
    private int criticos;
    private double totalUmbralDepreciacion;
    private double totalCapitalAsegurado;
    private double indiceRespaldoGlobal;
}