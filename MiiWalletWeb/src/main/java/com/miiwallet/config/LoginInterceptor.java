package com.miiwallet.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Se ejecuta ANTES de cada pagina privada: si no hay sesion iniciada, manda al login.
 * Las rutas publicas (login, registro, css) se excluyen en WebConfig.
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final UsuarioActual usuario;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (usuario.haySesion()) {
            return true; // sigue al controlador
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false; // corta la peticion
    }
}
