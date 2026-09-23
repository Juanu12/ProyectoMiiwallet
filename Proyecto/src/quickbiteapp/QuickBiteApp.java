/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package quickbiteapp;

import Controlador.Logica;
import Modelo.Producto;
import Modelo.Usuario;
import java.util.ArrayList;

/**
 * Clase principal de MiiWallet. Prueba el flujo:
 * main -> Logica (Controlador) -> ConexionSqlite (Modelo) -> base de datos.
 *
 * @author CSU22
 */
public class QuickBiteApp {

    public static void main(String[] args) {
        Logica logica = new Logica();

        // Usuario: crear, listar, actualizar, eliminar
        Usuario nuevo = logica.registrarUsuario("prueba@miiwallet.com", "hash123");
        System.out.println("Usuario creado con id: " + nuevo.id_usuario);

        ArrayList<Usuario> usuarios = logica.listarUsuarios();
        System.out.println("Usuarios registrados: " + usuarios.size());

        nuevo.correo = "prueba.editado@miiwallet.com";
        System.out.println("¿Se actualizó?: " + logica.editarUsuario(nuevo));

        // Producto: crear, listar, actualizar, eliminar
        // (usa un id_categoria que ya exista en tu tabla CATEGORIA)
        Producto p = logica.registrarProducto(1, "Arriendo");
        System.out.println("Producto creado con id: " + p.id_producto);

        ArrayList<Producto> productos = logica.listarProductos();
        System.out.println("Productos registrados: " + productos.size());

        p.nombre = "Arriendo apartamento";
        System.out.println("¿Se actualizó producto?: " + logica.editarProducto(p));

        // Limpieza de prueba
        System.out.println("¿Se eliminó producto?: " + logica.borrarProducto(p.id_producto));
        System.out.println("¿Se eliminó usuario?: " + logica.borrarUsuario(nuevo.id_usuario));
    }
}
