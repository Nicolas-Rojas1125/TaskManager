package com.taskmanager.equipo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taskmanager.equipo.model.MiembroEquipo;

@Repository
public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo, UUID> {
    // Para ver todos los miembros de un equipo
    List<MiembroEquipo> findByEquipoId(UUID equipoId);
    
    // Para ver a qué equipos pertenece un usuario
    List<MiembroEquipo> findByUsuarioId(UUID usuarioId);
    
    // Para evitar agregar a la misma persona dos veces
    Optional<MiembroEquipo> findByEquipoIdAndUsuarioId(UUID equipoId, UUID usuarioId);
}