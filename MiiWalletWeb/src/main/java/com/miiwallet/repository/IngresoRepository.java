package com.miiwallet.repository;

import com.miiwallet.model.Ingreso;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * CRUD de la tabla INGRESO. Todas las consultas filtran por usuario,
 * asi un usuario nunca ve ni modifica los ingresos de otro.
 */
@Repository
@RequiredArgsConstructor
public class IngresoRepository {

    private static final String SELECT = """
            SELECT i.id_ingreso, i.id_tipo_ingreso, i.monto, i.fecha, t.nombre AS tipo
            FROM INGRESO i JOIN TIPO_INGRESO t ON t.id_tipo_ingreso = i.id_tipo_ingreso
            WHERE i.id_usuario = ?
            """;

    // Las fechas se guardan como texto 'AAAA-MM-DD'
    private static final RowMapper<Ingreso> MAPPER = (rs, fila) -> Ingreso.builder()
            .id(rs.getLong("id_ingreso"))
            .idTipoIngreso(rs.getLong("id_tipo_ingreso"))
            .monto(rs.getBigDecimal("monto"))
            .fecha(LocalDate.parse(rs.getString("fecha").substring(0, 10)))
            .tipoNombre(rs.getString("tipo"))
            .build();

    private final JdbcTemplate jdbc;

    public List<Ingreso> listar(long idUsuario) {
        return jdbc.query(SELECT + " ORDER BY i.fecha DESC, i.id_ingreso DESC", MAPPER, idUsuario);
    }

    public Optional<Ingreso> buscarPorId(long id, long idUsuario) {
        return jdbc.query(SELECT + " AND i.id_ingreso = ?", MAPPER, idUsuario, id).stream().findFirst();
    }

    public void crear(Ingreso i, long idUsuario) {
        jdbc.update("INSERT INTO INGRESO (id_usuario, id_tipo_ingreso, monto, fecha) VALUES (?, ?, ?, ?)",
                idUsuario, i.getIdTipoIngreso(), i.getMonto(), i.getFecha().toString());
    }

    public int actualizar(Ingreso i, long idUsuario) {
        return jdbc.update(
                "UPDATE INGRESO SET id_tipo_ingreso = ?, monto = ?, fecha = ? WHERE id_ingreso = ? AND id_usuario = ?",
                i.getIdTipoIngreso(), i.getMonto(), i.getFecha().toString(), i.getId(), idUsuario);
    }

    public int eliminar(long id, long idUsuario) {
        return jdbc.update("DELETE FROM INGRESO WHERE id_ingreso = ? AND id_usuario = ?", id, idUsuario);
    }
}
