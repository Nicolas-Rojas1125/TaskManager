package com.taskmanager.reporte.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.reporte.dto.ReporteRequestDTO;
import com.taskmanager.reporte.model.Reporte;
import com.taskmanager.reporte.repository.ReporteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepo;

    public Reporte guardarReporte(ReporteRequestDTO dto) {
        Reporte reporte = new Reporte();
        reporte.setTitulo(dto.getTitulo());
        reporte.setTipo(dto.getTipo());
        reporte.setDatosJson(dto.getDatosJsonGenerados());
        reporte.setSolicitadoPor(dto.getSolicitadoPor());

        return reporteRepo.save(reporte);
    }

    public List<Reporte> listarHistorialReportes() {
        return reporteRepo.findAll();
    }

    public List<Reporte> listarReportesPorUsuario(UUID solicitadoPor) {
        return reporteRepo.findBySolicitadoPorOrderByFechaGeneracionDesc(solicitadoPor);
    }
    
    public Reporte obtenerReportePorId(UUID id) {
        return reporteRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
    }
}
