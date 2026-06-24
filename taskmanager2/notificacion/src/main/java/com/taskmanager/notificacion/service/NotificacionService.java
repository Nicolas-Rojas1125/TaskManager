package com.taskmanager.notificacion.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.notificacion.dto.NotificacionDTO;
import com.taskmanager.notificacion.model.Notificacion;
import com.taskmanager.notificacion.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.notificacion.client.UsuarioClient;
import com.taskmanager.notificacion.client.TareaClient;
import com.taskmanager.notificacion.client.dto.UsuarioDTO;
import com.taskmanager.notificacion.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final UsuarioClient usuarioClient;
    private final TareaClient tareaClient;

    private final NotificacionRepository notificacionRepo;

    public Notificacion crearNotificacion(NotificacionDTO dto) {
        validarUsuario(dto.getUsuarioId(), "destinatario");
        validarTarea(dto.getTaskId());
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setTaskId(dto.getTaskId());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setTipo(dto.getTipo());
        notificacion.setLeida(false); // Por defecto nace como NO leída

        return notificacionRepo.save(notificacion);
    }

    public List<Notificacion> listarPorUsuario(UUID usuarioId) {
        return notificacionRepo.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId);
    }

    public List<Notificacion> listarNoLeidasPorUsuario(UUID usuarioId) {
        return notificacionRepo.findByUsuarioIdAndLeidaFalseOrderByFechaEnvioDesc(usuarioId);
    }

    public Notificacion marcarComoLeida(UUID id) {
        Notificacion notificacion = notificacionRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        notificacion.setLeida(true);
        return notificacionRepo.save(notificacion);
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
