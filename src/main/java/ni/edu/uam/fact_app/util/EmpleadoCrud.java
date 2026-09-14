package ni.edu.uam.fact_app.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ni.edu.uam.fact_app.model.Empleado;

public class EmpleadoCrud implements Crud<Empleado> {

    /*
     * La lista es static para conservar los empleados
     * aunque se cierre y vuelva a abrir la ventana.
     */
    private static final ObservableList<Empleado> empleados =
            FXCollections.observableArrayList();


    @Override
    public void guardar(Empleado empleado) {

        empleados.add(empleado);
    }


    @Override
    public ObservableList<Empleado> listar() {

        return empleados;
    }


    @Override
    public void actualizar(
            int indice,
            Empleado empleado
    ) {

        if (indice >= 0 && indice < empleados.size()) {

            empleados.set(
                    indice,
                    empleado
            );
        }
    }


    @Override
    public void eliminar(
            Empleado empleado
    ) {

        empleados.remove(empleado);
    }
}