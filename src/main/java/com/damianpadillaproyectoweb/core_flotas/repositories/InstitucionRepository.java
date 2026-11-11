package com.damianpadillaproyectoweb.core_flotas.repositories;

import com.damianpadillaproyectoweb.core_flotas.models.Institucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {
}