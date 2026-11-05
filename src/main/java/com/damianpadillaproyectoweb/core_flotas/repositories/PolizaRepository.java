package com.damianpadillaproyectoweb.core_flotas.repositories;

import com.damianpadillaproyectoweb.core_flotas.models.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Integer> {

    // Consulta personalizada para el Dropdown Dependiente (Aseguradora -> Pólizas)
    List<Poliza> findByAseguradoraId(Integer aseguradoraId);
}