package com.miiwallet.repository;

import com.miiwallet.model.DetalleFactura;
import com.miiwallet.model.Factura;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * CRUD de FACTURA (el gasto) y de sus lineas en DETALLE_FACTURA.
 * Igual que con los ingresos, todo se filtra por usuario.
 */
@Repository
@RequiredArgsConstructor
public class FacturaRepository {

    private static final String SELECT = """
            SELECT f.id_factura, f.id_categoria, f.id_metodo, f.comercio, f.fecha, f.total,
                   c.nombre AS categoria, m.nombre AS metodo
            FROM FACTURA f
            JOIN CATEGORIA c   ON c.id_categoria = f.id_categoria
            JOIN METODO_PAGO m ON m.id_metodo = f.id_metodo
            WHERE f.id_usuario = ?
            """;

    private static final RowMapper<Factura> MAPPER = (rs, fila) -> Factura.builder()
            .id(rs.getLong("id_factura"))
            .idCategoria(rs.getLong("id_categoria"))
            .idMetodo(rs.getLong("id_metodo"))
            .comercio(rs.getString("comercio"))
            .fecha(LocalDate.parse(rs.getString("fecha").substring(0, 10)))
            .total(rs.getBigDecimal("total"))
            .categoriaNombre(rs.getString("categoria"))
            .metodoNombre(rs.getString("metodo"))
            .build();

    private static final RowMapper<DetalleFactura> MAPPER_DETALLE = (rs, fila) -> DetalleFactura.builder()
            .id(rs.getLong("id_detalle"))
            .idFactura(rs.getLong("id_factura"))
            .idProducto(rs.getLong("id_producto"))
            .cantidad(rs.getBigDecimal("cantidad"))
            .precioUnitario(rs.getBigDecimal("precio_unitario"))
            .productoNombre(rs.getString("producto"))
            .build();

    private final JdbcTemplate jdbc;

    // ---------- FACTURA ----------

    public List<Factura> listar(long idUsuario) {
        return jdbc.query(SELECT + " ORDER BY f.fecha DESC, f.id_factura DESC", MAPPER, idUsuario);
    }

    /** Historial: las ultimas N facturas registradas. */
    public List<Factura> ultimas(long idUsuario, int n) {
        return jdbc.query(SELECT + " ORDER BY f.fecha DESC, f.id_factura DESC LIMIT ?", MAPPER, idUsuario, n);
    }

    public Optional<Factura> buscarPorId(long id, long idUsuario) {
        return jdbc.query(SELECT + " AND f.id_factura = ?", MAPPER, idUsuario, id).stream().findFirst();
    }

    /** Crea la factura y devuelve su id (para ir directo a agregarle productos). */
    public long crear(Factura f, long idUsuario) {
        jdbc.update("""
                        INSERT INTO FACTURA (id_usuario, id_categoria, id_metodo, comercio, fecha, total)
                        VALUES (?, ?, ?, ?, ?, ?)""",
                idUsuario, f.getIdCategoria(), f.getIdMetodo(), f.getComercio(), f.getFecha().toString(), f.getTotal());
        // Con una sola conexion (pool de 1) last_insert_rowid() es el id que se acaba de crear
        Long id = jdbc.queryForObject("SELECT last_insert_rowid()", Long.class);
        return id == null ? 0 : id;
    }

    public int actualizar(Factura f, long idUsuario) {
        return jdbc.update("""
                        UPDATE FACTURA SET id_categoria = ?, id_metodo = ?, comercio = ?, fecha = ?, total = ?
                        WHERE id_factura = ? AND id_usuario = ?""",
                f.getIdCategoria(), f.getIdMetodo(), f.getComercio(), f.getFecha().toString(), f.getTotal(),
                f.getId(), idUsuario);
    }

    /** ON DELETE CASCADE borra tambien su detalle. */
    public int eliminar(long id, long idUsuario) {
        return jdbc.update("DELETE FROM FACTURA WHERE id_factura = ? AND id_usuario = ?", id, idUsuario);
    }

    public int actualizarTotal(long id, long idUsuario, BigDecimal total) {
        return jdbc.update("UPDATE FACTURA SET total = ? WHERE id_factura = ? AND id_usuario = ?",
                total, id, idUsuario);
    }

    // ---------- DETALLE_FACTURA ----------

    public List<DetalleFactura> detalles(long idFactura) {
        return jdbc.query("""
                SELECT d.id_detalle, d.id_factura, d.id_producto, d.cantidad, d.precio_unitario, p.nombre AS producto
                FROM DETALLE_FACTURA d JOIN PRODUCTO p ON p.id_producto = d.id_producto
                WHERE d.id_factura = ?
                ORDER BY d.id_detalle""", MAPPER_DETALLE, idFactura);
    }

    public BigDecimal sumaDetalles(long idFactura) {
        BigDecimal suma = jdbc.queryForObject(
                "SELECT COALESCE(SUM(cantidad * precio_unitario), 0) FROM DETALLE_FACTURA WHERE id_factura = ?",
                BigDecimal.class, idFactura);
        return suma == null ? BigDecimal.ZERO : suma;
    }

    public void agregarDetalle(DetalleFactura d) {
        jdbc.update("""
                        INSERT INTO DETALLE_FACTURA (id_factura, id_producto, cantidad, precio_unitario)
                        VALUES (?, ?, ?, ?)""",
                d.getIdFactura(), d.getIdProducto(), d.getCantidad(), d.getPrecioUnitario());
    }

    public int eliminarDetalle(long idDetalle, long idFactura) {
        return jdbc.update("DELETE FROM DETALLE_FACTURA WHERE id_detalle = ? AND id_factura = ?", idDetalle, idFactura);
    }
}
