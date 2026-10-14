package com.miiwallet.config;

import com.miiwallet.model.Categoria;
import com.miiwallet.repository.CatalogoRepository;
import com.miiwallet.repository.CategoriaRepository;
import com.miiwallet.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Se ejecuta al arrancar:
 *  - Si la base de datos es nueva (vacia), carga los catalogos basicos y las categorias.
 *    Los usuarios se crean desde /registro.
 *  - Si miiwallet.demo-contrasena tiene valor, les pone esa contrasena (con BCrypt) a los
 *    usuarios de ejemplo que tienen un hash falso. Solo para pruebas.
 */
@Slf4j
@Configuration
public class DatosIniciales {

    @Bean
    ApplicationRunner cargarDatosIniciales(CatalogoRepository catalogos, CategoriaRepository categorias,
                                           UsuarioRepository usuarios, PasswordEncoder encoder,
                                           @Value("${miiwallet.demo-contrasena:}") String demoContrasena) {
        return args -> {
            if (catalogos.contarTiposIngreso() == 0) {
                for (String t : new String[]{"Salario", "Freelance", "Inversión", "Otro"}) {
                    catalogos.crearTipoIngreso(t);
                }
                for (String m : new String[]{"Efectivo", "Tarjeta de crédito", "Tarjeta débito", "Transferencia"}) {
                    catalogos.crearMetodoPago(m);
                }
                catalogos.crearNivelRiesgo("Bajo", 0, 30);
                catalogos.crearNivelRiesgo("Medio", 30.01, 60);
                catalogos.crearNivelRiesgo("Alto", 60.01, 100);
                log.info("Catalogos iniciales cargados");
            }
            if (categorias.contar() == 0) {
                for (String c : new String[]{"Alimentación", "Transporte", "Vivienda", "Servicios", "Entretenimiento"}) {
                    categorias.crear(Categoria.builder().nombre(c).ambito("gasto").build());
                }
                categorias.crear(Categoria.builder().nombre("Educación").ambito("general").build());
                log.info("Categorias iniciales cargadas");
            }
            if (!demoContrasena.isBlank()) {
                List<Long> ids = usuarios.idsSinHashBcrypt();
                for (Long id : ids) {
                    usuarios.cambiarHash(id, encoder.encode(demoContrasena));
                }
                if (!ids.isEmpty()) {
                    log.warn("Se asigno la contrasena de prueba a {} usuario(s) de ejemplo: {}", ids.size(), ids);
                }
            }
        };
    }
}
