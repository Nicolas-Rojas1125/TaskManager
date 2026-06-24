package com.taskmanager.reporte.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.reporte.dto.ReporteRequestDTO;
import com.taskmanager.reporte.model.Reporte;
import com.taskmanager.reporte.repository.ReporteRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.reporte.client.UsuarioClient;
import com.taskmanager.reporte.client.dto.UsuarioDTO;
import com.taskmanager.reporte.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private final UsuarioClient usuarioClient;

    private final ReporteRepository reporteRepo;

    public Reporte guardarReporte(ReporteRequestDTO dto) {
        validarUsuario(dto.getSolicitadoPor(), "solicitante");
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

    // ---- Validacion distribuida via Feign ----
    private void validarUsuario(java.util.UUID id, String etiqueta) {
        if (id == null) return;
        try {
            UsuarioDTO u = usuarioClient.obtenerUsuario(id);
            if (u == null || Boolean.FALSE.equals(u.getActivo())) {
                throw new ValidacionRemotaException(
                    "El usuario (" + etiqueta + ") con id " + id + " no esta activo");
            }
        } catch (feign.FeignException.NotFound e) {
            throw new ValidacionRemotaException(
                "El usuario (" + etiqueta + ") con id " + id + " no existe");
        }
    }
}
