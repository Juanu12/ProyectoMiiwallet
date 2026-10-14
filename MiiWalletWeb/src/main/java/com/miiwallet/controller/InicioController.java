package com.miiwallet.controller;

import com.miiwallet.config.UsuarioActual;
import com.miiwallet.model.ResumenMes;
import com.miiwallet.repository.CatalogoRepository;
import com.miiwallet.repository.FacturaRepository;
import com.miiwallet.repository.PanelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;

/**
 * Panel de inicio: resumen del mes (ingresos, gastos, balance, nivel de riesgo),
 * gasto por categoria contra presupuesto, ultimas facturas y alertas.
 *
 *   GET /                    -> mes mas reciente con movimientos
 *   GET /?periodo=2026-01    -> un mes concreto
 */
@Controller
@RequiredArgsConstructor
public class InicioController {

    private final UsuarioActual usuario;
    private final PanelRepository panel;
    private final CatalogoRepository catalogos;
    private final FacturaRepository facturas;

    @GetMapping("/")
    public String inicio(@RequestParam(name = "periodo", required = false) String texto, Model model) {
        long idUsuario = usuario.getId();
        YearMonth periodo;
        try {
            periodo = YearMonth.parse(texto); // "2026-01"
        } catch (RuntimeException e) {
            // Sin periodo (o mal escrito): el mes mas reciente con movimientos
            periodo = panel.ultimoMesConMovimientos(idUsuario).orElse(YearMonth.now());
        }
        ResumenMes resumen = panel.resumen(idUsuario, periodo);

        model.addAttribute("periodo", periodo);
        model.addAttribute("resumen", resumen);
        model.addAttribute("nivel", resumen.getPorcentajeGasto() == null ? null
                : catalogos.nivelPara(resumen.getPorcentajeGasto()).orElse(null));
        model.addAttribute("gastosCategoria", panel.gastosPorCategoria(idUsuario, periodo));
        model.addAttribute("ultimasFacturas", facturas.ultimas(idUsuario, 5));
        model.addAttribute("alertas", panel.alertas(idUsuario, 5));
        return "inicio";
    }
}
