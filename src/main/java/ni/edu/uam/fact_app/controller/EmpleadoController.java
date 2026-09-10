package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.model.Cargo;
import ni.edu.uam.fact_app.model.Empleado;

import java.time.LocalDate;

public class EmpleadoController {

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidos;

    @FXML
    private ComboBox<Cargo> cmbCargo;

    @FXML
    private DatePicker dpFechaContratacion;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private TableView<Empleado> tblEmpleados;

    @FXML
    private TableColumn<Empleado, String> colNombres;

    @FXML
    private TableColumn<Empleado, String> colApellidos;

    @FXML
    private TableColumn<Empleado, Cargo> colCargo;

    @FXML
    private TableColumn<Empleado, LocalDate> colFecha;

    @FXML
    private TableColumn<Empleado, Boolean> colActivo;

    private final ObservableList<Empleado> empleados =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        cmbCargo.setItems(
                FXCollections.observableArrayList(
                        new Cargo(
                                1,
                                "Administrador",
                                "Administración"
                        ),
                        new Cargo(
                                2,
                                "Cajero",
                                "Atención de caja"
                        ),
                        new Cargo(
                                3,
                                "Vendedor",
                                "Ventas"
                        )
                )
        );

        colNombres.setCellValueFactory(
                new PropertyValueFactory<>("nombres")
        );

        colApellidos.setCellValueFactory(
                new PropertyValueFactory<>("apellidos")
        );

        colCargo.setCellValueFactory(
                new PropertyValueFactory<>("cargo")
        );

        colFecha.setCellValueFactory(
                new PropertyValueFactory<>("fechaContratacion")
        );

        colActivo.setCellValueFactory(
                new PropertyValueFactory<>("activo")
        );

        tblEmpleados.setItems(empleados);

        chkActivo.setSelected(true);
    }

    @FXML
    private void guardar() {

        if (txtNombres.getText().isBlank()
                || txtApellidos.getText().isBlank()
                || cmbCargo.getValue() == null
                || dpFechaContratacion.getValue() == null) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Complete todos los campos."
            );

            return;
        }

        Empleado empleado = new Empleado(
                null,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                cmbCargo.getValue(),
                dpFechaContratacion.getValue(),
                chkActivo.isSelected()
        );

        empleados.add(empleado);

        mensaje(
                Alert.AlertType.INFORMATION,
                "Empleado agregado correctamente."
        );

        limpiar();
    }

    @FXML
    private void limpiar() {

        txtNombres.clear();
        txtApellidos.clear();

        cmbCargo
                .getSelectionModel()
                .clearSelection();

        dpFechaContratacion.setValue(null);

        chkActivo.setSelected(true);
    }

    @FXML
    private void cerrar() {

        Stage stage =
                (Stage) txtNombres.getScene().getWindow();

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