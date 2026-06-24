package com.taskmanager.tarea.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.tarea.model.Etiqueta;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, UUID> {
    
    Optional<Etiqueta> findByNombre(String nombre);
}