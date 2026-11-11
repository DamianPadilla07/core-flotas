package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.models.Institucion;
import com.damianpadillaproyectoweb.core_flotas.repositories.InstitucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instituciones")
@CrossOrigin(origins = "*")
public class InstitucionController {

    @Autowired
    private InstitucionRepository institucionRepository;

    @GetMapping
    public List<Institucion> listarInstituciones() {
        return institucionRepository.findAll();
    }

    @PostMapping
    public Institucion crearInstitucion(@RequestBody Institucion institucion) {
        return institucionRepository.save(institucion);
    }
}