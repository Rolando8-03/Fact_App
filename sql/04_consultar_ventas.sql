-- Facturas registradas
SELECT id, numero_factura, fecha, usuario, vendedor, subtotal, iva, total
FROM venta ORDER BY id DESC;

-- Detalles por factura
SELECT v.numero_factura, d.codigo, d.nombre_producto, d.cantidad,
       d.precio_unitario, d.subtotal
FROM detalle_venta d JOIN venta v ON v.id = d.venta_id
ORDER BY v.id DESC, d.id;

-- Existencias actuales
SELECT codigo, nombre, existencia FROM producto ORDER BY id;
