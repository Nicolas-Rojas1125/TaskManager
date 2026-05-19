package com.taskmanager.equipo.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taskmanager.equipo.model.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, UUID> {
}