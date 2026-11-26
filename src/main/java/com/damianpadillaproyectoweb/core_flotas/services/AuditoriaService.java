package com.damianpadillaproyectoweb.core_flotas.services;

import com.damianpadillaproyectoweb.core_flotas.dto.AuditoriaVehiculoDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.DetalleAuditoriaItemDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.ResumenFlotaDTO;
import com.damianpadillaproyectoweb.core_flotas.models.*;
import com.damianpadillaproyectoweb.core_flotas.repositories.InstitucionRepository;
import com.damianpadillaproyectoweb.core_flotas.repositories.ReparacionRepository;
import com.damianpadillaproyectoweb.core_flotas.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditoriaService {

    private static final double PENALIZACION_REINCIDENCIA = 50.0;

    private final VehiculoRepository vehiculoRepository;
    private final ReparacionRepository reparacionRepository;
    private final InstitucionRepository institucionRepository;

    public AuditoriaService(
            VehiculoRepository vehiculoRepository,
            ReparacionRepository reparacionRepository,
            InstitucionRepository institucionRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.reparacionRepository = reparacionRepository;
        this.institucionRepository = institucionRepository;
    }

    public List<AuditoriaVehiculoDTO> auditarFlota(
            Integer institucionId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Vehiculo> vehiculos = (institucionId != null)
                ? vehiculoRepository.findByInstitucionId(institucionId)
                : vehiculoRepository.findAll();

        return vehiculos.stream()
                .map(v -> evaluarVehiculo(v, fechaInicio, fechaFin))
                .toList();
    }

    public ResumenFlotaDTO obtenerResumenFlota(
            Integer institucionId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<AuditoriaVehiculoDTO> auditorias = auditarFlota(institucionId, fechaInicio, fechaFin);

        String nombreInstitucion = "Consolidado Global";
        if (institucionId != null) {
            nombreInstitucion = institucionRepository.findById(institucionId)
                    .map(Institucion::getRazonSocial)
                    .orElse("Institución #" + institucionId);
        }

        double ahorroTotal = 0.0;
        double perdidasTotales = 0.0;
        int rentables = 0;
        int observacion = 0;
        int darDeBaja = 0;

        for (AuditoriaVehiculoDTO item : auditorias) {
            ahorroTotal += item.getAhorroSeguros();
            perdidasTotales += item.getGastoNeto();

            switch (item.getEstadoSugerido()) {
                case "RENTABLE" -> rentables++;
                case "EN OBSERVACIÓN" -> observacion++;
                default -> darDeBaja++;
            }
        }

        return new ResumenFlotaDTO(
                nombreInstitucion,
                auditorias.size(),
                redondear(ahorroTotal),
                redondear(perdidasTotales),
                rentables,
                observacion,
                darDeBaja
        );
    }

    private AuditoriaVehiculoDTO evaluarVehiculo(
            Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reparacion> reparaciones = (fechaInicio != null && fechaFin != null)
                ? reparacionRepository.findByVehiculoIdAndFechaIngresoBetweenOrderByFechaIngresoAsc(
                vehiculo.getId(), fechaInicio, fechaFin)
                : reparacionRepository.findByVehiculoIdOrderByFechaIngresoAsc(vehiculo.getId());

        Map<Integer, Integer> historialFallasPorRepuesto = new HashMap<>();
        List<DetalleAuditoriaItemDTO> desglose = new ArrayList<>();

        double ahorroSeguros = 0.0;
        double gastoTotalAcumulado = 0.0;

        for (Reparacion rep : reparaciones) {
            if (rep.getEstado() == EstadoReparacion.CANCELADO) {
                continue;
            }

            for (DetalleReparacion det : rep.getDetalles()) {
                double costoBase = redondear(det.getCantidad() * det.getPrecioUnitario());
                boolean cubierto = Boolean.TRUE.equals(det.getCubiertoPorSeguro());

                double coberturaMonto = cubierto ? costoBase : 0.0;
                int porcentajeCobertura = cubierto ? 100 : 0;
                double gastoNetoItem = cubierto ? 0.0 : costoBase;

                Integer repuestoId = det.getRepuesto().getId();
                int fallasPrevias = historialFallasPorRepuesto.getOrDefault(repuestoId, 0);
                double penalizacion = fallasPrevias * PENALIZACION_REINCIDENCIA;
                historialFallasPorRepuesto.put(repuestoId, fallasPrevias + 1);

                double gastoRealItem = redondear(gastoNetoItem + penalizacion);

                ahorroSeguros += coberturaMonto;
                gastoTotalAcumulado += gastoRealItem;

                desglose.add(new DetalleAuditoriaItemDTO(
                        rep.getFechaIngreso(),
                        det.getRepuesto().getNombre(),
                        det.getCantidad(),
                        costoBase,
                        coberturaMonto,
                        porcentajeCobertura,
                        penalizacion,
                        gastoRealItem
                ));
            }
        }

        double umbral = (vehiculo.getUmbralDepreciacion() != null) ? vehiculo.getUmbralDepreciacion() : 0.0;
        double porcentajeRiesgo = (umbral > 0)
                ? redondear((gastoTotalAcumulado / umbral) * 100.0)
                : 0.0;

        String estadoSugerido = clasificarRentabilidad(porcentajeRiesgo);

        Poliza poliza = vehiculo.getPoliza();
        String nombreInstitucion = (vehiculo.getInstitucion() != null)
                ? vehiculo.getInstitucion().getRazonSocial()
                : "Sin asignar";
        String nombreAseguradora = (poliza != null && poliza.getAseguradora() != null)
                ? poliza.getAseguradora().getNombre()
                : "Sin aseguradora";

        return new AuditoriaVehiculoDTO(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                vehiculo.getModelo(),
                nombreInstitucion,
                (poliza != null) ? poliza.getNumeroPoliza() : "N/A",
                nombreAseguradora,
                redondear(umbral),
                redondear(ahorroSeguros),
                redondear(gastoTotalAcumulado),
                porcentajeRiesgo,
                estadoSugerido,
                reparaciones.size(),
                desglose
        );
    }

    private String clasificarRentabilidad(double porcentajeRiesgo) {
        if (porcentajeRiesgo >= 100.0) {
            return "DAR DE BAJA";
        }
        if (porcentajeRiesgo >= 80.0) {
            return "EN OBSERVACIÓN";
        }
        return "RENTABLE";
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}