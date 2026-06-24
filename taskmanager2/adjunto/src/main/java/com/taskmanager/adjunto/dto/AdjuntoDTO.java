package com.taskmanager.adjunto.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjuntoDTO {

    @NotNull(message = "El ID de la tarea es obligatorio")
    private UUID taskId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID usuarioId;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String nombreArchivo;

    private String tipoMime;

    @NotBlank(message = "La URL o ruta del archivo es obligatoria")
    private String urlDescarga;

    private Long tamanoBytes;
}
