# TaskManager — Arquitectura de Microservicios

Sistema de gestión de tareas y equipos de trabajo construido con **Spring Boot** y **Spring Cloud**, compuesto por **10 microservicios de dominio** más **3 servicios de infraestructura** (Config Server, Eureka y API Gateway). La comunicación entre microservicios se realiza por **REST** usando **OpenFeign**, con descubrimiento de servicios vía Eureka.

> Proyecto académico — DSY1103, Duoc UC. Evaluación Parcial 3.

## Integrantes

- _(completar: nombre — sección)_
- _(completar: nombre — sección)_

## Stack técnico

- Java 17
- Spring Boot 3.3.11
- Spring Cloud 2023.0.5 (Eureka, Config, Gateway, OpenFeign)
- MySQL 8 (una base de datos por microservicio)
- Maven (cada servicio es un proyecto independiente con su wrapper `mvnw`)

## Arquitectura

```
                         ┌──────────────────┐
   Cliente / Postman ───▶│  API Gateway     │  :8080  (puerta única)
                         └────────┬─────────┘
                                  │ enruta por Path → lb://servicio
        ┌─────────────────────────┼─────────────────────────┐
        ▼                         ▼                          ▼
   usuario:8082            tarea:8083  ...           reporte:8091
        │                         │                          │
        └──── se registran ───────┴─── y leen config ────────┘
                    │                          │
            Eureka Server :8761        Config Server :8888
```

- **Config Server (8888):** centraliza la configuración de todos los servicios (perfil `native`, carpeta `configurations/`).
- **Eureka Server (8761):** registro de descubrimiento; los servicios se encuentran por nombre, no por IP.
- **API Gateway (8080):** punto único de entrada; enruta según el prefijo de la URL.

## Microservicios

| # | Servicio | Puerto | Base de datos | Ruta base | Consume (Feign) |
|---|---|---|---|---|---|
| 1 | usuario | 8082 | usuario_db | `/api/usuario` | — (núcleo) |
| 2 | tarea | 8083 | tareas_db | `/api/tareas` | usuario |
| 3 | notificacion | 8084 | notificaciones_db | `/api/notificaciones` | usuario, tarea |
| 4 | auditoria | 8085 | auditoria_db | `/api/auditoria` | usuario, tarea |
| 5 | recompensa | 8086 | gamificacion_db | `/api/gamificacion` | usuario |
| 6 | seguridad | 8087 | seguridad_db | `/api/seguridad` | usuario |
| 7 | comentario | 8088 | comentarios_db | `/api/comentarios` | usuario, tarea |
| 8 | adjunto | 8089 | adjuntos_db | `/api/adjuntos` | usuario, tarea |
| 9 | equipo | 8090 | equipos_db | `/api/equipos` | usuario |
| 10 | reporte | 8091 | reportes_db | `/api/reportes` | usuario |

> **usuario** es la fuente de verdad de las personas: no consume a nadie, por eso es el único sin Feign. Los demás lo consultan para validar que un usuario exista y esté activo. **tarea** es un hub secundario (comentario, adjunto, notificación y auditoría validan que la tarea exista).

## Comunicación entre microservicios (Feign)

Cada servicio que referencia datos de otro valida su existencia antes de guardar, mediante un cliente Feign que resuelve el destino por nombre vía Eureka:

```java
@FeignClient(name = "usuario")
public interface UsuarioClient {
    @GetMapping("/api/usuario/{id}")
    UsuarioDTO obtenerUsuario(@PathVariable("id") UUID id);
}
```

Los errores de la comunicación remota se traducen a códigos HTTP claros en un `@RestControllerAdvice`:

| Situación | Respuesta |
|---|---|
| Dato inválido (id inexistente o usuario inactivo) | 400 Bad Request |
| El recurso remoto no existe (404 del otro servicio) | 404 Not Found |
| El servicio remoto está caído | 503 Service Unavailable |

## Cómo ejecutar (local)

**Requisitos:** JDK 17+, MySQL corriendo en `localhost:3306` (usuario `root`, sin contraseña; las bases se crean solas).

Orden de arranque (obligatorio):

```
1. config-server   (8888)
2. eureka-server   (8761)
3. los 10 microservicios   (usuario primero)
4. api-gateway     (8080)
```

Para cada proyecto, desde su carpeta:

```bash
./mvnw spring-boot:run
```

o compilar el jar:

```bash
./mvnw clean package -DskipTests
java -jar target/<servicio>-0.0.1-SNAPSHOT.jar
```

> Se usa `-DskipTests` porque los tests por defecto (`contextLoads`) levantan el contexto completo y requieren la infraestructura arriba. Las pruebas unitarias con JUnit/Mockito quedan como trabajo pendiente (ver más abajo).

**Verificación:** abrir el panel de Eureka en `http://localhost:8761` y confirmar que los servicios aparecen registrados. Probar endpoints a través del Gateway, por ejemplo `GET http://localhost:8080/api/usuario`.

## Pendientes (rúbrica)

Estos elementos los pide la rúbrica y aún **no** están implementados; quedan como trabajo del equipo:

- Documentación **Swagger / OpenAPI** (`springdoc-openapi`).
- **Pruebas unitarias** JUnit + Mockito (cobertura objetivo 80%).
- **Despliegue** en Docker / Railway / Render.
- **Repositorio Git** con historial distribuido y tablero **Trello**.

## Estructura del repositorio

```
taskmanager2/
├── config-server/      # 8888 — configuración centralizada
├── eureka-server/      # 8761 — descubrimiento
├── api-gateway/        # 8080 — enrutamiento
├── usuario/  tarea/  equipo/  comentario/  adjunto/
├── notificacion/  auditoria/  recompensa/  reporte/  seguridad/
├── README.md
└── GUIA_DEFENSA.md     # guía de estudio para la defensa técnica
```
