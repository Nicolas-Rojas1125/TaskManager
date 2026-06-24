package com.taskmanager.tarea.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.taskmanager.tarea.model.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TareaDTO {
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    private String descripcion;
    @NotNull(message = "La prioridad es obligatoria")
    private Prioridad prioridad;
    private LocalDate fechaLimite;
    @NotNull(message = "El creador es obligatorio")
    private UUID creadorId; 
    private UUID asignadoId;
    private UUID categoriaId;
    private List<UUID> etiquetasIds;
}
