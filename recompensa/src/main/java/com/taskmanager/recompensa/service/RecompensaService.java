package com.taskmanager.recompensa.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.recompensa.dto.RecompensaDTO;
import com.taskmanager.recompensa.model.Recompensa;
import com.taskmanager.recompensa.repository.RecompensaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecompensaService {

    private final RecompensaRepository recompensaRepo;

    public Recompensa otorgarPuntos(RecompensaDTO dto) {
        Recompensa recompensa = new Recompensa();
        recompensa.setUsuarioId(dto.getUsuarioId());
        recompensa.setPuntos(dto.getPuntos());
        recompensa.setMotivo(dto.getMotivo());

        return recompensaRepo.save(recompensa);
    }

    public List<Recompensa> obtenerHistorial(UUID usuarioId) {
        return recompensaRepo.findByUsuarioIdOrderByOtorgadoEnDesc(usuarioId);
    }

    public Integer obtenerPuntosTotales(UUID usuarioId) {
        return recompensaRepo.calcularPuntosTotalesPorUsuario(usuarioId);
    }
}