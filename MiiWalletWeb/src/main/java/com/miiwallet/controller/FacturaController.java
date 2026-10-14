package com.miiwallet.controller;

import com.miiwallet.config.UsuarioActual;
import com.miiwallet.model.DetalleFactura;
import com.miiwallet.model.Factura;
import com.miiwallet.repository.CatalogoRepository;
import com.miiwallet.repository.CategoriaRepository;
import com.miiwallet.repository.FacturaRepository;
import com.miiwallet.repository.ProductoRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Facturas (gastos) y su detalle de productos.
 *
 *   GET  /facturas                                  -> historial
 *   GET  /facturas/nueva                            -> formulario vacio
 *   GET  /facturas/{id}                             -> ver factura + detalle de productos
 *   GET  /facturas/{id}/editar                      -> formulario con datos
 *   POST /facturas/guardar                          -> crea o actualiza
 *   POST /facturas/{id}/eliminar                    -> borra (y su detalle)
 *   POST /facturas/{id}/detalle                     -> agrega un producto
 *   POST /facturas/{id}/detalle/{idDetalle}/eliminar-> quita un producto
 *   POST /facturas/{id}/usar-suma                   -> total = suma del detalle
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaRepository repo;
    private final CategoriaRepository categorias;
    private final CatalogoRepository catalogos;
    private final ProductoRepository productos;
    private final UsuarioActual usuario;

    @GetMapping("/facturas")
    public String listar(Model model) {
        model.addAttribute("facturas", repo.listar(usuario.getId()));
        return "facturas/lista";
    }

    @GetMapping("/facturas/nueva")
    public String nueva(Model model) {
        Factura f = Factura.builder().fecha(LocalDate.now()).total(BigDecimal.ZERO).build();
        return mostrarFormulario(f, "Nueva factura", model);
    }

    @GetMapping("/facturas/{id}/editar")
    public String editar(@PathVariable long id, Model model, RedirectAttributes flash) {
        Optional<Factura> f = repo.buscarPorId(id, usuario.getId());
        if (f.isEmpty()) {
            flash.addFlashAttribute("error", "La factura ya no existe.");
            return "redirect:/facturas";
        }
        return mostrarFormulario(f.get(), "Editar factura", model);
    }

    @PostMapping("/facturas/guardar")
    public String guardar(@Valid @ModelAttribute("factura") Factura factura,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            return mostrarFormulario(factura, factura.getId() == null ? "Nueva factura" : "Editar factura", model);
        }
        if (factura.getId() == null) {
            long id = repo.crear(factura, usuario.getId());
            log.info("Factura creada: id={} {}", id, factura.getComercio());
            flash.addFlashAttribute("mensaje", "Factura registrada. Ahora puedes agregarle productos.");
            return "redirect:/facturas/" + id;
        }
        if (repo.actualizar(factura, usuario.getId()) > 0) {
            flash.addFlashAttribute("mensaje", "Factura actualizada.");
            return "redirect:/facturas/" + factura.getId();
        }
        flash.addFlashAttribute("error", "La factura ya no existe.");
        return "redirect:/facturas";
    }

    @PostMapping("/facturas/{id}/eliminar")
    public String eliminar(@PathVariable long id, RedirectAttributes flash) {
        if (repo.eliminar(id, usuario.getId()) > 0) {
            flash.addFlashAttribute("mensaje", "Factura eliminada.");
        } else {
            flash.addFlashAttribute("error", "La factura ya no existe.");
        }
        return "redirect:/facturas";
    }

    // ---------- Ver factura y su detalle ----------

    @GetMapping("/facturas/{id}")
    public String ver(@PathVariable long id, Model model, RedirectAttributes flash) {
        Optional<Factura> f = repo.buscarPorId(id, usuario.getId());
        if (f.isEmpty()) {
            flash.addFlashAttribute("error", "La factura ya no existe.");
            return "redirect:/facturas";
        }
        if (!model.containsAttribute("detalle")) {
            model.addAttribute("detalle", DetalleFactura.builder().cantidad(BigDecimal.ONE).build());
        }
        return mostrarFactura(f.get(), model);
    }

    @PostMapping("/facturas/{id}/detalle")
    public String agregarDetalle(@PathVariable long id,
                                 @Valid @ModelAttribute("detalle") DetalleFactura detalle,
                                 BindingResult errores, Model model, RedirectAttributes flash) {
        Optional<Factura> f = repo.buscarPorId(id, usuario.getId());
        if (f.isEmpty()) {
            flash.addFlashAttribute("error", "La factura ya no existe.");
            return "redirect:/facturas";
        }
        if (errores.hasErrors()) {
            return mostrarFactura(f.get(), model);
        }
        detalle.setIdFactura(id);
        repo.agregarDetalle(detalle);
        flash.addFlashAttribute("mensaje", "Producto agregado.");
        return "redirect:/facturas/" + id;
    }

    @PostMapping("/facturas/{id}/detalle/{idDetalle}/eliminar")
    public String eliminarDetalle(@PathVariable long id, @PathVariable long idDetalle, RedirectAttributes flash) {
        if (repo.buscarPorId(id, usuario.getId()).isPresent() && repo.eliminarDetalle(idDetalle, id) > 0) {
            flash.addFlashAttribute("mensaje", "Producto quitado de la factura.");
        } else {
            flash.addFlashAttribute("error", "Ese producto ya no está en la factura.");
        }
        return "redirect:/facturas/" + id;
    }

    @PostMapping("/facturas/{id}/usar-suma")
    public String usarSuma(@PathVariable long id, RedirectAttributes flash) {
        if (repo.actualizarTotal(id, usuario.getId(), repo.sumaDetalles(id)) > 0) {
            flash.addFlashAttribute("mensaje", "Total actualizado con la suma de los productos.");
        }
        return "redirect:/facturas/" + id;
    }

    // ---------- Ayudas ----------

    private String mostrarFormulario(Factura f, String titulo, Model model) {
        model.addAttribute("factura", f);
        model.addAttribute("titulo", titulo);
        model.addAttribute("categorias", categorias.opcionesDeGasto());
        model.addAttribute("metodos", catalogos.metodosPago());
        return "facturas/form";
    }

    private String mostrarFactura(Factura f, Model model) {
        model.addAttribute("factura", f);
        model.addAttribute("detalles", repo.detalles(f.getId()));
        model.addAttribute("sumaDetalles", repo.sumaDetalles(f.getId()));
        model.addAttribute("productos", productos.listar());
        return "facturas/ver";
    }
}
