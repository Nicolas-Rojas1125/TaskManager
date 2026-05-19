package com.taskmanager.reporte.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.reporte.dto.ReporteRequestDTO;
import com.taskmanager.reporte.model.Reporte;
import com.taskmanager.reporte.service.ReporteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping("/generar")
    public ResponseEntity<Reporte> generarReporte(@Valid @RequestBody ReporteRequestDTO dto) {
        return ResponseEntity.ok(reporteService.guardarReporte(dto));
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> listarHistorial() {
        return ResponseEntity.ok(reporteService.listarHistorialReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(reporteService.obtenerReportePorId(id));
    }

    @GetMapping("/usuario/{solicitadoPor}")
    public ResponseEntity<List<Reporte>> listarPorUsuario(@PathVariable UUID solicitadoPor) {
        return ResponseEntity.ok(reporteService.listarReportesPorUsuario(solicitadoPor));
    }
}