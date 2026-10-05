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

public class CategoriaController {

    @FXML private TextField txtNombre;
    @FXML private CheckBox chkActiva;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableColumn<Categoria, String> colNombre, colEstado;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbCriterio;
    @FXML private Button btnGuardar;
    @FXML private Label lblEstado;
    @FXML private TableView<Categoria> tblCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    private final CategoriaDAO dao = new CategoriaDAO();
    private final ObservableList<Categoria> datos = FXCollections.observableArrayList();
    private final FilteredList<Categoria> filtrados = new FilteredList<>(datos, p -> true);
    private Integer idEdicion;

    @FXML private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isActiva() ? "Activa" : "Inactiva"));

        tblCategorias.setItems(filtrados);
        tblCategorias.setPlaceholder(new Label("No hay registros para mostrar."));
        cmbCriterio.setItems(FXCollections.observableArrayList("Todos", "ID", "Nombre"));
        cmbCriterio.getSelectionModel().selectFirst();

        cmbEstado.setItems(FXCollections.observableArrayList("Todos", "Activos", "Inactivos"));
        cmbEstado.getSelectionModel().selectFirst();
        cmbEstado.valueProperty().addListener((o, a, b) -> filtrar());

        txtBuscar.textProperty().addListener((o, a, b) -> filtrar());
        cmbCriterio.valueProperty().addListener((o, a, b) -> filtrar());
        tblCategorias.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> { if (b != null) editar(b); });
        limpiar();
        refrescar();
        AutoRefresco.configurar(tblCategorias,
                () -> idEdicion == null && txtNombre.getText().isBlank(),
                dao::listar, nuevos -> { datos.setAll(nuevos); filtrar(); });
    }

    private void editar(Categoria objeto) {
        idEdicion = objeto.getId();
        lblEstado.setText("Editando registro #" + idEdicion);
        txtNombre.setText(objeto.getNombre()); chkActiva.setSelected(objeto.isActiva());
        btnGuardar.setText("Actualizar");
    }

    @FXML private void guardar() {
        if (txtNombre.getText().isBlank()) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Debe completar todos los campos obligatorios marcados con *."); return;
        }
        try {

            Categoria objeto = new Categoria(idEdicion, txtNombre.getText().trim(), chkActiva.isSelected());
            if (idEdicion == null) dao.guardar(objeto); else dao.actualizar(objeto);
            limpiar();
            refrescar();
            lblEstado.setText("Registro #" + objeto.getId() + " guardado correctamente.");
        } catch (NumberFormatException e) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Revise los valores numéricos del formulario.");
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    @FXML private void eliminar() {
        if (idEdicion == null) { Alertas.mostrar(Alert.AlertType.WARNING, "Seleccione un registro de la tabla."); return; }
        if (!Alertas.confirmar("¿Desea eliminar el registro seleccionado?")) return;
        try {
            dao.eliminar(idEdicion);
            limpiar();
            refrescar();
            lblEstado.setText("Registro eliminado correctamente.");
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    @FXML private void limpiar() {
        idEdicion = null;
        tblCategorias.getSelectionModel().clearSelection();
        txtNombre.clear(); chkActiva.setSelected(true);
        btnGuardar.setText("Guardar");
        lblEstado.setText("Nuevo registro");
    }

    @FXML private void refrescar() {
        try {
            java.util.List<Categoria> nuevos = dao.listar();
            
            limpiar();
            datos.setAll(nuevos);
            filtrar();
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        String criterio = cmbCriterio.getValue();
        filtrados.setPredicate(objeto -> {

            if ("Activos".equals(cmbEstado.getValue()) && !objeto.isActiva()) return false;
            if ("Inactivos".equals(cmbEstado.getValue()) && objeto.isActiva()) return false;

            if (texto.isEmpty()) return true;
            if ("ID".equals(criterio)) return String.valueOf(objeto.getId()).equals(texto);
            
            if ("Nombre".equals(criterio)) return (objeto.getNombre()).toLowerCase(Locale.ROOT).contains(texto);
            return String.valueOf(objeto.getId()).equals(texto) || (objeto.getNombre()).toLowerCase(Locale.ROOT).contains(texto);
        });
    }

    @FXML private void restablecerFiltros() {
        txtBuscar.clear();
        cmbCriterio.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        filtrar();
    }

    @FXML private void buscar() {
        DialogoBuscar.ResultadoBusqueda resultado = DialogoBuscar.mostrar("categorias");
        if (resultado == null) return;
        cmbCriterio.setValue(resultado.getCriterio() == DialogoBuscar.CriterioBusqueda.ID ? "ID" : "Nombre");
        txtBuscar.setText(resultado.getValor());
    }

}
