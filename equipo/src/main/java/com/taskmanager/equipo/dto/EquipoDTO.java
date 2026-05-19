package com.taskmanager.equipo.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipoDTO {
    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;
    private String descripcion;
    @NotNull(message = "El ID del creador es obligatorio")
    private UUID creadorId;
}
