package com.taskmanager.seguridad.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskmanager.seguridad.model.SmartDevice;

@Repository
public interface SmartDeviceRepository extends JpaRepository<SmartDevice, UUID> {
    List<SmartDevice> findByUsuarioIdAndActivoTrue(UUID usuarioId);
}