package ni.edu.uam.fact_app.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import ni.edu.uam.fact_app.model.Cargo;
import ni.edu.uam.fact_app.util.CargoCrud;
import ni.edu.uam.fact_app.util.Crud;

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


    /*
     * CRUD encargado de almacenar los cargos.
     */
    private final Crud<Cargo> cargoCrud =
            new CargoCrud();


    /*
     * Esta lista proviene de CargoCrud.
     *
     * Por eso los datos se mantienen aunque
     * se cierre y vuelva a abrir la ventana.
     */
    private final ObservableList<Cargo> cargos =
            cargoCrud.listar();


    @FXML
    private void initialize() {

        /*
         * Configuración de las columnas.
         */
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>(
                        "nombre"
                )
        );

        colDescripcion.setCellValueFactory(
                new PropertyValueFactory<>(
                        "descripcion"
                )
        );


        /*
         * Mostrar los cargos almacenados.
         */
        tblCargos.setItems(
                cargos
        );


        /*
         * Mensaje en español cuando
         * todavía no existen registros.
         */
        tblCargos.setPlaceholder(
                new Label(
                        "No hay cargos para mostrar."
                )
        );
    }


    @FXML
    private void guardar() {

        /*
         * Validar campos obligatorios.
         */
        if (txtNombre.getText().isBlank()
                || txtDescripcion.getText().isBlank()) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Debe completar todos los campos obligatorios marcados con *."
            );

            return;
        }


        /*
         * Crear el nuevo cargo.
         */
        Cargo cargo =
                new Cargo(
                        null,
                        txtNombre
                                .getText()
                                .trim(),
                        txtDescripcion
                                .getText()
                                .trim()
                );


        /*
         * Guardar mediante CRUD.
         */
        cargoCrud.guardar(
                cargo
        );


        mensaje(
                Alert.AlertType.INFORMATION,
                "Cargo agregado correctamente."
        );


        limpiarCampos();
    }


    /*
     * Limpiamos automáticamente después de guardar.
     *
     * Ya no existe un botón Limpiar.
     */
    private void limpiarCampos() {

        txtNombre.clear();

        txtDescripcion.clear();

        txtNombre.requestFocus();
    }


    /*
     * Alertas completamente en español.
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


        alerta.setHeaderText(null);

        alerta.showAndWait();
    }
}