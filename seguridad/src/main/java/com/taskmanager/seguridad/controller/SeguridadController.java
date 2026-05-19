package com.taskmanager.seguridad.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.seguridad.dto.DeviceDTO;
import com.taskmanager.seguridad.model.RefreshToken;
import com.taskmanager.seguridad.model.SmartDevice;
import com.taskmanager.seguridad.service.SeguridadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/seguridad")
@RequiredArgsConstructor
public class SeguridadController {

    private final SeguridadService seguridadService;

    @PostMapping("/token/crear/{usuarioId}")
    public ResponseEntity<RefreshToken> crearRefreshToken(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(seguridadService.crearRefreshToken(usuarioId));
    }

    @PatchMapping("/token/revocar")
    public ResponseEntity<Void> revocarToken(@RequestParam String token) {
        seguridadService.revocarToken(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/dispositivo")
    public ResponseEntity<SmartDevice> registrarDispositivo(@Valid @RequestBody DeviceDTO dto) {
        return ResponseEntity.ok(seguridadService.registrarDispositivo(dto));
    }

    @GetMapping("/dispositivo/usuario/{usuarioId}")
    public ResponseEntity<List<SmartDevice>> obtenerDispositivos(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(seguridadService.obtenerDispositivosActivos(usuarioId));
    }

    @DeleteMapping("/dispositivo/{id}")
    public ResponseEntity<Void> desactivarDispositivo(@PathVariable UUID id) {
        seguridadService.desactivarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
}