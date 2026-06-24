package com.taskmanager.tarea.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.taskmanager.tarea.dto.TareaDTO;
import com.taskmanager.tarea.model.Categoria;
import com.taskmanager.tarea.model.EstadoTarea;
import com.taskmanager.tarea.model.Etiqueta;
import com.taskmanager.tarea.model.Tarea;
import com.taskmanager.tarea.repository.CategoriaRepository;
import com.taskmanager.tarea.repository.EtiquetaRepository;
import com.taskmanager.tarea.repository.TareaRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.tarea.client.UsuarioClient;
import com.taskmanager.tarea.client.dto.UsuarioDTO;
import com.taskmanager.tarea.exception.ValidacionRemotaException;
import com.taskmanager.tarea.exception.RecursoNoEncontradoException;

@Service
@RequiredArgsConstructor
public class TareaService {
    private final UsuarioClient usuarioClient;

    private final TareaRepository tareaRepo;
    private final CategoriaRepository categoriaRepo;
    private final EtiquetaRepository etiquetaRepo;

    public List<Tarea> listarTareas() {
        return tareaRepo.findAll();
    }

    public List<Tarea> listarTareasPorAsignado(UUID asignadoId) {
        return tareaRepo.findByAsignadoId(asignadoId);
    }

    public Tarea crearTarea(TareaDTO dto) {
        validarUsuario(dto.getCreadorId(), "creador");
        validarUsuario(dto.getAsignadoId(), "asignado");
        Tarea tarea = new Tarea();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setPrioridad(dto.getPrioridad());
        tarea.setEstado(EstadoTarea.PENDIENTE);
        tarea.setFechaLimite(dto.getFechaLimite());
        tarea.setCreadorId(dto.getCreadorId());
        tarea.setAsignadoId(dto.getAsignadoId());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            tarea.setCategoria(categoria);
        }

        if (dto.getEtiquetasIds() != null && !dto.getEtiquetasIds().isEmpty()) {
            List<Etiqueta> etiquetas = etiquetaRepo.findAllById(dto.getEtiquetasIds());
            tarea.setEtiquetas(etiquetas);
        }

        return tareaRepo.save(tarea);
    }

    public Tarea cambiarEstado(UUID id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        
        tarea.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoTarea.COMPLETADA) {
            tarea.setFechaCompletada(LocalDateTime.now());
        }
        
        return tareaRepo.save(tarea);
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

    public Tarea obtenerPorId(java.util.UUID id) {
        return tareaRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada con id: " + id));
    }
}