package model.persistencia;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gestor estático de conexión JDBC.
 * Reutiliza una única conexión y la crea bajo demanda.
 */
public class conexio_db {
    /** Conexión activa reutilizable del proceso. */
    private static Connection conn = null;

    /**
     * Comprueba el estado de la conexión y la abre si no existe o está cerrada.
     */
    public static void comprobarConexion() {
        try {
            if (conn == null || conn.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
        }
    }

    /**
     * Inicializa la conexión usando {@link ConnectionFactory}.
     */
    private static void conectar() {
        try {
            conn = ConnectionFactory.crearConnexio();
            System.out.println("LOG: Conexión establecida con " + config.DB_TYPE);
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cierra la conexión si está abierta.
     */
    public static void desconectar() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("LOG: Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Devuelve la conexión activa, asegurando antes su disponibilidad.
     * @return conexión JDBC lista para usar.
     */
    public static Connection getConn() {
        comprobarConexion();
        return conn;
    }
}
