package com.miiwallet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Datos del formulario para crear una cuenta.
 */
@Data
public class RegistroForm {

    @NotBlank(message = "Escribe tu correo")
    @Email(message = "Correo no válido")
    @Size(max = 120, message = "Máximo 120 caracteres")
    private String correo;

    @NotBlank(message = "Escribe una contraseña")
    @Size(min = 6, max = 72, message = "Entre 6 y 72 caracteres")
    private String contrasena;

    @NotBlank(message = "Repite la contraseña")
    private String confirmacion;
}
