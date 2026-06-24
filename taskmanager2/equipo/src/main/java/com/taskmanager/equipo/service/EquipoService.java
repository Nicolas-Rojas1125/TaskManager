package com.taskmanager.equipo.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.taskmanager.equipo.dto.EquipoDTO;
import com.taskmanager.equipo.dto.MiembroDTO;
import com.taskmanager.equipo.model.Equipo;
import com.taskmanager.equipo.model.MiembroEquipo;
import com.taskmanager.equipo.model.RolEquipo;
import com.taskmanager.equipo.repository.EquipoRepository;
import com.taskmanager.equipo.repository.MiembroEquipoRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.equipo.client.UsuarioClient;
import com.taskmanager.equipo.client.dto.UsuarioDTO;
import com.taskmanager.equipo.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class EquipoService {
    private final UsuarioClient usuarioClient;

    private final EquipoRepository equipoRepo;
    private final MiembroEquipoRepository miembroRepo;

    public Equipo crearEquipo(EquipoDTO dto) {
        validarUsuario(dto.getCreadorId(), "creador");
        Equipo equipo = new Equipo();
        equipo.setNombre(dto.getNombre());
        equipo.setDescripcion(dto.getDescripcion());
        equipo.setCreadorId(dto.getCreadorId());

        Equipo equipoGuardado = equipoRepo.save(equipo);

        // Al crear el equipo, el creador se agrega automáticamente como LIDER
        MiembroEquipo lider = new MiembroEquipo();
        lider.setEquipoId(equipoGuardado.getId());
        lider.setUsuarioId(equipoGuardado.getCreadorId());
        lider.setRol(RolEquipo.LIDER);
        miembroRepo.save(lider);

        return equipoGuardado;
    }

    public MiembroEquipo agregarMiembro(UUID equipoId, MiembroDTO dto) {
        validarUsuario(dto.getUsuarioId(), "miembro");
        // Verificar que el equipo exista
        equipoRepo.findById(equipoId).orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        // Verificar si ya es miembro
        if (miembroRepo.findByEquipoIdAndUsuarioId(equipoId, dto.getUsuarioId()).isPresent()) {
            throw new RuntimeException("El usuario ya pertenece a este equipo");
        }

        MiembroEquipo nuevoMiembro = new MiembroEquipo();
        nuevoMiembro.setEquipoId(equipoId);
        nuevoMiembro.setUsuarioId(dto.getUsuarioId());
        nuevoMiembro.setRol(dto.getRol());

        return miembroRepo.save(nuevoMiembro);
    }

    public List<MiembroEquipo> listarMiembrosDeEquipo(UUID equipoId) {
        return miembroRepo.findByEquipoId(equipoId);
    }

    public List<MiembroEquipo> listarEquiposDeUsuario(UUID usuarioId) {
        return miembroRepo.findByUsuarioId(usuarioId);
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