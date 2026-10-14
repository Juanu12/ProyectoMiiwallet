package com.miiwallet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tabla INGRESO: dinero que entra (salario, freelance...), con su tipo y fecha.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingreso {

    private Long id;

    @NotNull(message = "Elige el tipo de ingreso")
    private Long idTipoIngreso;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0", message = "El monto no puede ser negativo")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    // Solo lectura: nombre del tipo (JOIN con TIPO_INGRESO)
    private String tipoNombre;
}
