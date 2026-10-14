package com.miiwallet.controller;

import com.miiwallet.config.UsuarioActual;
import com.miiwallet.model.LoginForm;
import com.miiwallet.model.RegistroForm;
import com.miiwallet.model.Usuario;
import com.miiwallet.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Inicio de sesion, registro y cierre de sesion.
 *
 *   GET  /login     -> formulario de login
 *   POST /login     -> valida correo + contrasena y abre la sesion
 *   GET  /registro  -> formulario para crear cuenta
 *   POST /registro  -> crea el usuario (contrasena con hash BCrypt) y abre la sesion
 *   POST /logout    -> cierra la sesion
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarios;
    private final UsuarioActual usuarioActual;
    private final PasswordEncoder encoder;

    // ---------- LOGIN ----------

    @GetMapping("/login")
    public String login(Model model) {
        if (usuarioActual.haySesion()) {
            return "redirect:/";
        }
        model.addAttribute("form", new LoginForm());
        return "auth/login";
    }

    @PostMapping("/login")
    public String entrar(@Valid @ModelAttribute("form") LoginForm form, BindingResult errores,
                         Model model, HttpServletRequest request) {
        if (errores.hasErrors()) {
            return "auth/login";
        }
        Optional<Usuario> u = usuarios.buscarPorCorreo(form.getCorreo());
        if (u.isEmpty() || !contrasenaValida(form.getContrasena(), u.get().getContrasenaHash())) {
            // Mismo mensaje en ambos casos: no revelamos si el correo existe
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            form.setContrasena(null);
            return "auth/login";
        }
        abrirSesion(request, u.get().getId(), u.get().getCorreo());
        log.info("Inicio de sesion: {}", u.get().getCorreo());
        return "redirect:/";
    }

    // ---------- REGISTRO ----------

    @GetMapping("/registro")
    public String registro(Model model) {
        if (usuarioActual.haySesion()) {
            return "redirect:/";
        }
        model.addAttribute("form", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("form") RegistroForm form, BindingResult errores,
                            HttpServletRequest request, RedirectAttributes flash) {
        if (!errores.hasFieldErrors("confirmacion") && form.getContrasena() != null
                && !form.getContrasena().equals(form.getConfirmacion())) {
            errores.rejectValue("confirmacion", "noCoincide", "Las contraseñas no coinciden");
        }
        if (!errores.hasFieldErrors("correo") && usuarios.existeCorreo(form.getCorreo())) {
            errores.rejectValue("correo", "duplicado", "Ya existe una cuenta con ese correo");
        }
        if (errores.hasErrors()) {
            form.setContrasena(null);
            form.setConfirmacion(null);
            return "auth/registro";
        }
        long id = usuarios.crear(form.getCorreo(), encoder.encode(form.getContrasena()));
        abrirSesion(request, id, form.getCorreo().trim().toLowerCase());
        log.info("Cuenta creada: id={}", id);
        flash.addFlashAttribute("mensaje", "¡Bienvenido a MiiWallet! Tu cuenta fue creada.");
        return "redirect:/";
    }

    // ---------- LOGOUT ----------

    @PostMapping("/logout")
    public String salir(RedirectAttributes flash) {
        usuarioActual.cerrar();
        flash.addFlashAttribute("mensaje", "Cerraste sesión.");
        return "redirect:/login";
    }

    // ---------- Ayudas ----------

    /** Cambia el id de la sesion antes de guardar el usuario (evita el robo de sesion por "fixation"). */
    private void abrirSesion(HttpServletRequest request, long id, String correo) {
        request.getSession(true);
        request.changeSessionId();
        usuarioActual.iniciar(id, correo);
    }

    /** Compara con BCrypt. Si el hash guardado no es BCrypt (datos viejos), no deja entrar. */
    private boolean contrasenaValida(String contrasena, String hash) {
        if (hash == null || !hash.startsWith("$2")) {
            return false;
        }
        return encoder.matches(contrasena, hash);
    }
}
