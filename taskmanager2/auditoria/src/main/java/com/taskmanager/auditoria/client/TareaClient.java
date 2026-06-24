package com.taskmanager.auditoria.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import com.taskmanager.auditoria.client.dto.TareaDTO;

/**
 * Cliente Feign hacia tarea-service.
 * El nombre "tarea" se resuelve por Eureka (sin URLs fijas):
 * Feign pregunta a Eureka donde esta el servicio y arma la peticion HTTP.
 */
@FeignClient(name = "tarea")
public interface TareaClient {

    @GetMapping("/api/tareas/{id}")
    TareaDTO obtenerTarea(@PathVariable("id") UUID id);
}
