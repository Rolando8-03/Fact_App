package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import ni.edu.uam.fact_app.model.Categoria;
import ni.edu.uam.fact_app.util.CategoriaCrud;
import ni.edu.uam.fact_app.util.Crud;

public class CategoriaController {

    @FXML
    private TextField txtNombre;

    @FXML
    private CheckBox chkActiva;

    @FXML
    private CheckBox chkVerTodos;

    @FXML
    private TableView<Categoria> tblCategorias;

    @FXML
    private TableColumn<Categoria, String> colNombre;


    /*
     * CRUD encargado de almacenar las categorías.
     */
    private final Crud<Categoria> categoriaCrud =
            new CategoriaCrud();


    /*
     * Lista con todas las categorías registradas.
     */
    private final ObservableList<Categoria> categorias =
            categoriaCrud.listar();


    /*
     * Lista que actualmente se muestra en la tabla.
     */
    private final ObservableList<Categoria> categoriasMostradas =
            FXCollections.observableArrayList();


    @FXML
    private void initialize() {

        /*
         * Configuración de columnas.
         */
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>(
                        "nombre"
                )
        );

        tblCategorias.setItems(
                categoriasMostradas
        );

        tblCategorias.setPlaceholder(
                new Label(
                        "No hay categorías para mostrar."
                )
        );


        /*
         * Valores iniciales.
         */
        chkActiva.setSelected(
                true
        );


        chkVerTodos.setSelected(
                true
        );


        actualizarTabla();
    }


    @FXML
    private void guardar() {

        /*
         * Validar campo obligatorio.
         */
        if (txtNombre.getText().isBlank()) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Debe completar todos los campos obligatorios marcados con *."
            );

            return;
        }


        /*
         * Crear categoría.
         */
        Categoria categoria =
                new Categoria(
                        null,

                        txtNombre
                                .getText()
                                .trim(),

                        chkActiva
                                .isSelected()
                );


        /*
         * Guardar mediante CRUD.
         */
        categoriaCrud.guardar(
                categoria
        );


        /*
         * Actualizar tabla respetando
         * el filtro de Ver todos.
         */
        actualizarTabla();


        mensaje(
                Alert.AlertType.INFORMATION,
                "Categoría agregada correctamente."
        );


        limpiarCampos();
    }


    @FXML
    private void filtrarCategorias() {

        actualizarTabla();
    }

    private void actualizarTabla() {

        categoriasMostradas.clear();


        if (chkVerTodos.isSelected()) {

            categoriasMostradas.addAll(
                    categorias
            );

        } else {

            for (Categoria categoria : categorias) {

                if (categoria.isActiva()) {

                    categoriasMostradas.add(
                            categoria
                    );
                }
            }
        }
    }


    private void limpiarCampos() {

        txtNombre.clear();


        chkActiva.setSelected(
                true
        );


        txtNombre.requestFocus();
    }


    /*
     * Todas las alertas en español.
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
