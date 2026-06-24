package com.taskmanager.adjunto.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.adjunto.dto.AdjuntoDTO;
import com.taskmanager.adjunto.model.Adjunto;
import com.taskmanager.adjunto.service.AdjuntoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/adjuntos")
@RequiredArgsConstructor
public class AdjuntoController {

    private final AdjuntoService adjuntoService;

    @PostMapping
    public ResponseEntity<Adjunto> registrarAdjunto(@Valid @RequestBody AdjuntoDTO dto) {
        return ResponseEntity.ok(adjuntoService.registrarAdjunto(dto));
    }

    @GetMapping("/tarea/{taskId}")
    public ResponseEntity<List<Adjunto>> listarPorTarea(@PathVariable UUID taskId) {
        return ResponseEntity.ok(adjuntoService.listarPorTarea(taskId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdjunto(@PathVariable UUID id) {
        adjuntoService.eliminarAdjunto(id);
        return ResponseEntity.noContent().build();
    }
}