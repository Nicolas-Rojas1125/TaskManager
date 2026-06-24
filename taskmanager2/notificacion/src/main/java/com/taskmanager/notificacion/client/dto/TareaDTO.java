package com.taskmanager.notificacion.client.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO espejo: recibe la respuesta JSON de tarea-service.
 * Solo trae los campos que este microservicio necesita para validar.
 */
@Data
public class TareaDTO {
    private UUID id;
    private String titulo;
    private String estado;
    private UUID creadorId;
    private UUID asignadoId;
}
