package controller;

import model.entidades.Escola;
import model.ResultadoCrud;
import model.persistencia.conexio_db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EscolaController {

    private static final String SQL_INSERTAR = "INSERT INTO escoles (nom, poblacio, aproximacio, num_vies, popularitat) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE escoles SET nom=?, poblacio=?, aproximacio=?, num_vies=?, popularitat=? WHERE id=?";
    private static final String SQL_POR_ID = "SELECT * FROM escoles WHERE id=?";
    private static final String SQL_TODAS = "SELECT * FROM escoles";
    private static final String SQL_ELIMINAR = "DELETE FROM escoles WHERE id=?";

    // ================================================
    // API DE NIVEL ALTO: Validacion + Transformacion
    // ================================================

    /**
     * Valida y crea una escuela a partir de datos del formulario.
     * 
     * @param datos mapa con claves: nombre, poblacion, aproximacion, numeroVias, popularidad
     * @return ResultadoCrud con exito o error
     */
    public ResultadoCrud validarYCrearEscuela(Map<String, String> datos) {
        // Validar campos
        String validacion = validarCamposEscuela(datos, false);
        if (validacion != null) {
            return ResultadoCrud.error(validacion);
        }

        // Transformar y crear
        try {
            Escola escuela = construirEscuelaDelFormulario(datos, false);
            boolean creada = crearEscuela(escuela);
            
            if (creada) {
                return ResultadoCrud.exitoso("Escuela creada correctamente.");
            } else {
                return ResultadoCrud.error("No se pudo crear la escuela en la base de datos.");
            }
        } catch (Exception e) {
            return ResultadoCrud.error("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Valida y modifica una escuela a partir de datos del formulario.
     * 
     * @param datos mapa con claves: id, nombre, poblacion, aproximacion, numeroVias, popularidad
     * @return ResultadoCrud con exito o error
     */
    public ResultadoCrud validarYModificarEscuela(Map<String, String> datos) {
        // Validar campos
        String validacion = validarCamposEscuela(datos, true);
        if (validacion != null) {
            return ResultadoCrud.error(validacion);
        }

        // Transformar y modificar
        try {
            Escola escuela = construirEscuelaDelFormulario(datos, true);
            boolean modificada = modificarEscuela(escuela);
            
            if (modificada) {
                return ResultadoCrud.exitoso("Escuela modificada correctamente.");
            } else {
                return ResultadoCrud.error("No se pudo modificar la escuela en la base de datos.");
            }
        } catch (Exception e) {
            return ResultadoCrud.error("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Busca y retorna una escuela para edicion.
     * 
     * @param idTexto ID como string (desde formulario)
     * @return ResultadoCrud con exito + Escuela, o error
     */
    public ResultadoCrud obtenerEscuelaParaEdicion(String idTexto) {
        Integer id = parsearId(idTexto);
        if (id == null) {
            return ResultadoCrud.error("El ID debe ser un numero entero.");
        }

        Escola escuela = buscarEscuelaPorId(id);
        if (escuela == null) {
            return ResultadoCrud.error("No existe escuela con ID " + id + ".");
        }

        return ResultadoCrud.exitoso("Escuela encontrada.", escuela);
    }

    /**
     * Valida y elimina una escuela.
     * 
     * @param idTexto ID como string
     * @return ResultadoCrud con exito o error
     */
    public ResultadoCrud validarYEliminarEscuela(String idTexto) {
        Integer id = parsearId(idTexto);
        if (id == null) {
            return ResultadoCrud.error("El ID debe ser un numero entero.");
        }

        boolean eliminada = eliminarEscuela(id);
        if (eliminada) {
            return ResultadoCrud.exitoso("Escuela eliminada correctamente.");
        } else {
            return ResultadoCrud.error("No se pudo eliminar la escuela.");
        }
    }

    // =============================================
    // HELPERS PRIVADOS: Validacion y Transformacion
    // =============================================

    /**
     * Valida los campos basicos de una escuela.
     * 
     * @param datos mapa de campos
     * @param incluirId si se espera campo "id"
     * @return mensaje de error, o null si todo es valido
     */
    private String validarCamposEscuela(Map<String, String> datos, boolean incluirId) {
        String nombre = obtenerYLimpiar(datos, "nombre");
        String poblacion = obtenerYLimpiar(datos, "poblacion");
        String aproximacion = obtenerYLimpiar(datos, "aproximacion");
        String numeroViasTexto = obtenerYLimpiar(datos, "numeroVias");

        if (nombre.isEmpty()) {
            return "El nombre no puede estar vacio.";
        }
        if (nombre.length() > 100) {
            return "El nombre no puede superar 100 caracteres.";
        }
        if (poblacion.isEmpty()) {
            return "La poblacion no puede estar vacia.";
        }
        if (aproximacion.isEmpty()) {
            return "La aproximacion no puede estar vacia.";
        }
        if (numeroViasTexto.isEmpty()) {
            return "El numero de vias no puede estar vacio.";
        }

        Integer numeroVias = parsearEntero(numeroViasTexto);
        if (numeroVias == null) {
            return "El numero de vias debe ser un entero valido.";
        }
        if (numeroVias < 0) {
            return "El numero de vias no puede ser negativo.";
        }

        if (incluirId) {
            String idTexto = obtenerYLimpiar(datos, "id");
            if (idTexto.isEmpty()) {
                return "El ID es requerido para modificar.";
            }
            Integer id = parsearEntero(idTexto);
            if (id == null) {
                return "El ID debe ser un entero valido.";
            }
        }

        return null; // Todo valido
    }

    /**
     * Construye un objeto Escuela a partir de datos del formulario.
     * 
     * @param datos mapa de campos
     * @param incluirId si se espera campo "id"
     * @return objeto Escuela
     */
    private Escola construirEscuelaDelFormulario(Map<String, String> datos, boolean incluirId) {
        String nombre = obtenerYLimpiar(datos, "nombre");
        String poblacion = obtenerYLimpiar(datos, "poblacion");
        String aproximacion = obtenerYLimpiar(datos, "aproximacion");
        int numeroVias = parsearEntero(obtenerYLimpiar(datos, "numeroVias"));
        String popularidadTexto = obtenerYLimpiar(datos, "popularidad").toUpperCase();
        Escola.Popularitat popularidad = Escola.Popularitat.valueOf(popularidadTexto);

        Escola escuela = new Escola(nombre, poblacion, aproximacion, numeroVias, popularidad);

        if (incluirId) {
            int id = parsearEntero(obtenerYLimpiar(datos, "id"));
            escuela.setId(id);
        }

        return escuela;
    }

    private String obtenerYLimpiar(Map<String, String> mapa, String clave) {
        return mapa.getOrDefault(clave, "").trim();
    }

    private Integer parsearId(String idTexto) {
        return parsearEntero(idTexto);
    }

    private Integer parsearEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ================================
    // API en castellano (recomendada)
    // ================================
    public boolean crearEscuela(Escola escuela) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            sentencia.setString(1, escuela.getNom());
            sentencia.setString(2, escuela.getPoblacio());
            sentencia.setString(3, escuela.getAproximacio());
            sentencia.setInt(4, escuela.getNumVies());
            sentencia.setString(5, escuela.getPopularitat().name().toLowerCase());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear la escuela: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarEscuela(Escola escuela) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, escuela.getNom());
            sentencia.setString(2, escuela.getPoblacio());
            sentencia.setString(3, escuela.getAproximacio());
            sentencia.setInt(4, escuela.getNumVies());
            sentencia.setString(5, escuela.getPopularitat().name().toLowerCase());
            sentencia.setInt(6, escuela.getId());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar la escuela: " + e.getMessage());
            return false;
        }
    }

    public Escola buscarEscuelaPorId(int id) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return null;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_ID)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearEscola(resultado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la escuela: " + e.getMessage());
        }
        return null;
    }

    public List<Escola> listarEscuelas() {
        List<Escola> escuelas = new ArrayList<>();
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return escuelas;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_TODAS);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                escuelas.add(mapearEscola(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar las escuelas: " + e.getMessage());
        }
        return escuelas;
    }

    public boolean eliminarEscuela(int id) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la escuela: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // Compatibilidad: metodos antiguos usados en el proyecto
    // ======================================================
    public boolean crearEscola(Escola escola) {
        return crearEscuela(escola);
    }

    public boolean modificarEscola(Escola escola) {
        return modificarEscuela(escola);
    }

    public Escola llistarEscola(int id) {
        return buscarEscuelaPorId(id);
    }

    public List<Escola> llistarTotesEscoles() {
        return listarEscuelas();
    }

    public boolean eliminarEscola(int id) {
        return eliminarEscuela(id);
    }

    // ==========================================
    // Helpers privados para simplificar el codigo
    // ==========================================
    private Connection obtenerConexionActiva() {
        conexio_db.comprobarConexion();
        Connection conexion = conexio_db.getConn();
        if (conexion == null) {
            System.err.println("ERROR: No hay conexion con la base de datos.");
        }
        return conexion;
    }

    private Escola mapearEscola(ResultSet resultado) throws SQLException {
        Escola escola = new Escola();
        escola.setId(resultado.getInt("id"));
        escola.setNom(resultado.getString("nom"));
        escola.setPoblacio(resultado.getString("poblacio"));
        escola.setAproximacio(resultado.getString("aproximacio"));
        escola.setNumVies(resultado.getInt("num_vies"));
        escola.setPopularitat(Escola.Popularitat.valueOf(resultado.getString("popularitat").toUpperCase()));
        return escola;
    }
}