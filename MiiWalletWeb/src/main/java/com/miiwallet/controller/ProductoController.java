package com.miiwallet.controller;

import com.miiwallet.model.Opcion;
import com.miiwallet.model.Producto;
import com.miiwallet.repository.CategoriaRepository;
import com.miiwallet.repository.ProductoRepository;
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
 *   GET  /productos               -> lista
 *   GET  /productos/nuevo         -> formulario vacio
 *   GET  /productos/{id}/editar   -> formulario con datos
 *   POST /productos/guardar       -> crea o actualiza
 *   POST /productos/{id}/eliminar -> borra (si no esta en ninguna factura)
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository repo;
    private final CategoriaRepository categorias;

    @ModelAttribute("categorias")
    public List<Opcion> categorias() {
        return categorias.opcionesDeGasto();
    }

    @GetMapping("/productos")
    public String listar(Model model) {
        model.addAttribute("productos", repo.listar());
        return "productos/lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Nuevo producto");
        return "productos/form";
    }

    @GetMapping("/productos/{id}/editar")
    public String editar(@PathVariable long id, Model model, RedirectAttributes flash) {
        return repo.buscarPorId(id)
                .map(p -> {
                    model.addAttribute("producto", p);
                    model.addAttribute("titulo", "Editar producto");
                    return "productos/form";
                })
                .orElseGet(() -> {
                    flash.addFlashAttribute("error", "El producto ya no existe.");
                    return "redirect:/productos";
                });
    }

    @PostMapping("/productos/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("titulo", producto.getId() == null ? "Nuevo producto" : "Editar producto");
            return "productos/form";
        }
        if (producto.getId() == null) {
            repo.crear(producto);
            log.info("Producto creado: {}", producto.getNombre());
            flash.addFlashAttribute("mensaje", "Producto creado.");
        } else if (repo.actualizar(producto) > 0) {
            flash.addFlashAttribute("mensaje", "Producto actualizado.");
        } else {
            flash.addFlashAttribute("error", "El producto ya no existe.");
        }
        return "redirect:/productos";
    }

    @PostMapping("/productos/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes flash) {
        try {
            if (repo.eliminar(id) > 0) {
                flash.addFlashAttribute("mensaje", "Producto eliminado.");
            } else {
                flash.addFlashAttribute("error", "El producto ya no existe.");
            }
        } catch (DataAccessException e) {
            log.warn("No se pudo eliminar el producto {}: {}", id, e.getMostSpecificCause().getMessage());
            flash.addFlashAttribute("error", "No se puede eliminar: el producto aparece en alguna factura.");
        }
        return "redirect:/productos";
    }
}
