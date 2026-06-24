package com.taskmanager.auditoria.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.auditoria.dto.AuditoriaDTO;
import com.taskmanager.auditoria.model.Auditoria;
import com.taskmanager.auditoria.repository.AuditoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepo;

    public Auditoria registrarAccion(AuditoriaDTO dto) {
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuarioId(dto.getUsuarioId());
        auditoria.setTaskId(dto.getTaskId());
        auditoria.setAccion(dto.getAccion());
        auditoria.setDetalle(dto.getDetalle());

        return auditoriaRepo.save(auditoria);
    }

    public List<Auditoria> listarTodo() {
        return auditoriaRepo.findAll();
    }

    public List<Auditoria> listarPorUsuario(UUID usuarioId) {
        return auditoriaRepo.findByUsuarioIdOrderByTimestampDesc(usuarioId);
    }

    public List<Auditoria> listarPorTarea(UUID taskId) {
        return auditoriaRepo.findByTaskIdOrderByTimestampDesc(taskId);
    }
}