package com.taskmanager.tarea.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taskmanager.tarea.model.Tarea;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, UUID> {
    List<Tarea> findByAsignadoId(UUID asignadoId);
    List<Tarea> findByCreadorId(UUID creadorId);
}