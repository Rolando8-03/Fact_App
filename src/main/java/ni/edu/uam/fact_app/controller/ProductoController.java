package ni.edu.uam.fact_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.dao.*;
import ni.edu.uam.fact_app.util.*;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;

public class ProductoController {

    @FXML private TextField txtCodigo, txtNombre, txtPrecio, txtExistencia;
    @FXML private ComboBox<Categoria> cmbCategoria, cmbFiltroCategoria;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private CheckBox chkActivo;
    @FXML private javafx.scene.image.ImageView imgProducto;
    @FXML private TableColumn<Producto, String> colCodigo, colNombre, colEstado;
    @FXML private TableColumn<Producto, Categoria> colCategoria;
    @FXML private TableColumn<Producto, java.math.BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colExistencia;
    private String rutaImagen;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbCriterio;
    @FXML private Button btnGuardar;
    @FXML private Label lblEstado;
    @FXML private TableView<Producto> tblProductos;
    @FXML private TableColumn<Producto, Integer> colId;
    private final ProductoDAO dao = new ProductoDAO();
    private final ObservableList<Producto> datos = FXCollections.observableArrayList();
    private final FilteredList<Producto> filtrados = new FilteredList<>(datos, p -> true);
    private Integer idEdicion;
    private Control campoError;

    @FXML private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isActivo() ? "Activo" : "Inactivo"));
        cmbFiltroCategoria.valueProperty().addListener((o, a, b) -> filtrar());

        tblProductos.setItems(filtrados);
        tblProductos.setPlaceholder(new Label("No hay registros para mostrar."));
        cmbCriterio.setItems(FXCollections.observableArrayList("Todos", "ID", "Nombre", "Código"));
        cmbCriterio.getSelectionModel().selectFirst();

        cmbEstado.setItems(FXCollections.observableArrayList("Todos", "Activos", "Inactivos"));
        cmbEstado.getSelectionModel().selectFirst();
        cmbEstado.valueProperty().addListener((o, a, b) -> filtrar());

        txtBuscar.textProperty().addListener((o, a, b) -> filtrar());
        cmbCriterio.valueProperty().addListener((o, a, b) -> filtrar());
        tblProductos.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> { if (b != null) editar(b); });
        limpiar();
        refrescar();
    }

    private void editar(Producto objeto) {
        idEdicion = objeto.getId();
        lblEstado.setText("Editando registro #" + idEdicion);
        
        txtCodigo.setText(objeto.getCodigo()); txtNombre.setText(objeto.getNombre());
        txtPrecio.setText(objeto.getPrecioVenta().toPlainString()); txtExistencia.setText(String.valueOf(objeto.getExistencia()));
        cmbCategoria.setValue(objeto.getCategoria()); chkActivo.setSelected(objeto.isActivo());
        rutaImagen = objeto.getRutaImagen(); mostrarImagen();

        btnGuardar.setDisable(true);
    }


    @FXML private void guardar() {
        if (idEdicion != null) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Para modificar el registro seleccionado utilice Actualizar. Para agregar otro, pulse Nuevo.");
            return;
        }
        guardarCambios(false);
    }

    @FXML private void actualizar() {
        if (!validarSeleccion("actualizar")) return;
        guardarCambios(true);
    }

    private boolean validarSeleccion(String operacion) {
        Producto seleccion = tblProductos.getSelectionModel().getSelectedItem();
        if (seleccion == null || seleccion.getId() == null || !Objects.equals(seleccion.getId(), idEdicion)) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Debe seleccionar el producto que desea " + operacion + ".");
            tblProductos.requestFocus();
            return false;
        }
        return true;
    }

    private void guardarCambios(boolean actualizar) {
        campoError = null;
        try {
            Producto objeto = obtenerProductoFormulario();
            if (dao.existeCodigo(objeto.getCodigo(), actualizar ? objeto.getId() : null)) {
                campoError = txtCodigo;
                throw new IllegalArgumentException("Ya existe un producto con ese código.");
            }
            if (actualizar) dao.actualizar(objeto); else dao.guardar(objeto);
            limpiar();
            boolean cargado = cargarDatos();
            lblEstado.setText("Registro #" + objeto.getId() + (actualizar ? " actualizado." : " guardado.")
                    + (cargado ? "" : " No se pudo refrescar la tabla; pulse Refrescar."));
        } catch (IllegalArgumentException e) {
            Alertas.mostrar(Alert.AlertType.WARNING, e.getMessage());
            if (campoError != null) campoError.requestFocus();
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    private Producto obtenerProductoFormulario() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        campoError = txtCodigo;
        if (codigo.isEmpty()) throw new IllegalArgumentException("El código del producto es obligatorio.");
        if (codigo.length() > 50) throw new IllegalArgumentException("El código admite hasta 50 caracteres.");
        campoError = txtNombre;
        if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (nombre.length() > 150) throw new IllegalArgumentException("El nombre del producto admite hasta 150 caracteres.");
        campoError = cmbCategoria;
        Categoria categoria = cmbCategoria.getValue();
        if (categoria == null || categoria.getId() == null)
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        if (!categoria.isActiva() && (idEdicion == null || datos.stream().noneMatch(p ->
                Objects.equals(p.getId(), idEdicion) && Objects.equals(p.getCategoria().getId(), categoria.getId()))))
            throw new IllegalArgumentException("Seleccione una categoría activa para un producto nuevo o un cambio de categoría.");

        campoError = txtPrecio;
        java.math.BigDecimal precio;
        try {
            precio = new java.math.BigDecimal(txtPrecio.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El precio debe ser un valor numérico.", e);
        }
        if (precio.signum() <= 0) throw new IllegalArgumentException("El precio de venta debe ser mayor que cero.");
        if (precio.compareTo(new java.math.BigDecimal("9999999999.99")) > 0)
            throw new IllegalArgumentException("El precio no puede superar 9,999,999,999.99.");
        try {
            precio = precio.setScale(2, java.math.RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("El precio admite un máximo de dos decimales.", e);
        }

        campoError = txtExistencia;
        int existencia;
        try {
            existencia = Integer.parseInt(txtExistencia.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La existencia debe ser un número entero entre 0 y 2147483647.", e);
        }
        if (existencia < 0) throw new IllegalArgumentException("La existencia no puede ser negativa.");
        campoError = null;
        return new Producto(idEdicion, codigo, nombre, categoria, precio, existencia, rutaImagen, chkActivo.isSelected());
    }

    @FXML private void eliminar() {
        if (!validarSeleccion("eliminar")) return;
        if (!Alertas.confirmar("¿Desea eliminar el producto seleccionado?")) return;
        try {
            if (dao.tieneVentas(idEdicion)) {
                Alertas.mostrar(Alert.AlertType.WARNING, "No puede eliminar el producto porque está asociado a ventas. Puede desactivarlo.");
                return;
            }
            dao.eliminar(idEdicion);
            limpiar();
            boolean cargado = cargarDatos();
            lblEstado.setText("Registro eliminado." + (cargado ? "" : " No se pudo refrescar la tabla; pulse Refrescar."));
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    @FXML private void limpiar() {
        idEdicion = null;
        tblProductos.getSelectionModel().clearSelection();
        txtCodigo.clear(); txtNombre.clear(); txtPrecio.clear(); txtExistencia.clear(); cmbCategoria.setValue(null); chkActivo.setSelected(true); imgProducto.setImage(null); rutaImagen = null;
        btnGuardar.setDisable(false);
        btnGuardar.setText("Guardar");
        lblEstado.setText("Nuevo registro");
    }

    @FXML private void refrescar() { cargarDatos(); }

    private boolean cargarDatos() {
        try {
            java.util.List<Producto> nuevos = dao.listar();
            
            java.util.List<Categoria> categorias = new CategoriaDAO().listar();
            cmbCategoria.setItems(FXCollections.observableArrayList(categorias));
            Categoria filtroAnterior = cmbFiltroCategoria.getValue();
            ObservableList<Categoria> opciones = FXCollections.observableArrayList(categorias);
            opciones.add(0, new Categoria(null, "Todas las categorías", true));
            cmbFiltroCategoria.setItems(opciones);
            cmbFiltroCategoria.getSelectionModel().selectFirst();
            if (filtroAnterior != null) for (Categoria c : opciones) {
                if (Objects.equals(c.getId(), filtroAnterior.getId())) cmbFiltroCategoria.setValue(c);
            }

            limpiar();
            datos.setAll(nuevos);
            filtrar();
            return true;
        } catch (SQLException e) { Alertas.errorBD(e); return false; }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        String criterio = cmbCriterio.getValue();
        filtrados.setPredicate(objeto -> {

            if ("Activos".equals(cmbEstado.getValue()) && !objeto.isActivo()) return false;
            if ("Inactivos".equals(cmbEstado.getValue()) && objeto.isActivo()) return false;

            Categoria categoria = cmbFiltroCategoria.getValue();
            if (categoria != null && categoria.getId() != null && !Objects.equals(categoria.getId(), objeto.getCategoria().getId())) return false;

            if (texto.isEmpty()) return true;
            if ("ID".equals(criterio)) return String.valueOf(objeto.getId()).equals(texto);
            if ("Código".equals(criterio)) return objeto.getCodigo().toLowerCase(Locale.ROOT).contains(texto);
            if ("Nombre".equals(criterio)) return (objeto.getNombre()).toLowerCase(Locale.ROOT).contains(texto);
            return String.valueOf(objeto.getId()).equals(texto) || (objeto.getNombre() + " " + objeto.getCodigo()).toLowerCase(Locale.ROOT).contains(texto);
        });
    }

    @FXML private void restablecerFiltros() {
        txtBuscar.clear();
        cmbCriterio.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        cmbFiltroCategoria.getSelectionModel().selectFirst();
        filtrar();
    }

    @FXML private void buscar() {
        DialogoBuscar.ResultadoBusqueda resultado = DialogoBuscar.mostrar("productos");
        if (resultado == null) return;
        cmbCriterio.setValue(resultado.getCriterio() == DialogoBuscar.CriterioBusqueda.ID ? "ID" : "Nombre");
        txtBuscar.setText(resultado.getValor());
    }

    @FXML private void seleccionarImagen() {
        javafx.stage.FileChooser selector = new javafx.stage.FileChooser();
        selector.setTitle("Seleccionar imagen del producto");
        selector.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        java.io.File archivo = selector.showOpenDialog(txtCodigo.getScene().getWindow());
        if (archivo == null) return;
        try {
            javafx.scene.image.Image imagen = new javafx.scene.image.Image(archivo.toURI().toString());
            if (imagen.isError()) throw new java.io.IOException("La imagen no es válida.");
            java.nio.file.Path carpeta = java.nio.file.Path.of("data", "imagenes");
            java.nio.file.Files.createDirectories(carpeta);
            String extension = archivo.getName().substring(archivo.getName().lastIndexOf('.')).toLowerCase(Locale.ROOT);
            java.nio.file.Path destino = carpeta.resolve(java.util.UUID.randomUUID() + extension);
            java.nio.file.Files.copy(archivo.toPath(), destino);
            rutaImagen = destino.toString().replace('\\', '/');
            imgProducto.setImage(imagen);
        } catch (Exception e) { Alertas.mostrar(Alert.AlertType.ERROR, "No se pudo cargar la imagen: " + e.getMessage()); }
    }
    private void mostrarImagen() {
        imgProducto.setImage(null);
        if (rutaImagen == null || rutaImagen.isBlank()) return;
        try {
            String url = rutaImagen.startsWith("file:") ? rutaImagen : java.nio.file.Path.of(rutaImagen).toUri().toString();
            javafx.scene.image.Image imagen = new javafx.scene.image.Image(url);
            if (!imagen.isError()) imgProducto.setImage(imagen);
        } catch (IllegalArgumentException e) { lblEstado.setText("La imagen guardada no está disponible en este equipo."); }
    }

}
