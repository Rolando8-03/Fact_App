package ni.edu.uam.fact_app.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import java.sql.SQLException;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class AutoRefresco {
    private AutoRefresco() { }
    public interface Consulta<T> { List<T> listar() throws SQLException; }

    public static <T> void configurar(TableView<T> tabla, BooleanSupplier sinEdicion,
                                     Consulta<T> consulta, Consumer<List<T>> aplicar) {
        boolean[] consultando = {false};
        Timeline reloj = new Timeline(new KeyFrame(Duration.seconds(2.5), event -> {
            if (consultando[0] || !sinEdicion.getAsBoolean()) return;
            consultando[0] = true;
            Thread hilo = new Thread(() -> {
                try {
                    List<T> nuevos = consulta.listar();
                    Platform.runLater(() -> {
                        consultando[0] = false;
                        if (tabla.getScene() != null && tabla.getScene().getWindow().isShowing()
                                && sinEdicion.getAsBoolean()) aplicar.accept(nuevos);
                    });
                } catch (SQLException e) {
                    // El botón Refrescar permite consultar el error sin alertas repetidas del temporizador.
                    Platform.runLater(() -> consultando[0] = false);
                }
            });
            hilo.setDaemon(true);
            hilo.start();
        }));
        reloj.setCycleCount(Timeline.INDEFINITE);
        tabla.sceneProperty().addListener((o, anterior, escena) -> {
            if (escena == null) { reloj.stop(); return; }
            escena.windowProperty().addListener((w, antes, ventana) -> {
                if (ventana == null) { reloj.stop(); return; }
                ventana.showingProperty().addListener((s, estabaVisible, visible) -> {
                    if (visible) reloj.play(); else reloj.stop();
                });
            });
        });
    }
}
