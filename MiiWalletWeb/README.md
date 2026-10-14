# MiiWallet

Aplicación web de gestión financiera personal: ingresos, gastos (facturas con su detalle de productos),
categorías y un panel mensual con balance, nivel de riesgo, presupuestos y alertas.

Stack: **Spring Boot + Thymeleaf + Bootstrap 5 + SQLite (JdbcTemplate) + Lombok**.
La base de datos es `gestion_financiera.db` (el mismo esquema del proyecto de escritorio).

## Cómo ejecutarlo

Requisitos: **JDK 17 o 21** y Maven. Lombok **no funciona con Java 26** (error `EndPosTable`).

```bash
# desde esta carpeta (macOS)
JAVA_HOME=$(/usr/libexec/java_home -v 17) mvn spring-boot:run
```

Abre http://localhost:8080 : te lleva al login. Crea una cuenta en **Crear una** o entra con un usuario
de ejemplo (`ana.gomez@example.com`, `carlos.perez@example.com`, `laura.diaz@example.com`)
con la contraseña `MiiWallet123`.

En IntelliJ: abre la carpeta como proyecto Maven, usa el JDK 17/21, activa el plugin de Lombok y marca
*Enable annotation processing* (Settings > Build > Compiler > Annotation Processors).
Luego ejecuta `MiiWalletApplication`.

Jar:

```bash
JAVA_HOME=$(/usr/libexec/java_home -v 17) mvn clean package
java -jar target/miiwallet.jar
```

## Qué hay por ahora

| Ruta | Qué hace |
|------|----------|
| `/` | Panel del mes: ingresos, gastos, balance, % gasto/ingreso con su nivel de riesgo (tabla `NIVEL_RIESGO`), gasto por categoría contra `PRESUPUESTO`, alertas y últimas facturas. `/?periodo=2026-01` para otro mes. |
| `/ingresos` | CRUD de ingresos (tipo, monto, fecha) |
| `/facturas` | CRUD de facturas (comercio, categoría, método de pago, fecha, total) |
| `/facturas/{id}` | Detalle de la factura: agregar/quitar productos y usar la suma como total |
| `/productos` | CRUD de productos (nombre + categoría) |
| `/categorias` | CRUD de categorías (nombre + ámbito: gasto, ingreso, general) |

**Inicio de sesión** (`/login`, `/registro`, botón *Salir*): las contraseñas se guardan con hash **BCrypt**
(`spring-security-crypto`, sin el resto de Spring Security). El usuario logueado vive en la sesión HTTP
(`UsuarioActual`) y `LoginInterceptor` manda al login a quien intente abrir una página privada sin sesión.
Ingresos, facturas y el panel siempre filtran por el usuario de la sesión.

`miiwallet.demo-contrasena` en `application.properties` les pone `MiiWallet123` a los usuarios de ejemplo
que traían hashes falsos (solo la primera vez). Bórrala antes de entregar.

Borrar una categoría o un producto en uso no está permitido: las llaves foráneas de SQLite lo impiden
(se activan con `?foreign_keys=true` en la URL) y la app muestra un mensaje.

## Pendiente (siguientes pasos)

- Guardar `RESUMEN_MENSUAL` y `PERFIL_RIESGO` (hoy el panel los calcula al vuelo).
- CRUD de `PRESUPUESTO` y generación automática de `ALERTA`.

## Estructura

```
src/main/java/com/miiwallet/
  MiiWalletApplication.java
  config/      UsuarioActual (usuario en sesión), LoginInterceptor + WebConfig (protege las rutas, BCrypt),
               DatosComunes (correo en la barra), DatosIniciales (catálogos si la BD es nueva)
  model/       Usuario, LoginForm, RegistroForm, Categoria, Producto, Ingreso, Factura, DetalleFactura, Opcion, NivelRiesgo, Alerta, ResumenMes, GastoCategoria
  repository/  SQL puro con JdbcTemplate (una clase por tabla + PanelRepository para el resumen)
  controller/  Auth (login/registro/logout), Inicio, Ingreso, Factura, Producto, Categoria
src/main/resources/
  application.properties, schema.sql (CREATE TABLE IF NOT EXISTS de las 13 tablas)
  templates/   auth/login.html, auth/registro.html, inicio.html, fragments/layout.html, ingresos/, facturas/, productos/, categorias/
  static/css/estilos.css
```

## Notas

- La sesión se cierra sola tras 30 minutos sin actividad (`server.servlet.session.timeout`).

- Si borras `gestion_financiera.db`, al arrancar se crea vacía con los catálogos básicos y un usuario de prueba.
- `.gitignore` excluye los `*.db`: si quieres subir la BD con datos de ejemplo al repo, quita esa línea.
- Bootstrap se carga desde un CDN: el navegador necesita internet.
