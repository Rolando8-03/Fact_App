package ni.edu.uam.fact_app.dao;

import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, activa FROM categoria ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getBoolean("activa")));
        }
        return lista;
    }

    public void guardar(Categoria objeto) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, activa) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            parametros(ps, objeto);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) objeto.setId(rs.getInt(1));
            }
        }
    }

    public void actualizar(Categoria objeto) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ?, activa = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            parametros(ps, objeto);
            ps.setInt(3, objeto.getId());
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM categoria WHERE id = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() != 1) throw new SQLException("El registro ya no existe. Pulse Refrescar.");
        }
    }

    private void parametros(PreparedStatement ps, Categoria objeto) throws SQLException {
        ps.setString(1, objeto.getNombre());
        ps.setBoolean(2, objeto.isActiva());
    }
}
