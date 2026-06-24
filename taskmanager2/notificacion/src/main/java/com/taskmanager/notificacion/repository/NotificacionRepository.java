package com.taskmanager.notificacion.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.notificacion.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {

    List<Notificacion> findByUsuarioIdOrderByFechaEnvioDesc(UUID usuarioId);
    

    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaEnvioDesc(UUID usuarioId);
}