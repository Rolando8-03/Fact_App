package ni.edu.uam.fact_app.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import java.sql.SQLException;

public final class Alertas {
    private Alertas() { }
    public static void mostrar(Alert.AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo, texto, new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE));
        alerta.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : tipo == Alert.AlertType.WARNING ? "Advertencia" : "Información");
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
    public static boolean confirmar(String texto) {
        ButtonType si = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, texto, si, no);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        return alerta.showAndWait().orElse(no) == si;
    }
    public static void errorBD(SQLException e) {
        String estado = e.getSQLState();
        String mensaje;
        if ("23503".equals(estado)) mensaje = "El registro está relacionado con otros datos. Revise la categoría o el cargo; si ya fue utilizado, desactívelo en lugar de eliminarlo.";
        else if ("23505".equals(estado)) mensaje = "Ya existe un registro con ese nombre o código.";
        else if ("23514".equals(estado) || "22003".equals(estado)) mensaje = "Revise los valores: precio positivo, existencia no negativa y cantidades dentro del rango permitido.";
        else if ("22001".equals(estado)) mensaje = "Uno de los textos supera la longitud permitida.";
        else if (estado != null && (estado.startsWith("08") || estado.startsWith("28") || estado.equals("3D000")))
            mensaje = "No se pudo conectar a PostgreSQL. Revise el servicio, la base fact_app y config/database.properties.";
        else mensaje = "No se completó la operación: " + e.getMessage();
        mostrar(Alert.AlertType.ERROR, mensaje);
    }
}
