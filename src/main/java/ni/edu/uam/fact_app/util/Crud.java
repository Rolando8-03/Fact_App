package ni.edu.uam.fact_app.util;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface Crud<T> {
    void guardar(T objeto) throws SQLException;
    ObservableList<T> listar() throws SQLException;
    void actualizar(int indice, T objeto) throws SQLException;
    void eliminar(T objeto) throws SQLException;
}
