package com.taskmanager.auditoria.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.auditoria.dto.AuditoriaDTO;
import com.taskmanager.auditoria.model.Auditoria;
import com.taskmanager.auditoria.repository.AuditoriaRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.auditoria.client.UsuarioClient;
import com.taskmanager.auditoria.client.TareaClient;
import com.taskmanager.auditoria.client.dto.UsuarioDTO;
import com.taskmanager.auditoria.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final UsuarioClient usuarioClient;
    private final TareaClient tareaClient;

    private final AuditoriaRepository auditoriaRepo;

    public Auditoria registrarAccion(AuditoriaDTO dto) {
        validarUsuario(dto.getUsuarioId(), "actor");
        validarTarea(dto.getTaskId());
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuarioId(dto.getUsuarioId());
        auditoria.setTaskId(dto.getTaskId());
        auditoria.setAccion(dto.getAccion());
        auditoria.setDetalle(dto.getDetalle());

        return auditoriaRepo.save(auditoria);
    }

    public List<Auditoria> listarTodo() {
        return auditoriaRepo.findAll();
    }

    public List<Auditoria> listarPorUsuario(UUID usuarioId) {
        return auditoriaRepo.findByUsuarioIdOrderByTimestampDesc(usuarioId);
    }

    public List<Auditoria> listarPorTarea(UUID taskId) {
        return auditoriaRepo.findByTaskIdOrderByTimestampDesc(taskId);
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

    private void validarTarea(java.util.UUID id) {
        if (id == null) return;
        try {
            tareaClient.obtenerTarea(id);
        } catch (feign.FeignException.NotFound e) {
            throw new ValidacionRemotaException("La tarea con id " + id + " no existe");
        }
    }
}