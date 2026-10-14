package com.miiwallet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tabla FACTURA: en MiiWallet la factura ES el gasto.
 * Tiene comercio, fecha, total, categoria y metodo de pago; sus productos van en DETALLE_FACTURA.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    private Long id;

    @NotNull(message = "Elige una categoria")
    private Long idCategoria;

    @NotNull(message = "Elige un metodo de pago")
    private Long idMetodo;

    @NotBlank(message = "El comercio es obligatorio")
    @Size(max = 80, message = "Maximo 80 caracteres")
    private String comercio;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0", message = "El total no puede ser negativo")
    private BigDecimal total;

    // Solo lectura (JOIN)
    private String categoriaNombre;
    private String metodoNombre;
}
