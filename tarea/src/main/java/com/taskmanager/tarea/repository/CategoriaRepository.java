package com.taskmanager.tarea.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.tarea.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    
    // Te servirá para listar solo las categorías que creó un usuario específico
    List<Categoria> findByCreatedBy(UUID createdBy);
}