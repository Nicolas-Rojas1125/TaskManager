package com.taskmanager.auditoria.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.auditoria.model.Auditoria;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, UUID> {

    List<Auditoria> findByUsuarioIdOrderByTimestampDesc(UUID usuarioId);

    List<Auditoria> findByTaskIdOrderByTimestampDesc(UUID taskId);
}