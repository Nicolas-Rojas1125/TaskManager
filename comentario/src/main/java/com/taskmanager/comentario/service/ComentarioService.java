package com.taskmanager.comentario.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.comentario.dto.ComentarioDTO;
import com.taskmanager.comentario.model.Comentario;
import com.taskmanager.comentario.repository.ComentarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepo;

    public Comentario agregarComentario(ComentarioDTO dto) {
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
}
