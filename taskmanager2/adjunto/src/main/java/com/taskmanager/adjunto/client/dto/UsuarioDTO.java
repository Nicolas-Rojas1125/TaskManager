package com.taskmanager.adjunto.client.dto;

import java.util.UUID;

import lombok.Data;

/**
 * DTO espejo: recibe la respuesta JSON de usuario-service.
 * Solo trae los campos que este microservicio necesita para validar.
 */
@Data
public class UsuarioDTO {
    private UUID id;
    private String nombre;
    private String email;
    private String rol;
    private Boolean activo;
}
