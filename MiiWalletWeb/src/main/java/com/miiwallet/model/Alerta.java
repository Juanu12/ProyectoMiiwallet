package com.miiwallet.model;

import lombok.Value;

import java.time.LocalDate;

/**
 * Tabla ALERTA: aviso para el usuario (presupuesto casi agotado, riesgo alto...).
 * Severidad: bajo, medio, alto o critico.
 */
@Value
public class Alerta {
    Long id;
    String nivelSeveridad;
    String mensaje;
    LocalDate fecha;

    /** Clase de color de Bootstrap segun la severidad. */
    public String getColor() {
        return switch (nivelSeveridad) {
            case "critico" -> "danger";
            case "alto" -> "warning";
            case "medio" -> "info";
            default -> "secondary";
        };
    }
}
