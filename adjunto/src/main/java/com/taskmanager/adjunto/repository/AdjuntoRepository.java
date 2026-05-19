package com.taskmanager.adjunto.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.adjunto.model.Adjunto;

@Repository
public interface AdjuntoRepository extends JpaRepository<Adjunto, UUID> {

    List<Adjunto> findByTaskIdOrderByFechaSubidaDesc(UUID taskId);

    List<Adjunto> findByUsuarioId(UUID usuarioId);
}
