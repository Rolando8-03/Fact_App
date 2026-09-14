package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Cargo;

public class CargoCrud implements Crud<Cargo> {

    /*
     * La lista es static para conservar los cargos
     * aunque se cierre y vuelva a abrir la ventana.
     */
    private static final ObservableList<Cargo> cargos =
            FXCollections.observableArrayList();


    @Override
    public void guardar(Cargo cargo) {

        cargos.add(cargo);
    }


    @Override
    public ObservableList<Cargo> listar() {

        return cargos;
    }


    @Override
    public void actualizar(
            int indice,
            Cargo cargo
    ) {

        if (indice >= 0 && indice < cargos.size()) {

            cargos.set(
                    indice,
                    cargo
            );
        }
    }


    @Override
    public void eliminar(
            Cargo cargo
    ) {

        cargos.remove(cargo);
    }
}