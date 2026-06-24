package com.taskmanager.auditoria.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuditoriaDTO {

    private UUID usuarioId;
    
    private UUID taskId;

    @NotBlank(message = "La acción es obligatoria")
    private String accion;

    private String detalle;
}
