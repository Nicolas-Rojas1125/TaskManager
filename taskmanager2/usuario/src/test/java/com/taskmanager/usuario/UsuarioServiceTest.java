package com.taskmanager.usuario;

import com.taskmanager.usuario.dto.UsuarioDTO;
import com.taskmanager.usuario.dto.UsuarioLoginRequest;
import com.taskmanager.usuario.dto.UsuarioResponse;
import com.taskmanager.usuario.exception.RecursoNoEncontradoException;
import com.taskmanager.usuario.model.Rol;
import com.taskmanager.usuario.model.Usuario;
import com.taskmanager.usuario.repository.UsuarioRepository;
import com.taskmanager.usuario.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios del UsuarioService.
 * Patrón Given-When-Then (Arrange-Act-Assert).
 * No levanta el contexto Spring (@ExtendWith(MockitoExtension.class)),
 * por eso son rápidos y no necesitan base de datos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService — pruebas unitarias")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UUID idFijo;
    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        idFijo = UUID.randomUUID();
        usuarioBase = new Usuario();
        usuarioBase.setId(idFijo);
        usuarioBase.setNombre("Ana García");
        usuarioBase.setEmail("ana@example.com");
        usuarioBase.setPasswordHash("secret");
        usuarioBase.setRol(Rol.USER);
        usuarioBase.setActivo(true);
        usuarioBase.setFechaRegistro(LocalDateTime.now());
    }

    // ─────────────────────────────────────────────────────────────────
    //  listarUsuarios
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("listarUsuarios — devuelve lista de usuarios")
    void listarUsuarios_devuelveLista() {
        // GIVEN
        Usuario segundo = new Usuario();
        segundo.setId(UUID.randomUUID());
        segundo.setNombre("Carlos López");
        segundo.setEmail("carlos@example.com");
        segundo.setRol(Rol.ADMIN);
        segundo.setActivo(true);
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuarioBase, segundo));

        // WHEN
        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        // THEN
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Ana García");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Carlos López");
        verify(usuarioRepository, times(1)).findAll();
    }

    // ─────────────────────────────────────────────────────────────────
    //  obtenerPorId
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId — usuario existe → retorna UsuarioResponse")
    void obtenerPorId_cuandoExiste_retornaUsuario() {
        // GIVEN
        when(usuarioRepository.findById(idFijo)).thenReturn(Optional.of(usuarioBase));

        // WHEN
        UsuarioResponse resultado = usuarioService.obtenerPorId(idFijo);

        // THEN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(idFijo);
        assertThat(resultado.getEmail()).isEqualTo("ana@example.com");
        assertThat(resultado.getActivo()).isTrue();
    }

    @Test
    @DisplayName("obtenerPorId — usuario NO existe → lanza RecursoNoEncontradoException")
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        // GIVEN
        UUID idInexistente = UUID.randomUUID();
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> usuarioService.obtenerPorId(idInexistente))
            .isInstanceOf(RecursoNoEncontradoException.class)
            .hasMessageContaining(idInexistente.toString());
    }

    // ─────────────────────────────────────────────────────────────────
    //  crearUsuario
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearUsuario — email nuevo → guarda y retorna usuario")
    void crearUsuario_emailNuevo_guardaUsuario() {
        // GIVEN
        UsuarioDTO dto = new UsuarioDTO("Ana García", "ana@example.com", "secret", Rol.USER);
        when(usuarioRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioBase);

        // WHEN
        UsuarioResponse resultado = usuarioService.crearUsuario(dto);

        // THEN
        assertThat(resultado.getNombre()).isEqualTo("Ana García");
        assertThat(resultado.getEmail()).isEqualTo("ana@example.com");
        assertThat(resultado.getActivo()).isTrue();
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crearUsuario — email duplicado → lanza RuntimeException")
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        // GIVEN
        UsuarioDTO dto = new UsuarioDTO("Ana García", "ana@example.com", "secret", Rol.USER);
        when(usuarioRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuarioBase));

        // WHEN / THEN
        assertThatThrownBy(() -> usuarioService.crearUsuario(dto))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("email");

        verify(usuarioRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────
    //  desactivarUsuario
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("desactivarUsuario — borrado lógico: activo pasa a false")
    void desactivarUsuario_ponActivoFalse() {
        // GIVEN
        when(usuarioRepository.findById(idFijo)).thenReturn(Optional.of(usuarioBase));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // WHEN
        usuarioService.desactivarUsuario(idFijo);

        // THEN — no se borra el registro; solo se cambia activo
        verify(usuarioRepository, never()).delete(any());
        verify(usuarioRepository).save(argThat(u -> !u.getActivo()));
    }

    @Test
    @DisplayName("desactivarUsuario — id inexistente → lanza RuntimeException")
    void desactivarUsuario_idInexistente_lanzaExcepcion() {
        // GIVEN
        UUID idFake = UUID.randomUUID();
        when(usuarioRepository.findById(idFake)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThatThrownBy(() -> usuarioService.desactivarUsuario(idFake))
            .isInstanceOf(RuntimeException.class);
    }

    // ─────────────────────────────────────────────────────────────────
    //  loginUsuario
    // ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("loginUsuario — credenciales correctas → retorna usuario")
    void loginUsuario_credencialesCorrectas_retornaUsuario() {
        // GIVEN
        UsuarioLoginRequest loginReq = new UsuarioLoginRequest();
        loginReq.setEmail("ana@example.com");
        loginReq.setContraseña("secret");
        when(usuarioRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuarioBase));

        // WHEN
        UsuarioResponse resultado = usuarioService.loginUsuario(loginReq);

        // THEN
        assertThat(resultado.getEmail()).isEqualTo("ana@example.com");
    }

    @Test
    @DisplayName("loginUsuario — contraseña incorrecta → lanza RuntimeException")
    void loginUsuario_contrasenaIncorrecta_lanzaExcepcion() {
        // GIVEN
        UsuarioLoginRequest loginReq = new UsuarioLoginRequest();
        loginReq.setEmail("ana@example.com");
        loginReq.setContraseña("incorrecta");
        when(usuarioRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuarioBase));

        // WHEN / THEN
        assertThatThrownBy(() -> usuarioService.loginUsuario(loginReq))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Credenciales");
    }

    @Test
    @DisplayName("loginUsuario — usuario inactivo → lanza RuntimeException")
    void loginUsuario_usuarioInactivo_lanzaExcepcion() {
        // GIVEN
        usuarioBase.setActivo(false);
        UsuarioLoginRequest loginReq = new UsuarioLoginRequest();
        loginReq.setEmail("ana@example.com");
        loginReq.setContraseña("secret");
        when(usuarioRepository.findByEmail("ana@example.com")).thenReturn(Optional.of(usuarioBase));

        // WHEN / THEN
        assertThatThrownBy(() -> usuarioService.loginUsuario(loginReq))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("inactivo");
    }
}
