package ni.edu.uam.fact_app.dao;

import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    public List<Empleado> listar() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT e.*, c.nombre AS cargo_nombre, c.descripcion AS cargo_descripcion FROM empleado e JOIN cargo c ON c.id=e.cargo_id ORDER BY e.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Empleado(rs.getInt("id"), rs.getString("nombres"), rs.getString("apellidos"), new Cargo(rs.getInt("cargo_id"), rs.getString("cargo_nombre"), rs.getString("cargo_descripcion")), rs.getDate("fecha_contratacion").toLocalDate(), rs.getBoolean("activo")));
        }
        return lista;
    }

    public void guardar(Empleado objeto) throws SQLException {
        String sql = "INSERT INTO empleado (nombres, apellidos, cargo_id, fecha_contratacion, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros(ps, objeto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) objeto.setId(rs.getInt(1));
            }
        }
    }

    public void actualizar(Empleado objeto) throws SQLException {
        String sql = "UPDATE empleado SET nombres = ?, apellidos = ?, cargo_id = ?, fecha_contratacion = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            parametros(ps, objeto);
            ps.setInt(6, objeto.getId());
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM empleado WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    private void parametros(PreparedStatement ps, Empleado objeto) throws SQLException {
        ps.setString(1, objeto.getNombres());
        ps.setString(2, objeto.getApellidos());
        ps.setInt(3, objeto.getCargo().getId());
        ps.setDate(4, Date.valueOf(objeto.getFechaContratacion()));
        ps.setBoolean(5, objeto.isActivo());
    }
}
