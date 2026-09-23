/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 * Objeto que representa un registro de la tabla USUARIO.
 *
 * @author CSU22
 */
public class Usuario {

    //Atributos
    public int id_usuario;
    public String correo;
    public String contrasena_hash;

    //Constructores
    public Usuario() {
    }

    public Usuario(String correo, String contrasena_hash) {
        this.correo = correo;
        this.contrasena_hash = contrasena_hash;
    }

    public Usuario(int id_usuario, String correo, String contrasena_hash) {
        this.id_usuario = id_usuario;
        this.correo = correo;
        this.contrasena_hash = contrasena_hash;
    }

    //Metodos de la clase
    public void info(Usuario u) {
        System.out.println("Usuario:");
        System.out.println("id: " + u.id_usuario);
        System.out.println("correo: " + u.correo);
    }
}
