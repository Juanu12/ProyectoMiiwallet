package com.miiwallet.repository;

import com.miiwallet.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CRUD de la tabla PRODUCTO. El JOIN trae el nombre de la categoria para mostrarlo.
 */
@Repository
@RequiredArgsConstructor
public class ProductoRepository {

    private static final String SELECT = """
            SELECT p.id_producto, p.nombre, p.id_categoria, c.nombre AS categoria
            FROM PRODUCTO p JOIN CATEGORIA c ON c.id_categoria = p.id_categoria
            """;

    private static final RowMapper<Producto> MAPPER = (rs, fila) -> Producto.builder()
            .id(rs.getLong("id_producto"))
            .nombre(rs.getString("nombre"))
            .idCategoria(rs.getLong("id_categoria"))
            .categoriaNombre(rs.getString("categoria"))
            .build();

    private final JdbcTemplate jdbc;

    public List<Producto> listar() {
        return jdbc.query(SELECT + " ORDER BY c.nombre, p.nombre", MAPPER);
    }

    public Optional<Producto> buscarPorId(long id) {
        return jdbc.query(SELECT + " WHERE p.id_producto = ?", MAPPER, id).stream().findFirst();
    }

    public void crear(Producto p) {
        jdbc.update("INSERT INTO PRODUCTO (id_categoria, nombre) VALUES (?, ?)", p.getIdCategoria(), p.getNombre());
    }

    public int actualizar(Producto p) {
        return jdbc.update("UPDATE PRODUCTO SET id_categoria = ?, nombre = ? WHERE id_producto = ?",
                p.getIdCategoria(), p.getNombre(), p.getId());
    }

    /** Falla (excepcion) si el producto aparece en el detalle de alguna factura. */
    public int eliminar(long id) {
        return jdbc.update("DELETE FROM PRODUCTO WHERE id_producto = ?", id);
    }
}
