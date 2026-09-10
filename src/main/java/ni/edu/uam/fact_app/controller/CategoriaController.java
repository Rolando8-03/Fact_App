package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.model.Categoria;

public class CategoriaController {

    @FXML
    private TextField txtNombre;

    @FXML
    private CheckBox chkActiva;

    @FXML
    private TableView<Categoria> tblCategorias;

    @FXML
    private TableColumn<Categoria, String> colNombre;

    @FXML
    private TableColumn<Categoria, Boolean> colActiva;

    private final ObservableList<Categoria> categorias =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colActiva.setCellValueFactory(
                new PropertyValueFactory<>("activa")
        );

        tblCategorias.setItems(categorias);

        chkActiva.setSelected(true);
    }

    @FXML
    private void guardar() {

        if (txtNombre.getText().isBlank()) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Ingrese el nombre de la categoría."
            );

            return;
        }

        Categoria categoria = new Categoria(
                null,
                txtNombre.getText().trim(),
                chkActiva.isSelected()
        );

        categorias.add(categoria);

        mensaje(
                Alert.AlertType.INFORMATION,
                "Categoría agregada correctamente."
        );

        limpiar();
    }

    @FXML
    private void limpiar() {

        txtNombre.clear();
        chkActiva.setSelected(true);
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