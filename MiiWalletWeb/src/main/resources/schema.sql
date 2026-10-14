-- Esquema de MiiWallet (el mismo de gestion_financiera.db).
-- IF NOT EXISTS lo hace seguro de ejecutar en cada arranque: si la tabla ya existe, no la toca.

CREATE TABLE IF NOT EXISTS USUARIO (
    id_usuario      INTEGER PRIMARY KEY AUTOINCREMENT,
    correo          TEXT NOT NULL UNIQUE,
    contrasena_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS TIPO_INGRESO (
    id_tipo_ingreso INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS CATEGORIA (
    id_categoria    INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT NOT NULL,
    ambito          TEXT NOT NULL CHECK (ambito IN ('ingreso', 'gasto', 'general'))
);

CREATE TABLE IF NOT EXISTS METODO_PAGO (
    id_metodo       INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS NIVEL_RIESGO (
    id_nivel        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT NOT NULL UNIQUE,
    porcentaje_min  DECIMAL(5,2) NOT NULL,
    porcentaje_max  DECIMAL(5,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS INGRESO (
    id_ingreso      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario      INTEGER NOT NULL,
    id_tipo_ingreso INTEGER NOT NULL,
    monto           DECIMAL(12,2) NOT NULL CHECK (monto >= 0),
    fecha           DATE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo_ingreso) REFERENCES TIPO_INGRESO(id_tipo_ingreso)
);

CREATE TABLE IF NOT EXISTS FACTURA (
    id_factura      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario      INTEGER NOT NULL,
    id_categoria    INTEGER NOT NULL,
    id_metodo       INTEGER NOT NULL,
    comercio        TEXT NOT NULL,
    fecha           DATE NOT NULL,
    total           DECIMAL(12,2) NOT NULL CHECK (total >= 0),
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria),
    FOREIGN KEY (id_metodo) REFERENCES METODO_PAGO(id_metodo)
);

CREATE TABLE IF NOT EXISTS PRODUCTO (
    id_producto     INTEGER PRIMARY KEY AUTOINCREMENT,
    id_categoria    INTEGER NOT NULL,
    nombre          TEXT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria)
);

CREATE TABLE IF NOT EXISTS DETALLE_FACTURA (
    id_detalle      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_factura      INTEGER NOT NULL,
    id_producto     INTEGER NOT NULL,
    cantidad        DECIMAL(10,2) NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(12,2) NOT NULL CHECK (precio_unitario >= 0),
    FOREIGN KEY (id_factura) REFERENCES FACTURA(id_factura) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES PRODUCTO(id_producto)
);

CREATE TABLE IF NOT EXISTS RESUMEN_MENSUAL (
    id_resumen      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario      INTEGER NOT NULL,
    anio            INTEGER NOT NULL,
    mes             INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    total_ingresos  DECIMAL(12,2) NOT NULL DEFAULT 0,
    total_gastos    DECIMAL(12,2) NOT NULL DEFAULT 0,
    balance         DECIMAL(12,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    UNIQUE (id_usuario, anio, mes)
);

CREATE TABLE IF NOT EXISTS PERFIL_RIESGO (
    id_perfil                  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_resumen                 INTEGER NOT NULL,
    id_nivel                   INTEGER NOT NULL,
    porcentaje_gasto_ingreso   DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_resumen) REFERENCES RESUMEN_MENSUAL(id_resumen) ON DELETE CASCADE,
    FOREIGN KEY (id_nivel) REFERENCES NIVEL_RIESGO(id_nivel)
);

CREATE TABLE IF NOT EXISTS PRESUPUESTO (
    id_presupuesto      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario          INTEGER NOT NULL,
    id_categoria        INTEGER NOT NULL,
    id_resumen          INTEGER,
    anio                INTEGER NOT NULL,
    mes                 INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    monto_limite        DECIMAL(12,2) NOT NULL CHECK (monto_limite >= 0),
    porcentaje_ingreso  DECIMAL(5,2),
    monto_gastado       DECIMAL(12,2) NOT NULL DEFAULT 0,
    estado              TEXT NOT NULL DEFAULT 'activo' CHECK (estado IN ('activo', 'excedido', 'cerrado')),
    fecha_creacion      DATE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria),
    FOREIGN KEY (id_resumen) REFERENCES RESUMEN_MENSUAL(id_resumen)
);

CREATE TABLE IF NOT EXISTS ALERTA (
    id_alerta       INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario      INTEGER NOT NULL,
    id_perfil       INTEGER,
    id_presupuesto  INTEGER,
    nivel_severidad TEXT NOT NULL CHECK (nivel_severidad IN ('bajo', 'medio', 'alto', 'critico')),
    mensaje         TEXT NOT NULL,
    fecha           DATE NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_perfil) REFERENCES PERFIL_RIESGO(id_perfil),
    FOREIGN KEY (id_presupuesto) REFERENCES PRESUPUESTO(id_presupuesto)
);

CREATE INDEX IF NOT EXISTS idx_ingreso_usuario     ON INGRESO(id_usuario);
CREATE INDEX IF NOT EXISTS idx_factura_usuario     ON FACTURA(id_usuario);
CREATE INDEX IF NOT EXISTS idx_factura_categoria   ON FACTURA(id_categoria);
CREATE INDEX IF NOT EXISTS idx_detalle_factura     ON DETALLE_FACTURA(id_factura);
CREATE INDEX IF NOT EXISTS idx_resumen_usuario     ON RESUMEN_MENSUAL(id_usuario);
CREATE INDEX IF NOT EXISTS idx_presupuesto_usuario ON PRESUPUESTO(id_usuario);
CREATE INDEX IF NOT EXISTS idx_alerta_usuario      ON ALERTA(id_usuario);
