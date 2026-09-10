package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.model.Cargo;

public class CargoController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TableView<Cargo> tblCargos;

    @FXML
    private TableColumn<Cargo, String> colNombre;

    @FXML
    private TableColumn<Cargo, String> colDescripcion;

    private final ObservableList<Cargo> cargos =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion")
        );

        tblCargos.setItems(cargos);
    }

    @FXML
    private void guardar() {

        if (txtNombre.getText().isBlank()
                || txtDescripcion.getText().isBlank()) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Complete todos los campos."
            );

            return;
        }

        Cargo cargo = new Cargo(
                null,
                txtNombre.getText().trim(),
                txtDescripcion.getText().trim()
        );

        cargos.add(cargo);

        mensaje(
                Alert.AlertType.INFORMATION,
                "Cargo agregado correctamente."
        );

        limpiar();
    }

    @FXML
    private void limpiar() {

        txtNombre.clear();
        txtDescripcion.clear();
    }

    @FXML
    private void cerrar() {

        Stage stage =
                (Stage) txtNombre.getScene().getWindow();

        stage.close();
    }

    private void mensaje(
            Alert.AlertType tipo,
            String texto
    ) {

        new Alert(
                tipo,
                texto,
                ButtonType.OK
        ).showAndWait();
    }
}