/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla METODO_PAGO.
 *
 * @author CSU22
 */
public class MetodoPago {

    public int id_metodo;
    public String nombre;

    public MetodoPago() {
    }

    public MetodoPago(String nombre) {
        this.nombre = nombre;
    }

    public MetodoPago(int id_metodo, String nombre) {
        this.id_metodo = id_metodo;
        this.nombre = nombre;
    }
}
