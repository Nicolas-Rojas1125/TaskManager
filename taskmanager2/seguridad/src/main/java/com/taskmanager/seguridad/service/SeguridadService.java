package com.taskmanager.seguridad.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.seguridad.dto.DeviceDTO;
import com.taskmanager.seguridad.model.RefreshToken;
import com.taskmanager.seguridad.model.SmartDevice;
import com.taskmanager.seguridad.repository.RefreshTokenRepository;
import com.taskmanager.seguridad.repository.SmartDeviceRepository;

import lombok.RequiredArgsConstructor;
import com.taskmanager.seguridad.client.UsuarioClient;
import com.taskmanager.seguridad.client.dto.UsuarioDTO;
import com.taskmanager.seguridad.exception.ValidacionRemotaException;

@Service
@RequiredArgsConstructor
public class SeguridadService {
    private final UsuarioClient usuarioClient;

    private final RefreshTokenRepository tokenRepo;
    private final SmartDeviceRepository deviceRepo;

    public RefreshToken crearRefreshToken(UUID usuarioId) {
        validarUsuario(usuarioId, "titular");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuarioId(usuarioId);
        refreshToken.setToken(UUID.randomUUID().toString()); 
        refreshToken.setExpiraEn(LocalDateTime.now().plusDays(7)); 
        
        return tokenRepo.save(refreshToken);
    }

    public void revocarToken(String tokenStr) {
        RefreshToken token = tokenRepo.findByToken(tokenStr)
            .orElseThrow(() -> new RuntimeException("Token no encontrado"));
        token.setRevocado(true);
        tokenRepo.save(token);
    }
    public SmartDevice registrarDispositivo(DeviceDTO dto) {
        validarUsuario(dto.getUsuarioId(), "titular");
        SmartDevice device = new SmartDevice();
        device.setUsuarioId(dto.getUsuarioId());
        device.setTipoDispositivo(dto.getTipo());
        device.setDeviceToken(dto.getDeviceToken());
        device.setSincronizadoEn(LocalDateTime.now());
        
        return deviceRepo.save(device);
    }

    public List<SmartDevice> obtenerDispositivosActivos(UUID usuarioId) {
        return deviceRepo.findByUsuarioIdAndActivoTrue(usuarioId);
    }
    
    public void desactivarDispositivo(UUID id) {
        SmartDevice device = deviceRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
        device.setActivo(false);
        deviceRepo.save(device);
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