package com.miiwallet.model;

import lombok.Value;

/**
 * Un registro de catalogo sencillo (id + nombre), para llenar los select de los formularios.
 * Se usa para TIPO_INGRESO, METODO_PAGO y CATEGORIA.
 *
 * @Value (Lombok) = clase inmutable: campos privados finales, getters y constructor con todos los campos.
 */
@Value
public class Opcion {
    Long id;
    String nombre;
}
