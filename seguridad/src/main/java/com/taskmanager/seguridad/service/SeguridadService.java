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

@Service
@RequiredArgsConstructor
public class SeguridadService {

    private final RefreshTokenRepository tokenRepo;
    private final SmartDeviceRepository deviceRepo;

    public RefreshToken crearRefreshToken(UUID usuarioId) {
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
}