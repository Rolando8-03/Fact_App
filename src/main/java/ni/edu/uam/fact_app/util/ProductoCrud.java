package ni.edu.uam.fact_app.util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Producto;
import ni.edu.uam.fact_app.dao.ProductoDAO;
import java.sql.SQLException;

public class ProductoCrud implements Crud<Producto> {
    private final ProductoDAO dao = new ProductoDAO();
    private final ObservableList<Producto> datos = FXCollections.observableArrayList();
    public void guardar(Producto objeto) throws SQLException { dao.guardar(objeto); listar(); }
    public ObservableList<Producto> listar() throws SQLException { datos.setAll(dao.listar()); return datos; }
    public void actualizar(int indice, Producto objeto) throws SQLException {
        if (indice < 0 || indice >= datos.size()) throw new SQLException("Seleccione un registro válido.");
        objeto.setId(datos.get(indice).getId());
        dao.actualizar(objeto);
        listar();
    }
    public void eliminar(Producto objeto) throws SQLException { dao.eliminar(objeto.getId()); listar(); }
}
