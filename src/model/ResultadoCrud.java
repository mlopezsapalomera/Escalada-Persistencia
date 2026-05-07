package model;

/**
 * Clase de transferencia para respuestas estandarizadas de operaciones CRUD.
 *
 * Uso:
 * - El controlador siempre retorna un ResultadoCrud
 * - La vista examina exito y mensaje
 * - Los datos (si aplican) están disponibles en datos
 */
public class ResultadoCrud {

    private boolean exito;
    private String mensaje;
    private Object datos;

    // Constructores
    public ResultadoCrud(boolean exito, String mensaje) {
        this(exito, mensaje, null);
    }

    public ResultadoCrud(boolean exito, String mensaje, Object datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    // Factory methods para facilitar creación
    public static ResultadoCrud exitoso(String mensaje) {
        return new ResultadoCrud(true, mensaje);
    }

    public static ResultadoCrud exitoso(String mensaje, Object datos) {
        return new ResultadoCrud(true, mensaje, datos);
    }

    public static ResultadoCrud error(String mensaje) {
        return new ResultadoCrud(false, mensaje);
    }

    // Getters
    public boolean esExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Object getDatos() {
        return datos;
    }
}
