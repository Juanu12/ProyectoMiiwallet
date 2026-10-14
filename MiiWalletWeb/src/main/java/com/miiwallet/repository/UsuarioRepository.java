package com.miiwallet.repository;

import com.miiwallet.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tabla USUARIO: buscar por correo (login) y crear cuentas (registro).
 * Los correos se guardan en minusculas para que "Ana@x.com" y "ana@x.com" sean el mismo.
 */
@Repository
@RequiredArgsConstructor
public class UsuarioRepository {

    private final JdbcTemplate jdbc;

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return jdbc.query(
                "SELECT id_usuario, correo, contrasena_hash FROM USUARIO WHERE lower(correo) = lower(?)",
                (rs, fila) -> new Usuario(rs.getLong("id_usuario"), rs.getString("correo"),
                        rs.getString("contrasena_hash")),
                correo.trim()).stream().findFirst();
    }

    public boolean existeCorreo(String correo) {
        return buscarPorCorreo(correo).isPresent();
    }

    /** Ids de usuarios cuyo hash no es BCrypt (datos de ejemplo viejos). */
    public List<Long> idsSinHashBcrypt() {
        return jdbc.queryForList("SELECT id_usuario FROM USUARIO WHERE contrasena_hash NOT LIKE '$2%'", Long.class);
    }

    public void cambiarHash(long idUsuario, String contrasenaHash) {
        jdbc.update("UPDATE USUARIO SET contrasena_hash = ? WHERE id_usuario = ?", contrasenaHash, idUsuario);
    }

    /** Crea el usuario y devuelve su id. */
    public long crear(String correo, String contrasenaHash) {
        jdbc.update("INSERT INTO USUARIO (correo, contrasena_hash) VALUES (?, ?)",
                correo.trim().toLowerCase(), contrasenaHash);
        Long id = jdbc.queryForObject("SELECT last_insert_rowid()", Long.class);
        return id == null ? 0 : id;
    }
}
