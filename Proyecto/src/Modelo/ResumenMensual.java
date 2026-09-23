/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla RESUMEN_MENSUAL.
 *
 * @author CSU22
 */
public class ResumenMensual {

    public int id_resumen;
    public int id_usuario;
    public int anio;
    public int mes;
    public double total_ingresos;
    public double total_gastos;
    public double balance;

    public ResumenMensual() {
    }

    public ResumenMensual(int id_usuario, int anio, int mes, double total_ingresos, double total_gastos, double balance) {
        this.id_usuario = id_usuario;
        this.anio = anio;
        this.mes = mes;
        this.total_ingresos = total_ingresos;
        this.total_gastos = total_gastos;
        this.balance = balance;
    }

    public ResumenMensual(int id_resumen, int id_usuario, int anio, int mes, double total_ingresos, double total_gastos, double balance) {
        this.id_resumen = id_resumen;
        this.id_usuario = id_usuario;
        this.anio = anio;
        this.mes = mes;
        this.total_ingresos = total_ingresos;
        this.total_gastos = total_gastos;
        this.balance = balance;
    }

    public double calcularPorcentajeGastoIngreso() {
        if (total_ingresos == 0) {
            return 0;
        }
        return (total_gastos / total_ingresos) * 100;
    }
}
