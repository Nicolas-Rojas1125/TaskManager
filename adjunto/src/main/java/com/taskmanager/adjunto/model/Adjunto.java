package com.taskmanager.adjunto.model;

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
@Table(name = "attachments")
@NoArgsConstructor
@AllArgsConstructor
public class Adjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "nombre_archivo", nullable = false, length = 200)
    private String nombreArchivo;

    @Column(name = "tipo_mime", length = 50)
    private String tipoMime;

    @Column(name = "url_descarga", nullable = false, length = 500)
    private String urlDescarga;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @CreationTimestamp
    @Column(name = "fecha_subida", updatable = false)
    private LocalDateTime fechaSubida;
}
