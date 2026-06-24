package com.taskmanager.equipo.dto;

import java.util.UUID;
import com.taskmanager.equipo.model.RolEquipo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MiembroDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private UUID usuarioId;
    @NotNull(message = "El rol es obligatorio")
    private RolEquipo rol;
}
