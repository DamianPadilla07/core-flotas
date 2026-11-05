package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.models.Vehiculo;
import com.damianpadillaproyectoweb.core_flotas.repositories.VehiculoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @GetMapping
    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    //  EL REQUISITO 1 DEL PROFE: Guardar con Validación de Dato Sensible
    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
        // Si el "umbralDepreciacion" viene en $0 o negativo desde Angular,
        // el código NUNCA llegará a esta línea. Spring Boot lo intercepta por el @Valid
        // y devuelve un error 400 Bad Request automáticamente gracias a tu modelo.

        Vehiculo nuevoVehiculo = vehiculoRepository.save(vehiculo);
        return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
    }
}