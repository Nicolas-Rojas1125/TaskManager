package com.taskmanager.equipo.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.equipo.dto.EquipoDTO;
import com.taskmanager.equipo.dto.MiembroDTO;
import com.taskmanager.equipo.model.Equipo;
import com.taskmanager.equipo.model.MiembroEquipo;
import com.taskmanager.equipo.service.EquipoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @PostMapping
    public ResponseEntity<Equipo> crearEquipo(@Valid @RequestBody EquipoDTO dto) {
        return ResponseEntity.ok(equipoService.crearEquipo(dto));
    }

    @PostMapping("/{equipoId}/miembros")
    public ResponseEntity<MiembroEquipo> agregarMiembro(
            @PathVariable UUID equipoId, 
            @Valid @RequestBody MiembroDTO dto) {
        return ResponseEntity.ok(equipoService.agregarMiembro(equipoId, dto));
    }

    @GetMapping("/{equipoId}/miembros")
    public ResponseEntity<List<MiembroEquipo>> listarMiembros(@PathVariable UUID equipoId) {
        return ResponseEntity.ok(equipoService.listarMiembrosDeEquipo(equipoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MiembroEquipo>> listarEquiposDeUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(equipoService.listarEquiposDeUsuario(usuarioId));
    }
}
