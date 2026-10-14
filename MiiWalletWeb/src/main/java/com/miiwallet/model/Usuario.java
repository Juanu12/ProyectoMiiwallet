package com.miiwallet.model;

import lombok.Value;

/**
 * Tabla USUARIO. contrasenaHash es el hash BCrypt, nunca la contrasena real.
 */
@Value
public class Usuario {
    Long id;
    String correo;
    String contrasenaHash;
}
