package com.taskmanager.tarea.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

/**
 * Manejo centralizado de errores. Convierte las fallas de la comunicacion
 * REST entre microservicios en codigos HTTP claros para el cliente final.
 */
@RestControllerAdvice
public class ManejadorErroresGlobal {

    // Dato invalido enviado por el cliente (id inexistente, usuario inactivo)
    @ExceptionHandler(ValidacionRemotaException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(ValidacionRemotaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("estado", 400, "error", "Solicitud invalida", "mensaje", ex.getMessage()));
    }

    // El microservicio remoto respondio 404 (el recurso no existe)
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(FeignException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("estado", 404, "error", "Recurso remoto no encontrado",
                        "mensaje", "El recurso referenciado no existe en el microservicio remoto"));
    }

    // El microservicio remoto no respondio (esta caido o no registrado en Eureka)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> manejarRemotoCaido(FeignException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("estado", 503, "error", "Servicio remoto no disponible",
                        "mensaje", "No fue posible comunicarse con el microservicio remoto"));
    }
}
