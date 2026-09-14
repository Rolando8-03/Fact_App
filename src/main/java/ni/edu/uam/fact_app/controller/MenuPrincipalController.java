package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import ni.edu.uam.fact_app.util.CargoCrud;
import ni.edu.uam.fact_app.util.CategoriaCrud;
import ni.edu.uam.fact_app.util.EmpleadoCrud;
import ni.edu.uam.fact_app.util.ProductoCrud;
import ni.edu.uam.fact_app.util.SceneManager;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private Label lblTotalCategorias;

    @FXML
    private Label lblTotalProductos;

    @FXML
    private Label lblTotalCargos;

    @FXML
    private Label lblTotalEmpleados;


    /*
     * CRUD de cada módulo.
     *
     * Se utilizan para consultar cuántos
     * registros existen actualmente.
     */
    private final CategoriaCrud categoriaCrud =
            new CategoriaCrud();

    private final ProductoCrud productoCrud =
            new ProductoCrud();

    private final CargoCrud cargoCrud =
            new CargoCrud();

    private final EmpleadoCrud empleadoCrud =
            new EmpleadoCrud();


    @FXML
    private void initialize() {

        actualizarResumen();
    }


    @FXML
    private void abrirCategorias() {

        try {

            SceneManager.abrirVentana(
                    "/ni/edu/uam/fact_app/fxml/categoria-view.fxml",
                    "Gestión de categorías"
            );


            /*
             * Al cerrar la ventana de categorías,
             * actualizamos el resumen.
             */
            actualizarResumen();

        } catch (IOException e) {

            mostrarError(
                    "No fue posible abrir Categorías."
            );
        }
    }


    @FXML
    private void abrirProductos() {

        try {

            SceneManager.abrirVentana(
                    "/ni/edu/uam/fact_app/fxml/producto-view.fxml",
                    "Gestión de productos"
            );


            actualizarResumen();

        } catch (IOException e) {

            mostrarError(
                    "No fue posible abrir Productos."
            );
        }
    }


    @FXML
    private void abrirCargos() {

        try {

            SceneManager.abrirVentana(
                    "/ni/edu/uam/fact_app/fxml/cargo-view.fxml",
                    "Gestión de cargos"
            );


            actualizarResumen();

        } catch (IOException e) {

            mostrarError(
                    "No fue posible abrir Cargos."
            );
        }
    }


    @FXML
    private void abrirEmpleados() {

        try {

            SceneManager.abrirVentana(
                    "/ni/edu/uam/fact_app/fxml/empleado-view.fxml",
                    "Gestión de empleados"
            );


            actualizarResumen();

        } catch (IOException e) {

            mostrarError(
                    "No fue posible abrir Empleados."
            );
        }
    }


    /*
     * Actualiza las cantidades que aparecen
     * en la pantalla principal.
     */
    private void actualizarResumen() {

        lblTotalCategorias.setText(
                String.valueOf(
                        categoriaCrud
                                .listar()
                                .size()
                )
        );


        lblTotalProductos.setText(
                String.valueOf(
                        productoCrud
                                .listar()
                                .size()
                )
        );


        lblTotalCargos.setText(
                String.valueOf(
                        cargoCrud
                                .listar()
                                .size()
                )
        );


        lblTotalEmpleados.setText(
                String.valueOf(
                        empleadoCrud
                                .listar()
                                .size()
                )
        );
    }


    /*
     * Se mantiene Salir únicamente
     * dentro del MenuBar.
     *
     * Ya no tendremos un botón Salir
     * en la ToolBar.
     */
    @FXML
    private void salir() {

        ButtonType si =
                new ButtonType(
                        "Sí"
                );

        ButtonType no =
                new ButtonType(
                        "No"
                );


        Alert alerta =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "¿Desea cerrar la aplicación?",
                        si,
                        no
                );


        alerta.setTitle(
                "Confirmar salida"
        );

        alerta.setHeaderText(
                null
        );


        if (alerta.showAndWait()
                .orElse(no)
                == si) {

            Platform.exit();
        }
    }


    /*
     * Error completamente en español.
     */
    private void mostrarError(
            String mensaje
    ) {

        ButtonType aceptar =
                new ButtonType(
                        "Aceptar"
                );


        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR,
                        mensaje,
                        aceptar
                );


        alerta.setTitle(
                "Error"
        );

        alerta.setHeaderText(
                null
        );


        alerta.showAndWait();
    }
}