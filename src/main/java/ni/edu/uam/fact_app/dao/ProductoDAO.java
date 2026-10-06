package ni.edu.uam.fact_app.dao;

import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre, c.activa AS categoria_activa FROM producto p JOIN categoria c ON c.id=p.categoria_id ORDER BY p.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Producto(rs.getInt("id"), rs.getString("codigo"), rs.getString("nombre"), new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nombre"), rs.getBoolean("categoria_activa")), rs.getBigDecimal("precio_venta"), rs.getInt("existencia"), rs.getString("ruta_imagen"), rs.getBoolean("activo")));
        }
        return lista;
    }

    public void guardar(Producto objeto) throws SQLException {
        String sql = "INSERT INTO producto (codigo, nombre, categoria_id, precio_venta, existencia, ruta_imagen, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros(ps, objeto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) objeto.setId(rs.getInt(1));
            }
        }
    }

    public void actualizar(Producto objeto) throws SQLException {
        if (objeto == null || objeto.getId() == null)
            throw new IllegalArgumentException("Debe seleccionar el registro que desea actualizar.");
        String sql = "UPDATE producto SET codigo = ?, nombre = ?, categoria_id = ?, precio_venta = ?, existencia = ?, ruta_imagen = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            parametros(ps, objeto);
            ps.setInt(8, objeto.getId());
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.", "02000");
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM producto WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.", "02000");
        }
    }


    public boolean existeCodigo(String valor) throws SQLException {
        return existeCodigo(valor, null);
    }

    public boolean existeCodigo(String valor, Integer idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM producto WHERE LOWER(TRIM(codigo)) = LOWER(TRIM(?))";
        if (idExcluir != null) sql += " AND id <> ?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, valor.trim());
            if (idExcluir != null) ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean tieneVentas(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalle_venta WHERE producto_id = ?";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void parametros(PreparedStatement ps, Producto objeto) throws SQLException {
        ps.setString(1, objeto.getCodigo());
        ps.setString(2, objeto.getNombre());
        ps.setInt(3, objeto.getCategoria().getId());
        ps.setBigDecimal(4, objeto.getPrecioVenta());
        ps.setInt(5, objeto.getExistencia());
        ps.setString(6, objeto.getRutaImagen());
        ps.setBoolean(7, objeto.isActivo());
    }
}
