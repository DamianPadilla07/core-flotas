package com.damianpadillaproyectoweb.core_flotas.repositories;

import com.damianpadillaproyectoweb.core_flotas.models.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {
    List<Reparacion> findByVehiculoIdOrderByFechaIngresoAsc(Integer vehiculoId);
    List<Reparacion> findByVehiculoIdAndFechaIngresoBetweenOrderByFechaIngresoAsc(
            Integer vehiculoId, LocalDate inicio, LocalDate fin);
}