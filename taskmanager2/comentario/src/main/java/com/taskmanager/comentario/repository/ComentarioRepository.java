package com.taskmanager.comentario.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.comentario.model.Comentario;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {

    List<Comentario> findByTaskIdOrderByFechaCreacionAsc(UUID taskId);

    Integer countByUsuarioId(UUID usuarioId);
}