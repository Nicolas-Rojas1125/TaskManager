package com.taskmanager.notificacion.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.notificacion.dto.NotificacionDTO;
import com.taskmanager.notificacion.model.Notificacion;
import com.taskmanager.notificacion.service.NotificacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        return ResponseEntity.ok(notificacionService.crearNotificacion(dto));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/noleidas")
    public ResponseEntity<List<Notificacion>> listarNoLeidasPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorUsuario(usuarioId));
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable UUID id) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }
}