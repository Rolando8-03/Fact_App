package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Categoria;

public class CategoriaCrud implements Crud<Categoria> {

    /*
     * Lista compartida de categorías.
     *
     * Al ser static, las categorías permanecen
     * aunque se cierre y vuelva a abrir la ventana.
     */
    private static final ObservableList<Categoria> categorias =
            FXCollections.observableArrayList();


    @Override
    public void guardar(Categoria categoria) {

        categorias.add(categoria);
    }


    @Override
    public ObservableList<Categoria> listar() {

        return categorias;
    }


    @Override
    public void actualizar(
            int indice,
            Categoria categoria
    ) {

        if (indice >= 0
                && indice < categorias.size()) {

            categorias.set(
                    indice,
                    categoria
            );
        }
    }


    @Override
    public void eliminar(
            Categoria categoria
    ) {

        categorias.remove(categoria);
    }
}