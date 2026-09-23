/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla DETALLE_FACTURA.
 *
 * @author CSU22
 */
public class DetalleFactura {

    public int id_detalle;
    public int id_factura;
    public int id_producto;
    public double cantidad;
    public double precio_unitario;

    public DetalleFactura() {
    }

    public DetalleFactura(int id_factura, int id_producto, double cantidad, double precio_unitario) {
        this.id_factura = id_factura;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
    }

    public DetalleFactura(int id_detalle, int id_factura, int id_producto, double cantidad, double precio_unitario) {
        this.id_detalle = id_detalle;
        this.id_factura = id_factura;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
    }

    public double calcularSubtotal() {
        return cantidad * precio_unitario;
    }
}
