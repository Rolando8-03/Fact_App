package ni.edu.uam.fact_app.util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Empleado;
import ni.edu.uam.fact_app.dao.EmpleadoDAO;
import java.sql.SQLException;

public class EmpleadoCrud implements Crud<Empleado> {
    private final EmpleadoDAO dao = new EmpleadoDAO();
    private final ObservableList<Empleado> datos = FXCollections.observableArrayList();
    public void guardar(Empleado objeto) throws SQLException { dao.guardar(objeto); listar(); }
    public ObservableList<Empleado> listar() throws SQLException { datos.setAll(dao.listar()); return datos; }
    public void actualizar(int indice, Empleado objeto) throws SQLException {
        if (indice < 0 || indice >= datos.size()) throw new SQLException("Seleccione un registro válido.");
        objeto.setId(datos.get(indice).getId());
        dao.actualizar(objeto);
        listar();
    }
    public void eliminar(Empleado objeto) throws SQLException { dao.eliminar(objeto.getId()); listar(); }
}
