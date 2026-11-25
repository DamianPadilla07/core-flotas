package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.dto.AuditoriaVehiculoDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.ResumenFlotaDTO;
import com.damianpadillaproyectoweb.core_flotas.services.AuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin(origins = "*")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/vehiculos")
    public ResponseEntity<List<AuditoriaVehiculoDTO>> auditarVehiculos(
            @RequestParam(required = false) Integer institucionId) {
        return ResponseEntity.ok(auditoriaService.auditarFlota(institucionId));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenFlotaDTO> obtenerResumen(
            @RequestParam(required = false) Integer institucionId) {
        return ResponseEntity.ok(auditoriaService.obtenerResumenFlota(institucionId));
    }
}
