/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;

/**
 * Objeto que representa un registro de la tabla FACTURA (hace de "gasto").
 *
 * @author CSU22
 */
public class Factura {

    public int id_factura;
    public int id_usuario;
    public int id_categoria;
    public int id_metodo;
    public String comercio;
    public LocalDate fecha;
    public double total;

    public Factura() {
    }

    public Factura(int id_usuario, int id_categoria, int id_metodo, String comercio, LocalDate fecha, double total) {
        this.id_usuario = id_usuario;
        this.id_categoria = id_categoria;
        this.id_metodo = id_metodo;
        this.comercio = comercio;
        this.fecha = fecha;
        this.total = total;
    }

    public Factura(int id_factura, int id_usuario, int id_categoria, int id_metodo, String comercio, LocalDate fecha, double total) {
        this.id_factura = id_factura;
        this.id_usuario = id_usuario;
        this.id_categoria = id_categoria;
        this.id_metodo = id_metodo;
        this.comercio = comercio;
        this.fecha = fecha;
        this.total = total;
    }
}
