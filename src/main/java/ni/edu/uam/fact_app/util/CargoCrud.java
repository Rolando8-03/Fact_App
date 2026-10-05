package ni.edu.uam.fact_app.util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Cargo;
import ni.edu.uam.fact_app.dao.CargoDAO;
import java.sql.SQLException;

public class CargoCrud implements Crud<Cargo> {
    private final CargoDAO dao = new CargoDAO();
    private final ObservableList<Cargo> datos = FXCollections.observableArrayList();
    public void guardar(Cargo objeto) throws SQLException { dao.guardar(objeto); listar(); }
    public ObservableList<Cargo> listar() throws SQLException { datos.setAll(dao.listar()); return datos; }
    public void actualizar(int indice, Cargo objeto) throws SQLException {
        if (indice < 0 || indice >= datos.size()) throw new SQLException("Seleccione un registro válido.");
        objeto.setId(datos.get(indice).getId());
        dao.actualizar(objeto);
        listar();
    }
    public void eliminar(Cargo objeto) throws SQLException { dao.eliminar(objeto.getId()); listar(); }
}
