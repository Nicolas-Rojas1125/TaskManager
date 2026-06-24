package com.taskmanager.seguridad.dto;

import java.util.UUID;

import com.taskmanager.seguridad.model.TipoDispositivo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceDTO {
    @NotNull(message = "El usuario es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "El tipo de dispositivo es obligatorio")
    private TipoDispositivo tipo;

    @NotBlank(message = "El token del dispositivo es obligatorio")
    private String deviceToken;
}
