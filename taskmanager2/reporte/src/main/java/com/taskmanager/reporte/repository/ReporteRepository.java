package com.taskmanager.reporte.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.reporte.model.Reporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, UUID> {
    List<Reporte> findBySolicitadoPorOrderByFechaGeneracionDesc(UUID solicitadoPor);
    List<Reporte> findByTipoOrderByFechaGeneracionDesc(com.taskmanager.reporte.model.TipoReporte tipo);
}