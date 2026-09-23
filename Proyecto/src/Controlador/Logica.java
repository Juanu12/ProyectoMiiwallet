/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.ConexionSqlite;
import Modelo.Producto;
import Modelo.Usuario;
import java.util.ArrayList;

/**
 * Controlador de la lógica de negocio. Aquí es donde se crean los objetos
 * (Usuario, Producto, ...) y se le piden a ConexionSqlite las operaciones
 * sobre la base de datos. Esta es la clase que llamaría la interfaz gráfica.
 *
 * @author CSU22
 */
public class Logica {

    //Atributo de la clase
    ConexionSqlite bd;

    //Metodos de la clase

    public void inicializarBaseDeDatos() {
        bd = new ConexionSqlite();
        bd.crearTablas();
    }

    // ---------- Usuario ----------

    public Usuario registrarUsuario(String correo, String contrasenaHash) {
        bd = new ConexionSqlite();
        Usuario u = new Usuario(correo, contrasenaHash);
        return bd.insertarUsuario(u);
    }

    public Usuario buscarUsuario(int idUsuario) {
        bd = new ConexionSqlite();
        return bd.consultarUsuarioPorId(idUsuario);
    }

    public ArrayList<Usuario> listarUsuarios() {
        bd = new ConexionSqlite();
        return bd.listarUsuarios();
    }

    public boolean editarUsuario(Usuario u) {
        bd = new ConexionSqlite();
        return bd.actualizarUsuario(u);
    }

    public boolean borrarUsuario(int idUsuario) {
        bd = new ConexionSqlite();
        return bd.eliminarUsuario(idUsuario);
    }

    // ---------- Producto ----------

    public Producto registrarProducto(int idCategoria, String nombre) {
        bd = new ConexionSqlite();
        Producto p = new Producto(idCategoria, nombre);
        return bd.insertarProducto(p);
    }

    public Producto buscarProducto(int idProducto) {
        bd = new ConexionSqlite();
        return bd.consultarProductoPorId(idProducto);
    }

    public ArrayList<Producto> listarProductos() {
        bd = new ConexionSqlite();
        return bd.listarProductos();
    }

    public boolean editarProducto(Producto p) {
        bd = new ConexionSqlite();
        return bd.actualizarProducto(p);
    }

    public boolean borrarProducto(int idProducto) {
        bd = new ConexionSqlite();
        return bd.eliminarProducto(idProducto);
    }

    public static void main(String[] args) {

    }
}
