/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Clase encargada de todo lo que tiene que ver con la base de datos:
 * abrir la conexión, crear las tablas si no existen, e insertar/consultar/
 * actualizar/eliminar cada entidad. La clase Logica (en Controlador) es
 * quien llama a estos métodos; esta clase es la única que sabe SQL.
 *
 * IMPORTANTE: el proyecto necesita el driver JDBC de SQLite (sqlite-jdbc)
 * agregado como librería en NetBeans: clic derecho en el proyecto ->
 * Properties -> Libraries -> Add Library / Add JAR.
 *
 * @author CSU22
 */
public class ConexionSqlite {

    // Ruta del archivo de base de datos. Ajusta si el .db no está
    // en la carpeta raíz del proyecto.
    private static final String URL = "jdbc:sqlite:gestion_financiera.db";

    /**
     * Abre una conexión nueva hacia gestion_financiera.db.
     * Cada método CRUD la abre y la cierra por su cuenta (try-with-resources),
     * así que normalmente no necesitas llamar este método directamente.
     */
    public Connection conectar() {
        try {
            Connection con = DriverManager.getConnection(URL);
            con.createStatement().execute("PRAGMA foreign_keys = ON;");
            return con;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea todas las tablas del esquema si no existen todavía
     * (útil si algún día se arranca con un .db vacío).
     */
    public void crearTablas() {
        String[] sentencias = {
            "CREATE TABLE IF NOT EXISTS USUARIO ("
            + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "correo TEXT NOT NULL UNIQUE,"
            + "contrasena_hash TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS CATEGORIA ("
            + "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "nombre TEXT NOT NULL,"
            + "ambito TEXT NOT NULL CHECK (ambito IN ('ingreso','gasto','general')))",

            "CREATE TABLE IF NOT EXISTS PRODUCTO ("
            + "id_producto INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "id_categoria INTEGER NOT NULL,"
            + "nombre TEXT NOT NULL,"
            + "FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria))"
        };

        try (Connection con = conectar()) {
            try (Statement stmt = con.createStatement()) {
                for (String sql : sentencias) {
                    stmt.execute(sql);
                }
            }
            System.out.println("Tablas verificadas/creadas correctamente");
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }

    // ==================== USUARIO ====================

    public Modelo.Usuario insertarUsuario(Modelo.Usuario u) {
        String sql = "INSERT INTO USUARIO (correo, contrasena_hash) VALUES (?, ?)";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, u.correo);
            stmt.setString(2, u.contrasena_hash);
            stmt.executeUpdate();

            try (ResultSet generadas = stmt.getGeneratedKeys()) {
                if (generadas.next()) {
                    u.id_usuario = generadas.getInt(1);
                }
            }
            return u;

        } catch (SQLException e) {
            System.out.println("Error al insertar el usuario: " + e.getMessage());
            return null;
        }
    }

    public Modelo.Usuario consultarUsuarioPorId(int id_usuario) {
        String sql = "SELECT id_usuario, correo, contrasena_hash FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id_usuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Modelo.Usuario(rs.getInt("id_usuario"), rs.getString("correo"), rs.getString("contrasena_hash"));
                }
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Error al consultar el usuario: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Modelo.Usuario> listarUsuarios() {
        String sql = "SELECT id_usuario, correo, contrasena_hash FROM USUARIO ORDER BY id_usuario";
        ArrayList<Modelo.Usuario> lista = new ArrayList<>();

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Modelo.Usuario(rs.getInt("id_usuario"), rs.getString("correo"), rs.getString("contrasena_hash")));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarUsuario(Modelo.Usuario u) {
        String sql = "UPDATE USUARIO SET correo = ?, contrasena_hash = ? WHERE id_usuario = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, u.correo);
            stmt.setString(2, u.contrasena_hash);
            stmt.setInt(3, u.id_usuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int id_usuario) {
        String sql = "DELETE FROM USUARIO WHERE id_usuario = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id_usuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el usuario: " + e.getMessage());
            return false;
        }
    }

    // ==================== PRODUCTO ====================

    public Modelo.Producto insertarProducto(Modelo.Producto p) {
        String sql = "INSERT INTO PRODUCTO (id_categoria, nombre) VALUES (?, ?)";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, p.id_categoria);
            stmt.setString(2, p.nombre);
            stmt.executeUpdate();

            try (ResultSet generadas = stmt.getGeneratedKeys()) {
                if (generadas.next()) {
                    p.id_producto = generadas.getInt(1);
                }
            }
            return p;

        } catch (SQLException e) {
            System.out.println("Error al insertar el producto: " + e.getMessage());
            return null;
        }
    }

    public Modelo.Producto consultarProductoPorId(int id_producto) {
        String sql = "SELECT id_producto, id_categoria, nombre FROM PRODUCTO WHERE id_producto = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id_producto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Modelo.Producto(rs.getInt("id_producto"), rs.getInt("id_categoria"), rs.getString("nombre"));
                }
            }
            return null;

        } catch (SQLException e) {
            System.out.println("Error al consultar el producto: " + e.getMessage());
            return null;
        }
    }

    public ArrayList<Modelo.Producto> listarProductos() {
        String sql = "SELECT id_producto, id_categoria, nombre FROM PRODUCTO ORDER BY id_producto";
        ArrayList<Modelo.Producto> lista = new ArrayList<>();

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Modelo.Producto(rs.getInt("id_producto"), rs.getInt("id_categoria"), rs.getString("nombre")));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los productos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarProducto(Modelo.Producto p) {
        String sql = "UPDATE PRODUCTO SET id_categoria = ?, nombre = ? WHERE id_producto = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, p.id_categoria);
            stmt.setString(2, p.nombre);
            stmt.setInt(3, p.id_producto);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int id_producto) {
        String sql = "DELETE FROM PRODUCTO WHERE id_producto = ?";

        try (Connection con = conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id_producto);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
            return false;
        }
    }
}
