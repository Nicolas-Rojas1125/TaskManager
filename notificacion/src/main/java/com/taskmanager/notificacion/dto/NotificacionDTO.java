package com.taskmanager.notificacion.dto;

import java.util.UUID;

import com.taskmanager.notificacion.model.TipoNotificacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionDTO {

    @NotNull(message = "El usuario de destino es obligatorio")
    private UUID usuarioId;

    private UUID taskId; 
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private TipoNotificacion tipo;
}
