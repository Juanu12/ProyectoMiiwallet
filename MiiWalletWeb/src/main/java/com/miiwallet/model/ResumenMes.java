package com.miiwallet.model;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Resumen de un mes calculado al vuelo desde INGRESO y FACTURA (para el panel de inicio).
 * Mas adelante se guardara en RESUMEN_MENSUAL / PERFIL_RIESGO.
 */
@Value
public class ResumenMes {
    int anio;
    int mes;
    BigDecimal totalIngresos;
    BigDecimal totalGastos;

    public BigDecimal getBalance() {
        return totalIngresos.subtract(totalGastos);
    }

    /** Porcentaje de los ingresos que se fue en gastos. null si no hubo ingresos. */
    public BigDecimal getPorcentajeGasto() {
        if (totalIngresos.signum() == 0) {
            return null;
        }
        return totalGastos.multiply(BigDecimal.valueOf(100))
                .divide(totalIngresos, 2, RoundingMode.HALF_UP);
    }
}
