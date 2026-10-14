package com.miiwallet.config;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * El usuario que inicio sesion. Su id y correo se guardan en la sesion HTTP
 * (cada navegador tiene la suya), asi cada persona solo ve sus propios datos.
 *
 * Spring inyecta aqui la sesion de la peticion que se esta atendiendo en ese momento.
 */
@Component
@RequiredArgsConstructor
public class UsuarioActual {

    private static final String ID = "usuarioId";
    private static final String CORREO = "usuarioCorreo";

    private final HttpSession session;

    /** Lo guarda en la sesion al iniciar sesion correctamente. */
    public void iniciar(long id, String correo) {
        session.setAttribute(ID, id);
        session.setAttribute(CORREO, correo);
    }

    /** Borra la sesion completa (cerrar sesion). */
    public void cerrar() {
        session.invalidate();
    }

    public boolean haySesion() {
        return session.getAttribute(ID) != null;
    }

    /** Id del usuario logueado. El LoginInterceptor garantiza que exista en las paginas privadas. */
    public long getId() {
        Long id = (Long) session.getAttribute(ID);
        if (id == null) {
            throw new IllegalStateException("No hay un usuario con sesion iniciada");
        }
        return id;
    }

    public String getCorreo() {
        return (String) session.getAttribute(CORREO);
    }
}
