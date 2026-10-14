package com.miiwallet.repository;

import com.miiwallet.model.Categoria;
import com.miiwallet.model.Opcion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CRUD de la tabla CATEGORIA con SQL puro.
 */
@Repository
@RequiredArgsConstructor
public class CategoriaRepository {

    private static final RowMapper<Categoria> MAPPER = (rs, fila) -> Categoria.builder()
            .id(rs.getLong("id_categoria"))
            .nombre(rs.getString("nombre"))
            .ambito(rs.getString("ambito"))
            .build();

    private final JdbcTemplate jdbc;

    public List<Categoria> listar() {
        return jdbc.query("SELECT id_categoria, nombre, ambito FROM CATEGORIA ORDER BY nombre", MAPPER);
    }

    /** Categorias que se pueden usar en una factura (gasto) o en un producto. */
    public List<Opcion> opcionesDeGasto() {
        return jdbc.query(
                "SELECT id_categoria, nombre FROM CATEGORIA WHERE ambito IN ('gasto', 'general') ORDER BY nombre",
                (rs, fila) -> new Opcion(rs.getLong("id_categoria"), rs.getString("nombre")));
    }

    public Optional<Categoria> buscarPorId(long id) {
        return jdbc.query("SELECT id_categoria, nombre, ambito FROM CATEGORIA WHERE id_categoria = ?", MAPPER, id)
                .stream().findFirst();
    }

    public void crear(Categoria c) {
        jdbc.update("INSERT INTO CATEGORIA (nombre, ambito) VALUES (?, ?)", c.getNombre(), c.getAmbito());
    }

    public int actualizar(Categoria c) {
        return jdbc.update("UPDATE CATEGORIA SET nombre = ?, ambito = ? WHERE id_categoria = ?",
                c.getNombre(), c.getAmbito(), c.getId());
    }

    /** Falla (excepcion) si la categoria esta usada por productos, facturas o presupuestos. */
    public int eliminar(long id) {
        return jdbc.update("DELETE FROM CATEGORIA WHERE id_categoria = ?", id);
    }

    public int contar() {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM CATEGORIA", Integer.class);
        return n == null ? 0 : n;
    }
}
