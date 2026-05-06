package model.persintencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        switch (config.DB_TYPE) {
            case "MYSQL":
                Class.forName(config.DRIVER);
                return DriverManager.getConnection(config.URL, config.USER, config.PASS);
            default:
                throw new IllegalArgumentException("Tecnología no soportada: " + config.DB_TYPE);
        }
    }
}
