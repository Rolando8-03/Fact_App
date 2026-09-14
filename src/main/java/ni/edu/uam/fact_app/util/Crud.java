package ni.edu.uam.fact_app.util;

import javafx.collections.ObservableList;

public interface Crud<T> {

    void guardar(T objeto);

    ObservableList<T> listar();

    void actualizar(int indice, T objeto);

    void eliminar(T objeto);
}