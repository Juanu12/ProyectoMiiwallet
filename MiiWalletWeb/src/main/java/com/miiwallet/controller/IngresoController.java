package com.miiwallet.controller;

import com.miiwallet.config.UsuarioActual;
import com.miiwallet.model.Ingreso;
import com.miiwallet.model.Opcion;
import com.miiwallet.repository.CatalogoRepository;
import com.miiwallet.repository.IngresoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 *   GET  /ingresos               -> lista del usuario actual
 *   GET  /ingresos/nuevo         -> formulario vacio
 *   GET  /ingresos/{id}/editar   -> formulario con datos
 *   POST /ingresos/guardar       -> crea o actualiza
 *   POST /ingresos/{id}/eliminar -> borra
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class IngresoController {

    private final IngresoRepository repo;
    private final CatalogoRepository catalogos;
    private final UsuarioActual usuario;

    @ModelAttribute("tipos")
    public List<Opcion> tipos() {
        return catalogos.tiposIngreso();
    }

    @GetMapping("/ingresos")
    public String listar(Model model) {
        model.addAttribute("ingresos", repo.listar(usuario.getId()));
        return "ingresos/lista";
    }

    @GetMapping("/ingresos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("ingreso", Ingreso.builder().fecha(LocalDate.now()).build());
        model.addAttribute("titulo", "Nuevo ingreso");
        return "ingresos/form";
    }

    @GetMapping("/ingresos/{id}/editar")
    public String editar(@PathVariable long id, Model model, RedirectAttributes flash) {
        return repo.buscarPorId(id, usuario.getId())
                .map(i -> {
                    model.addAttribute("ingreso", i);
                    model.addAttribute("titulo", "Editar ingreso");
                    return "ingresos/form";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "El ingreso ya no existe.");
                    return "redirect:/ingresos";
                });
    }

    @PostMapping("/ingresos/guardar")
    public String guardar(@Valid @ModelAttribute("ingreso") Ingreso ingreso,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("titulo", ingreso.getId() == null ? "Nuevo ingreso" : "Editar ingreso");
            return "ingresos/form";
        }
        if (ingreso.getId() == null) {
            repo.crear(ingreso, usuario.getId());
            log.info("Ingreso creado: {}", ingreso.getMonto());
            flash.addFlashAttribute("mensaje", "Ingreso registrado.");
        } else if (repo.actualizar(ingreso, usuario.getId()) > 0) {
            flash.addFlashAttribute("mensaje", "Ingreso actualizado.");
        } else {
            flash.addFlashAttribute("error", "El ingreso ya no existe.");
        }
        return "redirect:/ingresos";
    }

    @PostMapping("/ingresos/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes flash) {
        if (repo.eliminar(id, usuario.getId()) > 0) {
            flash.addFlashAttribute("mensaje", "Ingreso eliminado.");
        } else {
            flash.addFlashAttribute("error", "El ingreso ya no existe.");
        }
        return "redirect:/ingresos";
    }
}
