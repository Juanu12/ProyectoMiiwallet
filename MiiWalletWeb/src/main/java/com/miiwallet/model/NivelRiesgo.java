package com.miiwallet.model;

import lombok.Value;

import java.math.BigDecimal;

/**
 * Tabla NIVEL_RIESGO: catalogo con los rangos del porcentaje gasto/ingreso
 * (por ejemplo Bajo 0-30, Medio 30.01-60, Alto 60.01-100).
 */
@Value
public class NivelRiesgo {
    Long id;
    String nombre;
    BigDecimal porcentajeMin;
    BigDecimal porcentajeMax;
}
