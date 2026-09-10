package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ni.edu.uam.fact_app.util.SceneManager;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    private void abrirCategorias() {

        try {

            SceneManager.abrirVentana(
                    "/ni/edu/uam/fact_app/fxml/categoria-view.fxml",
                    "Gestión de categorías"
            );

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

        } catch (IOException e) {

            mostrarError(
                    "No fue posible abrir Empleados."
            );
        }
    }

    @FXML
    private void salir() {

        Alert alerta =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "¿Desea cerrar la aplicación?",
                        ButtonType.OK,
                        ButtonType.CANCEL
                );

        if (alerta.showAndWait()
                .orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            Platform.exit();
        }
    }

    private void mostrarError(
            String mensaje
    ) {

        new Alert(
                Alert.AlertType.ERROR,
                mensaje,
                ButtonType.OK
        ).showAndWait();
    }
}