package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import ni.edu.uam.fact_app.model.Categoria;
import ni.edu.uam.fact_app.model.Producto;
import ni.edu.uam.fact_app.util.CategoriaCrud;
import ni.edu.uam.fact_app.util.Crud;
import ni.edu.uam.fact_app.util.ProductoCrud;

import java.io.File;
import java.math.BigDecimal;

public class ProductoController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtExistencia;

    @FXML
    private ComboBox<Categoria> cmbCategoria;

    @FXML
    private CheckBox chkActivo;

    @FXML
    private CheckBox chkVerTodos;

    @FXML
    private ImageView imgProducto;

    @FXML
    private TableView<Producto> tblProductos;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Categoria> colCategoria;

    @FXML
    private TableColumn<Producto, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colExistencia;

    private final Crud<Producto> productoCrud =
            new ProductoCrud();

    private final Crud<Categoria> categoriaCrud =
            new CategoriaCrud();

    private final ObservableList<Producto> productos =
            productoCrud.listar();

    private final ObservableList<Producto> productosMostrados =
            FXCollections.observableArrayList();

    private String rutaImagen;

    @FXML
    private void initialize() {

        cmbCategoria.setItems(
                categoriaCrud.listar()
        );

        colCodigo.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precioVenta")
        );

        colExistencia.setCellValueFactory(
                new PropertyValueFactory<>("existencia")
        );

        tblProductos.setItems(
                productosMostrados
        );

        tblProductos.setPlaceholder(
                new Label(
                        "No hay productos para mostrar."
                )
        );

        chkActivo.setSelected(true);
        chkVerTodos.setSelected(true);

        actualizarTabla();
    }

    @FXML
    private void seleccionarImagen() {

        FileChooser chooser =
                new FileChooser();

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        File archivo =
                chooser.showOpenDialog(
                        txtCodigo
                                .getScene()
                                .getWindow()
                );

        if (archivo != null) {

            rutaImagen =
                    archivo.toURI().toString();

            imgProducto.setImage(
                    new Image(rutaImagen)
            );
        }
    }

    @FXML
    private void guardar() {

        if (txtCodigo.getText().isBlank()
                || txtNombre.getText().isBlank()
                || txtPrecio.getText().isBlank()
                || txtExistencia.getText().isBlank()
                || cmbCategoria.getValue() == null) {

            mensaje(
                    Alert.AlertType.WARNING,
                    "Debe completar todos los campos obligatorios marcados con *."
            );

            return;
        }

        try {

            BigDecimal precio =
                    new BigDecimal(
                            txtPrecio
                                    .getText()
                                    .trim()
                    );

            int existencia =
                    Integer.parseInt(
                            txtExistencia
                                    .getText()
                                    .trim()
                    );

            if (precio.signum() <= 0) {

                mensaje(
                        Alert.AlertType.WARNING,
                        "El precio debe ser mayor que cero."
                );

                return;
            }

            if (existencia < 0) {

                mensaje(
                        Alert.AlertType.WARNING,
                        "La existencia no puede ser negativa."
                );

                return;
            }

            Producto producto =
                    new Producto(
                            null,
                            txtCodigo
                                    .getText()
                                    .trim(),
                            txtNombre
                                    .getText()
                                    .trim(),
                            cmbCategoria.getValue(),
                            precio,
                            existencia,
                            rutaImagen,
                            chkActivo.isSelected()
                    );

            productoCrud.guardar(
                    producto
            );

            actualizarTabla();

            mensaje(
                    Alert.AlertType.INFORMATION,
                    "Producto agregado correctamente."
            );

            limpiar();

        } catch (NumberFormatException e) {

            mensaje(
                    Alert.AlertType.ERROR,
                    "El precio y la existencia deben contener valores numéricos válidos."
            );
        }
    }

    @FXML
    private void filtrarProductos() {

        actualizarTabla();
    }

    private void actualizarTabla() {

        productosMostrados.clear();

        if (chkVerTodos.isSelected()) {

            productosMostrados.addAll(
                    productos
            );

        } else {

            for (Producto producto : productos) {

                if (producto.isActivo()) {

                    productosMostrados.add(
                            producto
                    );
                }
            }
        }
    }

    private void limpiar() {

        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtExistencia.clear();

        cmbCategoria
                .getSelectionModel()
                .clearSelection();

        chkActivo.setSelected(true);

        imgProducto.setImage(null);

        rutaImagen = null;

        txtCodigo.requestFocus();
    }

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