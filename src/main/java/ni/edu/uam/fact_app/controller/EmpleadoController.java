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

public class EmpleadoController {

    @FXML private TextField txtNombres, txtApellidos;
    @FXML private ComboBox<Cargo> cmbCargo;
    @FXML private DatePicker dpFechaContratacion;
    @FXML private CheckBox chkActivo;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableColumn<Empleado, String> colNombres, colApellidos, colEstado;
    @FXML private TableColumn<Empleado, Cargo> colCargo;
    @FXML private TableColumn<Empleado, java.time.LocalDate> colFecha;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbCriterio;
    @FXML private Button btnGuardar;
    @FXML private Label lblEstado;
    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    private final EmpleadoDAO dao = new EmpleadoDAO();
    private final ObservableList<Empleado> datos = FXCollections.observableArrayList();
    private final FilteredList<Empleado> filtrados = new FilteredList<>(datos, p -> true);
    private Integer idEdicion;

    @FXML private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isActivo() ? "Activo" : "Inactivo"));

        tblEmpleados.setItems(filtrados);
        tblEmpleados.setPlaceholder(new Label("No hay registros para mostrar."));
        cmbCriterio.setItems(FXCollections.observableArrayList("Todos", "ID", "Nombre"));
        cmbCriterio.getSelectionModel().selectFirst();

        cmbEstado.setItems(FXCollections.observableArrayList("Todos", "Activos", "Inactivos"));
        cmbEstado.getSelectionModel().selectFirst();
        cmbEstado.valueProperty().addListener((o, a, b) -> filtrar());

        txtBuscar.textProperty().addListener((o, a, b) -> filtrar());
        cmbCriterio.valueProperty().addListener((o, a, b) -> filtrar());
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> { if (b != null) editar(b); });
        limpiar();
        refrescar();
    }

    private void editar(Empleado objeto) {
        idEdicion = objeto.getId();
        lblEstado.setText("Editando registro #" + idEdicion);
        
        txtNombres.setText(objeto.getNombres());
        txtApellidos.setText(objeto.getApellidos());
        cmbCargo.setValue(objeto.getCargo());
        dpFechaContratacion.setValue(objeto.getFechaContratacion());
        chkActivo.setSelected(objeto.isActivo());

        btnGuardar.setText("Actualizar");
    }

    @FXML private void guardar() {
        if (txtNombres.getText().isBlank() || txtApellidos.getText().isBlank() || cmbCargo.getValue() == null || dpFechaContratacion.getValue() == null) {
            Alertas.mostrar(Alert.AlertType.WARNING, "Debe completar todos los campos obligatorios marcados con *."); return;
        }
        try {

            if (dpFechaContratacion.getValue().isAfter(java.time.LocalDate.now())) {
                Alertas.mostrar(Alert.AlertType.WARNING, "La fecha de contratación no puede ser posterior a la fecha actual.");
                return;
            }

            Empleado objeto = new Empleado(idEdicion, txtNombres.getText().trim(), txtApellidos.getText().trim(), cmbCargo.getValue(), dpFechaContratacion.getValue(), chkActivo.isSelected());
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
        tblEmpleados.getSelectionModel().clearSelection();
        txtNombres.clear(); txtApellidos.clear(); cmbCargo.setValue(null); dpFechaContratacion.setValue(null); chkActivo.setSelected(true);
        btnGuardar.setText("Guardar");
        lblEstado.setText("Nuevo registro");
    }

    @FXML private void refrescar() {
        try {
            java.util.List<Empleado> nuevos = dao.listar();
            cmbCargo.setItems(FXCollections.observableArrayList(new CargoDAO().listar()));
            limpiar();
            datos.setAll(nuevos);
            filtrar();
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase(Locale.ROOT);
        String criterio = cmbCriterio.getValue();
        filtrados.setPredicate(objeto -> {

            if ("Activos".equals(cmbEstado.getValue()) && !objeto.isActivo()) return false;
            if ("Inactivos".equals(cmbEstado.getValue()) && objeto.isActivo()) return false;

            if (texto.isEmpty()) return true;
            if ("ID".equals(criterio)) return String.valueOf(objeto.getId()).equals(texto);
            
            if ("Nombre".equals(criterio)) return (objeto.getNombres() + " " + objeto.getApellidos()).toLowerCase(Locale.ROOT).contains(texto);
            return String.valueOf(objeto.getId()).equals(texto) || (objeto.getNombres() + " " + objeto.getApellidos()).toLowerCase(Locale.ROOT).contains(texto);
        });
    }

    @FXML private void restablecerFiltros() {
        txtBuscar.clear();
        cmbCriterio.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().selectFirst();
        filtrar();
    }

    @FXML private void buscar() {
        DialogoBuscar.ResultadoBusqueda resultado = DialogoBuscar.mostrar("empleados");
        if (resultado == null) return;
        cmbCriterio.setValue(resultado.getCriterio() == DialogoBuscar.CriterioBusqueda.ID ? "ID" : "Nombre");
        txtBuscar.setText(resultado.getValor());
    }

    @FXML private void validarFecha() {
        if (dpFechaContratacion.getValue() != null && dpFechaContratacion.getValue().isAfter(java.time.LocalDate.now())) {
            Alertas.mostrar(Alert.AlertType.WARNING, "La fecha de contratación no puede ser posterior a la fecha actual.");
            dpFechaContratacion.setValue(null);
        }
    }

}
