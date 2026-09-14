package ni.edu.uam.fact_app.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import ni.edu.uam.fact_app.model.Cargo;
import ni.edu.uam.fact_app.model.Empleado;
import ni.edu.uam.fact_app.util.CargoCrud;
import ni.edu.uam.fact_app.util.Crud;
import ni.edu.uam.fact_app.util.EmpleadoCrud;

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


    /*
     * CRUD encargado de almacenar los empleados.
     */
    private final Crud<Empleado> empleadoCrud =
            new EmpleadoCrud();


    /*
     * CRUD de cargos.
     *
     * Esto permite utilizar en el ComboBox
     * los cargos registrados desde la ventana
     * de Gestión de cargos.
     */
    private final Crud<Cargo> cargoCrud =
            new CargoCrud();


    /*
     * Lista compartida de empleados.
     *
     * Los registros permanecen aunque se cierre
     * y vuelva a abrir la ventana.
     */
    private final ObservableList<Empleado> empleados =
            empleadoCrud.listar();


    @FXML
    private void initialize() {

        /*
         * Mostrar los cargos registrados.
         */
        cmbCargo.setItems(
                cargoCrud.listar()
        );


        /*
         * Configuración de columnas.
         */
        colNombres.setCellValueFactory(
                new PropertyValueFactory<>(
                        "nombres"
                )
        );

        colApellidos.setCellValueFactory(
                new PropertyValueFactory<>(
                        "apellidos"
                )
        );

        colCargo.setCellValueFactory(
                new PropertyValueFactory<>(
                        "cargo"
                )
        );

        colFecha.setCellValueFactory(
                new PropertyValueFactory<>(
                        "fechaContratacion"
                )
        );


        /*
         * Mostrar empleados almacenados.
         */
        tblEmpleados.setItems(
                empleados
        );


        /*
         * Mensaje en español cuando
         * la tabla esté vacía.
         */
        tblEmpleados.setPlaceholder(
                new Label(
                        "No hay empleados para mostrar."
                )
        );


        /*
         * Estado inicial.
         */
        chkActivo.setSelected(true);
    }


    /*
     * Se ejecuta al seleccionar una fecha.
     *
     * Si la fecha es posterior al día actual,
     * muestra la advertencia y elimina
     * la selección.
     */
    @FXML
    private void validarFecha() {

        LocalDate fecha =
                dpFechaContratacion.getValue();


        if (fecha != null
                && fecha.isAfter(LocalDate.now())) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "La fecha de contratación no puede ser posterior a la fecha actual."
            );


            dpFechaContratacion.setValue(
                    null
            );
        }
    }


    @FXML
    private void guardar() {

        /*
         * Validar campos obligatorios.
         */
        if (txtNombres.getText().isBlank()
                || txtApellidos.getText().isBlank()
                || cmbCargo.getValue() == null
                || dpFechaContratacion.getValue() == null) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Debe completar todos los campos obligatorios marcados con *."
            );

            return;
        }


        /*
         * Volver a verificar la fecha
         * antes de guardar.
         */
        if (dpFechaContratacion
                .getValue()
                .isAfter(LocalDate.now())) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "La fecha de contratación no puede ser posterior a la fecha actual."
            );

            dpFechaContratacion.setValue(
                    null
            );

            return;
        }


        /*
         * Crear empleado.
         */
        Empleado empleado =
                new Empleado(
                        null,

                        txtNombres
                                .getText()
                                .trim(),

                        txtApellidos
                                .getText()
                                .trim(),

                        cmbCargo
                                .getValue(),

                        dpFechaContratacion
                                .getValue(),

                        chkActivo
                                .isSelected()
                );


        /*
         * Guardar mediante CRUD.
         */
        empleadoCrud.guardar(
                empleado
        );


        mensaje(
                Alert.AlertType.INFORMATION,
                "Empleado agregado correctamente."
        );


        limpiarCampos();
    }


    /*
     * Limpieza automática después
     * de guardar correctamente.
     *
     * No existe botón Limpiar.
     */
    private void limpiarCampos() {

        txtNombres.clear();

        txtApellidos.clear();


        cmbCargo
                .getSelectionModel()
                .clearSelection();


        dpFechaContratacion.setValue(
                null
        );


        chkActivo.setSelected(
                true
        );


        txtNombres.requestFocus();
    }


    /*
     * Todas las alertas se muestran
     * completamente en español.
     */
    private void mensaje(
            Alert.AlertType tipo,
            String texto
    ) {

        ButtonType aceptar =
                new ButtonType(
                        "Aceptar"
                );


        Alert alerta =
                new Alert(
                        tipo,
                        texto,
                        aceptar
                );


        if (tipo == Alert.AlertType.WARNING) {

            alerta.setTitle(
                    "Advertencia"
            );

        } else if (tipo == Alert.AlertType.ERROR) {

            alerta.setTitle(
                    "Error"
            );

        } else if (tipo == Alert.AlertType.INFORMATION) {

            alerta.setTitle(
                    "Información"
            );

        } else {

            alerta.setTitle(
                    "Mensaje"
            );
        }


        alerta.setHeaderText(
                null
        );


        alerta.showAndWait();
    }
}