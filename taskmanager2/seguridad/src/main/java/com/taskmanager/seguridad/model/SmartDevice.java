package com.taskmanager.seguridad.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "smart_device_integrations")
@NoArgsConstructor
@AllArgsConstructor
public class SmartDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dispositivo", nullable = false)
    private TipoDispositivo tipoDispositivo;

    @Column(name = "device_token", nullable = false, length = 255)
    private String deviceToken;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "sincronizado_en")
    private LocalDateTime sincronizadoEn;
}
