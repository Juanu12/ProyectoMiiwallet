package com.miiwallet.model;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cuanto se gasto en una categoria durante un mes y, si existe, el limite
 * del PRESUPUESTO de esa categoria para ese mes.
 */
@Value
public class GastoCategoria {
    String categoria;
    BigDecimal total;
    BigDecimal limite;

    /** Porcentaje del presupuesto usado. null si la categoria no tiene presupuesto. */
    public BigDecimal getPorcentajeUsado() {
        if (limite == null || limite.signum() == 0) {
            return null;
        }
        return total.multiply(BigDecimal.valueOf(100)).divide(limite, 0, RoundingMode.HALF_UP);
    }
}
