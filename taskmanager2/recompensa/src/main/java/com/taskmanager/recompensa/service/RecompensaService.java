package com.taskmanager.recompensa.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.recompensa.dto.RecompensaDTO;
import com.taskmanager.recompensa.model.Recompensa;
import com.taskmanager.recompensa.repository.RecompensaRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.recompensa.client.UsuarioClient;
import com.taskmanager.recompensa.client.dto.UsuarioDTO;
import com.taskmanager.recompensa.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class RecompensaService {
    private final UsuarioClient usuarioClient;

    private final RecompensaRepository recompensaRepo;

    public Recompensa otorgarPuntos(RecompensaDTO dto) {
        validarUsuario(dto.getUsuarioId(), "beneficiario");
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

    // ---- Validacion distribuida via Feign ----
    private void validarUsuario(java.util.UUID id, String etiqueta) {
        if (id == null) return;
        try {
            UsuarioDTO u = usuarioClient.obtenerUsuario(id);
            if (u == null || Boolean.FALSE.equals(u.getActivo())) {
                throw new ValidacionRemotaException(
                    "El usuario (" + etiqueta + ") con id " + id + " no esta activo");
            }
        } catch (feign.FeignException.NotFound e) {
            throw new ValidacionRemotaException(
                "El usuario (" + etiqueta + ") con id " + id + " no existe");
        }
    }
}