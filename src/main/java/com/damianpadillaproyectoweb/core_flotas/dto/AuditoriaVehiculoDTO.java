package com.damianpadillaproyectoweb.core_flotas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaVehiculoDTO {
    private Integer vehiculoId;
    private String placa;
    private String modelo;
    private String institucion;
    private String numeroPoliza;
    private String aseguradora;
    private double umbralMaximo;
    private double ahorroSeguros;
    private double gastoNeto;
    private double porcentajeRiesgo;
    private String estadoSugerido;
    private int totalReparaciones;
    private List<DetalleAuditoriaItemDTO> detalles;
}