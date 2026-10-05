-- Datos opcionales de prueba. No modifica los registros que ya existan.
BEGIN;
INSERT INTO categoria(nombre, activa) VALUES ('Tecnología', TRUE), ('Oficina', TRUE) ON CONFLICT DO NOTHING;
INSERT INTO cargo(nombre, descripcion) VALUES
('Administrador', 'Administración del sistema'),
('Cajero', 'Registro de ventas'),
('Bodeguero', 'Gestión de inventario') ON CONFLICT DO NOTHING;
INSERT INTO producto(codigo, nombre, categoria_id, precio_venta, existencia, activo)
SELECT 'P001', 'Mouse', id, 250, 20, TRUE FROM categoria WHERE lower(nombre) = lower('Tecnología')
ON CONFLICT DO NOTHING;
COMMIT;
