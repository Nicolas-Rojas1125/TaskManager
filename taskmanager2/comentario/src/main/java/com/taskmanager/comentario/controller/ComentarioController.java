package com.taskmanager.comentario.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.comentario.dto.ComentarioDTO;
import com.taskmanager.comentario.model.Comentario;
import com.taskmanager.comentario.service.ComentarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<Comentario> agregarComentario(@Valid @RequestBody ComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.agregarComentario(dto));
    }

    @GetMapping("/tarea/{taskId}")
    public ResponseEntity<List<Comentario>> listarPorTarea(@PathVariable UUID taskId) {
        return ResponseEntity.ok(comentarioService.listarPorTarea(taskId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> editarComentario(@PathVariable UUID id, @RequestBody String nuevoContenido) {
        return ResponseEntity.ok(comentarioService.editarComentario(id, nuevoContenido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable UUID id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
