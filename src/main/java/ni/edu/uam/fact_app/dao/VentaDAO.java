package ni.edu.uam.fact_app.dao;

import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class VentaDAO {
    public Venta guardar(List<DetalleVenta> carrito, Usuario usuario) throws SQLException {
        if (carrito.isEmpty() || usuario == null || usuario.getEmpleado() == null) throw new SQLException("La venta necesita productos y un vendedor.");
        List<DetalleVenta> detalles = new ArrayList<>(carrito);
        detalles.sort(Comparator.comparing(d -> d.getProducto().getId()));
        BigDecimal subtotal = BigDecimal.ZERO;
        HashSet<Integer> ids = new HashSet<>();
        for (DetalleVenta d : detalles) {
            if (d.getCantidad() <= 0 || d.getPrecioUnitario().signum() <= 0 || !ids.add(d.getProducto().getId()))
                throw new SQLException("Revise las cantidades y los productos de la factura.");
            subtotal = subtotal.add(d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())));
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        Venta venta = new Venta();
        venta.setVendedor(usuario.getEmpleado()); venta.setDetalles(detalles);
        venta.setSubtotal(subtotal); venta.setIva(iva); venta.setTotal(subtotal.add(iva));

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Bloquea los productos en orden para validar y descontar dentro de la misma transacción.
                for (DetalleVenta d : detalles) {
                    try (PreparedStatement ps = conn.prepareStatement("SELECT codigo, nombre, precio_venta, existencia, activo FROM producto WHERE id = ? FOR UPDATE")) {
                        ps.setInt(1, d.getProducto().getId());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || !rs.getBoolean("activo") || rs.getInt("existencia") < d.getCantidad())
                                throw new SQLException("El producto " + d.getNombreProducto() + " ya no está disponible en la cantidad solicitada. Revise el carrito.");
                            if (rs.getBigDecimal("precio_venta").compareTo(d.getPrecioUnitario()) != 0)
                                throw new SQLException("Cambió el precio de " + d.getNombreProducto() + ". Quite ese artículo, refresque el catálogo y vuelva a agregarlo.");
                        }
                    }
                }
                String sql = "INSERT INTO venta (usuario, vendedor, subtotal, iva, total) VALUES (?, ?, ?, ?, ?) RETURNING id, numero_factura, fecha";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, usuario.getUsername());
                    ps.setString(2, usuario.getEmpleado().getNombres() + " " + usuario.getEmpleado().getApellidos());
                    ps.setBigDecimal(3, subtotal); ps.setBigDecimal(4, iva); ps.setBigDecimal(5, venta.getTotal());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next(); venta.setId(rs.getInt("id")); venta.setNumeroFactura(rs.getString("numero_factura"));
                        venta.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    }
                }
                for (DetalleVenta d : detalles) {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO detalle_venta (venta_id, producto_id, codigo, nombre_producto, cantidad, precio_unitario, subtotal) SELECT ?, id, codigo, nombre, ?, ?, ? FROM producto WHERE id = ?")) {
                        ps.setInt(1, venta.getId()); ps.setInt(2, d.getCantidad()); ps.setBigDecimal(3, d.getPrecioUnitario());
                        ps.setBigDecimal(4, d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad()))); ps.setInt(5, d.getProducto().getId());
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement("UPDATE producto SET existencia = existencia - ? WHERE id = ?")) {
                        ps.setInt(1, d.getCantidad()); ps.setInt(2, d.getProducto().getId()); ps.executeUpdate();
                    }
                }
                conn.commit();
                return venta;
            } catch (SQLException | RuntimeException e) {
                try { conn.rollback(); } catch (SQLException fallo) { e.addSuppressed(fallo); }
                throw e;
            }
        }
    }
}
