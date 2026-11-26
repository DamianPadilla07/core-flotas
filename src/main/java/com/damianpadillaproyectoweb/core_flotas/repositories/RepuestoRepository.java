package com.damianpadillaproyectoweb.core_flotas.repositories;

import com.damianpadillaproyectoweb.core_flotas.models.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {
}