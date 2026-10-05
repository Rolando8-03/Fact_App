package ni.edu.uam.fact_app.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ni.edu.uam.fact_app.util.*;
import java.io.IOException;
import java.sql.SQLException;

public class MenuPrincipalController {
    @FXML private Label lblTotalCategorias, lblTotalProductos, lblTotalCargos, lblTotalEmpleados;
    @FXML private Label lblUsuarioActual, lblRolActual, lblEstadoConexion;
    @FXML private Button btnCategorias, btnProductos, btnCargos, btnEmpleados, btnVentas;
    @FXML private MenuItem mnuCategorias, mnuProductos, mnuCargos, mnuEmpleados, mnuVentas;

    @FXML private void initialize() {
        var usuario = SesionUsuario.getUsuarioActual();
        if (usuario != null) {
            lblUsuarioActual.setText(usuario.getUsername());
            lblRolActual.setText(usuario.getEmpleado().getCargo().getNombre());
        }
        permiso(btnCategorias, mnuCategorias, "categoria");
        permiso(btnProductos, mnuProductos, "producto");
        permiso(btnCargos, mnuCargos, "cargo");
        permiso(btnEmpleados, mnuEmpleados, "empleado");
        permiso(btnVentas, mnuVentas, "venta");
        actualizarResumen();
    }
    private void permiso(Button boton, MenuItem menu, String modulo) {
        boolean permitido = SceneManager.permitido(modulo + "-view.fxml");
        boton.setDisable(!permitido); menu.setDisable(!permitido);
    }
    @FXML private void abrirCategorias() { abrir("categoria", "Gestión de categorías"); }
    @FXML private void abrirProductos() { abrir("producto", "Gestión de productos"); }
    @FXML private void abrirCargos() { abrir("cargo", "Gestión de cargos"); }
    @FXML private void abrirEmpleados() { abrir("empleado", "Gestión de empleados"); }
    @FXML private void abrirVentas() { abrir("venta", "Registro de ventas"); }
    private void abrir(String modulo, String titulo) {
        try {
            SceneManager.abrirVentana("/ni/edu/uam/fact_app/fxml/" + modulo + "-view.fxml", titulo);
            actualizarResumen();
        } catch (IOException e) { Alertas.mostrar(Alert.AlertType.ERROR, "No se pudo abrir el módulo: " + e.getMessage()); }
    }
    @FXML private void actualizarResumen() {
        String sql = "SELECT (SELECT count(*) FROM categoria), (SELECT count(*) FROM producto), (SELECT count(*) FROM cargo), (SELECT count(*) FROM empleado)";
        try (var conexion = DatabaseConnection.getConnection(); var ps = conexion.prepareStatement(sql); var rs = ps.executeQuery()) {
            if (rs.next()) {
                lblTotalCategorias.setText(rs.getString(1)); lblTotalProductos.setText(rs.getString(2));
                lblTotalCargos.setText(rs.getString(3)); lblTotalEmpleados.setText(rs.getString(4));
            }
            lblEstadoConexion.setText("Conectado a PostgreSQL");
        } catch (SQLException e) {
            lblTotalCategorias.setText("—"); lblTotalProductos.setText("—"); lblTotalCargos.setText("—"); lblTotalEmpleados.setText("—");
            lblEstadoConexion.setText("No se pudo consultar la base de datos");
            Alertas.errorBD(e);
        }
    }
    @FXML private void cerrarSesion() {
        if (!Alertas.confirmar("¿Desea cerrar la sesión?")) return;
        try {
            SceneManager.cambiarEscena("/ni/edu/uam/fact_app/fxml/login-view.fxml", "Iniciar sesión");
            SesionUsuario.cerrarSesion();
        } catch (IOException e) { Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage()); }
    }
    @FXML private void salir() { if (Alertas.confirmar("¿Desea cerrar la aplicación?")) Platform.exit(); }
}
