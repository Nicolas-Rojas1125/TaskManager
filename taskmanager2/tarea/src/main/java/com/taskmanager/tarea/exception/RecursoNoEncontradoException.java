package com.taskmanager.tarea.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Recurso no encontrado. La anotacion @ResponseStatus hace que Spring
 * devuelva HTTP 404 automaticamente. Esto es clave: permite que un cliente
 * Feign remoto distinga "no existe" (404) de "error interno" (500).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
