package com.taskmanager.adjunto.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.adjunto.dto.AdjuntoDTO;
import com.taskmanager.adjunto.model.Adjunto;
import com.taskmanager.adjunto.repository.AdjuntoRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.adjunto.client.UsuarioClient;
import com.taskmanager.adjunto.client.TareaClient;
import com.taskmanager.adjunto.client.dto.UsuarioDTO;
import com.taskmanager.adjunto.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class AdjuntoService {
    private final UsuarioClient usuarioClient;
    private final TareaClient tareaClient;

    private final AdjuntoRepository adjuntoRepo;

    public Adjunto registrarAdjunto(AdjuntoDTO dto) {
        validarTarea(dto.getTaskId());
        validarUsuario(dto.getUsuarioId(), "subidor");
        Adjunto adjunto = new Adjunto();
        adjunto.setTaskId(dto.getTaskId());
        adjunto.setUsuarioId(dto.getUsuarioId());
        adjunto.setNombreArchivo(dto.getNombreArchivo());
        adjunto.setTipoMime(dto.getTipoMime());
        adjunto.setUrlDescarga(dto.getUrlDescarga());
        adjunto.setTamanoBytes(dto.getTamanoBytes());

        return adjuntoRepo.save(adjunto);
    }

    public List<Adjunto> listarPorTarea(UUID taskId) {
        return adjuntoRepo.findByTaskIdOrderByFechaSubidaDesc(taskId);
    }

    public void eliminarAdjunto(UUID id) {
        if (!adjuntoRepo.existsById(id)) {
            throw new RuntimeException("Archivo adjunto no encontrado");
        }
        adjuntoRepo.deleteById(id);
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