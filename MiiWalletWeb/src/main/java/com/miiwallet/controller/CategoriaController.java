package com.miiwallet.controller;

import com.miiwallet.model.Categoria;
import com.miiwallet.repository.CategoriaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 *   GET  /categorias               -> lista
 *   GET  /categorias/nueva         -> formulario vacio
 *   GET  /categorias/{id}/editar   -> formulario con datos
 *   POST /categorias/guardar       -> crea o actualiza
 *   POST /categorias/{id}/eliminar -> borra (si no esta en uso)
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository repo;

    @ModelAttribute("ambitos")
    public List<String> ambitos() {
        return List.of("gasto", "ingreso", "general");
    }

    @GetMapping("/categorias")
    public String listar(Model model) {
        model.addAttribute("categorias", repo.listar());
        return "categorias/lista";
    }

    @GetMapping("/categorias/nueva")
    public String nueva(Model model) {
        model.addAttribute("categoria", Categoria.builder().ambito("gasto").build());
        model.addAttribute("titulo", "Nueva categoría");
        return "categorias/form";
    }

    @GetMapping("/categorias/{id}/editar")
    public String editar(@PathVariable long id, Model model, RedirectAttributes flash) {
        return repo.buscarPorId(id)
                .map(c -> {
                    model.addAttribute("categoria", c);
                    model.addAttribute("titulo", "Editar categoría");
                    return "categorias/form";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "La categoría ya no existe.");
                    return "redirect:/categorias";
                });
    }

    @PostMapping("/categorias/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("titulo", categoria.getId() == null ? "Nueva categoría" : "Editar categoría");
            return "categorias/form";
        }
        if (categoria.getId() == null) {
            repo.crear(categoria);
            flash.addFlashAttribute("mensaje", "Categoría creada.");
        } else if (repo.actualizar(categoria) > 0) {
            flash.addFlashAttribute("mensaje", "Categoría actualizada.");
        } else {
            flash.addFlashAttribute("error", "La categoría ya no existe.");
        }
        return "redirect:/categorias";
    }

    @PostMapping("/categorias/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes flash) {
        try {
            if (repo.eliminar(id) > 0) {
                flash.addFlashAttribute("mensaje", "Categoría eliminada.");
            } else {
                flash.addFlashAttribute("error", "La categoría ya no existe.");
            }
        } catch (DataAccessException e) {
            // La llave foranea lo impide: hay productos, facturas o presupuestos con esta categoria
            log.warn("No se pudo eliminar la categoria {}: {}", id, e.getMostSpecificCause().getMessage());
            flash.addFlashAttribute("error",
                    "No se puede eliminar: la categoría está en uso (productos, facturas o presupuestos).");
        }
        return "redirect:/categorias";
    }
}
