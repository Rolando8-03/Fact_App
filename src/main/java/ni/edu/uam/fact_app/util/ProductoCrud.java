package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Producto;

public class ProductoCrud implements Crud<Producto> {

    private static final ObservableList<Producto> productos =
            FXCollections.observableArrayList();

    @Override
    public void guardar(Producto producto) {
        productos.add(producto);
    }

    @Override
    public ObservableList<Producto> listar() {
        return productos;
    }

    @Override
    public void actualizar(int indice, Producto producto) {

        if (indice >= 0 && indice < productos.size()) {
            productos.set(indice, producto);
        }
    }

    @Override
    public void eliminar(Producto producto) {
        productos.remove(producto);
    }
}