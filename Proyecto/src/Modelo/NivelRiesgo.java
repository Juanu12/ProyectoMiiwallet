/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla NIVEL_RIESGO.
 *
 * @author CSU22
 */
public class NivelRiesgo {

    public int id_nivel;
    public String nombre;
    public double porcentaje_min;
    public double porcentaje_max;

    public NivelRiesgo() {
    }

    public NivelRiesgo(String nombre, double porcentaje_min, double porcentaje_max) {
        this.nombre = nombre;
        this.porcentaje_min = porcentaje_min;
        this.porcentaje_max = porcentaje_max;
    }

    public NivelRiesgo(int id_nivel, String nombre, double porcentaje_min, double porcentaje_max) {
        this.id_nivel = id_nivel;
        this.nombre = nombre;
        this.porcentaje_min = porcentaje_min;
        this.porcentaje_max = porcentaje_max;
    }
}
