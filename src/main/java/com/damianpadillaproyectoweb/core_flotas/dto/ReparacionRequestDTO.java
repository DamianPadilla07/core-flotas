package com.damianpadillaproyectoweb.core_flotas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ReparacionRequestDTO {

    @NotNull(message = "El vehículo es obligatorio")
    private Integer vehiculoId;

    @NotNull(message = "El jefe de taller es obligatorio")
    private Integer jefeTallerId;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    private String estado;

    @NotEmpty(message = "Debe incluir al menos un repuesto o intervención")
    @Valid
    private List<ItemRepuestoDTO> detalles;

    @Data
    public static class ItemRepuestoDTO {
        @NotNull(message = "El repuesto es obligatorio")
        private Integer repuestoId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad mínima es 1")
        private Integer cantidad;

        private Double precioUnitario;

        @NotNull(message = "Debe indicar si está cubierto por seguro")
        private Boolean cubiertoPorSeguro;
    }
}