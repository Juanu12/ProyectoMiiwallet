/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;

/**
 * Objeto que representa un registro de la tabla INGRESO.
 *
 * @author CSU22
 */
public class Ingreso {

    public int id_ingreso;
    public int id_usuario;
    public int id_tipo_ingreso;
    public double monto;
    public LocalDate fecha;

    public Ingreso() {
    }

    public Ingreso(int id_usuario, int id_tipo_ingreso, double monto, LocalDate fecha) {
        this.id_usuario = id_usuario;
        this.id_tipo_ingreso = id_tipo_ingreso;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Ingreso(int id_ingreso, int id_usuario, int id_tipo_ingreso, double monto, LocalDate fecha) {
        this.id_ingreso = id_ingreso;
        this.id_usuario = id_usuario;
        this.id_tipo_ingreso = id_tipo_ingreso;
        this.monto = monto;
        this.fecha = fecha;
    }
}
