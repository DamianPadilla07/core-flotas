package com.damianpadillaproyectoweb.core_flotas.services;

import com.damianpadillaproyectoweb.core_flotas.dto.ReparacionRequestDTO;
import com.damianpadillaproyectoweb.core_flotas.models.*;
import com.damianpadillaproyectoweb.core_flotas.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TallerService {

    private final RepuestoRepository repuestoRepository;
    private final ReparacionRepository reparacionRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public TallerService(
            RepuestoRepository repuestoRepository,
            ReparacionRepository reparacionRepository,
            VehiculoRepository vehiculoRepository,
            UsuarioRepository usuarioRepository) {
        this.repuestoRepository = repuestoRepository;
        this.reparacionRepository = reparacionRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Repuesto> listarRepuestos() {
        return repuestoRepository.findAll();
    }

    public Repuesto crearRepuesto(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }

    public List<Reparacion> listarReparaciones() {
        return reparacionRepository.findAll();
    }

    @Transactional
    public Reparacion registrarReparacion(ReparacionRequestDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));

        Usuario jefeTaller = usuarioRepository.findById(dto.getJefeTallerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jefe de taller no encontrado"));

        EstadoReparacion estadoReparacion = (dto.getEstado() != null && !dto.getEstado().isBlank())
                ? EstadoReparacion.valueOf(dto.getEstado().toUpperCase())
                : EstadoReparacion.EN_PROGRESO;

        Reparacion reparacion = new Reparacion();
        reparacion.setVehiculo(vehiculo);
        reparacion.setJefeTaller(jefeTaller);
        reparacion.setFechaIngreso(dto.getFechaIngreso());
        reparacion.setEstado(estadoReparacion);

        for (ReparacionRequestDTO.ItemRepuestoDTO itemDto : dto.getDetalles()) {
            Repuesto repuesto = repuestoRepository.findById(itemDto.getRepuestoId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Repuesto no encontrado: " + itemDto.getRepuestoId()));

            double precioFinal = (itemDto.getPrecioUnitario() != null && itemDto.getPrecioUnitario() > 0)
                    ? itemDto.getPrecioUnitario()
                    : repuesto.getPrecioBase();

            DetalleReparacion detalle = new DetalleReparacion();
            detalle.setReparacion(reparacion);
            detalle.setRepuesto(repuesto);
            detalle.setCantidad(itemDto.getCantidad());
            detalle.setPrecioUnitario(precioFinal);
            detalle.setCubiertoPorSeguro(itemDto.getCubiertoPorSeguro());

            reparacion.getDetalles().add(detalle);
        }

        if (estadoReparacion == EstadoReparacion.PENDIENTE || estadoReparacion == EstadoReparacion.EN_PROGRESO) {
            vehiculo.setEstado(EstadoVehiculo.EN_TALLER);
            vehiculoRepository.save(vehiculo);
        }

        return reparacionRepository.save(reparacion);
    }

    @Transactional
    public Reparacion actualizarEstadoReparacion(Integer reparacionId, String nuevoEstado) {
        Reparacion reparacion = reparacionRepository.findById(reparacionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reparación no encontrada"));

        EstadoReparacion estado = EstadoReparacion.valueOf(nuevoEstado.toUpperCase());
        reparacion.setEstado(estado);

        Vehiculo vehiculo = reparacion.getVehiculo();
        if (estado == EstadoReparacion.COMPLETADO || estado == EstadoReparacion.CANCELADO) {
            vehiculo.setEstado(EstadoVehiculo.OPERATIVO);
            vehiculoRepository.save(vehiculo);
        }

        return reparacionRepository.save(reparacion);
    }
}