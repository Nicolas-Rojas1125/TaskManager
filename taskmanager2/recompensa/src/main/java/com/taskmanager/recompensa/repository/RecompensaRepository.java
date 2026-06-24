package com.taskmanager.recompensa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskmanager.recompensa.model.Recompensa;

@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, UUID> {
    

    List<Recompensa> findByUsuarioIdOrderByOtorgadoEnDesc(UUID usuarioId);

    @Query("SELECT COALESCE(SUM(r.puntos), 0) FROM Recompensa r WHERE r.usuarioId = :usuarioId")
    Integer calcularPuntosTotalesPorUsuario(@Param("usuarioId") UUID usuarioId);
}