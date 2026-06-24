package com.taskmanager.tarea.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import com.taskmanager.tarea.client.dto.UsuarioDTO;

/**
 * Cliente Feign hacia usuario-service.
 * El nombre "usuario" se resuelve por Eureka (sin URLs fijas):
 * Feign pregunta a Eureka donde esta el servicio y arma la peticion HTTP.
 */
@FeignClient(name = "usuario")
public interface UsuarioClient {

    @GetMapping("/api/usuario/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") UUID id);
}
