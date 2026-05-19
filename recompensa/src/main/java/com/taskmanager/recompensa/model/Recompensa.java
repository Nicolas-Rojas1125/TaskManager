package com.taskmanager.recompensa.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "rewards")
@NoArgsConstructor
@AllArgsConstructor
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false)
    private Integer puntos;

    @Column(nullable = false, length = 255)
    private String motivo;

    @CreationTimestamp
    @Column(name = "otorgado_en", updatable = false)
    private LocalDateTime otorgadoEn;
}