/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;

/**
 * Objeto que representa un registro de la tabla PRESUPUESTO.
 * estado debe ser uno de: "activo", "excedido", "cerrado".
 * id_resumen y porcentaje_ingreso pueden quedar en null si no aplican.
 *
 * @author CSU22
 */
public class Presupuesto {

    public int id_presupuesto;
    public int id_usuario;
    public int id_categoria;
    public Integer id_resumen;
    public int anio;
    public int mes;
    public double monto_limite;
    public Double porcentaje_ingreso;
    public double monto_gastado;
    public String estado;
    public LocalDate fecha_creacion;

    public Presupuesto() {
    }

    public Presupuesto(int id_usuario, int id_categoria, Integer id_resumen, int anio, int mes,
            double monto_limite, Double porcentaje_ingreso, double monto_gastado, String estado, LocalDate fecha_creacion) {
        this.id_usuario = id_usuario;
        this.id_categoria = id_categoria;
        this.id_resumen = id_resumen;
        this.anio = anio;
        this.mes = mes;
        this.monto_limite = monto_limite;
        this.porcentaje_ingreso = porcentaje_ingreso;
        this.monto_gastado = monto_gastado;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
    }

    public Presupuesto(int id_presupuesto, int id_usuario, int id_categoria, Integer id_resumen, int anio, int mes,
            double monto_limite, Double porcentaje_ingreso, double monto_gastado, String estado, LocalDate fecha_creacion) {
        this.id_presupuesto = id_presupuesto;
        this.id_usuario = id_usuario;
        this.id_categoria = id_categoria;
        this.id_resumen = id_resumen;
        this.anio = anio;
        this.mes = mes;
        this.monto_limite = monto_limite;
        this.porcentaje_ingreso = porcentaje_ingreso;
        this.monto_gastado = monto_gastado;
        this.estado = estado;
        this.fecha_creacion = fecha_creacion;
    }

    public boolean seExcederiaCon(double montoAdicional) {
        return (monto_gastado + montoAdicional) > monto_limite;
    }
}
