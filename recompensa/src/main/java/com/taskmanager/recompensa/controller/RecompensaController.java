package com.taskmanager.recompensa.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.recompensa.dto.RecompensaDTO;
import com.taskmanager.recompensa.model.Recompensa;
import com.taskmanager.recompensa.service.RecompensaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/gamificacion")
@RequiredArgsConstructor
public class RecompensaController {

    private final RecompensaService recompensaService;


    @PostMapping("/otorgar")
    public ResponseEntity<Recompensa> otorgarPuntos(@Valid @RequestBody RecompensaDTO dto) {
        return ResponseEntity.ok(recompensaService.otorgarPuntos(dto));
    }

    @GetMapping("/usuario/{usuarioId}/historial")
    public ResponseEntity<List<Recompensa>> obtenerHistorial(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(recompensaService.obtenerHistorial(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Integer> obtenerPuntosTotales(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(recompensaService.obtenerPuntosTotales(usuarioId));
    }
}