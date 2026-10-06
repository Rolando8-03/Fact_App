package ni.edu.uam.fact_app.util;

import java.sql.SQLException;

// Mensajes de negocio redactados por la aplicación, sin detalles del servidor.
public class OperacionException extends SQLException {
    public OperacionException(String mensaje) { super(mensaje); }
}
