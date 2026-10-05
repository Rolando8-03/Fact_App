-- Ejecutar conectado a fact_app. No elimina tablas ni registros existentes.
BEGIN;
CREATE TABLE IF NOT EXISTS categoria (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL CHECK (btrim(nombre) <> ''),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE UNIQUE INDEX IF NOT EXISTS uq_categoria_nombre ON categoria (lower(btrim(nombre)));
CREATE TABLE IF NOT EXISTS cargo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL CHECK (btrim(nombre) <> ''),
    descripcion VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uq_cargo_nombre ON cargo (lower(btrim(nombre)));
CREATE TABLE IF NOT EXISTS producto (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL CHECK (btrim(codigo) <> ''),
    nombre VARCHAR(150) NOT NULL CHECK (btrim(nombre) <> ''),
    categoria_id INTEGER NOT NULL REFERENCES categoria(id),
    precio_venta NUMERIC(12,2) NOT NULL CHECK (precio_venta > 0),
    existencia INTEGER NOT NULL CHECK (existencia >= 0),
    ruta_imagen TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE UNIQUE INDEX IF NOT EXISTS uq_producto_codigo ON producto (lower(btrim(codigo)));
CREATE TABLE IF NOT EXISTS empleado (
    id SERIAL PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL CHECK (btrim(nombres) <> ''),
    apellidos VARCHAR(100) NOT NULL CHECK (btrim(apellidos) <> ''),
    cargo_id INTEGER NOT NULL REFERENCES cargo(id),
    fecha_contratacion DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS venta (
    id SERIAL PRIMARY KEY,
    numero_factura TEXT GENERATED ALWAYS AS ('FAC-' || (id + 1000)::text) STORED UNIQUE,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(100) NOT NULL,
    vendedor VARCHAR(205) NOT NULL,
    subtotal NUMERIC(18,2) NOT NULL CHECK (subtotal > 0),
    iva NUMERIC(18,2) NOT NULL CHECK (iva >= 0),
    total NUMERIC(18,2) NOT NULL CHECK (total = subtotal + iva)
);
CREATE TABLE IF NOT EXISTS detalle_venta (
    id SERIAL PRIMARY KEY,
    venta_id INTEGER NOT NULL REFERENCES venta(id),
    producto_id INTEGER NOT NULL REFERENCES producto(id),
    codigo VARCHAR(50) NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario > 0),
    subtotal NUMERIC(18,2) NOT NULL CHECK (subtotal = precio_unitario * cantidad)
);
COMMIT;
