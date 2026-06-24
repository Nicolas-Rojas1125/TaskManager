package com.taskmanager.recompensa.exception;

/**
 * Se lanza cuando un dato que viene de otro microservicio es invalido
 * (por ejemplo: el id referenciado no existe o el usuario esta inactivo).
 */
public class ValidacionRemotaException extends RuntimeException {
    public ValidacionRemotaException(String mensaje) {
        super(mensaje);
    }
}
