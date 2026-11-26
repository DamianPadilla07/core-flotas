package com.damianpadillaproyectoweb.core_flotas.repositories;

import com.damianpadillaproyectoweb.core_flotas.models.DetalleReparacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleReparacionRepository extends JpaRepository<DetalleReparacion, Integer> {
}