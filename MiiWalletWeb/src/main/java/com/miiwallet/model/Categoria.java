package com.miiwallet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tabla CATEGORIA. El ambito dice donde se usa: en ingresos, en gastos o en ambos (general).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "Maximo 60 caracteres")
    private String nombre;

    @NotBlank(message = "Elige un ambito")
    @Pattern(regexp = "ingreso|gasto|general", message = "Ambito no valido")
    private String ambito;
}
