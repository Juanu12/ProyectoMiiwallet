package com.miiwallet.repository;

import com.miiwallet.model.NivelRiesgo;
import com.miiwallet.model.Opcion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Catalogos fijos: TIPO_INGRESO, METODO_PAGO y NIVEL_RIESGO (solo lectura y carga inicial).
 */
@Repository
@RequiredArgsConstructor
public class CatalogoRepository {

    private final JdbcTemplate jdbc;

    public List<Opcion> tiposIngreso() {
        return jdbc.query("SELECT id_tipo_ingreso, nombre FROM TIPO_INGRESO ORDER BY nombre",
                (rs, fila) -> new Opcion(rs.getLong("id_tipo_ingreso"), rs.getString("nombre")));
    }

    public List<Opcion> metodosPago() {
        return jdbc.query("SELECT id_metodo, nombre FROM METODO_PAGO ORDER BY nombre",
                (rs, fila) -> new Opcion(rs.getLong("id_metodo"), rs.getString("nombre")));
    }

    public List<NivelRiesgo> nivelesRiesgo() {
        return jdbc.query("SELECT * FROM NIVEL_RIESGO ORDER BY porcentaje_min",
                (rs, fila) -> new NivelRiesgo(rs.getLong("id_nivel"), rs.getString("nombre"),
                        rs.getBigDecimal("porcentaje_min"), rs.getBigDecimal("porcentaje_max")));
    }

    /**
     * Busca el nivel cuyo rango contiene el porcentaje.
     * Si pasa del 100% (se gasto mas de lo que entro) se toma el nivel mas alto.
     */
    public Optional<NivelRiesgo> nivelPara(BigDecimal porcentaje) {
        List<NivelRiesgo> niveles = nivelesRiesgo();
        for (NivelRiesgo n : niveles) {
            if (porcentaje.compareTo(n.getPorcentajeMin()) >= 0 && porcentaje.compareTo(n.getPorcentajeMax()) <= 0) {
                return Optional.of(n);
            }
        }
        if (!niveles.isEmpty() && porcentaje.compareTo(niveles.get(niveles.size() - 1).getPorcentajeMax()) > 0) {
            return Optional.of(niveles.get(niveles.size() - 1));
        }
        return Optional.empty();
    }

    // ---- Carga inicial (solo si la BD es nueva) ----

    public int contarTiposIngreso() {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM TIPO_INGRESO", Integer.class);
        return n == null ? 0 : n;
    }

    public void crearTipoIngreso(String nombre) {
        jdbc.update("INSERT OR IGNORE INTO TIPO_INGRESO (nombre) VALUES (?)", nombre);
    }

    public void crearMetodoPago(String nombre) {
        jdbc.update("INSERT OR IGNORE INTO METODO_PAGO (nombre) VALUES (?)", nombre);
    }

    public void crearNivelRiesgo(String nombre, double min, double max) {
        jdbc.update("INSERT OR IGNORE INTO NIVEL_RIESGO (nombre, porcentaje_min, porcentaje_max) VALUES (?, ?, ?)",
                nombre, min, max);
    }
}
