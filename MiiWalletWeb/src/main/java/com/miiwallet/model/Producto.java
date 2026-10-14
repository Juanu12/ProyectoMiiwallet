package com.miiwallet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tabla PRODUCTO: lo que se compra en una factura, puede ser unitario ("Dona")
 * o grande ("Mercado del mes"). Siempre pertenece a una categoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "Maximo 80 caracteres")
    private String nombre;

    @NotNull(message = "Elige una categoria")
    private Long idCategoria;

    // Solo lectura: viene del JOIN con CATEGORIA para mostrarlo en la lista
    private String categoriaNombre;
}
