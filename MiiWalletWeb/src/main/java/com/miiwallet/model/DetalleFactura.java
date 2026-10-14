package com.miiwallet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Tabla DETALLE_FACTURA: cada producto comprado en una factura, con cantidad y precio unitario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFactura {

    private Long id;

    private Long idFactura;

    @NotNull(message = "Elige un producto")
    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0", inclusive = false, message = "La cantidad debe ser mayor que 0")
    private BigDecimal cantidad;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0", message = "El precio no puede ser negativo")
    private BigDecimal precioUnitario;

    // Solo lectura (JOIN con PRODUCTO)
    private String productoNombre;

    public BigDecimal getSubtotal() {
        if (cantidad == null || precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        return cantidad.multiply(precioUnitario);
    }
}
