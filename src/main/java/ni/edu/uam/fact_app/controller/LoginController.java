package ni.edu.uam.fact_app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ni.edu.uam.fact_app.model.*;
import ni.edu.uam.fact_app.util.*;
import java.time.LocalDate;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    // Cuentas de práctica del proyecto de referencia; no son usuarios de PostgreSQL.
    @FXML private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();
        String rol;
        if (usuario.equals("admin") && password.equals("admin123")) rol = "Administrador";
        else if (usuario.equals("cajero") && password.equals("cajero123")) rol = "Cajero";
        else if (usuario.equals("bodega") && password.equals("bodega123")) rol = "Bodeguero";
        else { lblError.setText("Usuario o contraseña incorrectos."); return; }
        try (var conexion = DatabaseConnection.getConnection()) {
            Empleado empleado = new Empleado(null, rol, "de práctica", new Cargo(null, rol, rol), LocalDate.now(), true);
            SesionUsuario.setUsuarioActual(new Usuario(usuario, null, empleado));
            try {
                SceneManager.cambiarEscena("/ni/edu/uam/fact_app/fxml/menu-principal.fxml", "Sistema de facturación");
            } catch (IOException e) {
                SesionUsuario.cerrarSesion();
                lblError.setText("No se pudo abrir el menú principal.");
                Alertas.mostrar(Alert.AlertType.ERROR, e.getMessage());
            }
        } catch (SQLException e) { lblError.setText("Revise la configuración de PostgreSQL."); Alertas.errorBD(e); }
    }
}
