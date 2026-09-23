# MiiWallet — Puesta en marcha inicial (NetBeans)

Guía mínima para dejar la base de datos SQLite funcionando y probar la
conexión desde NetBeans. Asegura que el
proyecto arranque y hable con `gestion_financiera.db`.

## 1. Requisitos

- NetBeans con el proyecto `Miiwallet` abierto.
- JDK 25 (ya configurado en `project.properties`).
- El archivo `gestion_financiera.db`.

## 2. Agregar el driver JDBC de SQLite

El proyecto no trae ninguna librería agregada, así que sin este paso
`ConexionSqlite` no compila.

1. Descarga el driver: busca **"sqlite-jdbc"** en Maven Central
   (`org.xerial:sqlite-jdbc`) y baja el `.jar` (por ejemplo
   `sqlite-jdbc-3.46.0.0.jar`).
2. En NetBeans: clic derecho sobre el proyecto **Miiwallet** →
   **Properties** → **Libraries**.
3. En la pestaña **Compile**, botón **Add JAR/Folder** → selecciona el
   `.jar` que descargaste → **OK**.

## 3. Copiar los archivos fuente

1. Extrae el `.zip` de fuentes.
2. Revisar las carpetas y el src del proyecto.


## 4. Ubicar la base de datos

`ConexionSqlite` usa una ruta relativa:

```java
private static final String URL = "jdbc:sqlite:gestion_financiera.db";
```

Eso significa que `gestion_financiera.db` debe quedar en la
**carpeta raíz del proyecto** (la misma donde están `build.xml` y
`manifest.mf`, al mismo nivel que la carpeta `src`). Si NetBeans corre
el proyecto desde otra carpeta, ajusta la ruta o usa una ruta absoluta
temporalmente para probar.

## 5. Ejecutar y verificar

1. Ejecutar individualmente, en el controlador , la conexion con la db, y verificar que funcione

