package model.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

public class conexio_db {
    private static Connection conn = null;

    public static void comprobarConexion() {
        try {
            if (conn == null || conn.isClosed()) {
                conectar();
            } else {
                desconectar();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
        }
    }

    private static void conectar() {
        try {
            conn = ConnectionFactory.createConnection();
            System.out.println("LOG: Conexión establecida con " + config.DB_TYPE);
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void desconectar() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("LOG: Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("ERROR: Al cerrar la conexión: " + e.getMessage());
        }
    }

    public static Connection getConn() {
        return conn;
    }
}
