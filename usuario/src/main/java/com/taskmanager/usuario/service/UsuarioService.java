package com.taskmanager.usuario.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taskmanager.usuario.dto.UsuarioDTO;
import com.taskmanager.usuario.dto.UsuarioLoginRequest;
import com.taskmanager.usuario.dto.UsuarioResponse;
import com.taskmanager.usuario.model.Usuario;
import com.taskmanager.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private UsuarioResponse mapToDTO(Usuario u){
        return new UsuarioResponse(
            u.getId(), u.getNombre(), u.getEmail(), 
            u.getRol(), u.getActivo(), u.getFechaRegistro()
        );
    }

    public List<UsuarioResponse> listarUsuarios(){
        return usuarioRepo.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public UsuarioResponse obtenerPorId(UUID id){
        return usuarioRepo.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public UsuarioResponse buscarPorEmail(String email){
        return usuarioRepo.findByEmail(email)
            .map(this::mapToDTO)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public UsuarioResponse crearUsuario(UsuarioDTO dto){
        if(usuarioRepo.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre(dto.getNombre());
        usuarioNuevo.setEmail(dto.getEmail());
        usuarioNuevo.setPasswordHash(dto.getPassword()); 
        usuarioNuevo.setRol(dto.getRol());
        usuarioNuevo.setActivo(true);

        return mapToDTO(usuarioRepo.save(usuarioNuevo));
    }
    
    public UsuarioResponse modificarUsuario(UUID id, UsuarioDTO dto){
        Usuario usuario = usuarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("La id " + id + " no existe"));

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        
        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(dto.getPassword()); 
        }

        return mapToDTO(usuarioRepo.save(usuario));
    }

    public void desactivarUsuario(UUID id){
        Usuario usuario = usuarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("La id " + id + " no existe"));
        
        usuario.setActivo(false);
        usuarioRepo.save(usuario);
    }

    public UsuarioResponse loginUsuario(UsuarioLoginRequest login){
        Usuario usuario = usuarioRepo.findByEmail(login.getEmail())
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario está inactivo");
        }

        if (!usuario.getPasswordHash().equals(login.getContraseña())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        return mapToDTO(usuario);
    }
}