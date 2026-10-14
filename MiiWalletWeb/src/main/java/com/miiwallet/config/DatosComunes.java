package com.miiwallet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Agrega a TODAS las paginas el correo del usuario con sesion (se muestra en la barra superior).
 */
@ControllerAdvice
@RequiredArgsConstructor
public class DatosComunes {

    private final UsuarioActual usuario;

    @ModelAttribute("usuarioCorreo")
    public String usuarioCorreo() {
        return usuario.haySesion() ? usuario.getCorreo() : null;
    }
}
