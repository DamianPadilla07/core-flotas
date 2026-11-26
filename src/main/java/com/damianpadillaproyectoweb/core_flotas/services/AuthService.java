package com.damianpadillaproyectoweb.core_flotas.services;

import com.damianpadillaproyectoweb.core_flotas.dto.LoginRequestDTO;
import com.damianpadillaproyectoweb.core_flotas.dto.RegistroUsuarioDTO;
import com.damianpadillaproyectoweb.core_flotas.models.Rol;
import com.damianpadillaproyectoweb.core_flotas.models.Usuario;
import com.damianpadillaproyectoweb.core_flotas.repositories.RolRepository;
import com.damianpadillaproyectoweb.core_flotas.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public Usuario registrarUsuario(RegistroUsuarioDTO dto) {
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        String nombreRolNormalizado = dto.getRol().trim().toUpperCase();
        Rol rol = rolRepository.findByNombre(nombreRolNormalizado)
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre(nombreRolNormalizado);
                    nuevoRol.setDescripcion("Rol operativo: " + nombreRolNormalizado);
                    return rolRepository.save(nuevoRol);
                });

        Usuario usuario = new Usuario();
        usuario.setCorreo(dto.getCorreo().trim().toLowerCase());
        usuario.setNombre(dto.getNombre().trim());
        usuario.setApellido(dto.getApellido().trim());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario autenticar(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales incorrectas"));

        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        return usuario;
    }
}