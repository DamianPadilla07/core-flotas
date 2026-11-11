package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.models.Poliza;
import com.damianpadillaproyectoweb.core_flotas.repositories.PolizaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*")
public class PolizaController {

    @Autowired
    private PolizaRepository polizaRepository;

    @PostMapping
    public ResponseEntity<Poliza> crearPoliza(@RequestBody Poliza poliza) {
        Poliza nuevaPoliza = polizaRepository.save(poliza);
        return new ResponseEntity<>(nuevaPoliza, HttpStatus.CREATED);
    }
}