package com.damianpadillaproyectoweb.core_flotas.controllers;

import com.damianpadillaproyectoweb.core_flotas.dto.LoginRequestDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.RegistroUsuarioDTO;
import com.damianpadillaproyectoweb.core_flotas.models.Usuario;
import com.damianpadillaproyectoweb.core_flotas.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody RegistroUsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.autenticar(dto));
    }
}