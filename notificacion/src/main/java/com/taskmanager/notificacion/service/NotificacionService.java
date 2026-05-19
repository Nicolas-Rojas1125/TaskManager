package com.taskmanager.notificacion.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.notificacion.dto.NotificacionDTO;
import com.taskmanager.notificacion.model.Notificacion;
import com.taskmanager.notificacion.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepo;

    public Notificacion crearNotificacion(NotificacionDTO dto) {
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
}
