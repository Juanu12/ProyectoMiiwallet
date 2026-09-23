/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla PRODUCTO.
 *
 * @author CSU22
 */
public class Producto {

    //Atributos
    public int id_producto;
    public int id_categoria;
    public String nombre;

    //Constructores
    public Producto() {
    }

    public Producto(int id_categoria, String nombre) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
    }

    public Producto(int id_producto, int id_categoria, String nombre) {
        this.id_producto = id_producto;
        this.id_categoria = id_categoria;
        this.nombre = nombre;
    }

    //Metodos de la clase
    public void info(Producto p) {
        System.out.println("Producto:");
        System.out.println("id: " + p.id_producto);
        System.out.println("nombre: " + p.nombre);
        System.out.println("id_categoria: " + p.id_categoria);
    }
}
