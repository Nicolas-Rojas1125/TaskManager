package com.taskmanager.auditoria.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.auditoria.dto.AuditoriaDTO;
import com.taskmanager.auditoria.model.Auditoria;
import com.taskmanager.auditoria.service.AuditoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @PostMapping
    public ResponseEntity<Auditoria> registrarAccion(@Valid @RequestBody AuditoriaDTO dto) {
        return ResponseEntity.ok(auditoriaService.registrarAccion(dto));
    }

    @GetMapping
    public ResponseEntity<List<Auditoria>> listarTodo() {
        return ResponseEntity.ok(auditoriaService.listarTodo());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Auditoria>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(auditoriaService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/tarea/{taskId}")
    public ResponseEntity<List<Auditoria>> listarPorTarea(@PathVariable UUID taskId) {
        return ResponseEntity.ok(auditoriaService.listarPorTarea(taskId));
    }
}