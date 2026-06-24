package com.taskmanager.usuario.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.usuario.dto.UsuarioDTO;
import com.taskmanager.usuario.dto.UsuarioLoginRequest;
import com.taskmanager.usuario.dto.UsuarioResponse;
import com.taskmanager.usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable UUID id){
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponse> obtenerPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {    
        return ResponseEntity.ok(usuarioService.crearUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@PathVariable UUID id, @Valid @RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(usuarioService.modificarUsuario(id, dto));
    }
    
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> loginUsuario(@Valid @RequestBody UsuarioLoginRequest login ){
        return ResponseEntity.ok(usuarioService.loginUsuario(login));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable UUID id){
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}