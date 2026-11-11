package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.models.Aseguradora;
import com.damianpadillaproyectoweb.core_flotas.models.Poliza;
import com.damianpadillaproyectoweb.core_flotas.repositories.AseguradoraRepository;
import com.damianpadillaproyectoweb.core_flotas.repositories.PolizaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aseguradoras")
@CrossOrigin(origins = "*") // ¡VITAL! Sin esto, Angular bloqueará las peticiones por seguridad (CORS)
public class AseguradoraController {

    @Autowired
    private AseguradoraRepository aseguradoraRepository;

    @Autowired
    private PolizaRepository polizaRepository;

    // URL para llenar el Dropdown 1 (Todas las aseguradoras)
    @GetMapping
    public List<Aseguradora> listarTodas() {
        return aseguradoraRepository.findAll();
    }

    // 🚨 EL REQUISITO 2 DEL PROFE: URL para llenar el Dropdown 2 (Pólizas dependientes)
    // Ejemplo: Si Angular llama a /api/aseguradoras/1/polizas, devuelve solo las de Mapfre
    @GetMapping("/{id}/polizas")
    public List<Poliza> obtenerPolizasPorAseguradora(@PathVariable Integer id) {
        return polizaRepository.findByAseguradoraId(id);
    }

    // Guardar nueva aseguradora desde Angular
    @PostMapping
    public ResponseEntity<Aseguradora> crearAseguradora(@RequestBody Aseguradora aseguradora) {
        Aseguradora nuevaAseguradora = aseguradoraRepository.save(aseguradora);
        return new ResponseEntity<>(nuevaAseguradora, HttpStatus.CREATED);
    }
}