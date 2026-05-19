package com.taskmanager.recompensa.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecompensaDTO {

    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "La cantidad de puntos es obligatoria")
    @Min(value = 1, message = "Los puntos deben ser mayor a 0")
    private Integer puntos;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
}