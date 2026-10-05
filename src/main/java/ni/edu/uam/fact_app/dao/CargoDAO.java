package ni.edu.uam.fact_app.dao;

import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    public List<Cargo> listar() throws SQLException {
        List<Cargo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM cargo ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Cargo(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
        }
        return lista;
    }

    public void guardar(Cargo objeto) throws SQLException {
        String sql = "INSERT INTO cargo (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros(ps, objeto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) objeto.setId(rs.getInt(1));
            }
        }
    }

    public void actualizar(Cargo objeto) throws SQLException {
        String sql = "UPDATE cargo SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            parametros(ps, objeto);
            ps.setInt(3, objeto.getId());
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cargo WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    private void parametros(PreparedStatement ps, Cargo objeto) throws SQLException {
        ps.setString(1, objeto.getNombre());
        ps.setString(2, objeto.getDescripcion());
    }
}
