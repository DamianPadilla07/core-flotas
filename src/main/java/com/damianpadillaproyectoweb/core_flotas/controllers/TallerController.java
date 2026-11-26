package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.dto.ReparacionRequestDTO;
import com.damianpadillaproyectoweb.core_flotas.models.Reparacion;
import com.damianpadillaproyectoweb.core_flotas.models.Repuesto;
import com.damianpadillaproyectoweb.core_flotas.services.TallerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/taller")
@CrossOrigin(origins = "*")
public class TallerController {

    private final TallerService tallerService;

    public TallerController(TallerService tallerService) {
        this.tallerService = tallerService;
    }

    @GetMapping("/repuestos")
    public ResponseEntity<List<Repuesto>> listarRepuestos() {
        return ResponseEntity.ok(tallerService.listarRepuestos());
    }

    @PostMapping("/repuestos")
    public ResponseEntity<Repuesto> crearRepuesto(@Valid @RequestBody Repuesto repuesto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.crearRepuesto(repuesto));
    }

    @GetMapping("/reparaciones")
    public ResponseEntity<List<Reparacion>> listarReparaciones() {
        return ResponseEntity.ok(tallerService.listarReparaciones());
    }

    @PostMapping("/reparaciones")
    public ResponseEntity<Reparacion> registrarReparacion(@Valid @RequestBody ReparacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.registrarReparacion(dto));
    }

    @PatchMapping("/reparaciones/{id}/estado")
    public ResponseEntity<Reparacion> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(tallerService.actualizarEstadoReparacion(id, body.get("estado")));
    }
}