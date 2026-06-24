package com.taskmanager.adjunto.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.adjunto.dto.AdjuntoDTO;
import com.taskmanager.adjunto.model.Adjunto;
import com.taskmanager.adjunto.repository.AdjuntoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjuntoService {

    private final AdjuntoRepository adjuntoRepo;

    public Adjunto registrarAdjunto(AdjuntoDTO dto) {
        Adjunto adjunto = new Adjunto();
        adjunto.setTaskId(dto.getTaskId());
        adjunto.setUsuarioId(dto.getUsuarioId());
        adjunto.setNombreArchivo(dto.getNombreArchivo());
        adjunto.setTipoMime(dto.getTipoMime());
        adjunto.setUrlDescarga(dto.getUrlDescarga());
        adjunto.setTamanoBytes(dto.getTamanoBytes());

        return adjuntoRepo.save(adjunto);
    }

    public List<Adjunto> listarPorTarea(UUID taskId) {
        return adjuntoRepo.findByTaskIdOrderByFechaSubidaDesc(taskId);
    }

    public void eliminarAdjunto(UUID id) {
        if (!adjuntoRepo.existsById(id)) {
            throw new RuntimeException("Archivo adjunto no encontrado");
        }
        adjuntoRepo.deleteById(id);
    }
}