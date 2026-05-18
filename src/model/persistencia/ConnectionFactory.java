package model.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.lang.reflect.InvocationTargetException;

/**
 * Fábrica de conexiones JDBC.
 * Implementa carga estándar del driver y un fallback dinámico desde JAR.
 */
public class ConnectionFactory {
    /**
     * Crea una conexión según el motor configurado en {@link config}.
     * @return conexión JDBC abierta.
     * @throws SQLException si falla la conexión.
     * @throws ClassNotFoundException si no se puede cargar el driver.
     */
    public static Connection crearConnexio() throws SQLException, ClassNotFoundException {
        switch (config.DB_TYPE) {
            case "MYSQL":
                try {
                    Class.forName(config.DRIVER);
                } catch (ClassNotFoundException e) {
                    // Fallback: intentar cargar el conector desde un JAR externo.
                    try {
                        String cfgPath = model.persistencia.config.DRIVER_JAR;
                        String envPath = System.getenv("MYSQL_CONNECTOR_JAR");
                        File jar = null;
                        if (cfgPath != null && !cfgPath.isEmpty()) jar = new File(cfgPath);
                        else if (envPath != null && !envPath.isEmpty()) jar = new File(envPath);
                        if (jar == null || !jar.exists()) {
                            // Intentar localizar un JAR mysql-connector en rutas comunes.
                            File found = trobarConnectorJar();
                            if (found != null) jar = found;
                        }
                        if (jar != null && jar.exists()) {
                            URL url = jar.toURI().toURL();
                            URLClassLoader ucl = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
                            Class<?> drvCls = Class.forName(config.DRIVER, true, ucl);
                            try {
                                Driver drvInstance = (Driver) drvCls.getDeclaredConstructor().newInstance();
                                DriverManager.registerDriver(new DriverShim(drvInstance));
                            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | SQLException ex) {
                                throw new ClassNotFoundException("Driver loaded but registration failed: " + ex.getMessage(), ex);
                            }
                        } else {
                            throw e; // rethrow original
                        }
                    } catch (ClassNotFoundException | java.net.MalformedURLException ex) {
                        throw new ClassNotFoundException("Driver not found and fallback loading failed: " + ex.getMessage(), ex);
                    }
                }
                return DriverManager.getConnection(config.URL, config.USER, config.PASS);
            default:
                throw new IllegalArgumentException("Tecnología no soportada: " + config.DB_TYPE);
        }
    }

    /**
     * Intenta localizar un JAR de mysql connector en ubicaciones comunes.
     * @return archivo encontrado o null si no hay coincidencias.
     */
    private static File trobarConnectorJar() {
        try {
            File cwd = new File(System.getProperty("user.dir"));
            File cur = cwd;
            while (cur != null) {
                File[] matches = cur.listFiles((dir, name) -> name.toLowerCase().startsWith("mysql-connector") && name.toLowerCase().endsWith(".jar"));
                if (matches != null && matches.length > 0) return matches[0];
                File lib = new File(cur, "lib");
                if (lib.exists()) {
                    File[] m2 = lib.listFiles((dir, name) -> name.toLowerCase().startsWith("mysql-connector") && name.toLowerCase().endsWith(".jar"));
                    if (m2 != null && m2.length > 0) return m2[0];
                }
                cur = cur.getParentFile();
            }
            File home = new File(System.getProperty("user.home"));
            if (home.exists()) {
                File[] hmatches = home.listFiles((dir, name) -> name.toLowerCase().startsWith("mysql-connector") && name.toLowerCase().endsWith(".jar"));
                if (hmatches != null && hmatches.length > 0) return hmatches[0];
            }
        } catch (Exception ignored) {}
        return null;
    }
}

/**
 * Adaptador para registrar en {@link DriverManager} drivers cargados con un classloader externo.
 */
class DriverShim implements Driver {
    private final Driver delegate;

    /**
     * Crea un adaptador para el driver indicado.
     * @param d driver delegado real.
     */
    DriverShim(Driver d) {
        this.delegate = d;
    }

    public boolean acceptsURL(String u) throws SQLException { return delegate.acceptsURL(u); }
    public java.sql.Connection connect(String u, java.util.Properties p) throws SQLException { return delegate.connect(u, p); }
    public int getMajorVersion() { return delegate.getMajorVersion(); }
    public int getMinorVersion() { return delegate.getMinorVersion(); }
    public java.sql.DriverPropertyInfo[] getPropertyInfo(String u, java.util.Properties p) throws SQLException { return delegate.getPropertyInfo(u, p); }
    public boolean jdbcCompliant() { return delegate.jdbcCompliant(); }
    public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException { return delegate.getParentLogger(); }
}
