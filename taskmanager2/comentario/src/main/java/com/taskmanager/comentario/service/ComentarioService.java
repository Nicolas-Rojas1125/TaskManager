package com.taskmanager.comentario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.comentario.dto.ComentarioDTO;
import com.taskmanager.comentario.model.Comentario;
import com.taskmanager.comentario.repository.ComentarioRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.comentario.client.UsuarioClient;
import com.taskmanager.comentario.client.TareaClient;
import com.taskmanager.comentario.client.dto.UsuarioDTO;
import com.taskmanager.comentario.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class ComentarioService {
    private final UsuarioClient usuarioClient;
    private final TareaClient tareaClient;

    private final ComentarioRepository comentarioRepo;

    public Comentario agregarComentario(ComentarioDTO dto) {
        validarTarea(dto.getTaskId());
        validarUsuario(dto.getUsuarioId(), "autor");
        Comentario comentario = new Comentario();
        comentario.setTaskId(dto.getTaskId());
        comentario.setUsuarioId(dto.getUsuarioId());
        comentario.setContenido(dto.getContenido());
        comentario.setEditado(false);

        return comentarioRepo.save(comentario);
    }

    public List<Comentario> listarPorTarea(UUID taskId) {
        return comentarioRepo.findByTaskIdOrderByFechaCreacionAsc(taskId);
    }

    public Comentario editarComentario(UUID id, String nuevoContenido) {
        Comentario comentario = comentarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        
        comentario.setContenido(nuevoContenido);
        comentario.setEditado(true);
        
        return comentarioRepo.save(comentario);
    }

    public void eliminarComentario(UUID id) {
        if (!comentarioRepo.existsById(id)) {
            throw new RuntimeException("Comentario no encontrado");
        }
        comentarioRepo.deleteById(id);
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

    private void validarTarea(java.util.UUID id) {
        if (id == null) return;
        try {
            tareaClient.obtenerTarea(id);
        } catch (feign.FeignException.NotFound e) {
            throw new ValidacionRemotaException("La tarea con id " + id + " no existe");
        }
    }
}
