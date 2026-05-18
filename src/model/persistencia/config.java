package model.persistencia;

/**
 * Configuración centralizada de persistencia.
 * Agrupa constantes de conexión para facilitar cambios de entorno o motor.
 */
public class config {
    /** Motor de base de datos activo. */
    public static final String DB_TYPE = "MYSQL";

    /** URL JDBC de la base de datos. */
    public static final String URL = "jdbc:mysql://localhost:3306/db_escalada";
    /** Usuario de conexión. */
    public static final String USER = "root";
    /** Contraseña de conexión (vacía por defecto en XAMPP). */
    public static final String PASS = "";
    /** Clase del driver JDBC. */
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    /**
     * Ruta opcional al JAR del driver MySQL si no está en el classpath.
     * También puede usarse la variable de entorno MYSQL_CONNECTOR_JAR.
     */
    public static final String DRIVER_JAR = ".\\connectorMysql\\mysql-connector-j-9.7.0.jar";

}


