package com.taskmanager.reporte.dto;

import java.util.UUID;

import com.taskmanager.reporte.model.TipoReporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteRequestDTO {
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotNull(message = "El tipo de reporte es obligatorio")
    private TipoReporte tipo;

    @NotNull(message = "El ID del solicitante es obligatorio")
    private UUID solicitadoPor;
    
    // Estos datos simulan lo que se obtiene de otros servicios (Feign)
    @NotBlank(message = "Los datos del reporte no pueden estar vacíos")
    private String datosJsonGenerados; 
}