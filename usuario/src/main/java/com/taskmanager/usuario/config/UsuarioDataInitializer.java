package com.taskmanager.usuario.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.taskmanager.usuario.model.Rol;
import com.taskmanager.usuario.model.Usuario;
import com.taskmanager.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class UsuarioDataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Usuarios ya han sido cargados");
            return;
        }
    
        log.info("Cargando usuarios");
        
        
        usuarioRepository.save(new Usuario(null, "Juan Perez", "elpapu@gmail.com", "1234", Rol.ADMIN, true, null, null));
        usuarioRepository.save(new Usuario(null, "Alan Arredondo", "johan@gmail.com", "4321", Rol.MANAGER, true, null, null));
        usuarioRepository.save(new Usuario(null, "Nicolas Rojas", "nico21@gmail.com", "5678", Rol.USER, true, null, null));
        
        log.info("Usuarios cargados exitosamente");
    }
}

