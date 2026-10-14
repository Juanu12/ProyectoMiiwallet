package com.miiwallet.repository;

import com.miiwallet.model.Alerta;
import com.miiwallet.model.GastoCategoria;
import com.miiwallet.model.ResumenMes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Consultas de solo lectura para el panel de inicio: totales del mes,
 * gasto por categoria contra su presupuesto y alertas.
 * Los meses se comparan con substr(fecha, 1, 7) = 'AAAA-MM'.
 */
@Repository
@RequiredArgsConstructor
public class PanelRepository {

    private final JdbcTemplate jdbc;

    public ResumenMes resumen(long idUsuario, YearMonth periodo) {
        String mes = periodo.toString(); // "2026-01"
        BigDecimal ingresos = jdbc.queryForObject(
                "SELECT COALESCE(SUM(monto), 0) FROM INGRESO WHERE id_usuario = ? AND substr(fecha, 1, 7) = ?",
                BigDecimal.class, idUsuario, mes);
        BigDecimal gastos = jdbc.queryForObject(
                "SELECT COALESCE(SUM(total), 0) FROM FACTURA WHERE id_usuario = ? AND substr(fecha, 1, 7) = ?",
                BigDecimal.class, idUsuario, mes);
        return new ResumenMes(periodo.getYear(), periodo.getMonthValue(),
                ingresos == null ? BigDecimal.ZERO : ingresos,
                gastos == null ? BigDecimal.ZERO : gastos);
    }

    public List<GastoCategoria> gastosPorCategoria(long idUsuario, YearMonth periodo) {
        return jdbc.query("""
                        SELECT c.nombre AS categoria, SUM(f.total) AS total,
                               (SELECT p.monto_limite FROM PRESUPUESTO p
                                 WHERE p.id_usuario = f.id_usuario AND p.id_categoria = c.id_categoria
                                   AND p.anio = ? AND p.mes = ?
                                 LIMIT 1) AS limite
                        FROM FACTURA f JOIN CATEGORIA c ON c.id_categoria = f.id_categoria
                        WHERE f.id_usuario = ? AND substr(f.fecha, 1, 7) = ?
                        GROUP BY c.id_categoria, c.nombre
                        ORDER BY total DESC""",
                (rs, fila) -> new GastoCategoria(rs.getString("categoria"),
                        rs.getBigDecimal("total"), rs.getBigDecimal("limite")),
                periodo.getYear(), periodo.getMonthValue(), idUsuario, periodo.toString());
    }

    public List<Alerta> alertas(long idUsuario, int n) {
        return jdbc.query("""
                        SELECT id_alerta, nivel_severidad, mensaje, fecha FROM ALERTA
                        WHERE id_usuario = ? ORDER BY fecha DESC, id_alerta DESC LIMIT ?""",
                (rs, fila) -> new Alerta(rs.getLong("id_alerta"), rs.getString("nivel_severidad"),
                        rs.getString("mensaje"), LocalDate.parse(rs.getString("fecha").substring(0, 10))),
                idUsuario, n);
    }

    /** El mes mas reciente con ingresos o facturas, para abrir el panel en un mes con datos. */
    public Optional<YearMonth> ultimoMesConMovimientos(long idUsuario) {
        String mes = jdbc.queryForObject("""
                        SELECT MAX(m) FROM (
                            SELECT substr(fecha, 1, 7) AS m FROM INGRESO WHERE id_usuario = ?
                            UNION ALL
                            SELECT substr(fecha, 1, 7) FROM FACTURA WHERE id_usuario = ?)""",
                String.class, idUsuario, idUsuario);
        return mes == null ? Optional.empty() : Optional.of(YearMonth.parse(mes));
    }
}
