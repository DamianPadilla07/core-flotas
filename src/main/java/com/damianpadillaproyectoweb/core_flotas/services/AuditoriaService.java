package com.damianpadillaproyectoweb.core_flotas.services;

import com.damianpadillaproyectoweb.core_flotas.dto.AuditoriaVehiculoDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.ResumenFlotaDTO;
import com.damianpadillaproyectoweb.core_flotas.models.Poliza;
import com.damianpadillaproyectoweb.core_flotas.models.Vehiculo;
import com.damianpadillaproyectoweb.core_flotas.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuditoriaService {

    private final VehiculoRepository vehiculoRepository;

    public AuditoriaService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<AuditoriaVehiculoDTO> auditarFlota(Integer institucionId) {
        List<Vehiculo> vehiculos = (institucionId != null)
                ? vehiculoRepository.findByInstitucionId(institucionId)
                : vehiculoRepository.findAll();

        return vehiculos.stream()
                .map(this::evaluarVehiculo)
                .toList();
    }

    public ResumenFlotaDTO obtenerResumenFlota(Integer institucionId) {
        List<AuditoriaVehiculoDTO> auditorias = auditarFlota(institucionId);

        int optimos = 0;
        int moderados = 0;
        int criticos = 0;
        double sumaUmbrales = 0.0;
        double sumaCoberturas = 0.0;

        for (AuditoriaVehiculoDTO item : auditorias) {
            sumaUmbrales += item.getUmbralDepreciacion();
            sumaCoberturas += item.getMontoCobertura();

            switch (item.getNivelRiesgo()) {
                case "OPTIMO" -> optimos++;
                case "MODERADO" -> moderados++;
                default -> criticos++;
            }
        }

        double indiceGlobal = (sumaUmbrales > 0)
                ? redondear(sumaCoberturas / sumaUmbrales)
                : 0.0;

        return new ResumenFlotaDTO(
                auditorias.size(),
                optimos,
                moderados,
                criticos,
                redondear(sumaUmbrales),
                redondear(sumaCoberturas),
                indiceGlobal
        );
    }

    private AuditoriaVehiculoDTO evaluarVehiculo(Vehiculo vehiculo) {
        Poliza poliza = vehiculo.getPoliza();
        double umbral = (vehiculo.getUmbralDepreciacion() != null) ? vehiculo.getUmbralDepreciacion() : 0.0;
        double cobertura = (poliza != null && poliza.getMontoCobertura() != null) ? poliza.getMontoCobertura() : 0.0;

        long diasParaVencer = (poliza != null && poliza.getFechaExpiracion() != null)
                ? ChronoUnit.DAYS.between(LocalDate.now(), poliza.getFechaExpiracion())
                : -1;

        double brecha = redondear(cobertura - umbral);
        double ratio = (umbral > 0) ? redondear(cobertura / umbral) : 0.0;

        String nivelRiesgo = determinarNivelRiesgo(diasParaVencer, cobertura, umbral, ratio);
        String dictamen = generarDictamen(diasParaVencer, brecha, ratio);

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
                nombreAseguradora,
                (poliza != null) ? poliza.getNumeroPoliza() : "N/A",
                (poliza != null) ? poliza.getFechaExpiracion() : null,
                diasParaVencer,
                redondear(umbral),
                redondear(cobertura),
                brecha,
                ratio,
                nivelRiesgo,
                dictamen
        );
    }

    private String determinarNivelRiesgo(long diasParaVencer, double cobertura, double umbral, double ratio) {
        if (diasParaVencer < 0 || cobertura < umbral) {
            return "CRITICO";
        }
        if (diasParaVencer <= 30 || ratio < 1.20) {
            return "MODERADO";
        }
        return "OPTIMO";
    }

    private String generarDictamen(long diasParaVencer, double brecha, double ratio) {
        if (diasParaVencer < 0) {
            return "Poliza vencida hace " + Math.abs(diasParaVencer) + " dias. Activo sin respaldo.";
        }
        if (brecha < 0) {
            return "Infraseguro detectado: deficit de $" + Math.abs(brecha) + " frente al umbral de depreciacion.";
        }
        if (diasParaVencer <= 30) {
            return "Cobertura vigente (" + ratio + "x), pero la poliza vence en " + diasParaVencer + " dias.";
        }
        if (ratio < 1.20) {
            return "Cobertura ajustada (" + ratio + "x). Se recomienda ampliar el margen sobre el umbral.";
        }
        return "Activo respaldado con indice de cobertura saludable (" + ratio + "x).";
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}