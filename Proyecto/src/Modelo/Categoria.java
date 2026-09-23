/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla CATEGORIA.
 * ambito debe ser uno de: "ingreso", "gasto", "general".
 *
 * @author CSU22
 */
public class Categoria {

    public int id_categoria;
    public String nombre;
    public String ambito;

    public Categoria() {
    }

    public Categoria(String nombre, String ambito) {
        this.nombre = nombre;
        this.ambito = ambito;
    }

    public Categoria(int id_categoria, String nombre, String ambito) {
        this.id_categoria = id_categoria;
        this.nombre = nombre;
        this.ambito = ambito;
    }
}
