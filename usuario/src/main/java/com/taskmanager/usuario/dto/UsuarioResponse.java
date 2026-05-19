package com.taskmanager.usuario.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.taskmanager.usuario.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private UUID id;
    private String nombre;
    private String email;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}