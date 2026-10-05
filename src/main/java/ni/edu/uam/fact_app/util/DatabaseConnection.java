package ni.edu.uam.fact_app.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        Properties config = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
            if (in != null) config.load(in);
            Path externo = Path.of("config", "database.properties");
            if (Files.exists(externo)) {
                try (InputStream archivo = Files.newInputStream(externo)) { config.load(archivo); }
            }
        } catch (IOException e) {
            throw new SQLException("No se pudo leer la configuración de la base de datos.", e);
        }
        Properties datos = new Properties();
        datos.setProperty("user", valor("FACT_DB_USER", config, "db.user"));
        datos.setProperty("password", valor("FACT_DB_PASSWORD", config, "db.password"));
        datos.setProperty("connectTimeout", "5");
        datos.setProperty("socketTimeout", "15");
        return DriverManager.getConnection(valor("FACT_DB_URL", config, "db.url"), datos);
    }

    private static String valor(String entorno, Properties config, String clave) {
        String valor = System.getenv(entorno);
        return valor != null ? valor : config.getProperty(clave, "");
    }
}
