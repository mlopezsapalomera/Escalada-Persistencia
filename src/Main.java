import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.persintencia.conexio_db;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando Aplicación Pillam Ltd. Co. ---");

        // 1. Llamamos a comprobarConexion para CONECTAR
        conexio_db.comprobarConexion();

        Connection connection = conexio_db.getConn();

        if (connection != null) {
            try {
                // 2. Usamos MetaData para listar las tablas
                DatabaseMetaData metaData = connection.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = metaData.getTables("db_escalada", null, "%", types);

                System.out.println("\nTablas encontradas en la base de datos:");
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    System.out.println("  - " + tableName);
                }
                rs.close();

            } catch (SQLException e) {
                System.err.println("Error al consultar las tablas: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // 3. Llamamos a comprobarConexion para DESCONECTAR
                conexio_db.comprobarConexion();
            }
        } else {
            System.err.println("No se pudo realizar la consulta porque no hay conexión.");
        }
    }
}
