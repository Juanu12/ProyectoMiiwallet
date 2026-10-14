package com.miiwallet.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Datos del formulario de inicio de sesion.
 */
@Data
public class LoginForm {

    @NotBlank(message = "Escribe tu correo")
    private String correo;

    @NotBlank(message = "Escribe tu contraseña")
    private String contrasena;
}
