package com.taskmanager.reporte.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.taskmanager.reporte.model.Reporte;
import com.taskmanager.reporte.model.TipoReporte;
import com.taskmanager.reporte.repository.ReporteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class DataInitializerReporte implements CommandLineRunner {

    private final ReporteRepository reporteRepository;

    @Override
    public void run(String... args) {
        if (reporteRepository.count() > 0) {
            log.info("Los reportes ya han sido cargados");
            return;
        }

        log.info("Cargando reportes iniciales...");

        UUID usuarioAdmin = UUID.randomUUID();
        UUID usuarioManager = UUID.randomUUID();

        String jsonEstado = "{\"totalTareas\": 150, \"pendientes\": 45, \"completadas\": 105}";
        String jsonRendimiento = "{\"equipo\": \"Backend Alpha\", \"tareasCompletadas\": 80, \"velocidadPromedio\": \"2.5 días/tarea\"}";
        String jsonGamificacion = "{\"topUsuarios\": [{\"nombre\": \"Juan Perez\", \"puntos\": 1500}, {\"nombre\": \"Alan Arredondo\", \"puntos\": 1200}]}";

        reporteRepository.save(new Reporte(null, "Resumen General de Tareas", TipoReporte.ESTADO_TAREAS, jsonEstado, usuarioAdmin, null));
        reporteRepository.save(new Reporte(null, "Productividad de Equipos Marzo", TipoReporte.RENDIMIENTO_EQUIPO, jsonRendimiento, usuarioManager, null));
        reporteRepository.save(new Reporte(null, "Ranking Histórico de Gamificación", TipoReporte.PUNTOS_USUARIOS, jsonGamificacion, usuarioAdmin, null));

        log.info("Reportes iniciales cargados exitosamente");
    }
}