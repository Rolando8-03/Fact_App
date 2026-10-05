package ni.edu.uam.fact_app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public final class SceneManager {
    private static Stage principal;
    private SceneManager() { }
    public static void setPrincipal(Stage stage) { principal = stage; }
    public static void cambiarEscena(String recurso, String titulo) throws IOException {
        Scene escena = cargar(recurso);
        principal.setScene(escena);
        principal.setTitle(titulo);
        principal.sizeToScene();
        principal.centerOnScreen();
    }
    public static void abrirVentana(String recurso, String titulo) throws IOException {
        if (!permitido(recurso)) throw new IOException("Su usuario no tiene permiso para abrir este módulo.");
        Stage stage = new Stage();
        stage.initOwner(principal);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(titulo);
        stage.setScene(cargar(recurso));
        stage.setResizable(false);
        stage.showAndWait();
    }
    private static Scene cargar(String recurso) throws IOException {
        var url = SceneManager.class.getResource(recurso);
        if (url == null) throw new IOException("FXML no encontrado: " + recurso);
        return new Scene(new FXMLLoader(url).load());
    }
    public static boolean permitido(String recurso) {
        if (SesionUsuario.esAdministrador()) return true;
        if (SesionUsuario.esCajero()) return recurso.endsWith("producto-view.fxml") || recurso.endsWith("venta-view.fxml");
        if (SesionUsuario.esBodeguero()) return recurso.endsWith("producto-view.fxml") || recurso.endsWith("categoria-view.fxml");
        return false;
    }
}
