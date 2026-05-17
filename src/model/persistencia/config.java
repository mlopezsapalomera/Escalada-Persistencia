/*
* Aqui solo guardamos las constantes, url, usuario, contraseña
* De esta forma, si cambiamos de base de datos, solo hemos de modificar esto
* */

package model.persistencia;

public class config {
    // Cambiando esta variable, el pryecto sabra que driver y que URL usar
    public static final String DB_TYPE = "MYSQL";
    // Configuracion para MySQL (XAMPP)
    public static final String URL = "jdbc:mysql://localhost:3306/db_escalada";
    public static final String USER = "root";
    public static final String PASS = ""; //Ninguna por que en xampp por defecto no es ninguna
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    // Opcional: ruta absoluta/local al JAR del driver MySQL si no está en el classpath
    public static final String DRIVER_JAR = ""; // ejemplo: C:/drivers/mysql-connector-java-9.7.0.jar

    // Ejemplo para implementacion (Postgres)

}


