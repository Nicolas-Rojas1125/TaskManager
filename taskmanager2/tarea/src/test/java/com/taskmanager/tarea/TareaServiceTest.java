package com.taskmanager.tarea;

import com.taskmanager.tarea.client.UsuarioClient;
import com.taskmanager.tarea.client.dto.UsuarioDTO;
import com.taskmanager.tarea.dto.TareaDTO;
import com.taskmanager.tarea.exception.ValidacionRemotaException;
import com.taskmanager.tarea.model.*;
import com.taskmanager.tarea.repository.CategoriaRepository;
import com.taskmanager.tarea.repository.EtiquetaRepository;
import com.taskmanager.tarea.repository.TareaRepository;
import com.taskmanager.tarea.service.TareaService;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del TareaService.
 * Especial énfasis en la comunicacion Feign con usuario-service:
 * se prueba el camino feliz y los casos de error (usuario no existe,
 * usuario inactivo, servicio caido).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TareaService — pruebas unitarias (incluye Feign)")
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private EtiquetaRepository etiquetaRepository;

    @Mock
    private UsuarioClient usuarioClient;   // <-- el cliente Feign es mockeado

    @InjectMocks
    private TareaService tareaService;

    private UUID creadorId;
    private UUID asignadoId;
    private UsuarioDTO creadorActivo;
    private UsuarioDTO asignadoActivo;

    @BeforeEach
    void setUp() {
        creadorId  = UUID.randomUUID();
        asignadoId = UUID.randomUUID();

        creadorActivo = new UsuarioDTO();
        creadorActivo.setId(creadorId);
        creadorActivo.setNombre("Ana García");
        creadorActivo.setActivo(true);

        asignadoActivo = new UsuarioDTO();
        asignadoActivo.setId(asignadoId);
        asignadoActivo.setNombre("Carlos López");
        asignadoActivo.setActivo(true);
    }

    // ─────────────────────────────────────────────────────────────────
    //  crearTarea — camino feliz
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearTarea — creador y asignado activos → guarda la tarea")
    void crearTarea_usuariosActivos_guardaTarea() {
        // GIVEN
        TareaDTO dto = new TareaDTO();
        dto.setTitulo("Implementar login");
        dto.setPrioridad(Prioridad.ALTA);
        dto.setCreadorId(creadorId);
        dto.setAsignadoId(asignadoId);

        when(usuarioClient.obtenerUsuario(creadorId)).thenReturn(creadorActivo);
        when(usuarioClient.obtenerUsuario(asignadoId)).thenReturn(asignadoActivo);

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setId(UUID.randomUUID());
        tareaGuardada.setTitulo("Implementar login");
        tareaGuardada.setEstado(EstadoTarea.PENDIENTE);
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tareaGuardada);

        // WHEN
        Tarea resultado = tareaService.crearTarea(dto);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Implementar login");
        // El estado inicial siempre es PENDIENTE
        assertThat(resultado.getEstado()).isEqualTo(EstadoTarea.PENDIENTE);
        verify(tareaRepository).save(any(Tarea.class));
        // Feign fue llamado exactamente una vez para cada usuario
        verify(usuarioClient).obtenerUsuario(creadorId);
        verify(usuarioClient).obtenerUsuario(asignadoId);
    }

    // ─────────────────────────────────────────────────────────────────
    //  crearTarea — validación Feign: usuario no existe
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearTarea — creador NO existe en usuario-service → 404 → lanza ValidacionRemotaException")
    void crearTarea_creadorNoExiste_lanzaExcepcion() {
        // GIVEN: Feign recibe un 404 desde usuario-service
        TareaDTO dto = new TareaDTO();
        dto.setTitulo("Implementar login");
        dto.setPrioridad(Prioridad.MEDIA);
        dto.setCreadorId(creadorId);

        when(usuarioClient.obtenerUsuario(creadorId))
            .thenThrow(FeignException.NotFound.class);

        // WHEN / THEN
        assertThatThrownBy(() -> tareaService.crearTarea(dto))
            .isInstanceOf(ValidacionRemotaException.class)
            .hasMessageContaining("creador");

        // La tarea NO se guarda si la validación falla
        verify(tareaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────
    //  crearTarea — validación Feign: usuario inactivo
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearTarea — creador INACTIVO → lanza ValidacionRemotaException")
    void crearTarea_creadorInactivo_lanzaExcepcion() {
        // GIVEN
        TareaDTO dto = new TareaDTO();
        dto.setTitulo("Tarea con creador inactivo");
        dto.setPrioridad(Prioridad.BAJA);
        dto.setCreadorId(creadorId);

        UsuarioDTO creadorInactivo = new UsuarioDTO();
        creadorInactivo.setId(creadorId);
        creadorInactivo.setActivo(false);   // <-- inactivo

        when(usuarioClient.obtenerUsuario(creadorId)).thenReturn(creadorInactivo);

        // WHEN / THEN
        assertThatThrownBy(() -> tareaService.crearTarea(dto))
            .isInstanceOf(ValidacionRemotaException.class)
            .hasMessageContaining("activo");

        verify(tareaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────
    //  crearTarea — validación Feign: servicio caído
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearTarea — usuario-service CAIDO → FeignException → propagada")
    void crearTarea_servicioUsuarioCaido_propagaFeignException() {
        // GIVEN: Feign lanza excepción genérica (503)
        TareaDTO dto = new TareaDTO();
        dto.setTitulo("Tarea servicio caido");
        dto.setPrioridad(Prioridad.ALTA);
        dto.setCreadorId(creadorId);

        // Simulamos un error genérico de conexión (no 404)
        when(usuarioClient.obtenerUsuario(creadorId))
            .thenThrow(new RuntimeException("Connection refused"));

        // WHEN / THEN — la excepción debe propagarse
        assertThatThrownBy(() -> tareaService.crearTarea(dto))
            .isInstanceOf(RuntimeException.class);

        verify(tareaRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────
    //  cambiarEstado
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("cambiarEstado — PENDIENTE a COMPLETADA → asigna fechaCompletada")
    void cambiarEstado_aCompletada_asignaFecha() {
        // GIVEN
        UUID tareaId = UUID.randomUUID();
        Tarea tarea = new Tarea();
        tarea.setId(tareaId);
        tarea.setTitulo("Implementar login");
        tarea.setEstado(EstadoTarea.PENDIENTE);

        when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        Tarea resultado = tareaService.cambiarEstado(tareaId, EstadoTarea.COMPLETADA);

        // THEN
        assertThat(resultado.getEstado()).isEqualTo(EstadoTarea.COMPLETADA);
        assertThat(resultado.getFechaCompletada()).isNotNull();
    }

    @Test
    @DisplayName("cambiarEstado — tarea no existe → lanza RuntimeException")
    void cambiarEstado_tareaNoExiste_lanzaExcepcion() {
        // GIVEN
        UUID idFake = UUID.randomUUID();
        when(tareaRepository.findById(idFake)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> tareaService.cambiarEstado(idFake, EstadoTarea.EN_PROGRESO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("no encontrada");
    }

    // ─────────────────────────────────────────────────────────────────
    //  listarTareasPorAsignado
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("listarTareasPorAsignado — filtra solo las del usuario dado")
    void listarTareasPorAsignado_retareasFiltradas() {
        // GIVEN
        Tarea t1 = new Tarea();
        t1.setId(UUID.randomUUID());
        t1.setAsignadoId(asignadoId);

        when(tareaRepository.findByAsignadoId(asignadoId)).thenReturn(List.of(t1));

        // WHEN
        List<Tarea> resultado = tareaService.listarTareasPorAsignado(asignadoId);

        // THEN
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAsignadoId()).isEqualTo(asignadoId);
    }
}
