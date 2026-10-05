package ni.edu.uam.fact_app.application;

import javafx.application.Application;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.util.Alertas;
import ni.edu.uam.fact_app.util.SceneManager;

public class FacturacionApplication extends Application {
    @Override public void start(Stage stage) throws Exception {
        SceneManager.setPrincipal(stage);
        SceneManager.cambiarEscena("/ni/edu/uam/fact_app/fxml/login-view.fxml", "Sistema de facturación - Iniciar sesión");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            if (!Alertas.confirmar("¿Desea cerrar la aplicación?")) event.consume();
        });
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
