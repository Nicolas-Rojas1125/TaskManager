package com.taskmanager.tarea.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.tarea.dto.TareaDTO;
import com.taskmanager.tarea.model.EstadoTarea;
import com.taskmanager.tarea.model.Tarea;
import com.taskmanager.tarea.service.TareaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<Tarea>> listarTareas() {
        return ResponseEntity.ok(tareaService.listarTareas());
    }

    @GetMapping("/asignado/{asignadoId}")
    public ResponseEntity<List<Tarea>> listarPorAsignado(@PathVariable UUID asignadoId) {
        return ResponseEntity.ok(tareaService.listarTareasPorAsignado(asignadoId));
    }

    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@Valid @RequestBody TareaDTO dto) {
        return ResponseEntity.ok(tareaService.crearTarea(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Tarea> cambiarEstado(@PathVariable UUID id, @RequestParam EstadoTarea estado) {
        return ResponseEntity.ok(tareaService.cambiarEstado(id, estado));
    }
}