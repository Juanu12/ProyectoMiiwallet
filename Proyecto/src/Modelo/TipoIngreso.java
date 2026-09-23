/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla TIPO_INGRESO.
 *
 * @author CSU22
 */
public class TipoIngreso {

    public int id_tipo_ingreso;
    public String nombre;

    public TipoIngreso() {
    }

    public TipoIngreso(String nombre) {
        this.nombre = nombre;
    }

    public TipoIngreso(int id_tipo_ingreso, String nombre) {
        this.id_tipo_ingreso = id_tipo_ingreso;
        this.nombre = nombre;
    }
}
