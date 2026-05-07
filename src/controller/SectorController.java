package controller;

import model.ResultadoCrud;
import model.entidades.Escola;
import model.entidades.Sector;
import model.persistencia.conexio_db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SectorController {

    private static final String SQL_INSERTAR = "INSERT INTO sectors (id_escola, nom, latitud, longitud, aproximacio, num_vies, popularitat, restriccions, tipus_sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR = "UPDATE sectors SET id_escola=?, nom=?, latitud=?, longitud=?, aproximacio=?, num_vies=?, popularitat=?, restriccions=?, tipus_sector=? WHERE id=?";
    private static final String SQL_POR_ID = "SELECT s.*, e.nom AS escola_nom, e.poblacio AS escola_poblacio, e.aproximacio AS escola_aproximacio, e.num_vies AS escola_num_vies, e.popularitat AS escola_popularitat FROM sectors s JOIN escoles e ON s.id_escola = e.id WHERE s.id=?";
    private static final String SQL_TODOS = "SELECT s.*, e.nom AS escola_nom, e.poblacio AS escola_poblacio, e.aproximacio AS escola_aproximacio, e.num_vies AS escola_num_vies, e.popularitat AS escola_popularitat FROM sectors s JOIN escoles e ON s.id_escola = e.id ORDER BY s.id";
    private static final String SQL_POR_ESCUELA = "SELECT s.*, e.nom AS escola_nom, e.poblacio AS escola_poblacio, e.aproximacio AS escola_aproximacio, e.num_vies AS escola_num_vies, e.popularitat AS escola_popularitat FROM sectors s JOIN escoles e ON s.id_escola = e.id WHERE s.id_escola=? ORDER BY s.id";
    private static final String SQL_ELIMINAR = "DELETE FROM sectors WHERE id=?";
    private static final String SQL_EXISTE_ESCUELA = "SELECT id FROM escoles WHERE id=?";
    private static final String SQL_EXISTE_NOMBRE_EN_ESCUELA = "SELECT COUNT(*) FROM sectors WHERE id_escola=? AND nom=? AND (? = 0 OR id <> ?)";
    private static final String SQL_CONTAR_VIAS_INCOMPATIBLES = "SELECT COUNT(*) FROM vies WHERE id_sector=? AND ((?='gel' AND tipus_via<>'gel') OR (?='mixte_roca' AND tipus_via='gel'))";

    // ================================================
    // API DE NIVEL ALTO: Validacion + Transformacion
    // ================================================

    public ResultadoCrud validarYCrearSector(Map<String, String> datos) {
        String validacion = validarCamposSector(datos, false);
        if (validacion != null) {
            return ResultadoCrud.error(validacion);
        }

        try {
            Sector sector = construirSectorDelFormulario(datos, false);
            boolean creado = crearSector(sector);

            if (creado) {
                return ResultadoCrud.exitoso("Sector creado correctamente.");
            }
            return ResultadoCrud.error("No se pudo crear el sector en la base de datos.");
        } catch (Exception e) {
            return ResultadoCrud.error("Error inesperado: " + e.getMessage());
        }
    }

    public ResultadoCrud validarYModificarSector(Map<String, String> datos) {
        String validacion = validarCamposSector(datos, true);
        if (validacion != null) {
            return ResultadoCrud.error(validacion);
        }

        try {
            Sector sector = construirSectorDelFormulario(datos, true);
            boolean modificado = modificarSector(sector);

            if (modificado) {
                return ResultadoCrud.exitoso("Sector modificado correctamente.");
            }
            return ResultadoCrud.error("No se pudo modificar el sector en la base de datos.");
        } catch (Exception e) {
            return ResultadoCrud.error("Error inesperado: " + e.getMessage());
        }
    }

    public ResultadoCrud obtenerSectorParaEdicion(String idTexto) {
        Integer id = parsearId(idTexto);
        if (id == null) {
            return ResultadoCrud.error("El ID debe ser un numero entero.");
        }

        Sector sector = buscarSectorPorId(id);
        if (sector == null) {
            return ResultadoCrud.error("No existe sector con ID " + id + ".");
        }

        return ResultadoCrud.exitoso("Sector encontrado.", sector);
    }

    public ResultadoCrud validarYEliminarSector(String idTexto) {
        Integer id = parsearId(idTexto);
        if (id == null) {
            return ResultadoCrud.error("El ID debe ser un numero entero.");
        }

        boolean eliminado = eliminarSector(id);
        if (eliminado) {
            return ResultadoCrud.exitoso("Sector eliminado correctamente.");
        }
        return ResultadoCrud.error("No se pudo eliminar el sector.");
    }

    // =============================================
    // HELPERS PRIVADOS: Validacion y Transformacion
    // =============================================

    private String validarCamposSector(Map<String, String> datos, boolean incluirId) {
        String idEscuelaTexto = obtenerYLimpiar(datos, "idEscuela");
        String nombre = obtenerYLimpiar(datos, "nombre");
        String latitudTexto = obtenerYLimpiar(datos, "latitud");
        String longitudTexto = obtenerYLimpiar(datos, "longitud");
        String aproximacion = obtenerYLimpiar(datos, "aproximacion");
        String numeroViasTexto = obtenerYLimpiar(datos, "numeroVias");
        String popularidadTexto = obtenerYLimpiar(datos, "popularidad");
        String tipoSectorTexto = obtenerYLimpiar(datos, "tipoSector");

        Integer id = null;
        if (incluirId) {
            String idTexto = obtenerYLimpiar(datos, "id");
            if (idTexto.isEmpty()) {
                return "El ID es requerido para modificar.";
            }
            id = parsearEntero(idTexto);
            if (id == null || id <= 0) {
                return "El ID debe ser un entero positivo.";
            }
            if (buscarSectorPorId(id) == null) {
                return "No existe sector con ID " + id + ".";
            }
        }

        Integer idEscuela = parsearEntero(idEscuelaTexto);
        if (idEscuela == null || idEscuela <= 0) {
            return "Debe seleccionar una escuela valida.";
        }
        if (!existeEscuela(idEscuela)) {
            return "La escuela seleccionada no existe en la base de datos.";
        }

        if (nombre.isEmpty()) {
            return "El nombre del sector no puede estar vacio.";
        }
        if (nombre.length() > 100) {
            return "El nombre del sector no puede superar 100 caracteres.";
        }
        if (existeNombreEnEscuela(idEscuela, nombre, id)) {
            return "Ya existe un sector con ese nombre en la escuela seleccionada.";
        }

        BigDecimal latitud = parsearDecimal(latitudTexto);
        if (latitud == null) {
            return "La latitud debe ser un numero decimal valido.";
        }
        if (latitud.compareTo(BigDecimal.valueOf(-90)) < 0 || latitud.compareTo(BigDecimal.valueOf(90)) > 0) {
            return "La latitud debe estar entre -90 y 90.";
        }

        BigDecimal longitud = parsearDecimal(longitudTexto);
        if (longitud == null) {
            return "La longitud debe ser un numero decimal valido.";
        }
        if (longitud.compareTo(BigDecimal.valueOf(-180)) < 0 || longitud.compareTo(BigDecimal.valueOf(180)) > 0) {
            return "La longitud debe estar entre -180 y 180.";
        }

        if (aproximacion.isEmpty()) {
            return "La aproximacion no puede estar vacia.";
        }

        Integer numeroVias = parsearEntero(numeroViasTexto);
        if (numeroVias == null) {
            return "El numero de vias debe ser un entero valido.";
        }
        if (numeroVias < 0) {
            return "El numero de vias no puede ser negativo.";
        }

        if (popularidadTexto.isEmpty()) {
            return "La popularidad es obligatoria.";
        }
        if (!esPopularidadValida(popularidadTexto)) {
            return "La popularidad debe ser BAIXA, MITJANA o ALTA.";
        }

        if (tipoSectorTexto.isEmpty()) {
            return "El tipo de sector es obligatorio.";
        }
        if (!esTipoSectorValido(tipoSectorTexto)) {
            return "El tipo de sector debe ser GEL o MIXTE_ROCA.";
        }

        if (incluirId && hayViasIncompatiblesConTipo(id, tipoSectorTexto)) {
            return "El tipo de sector no es compatible con las vias ya existentes en ese sector.";
        }

        return null;
    }

    private Sector construirSectorDelFormulario(Map<String, String> datos, boolean incluirId) {
        int idEscuela = parsearEntero(obtenerYLimpiar(datos, "idEscuela"));
        String nombre = obtenerYLimpiar(datos, "nombre");
        BigDecimal latitud = parsearDecimal(obtenerYLimpiar(datos, "latitud"));
        BigDecimal longitud = parsearDecimal(obtenerYLimpiar(datos, "longitud"));
        String aproximacion = obtenerYLimpiar(datos, "aproximacion");
        int numeroVias = parsearEntero(obtenerYLimpiar(datos, "numeroVias"));
        String popularidadTexto = obtenerYLimpiar(datos, "popularidad").toUpperCase();
        String restricciones = obtenerYLimpiar(datos, "restricciones");
        String tipoSectorTexto = obtenerYLimpiar(datos, "tipoSector").toUpperCase();

        Escola escuela = new Escola();
        escuela.setId(idEscuela);

        Sector sector = new Sector(
                escuela,
                nombre,
                latitud,
                longitud,
                aproximacion,
                numeroVias,
                Sector.Popularitat.valueOf(popularidadTexto),
                restricciones,
                Sector.TipusSector.valueOf(tipoSectorTexto)
        );

        if (incluirId) {
            sector.setId(parsearEntero(obtenerYLimpiar(datos, "id")));
        }

        return sector;
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

    private BigDecimal parsearDecimal(String texto) {
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean esPopularidadValida(String popularidadTexto) {
        try {
            Sector.Popularitat.valueOf(popularidadTexto.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean esTipoSectorValido(String tipoTexto) {
        try {
            Sector.TipusSector.valueOf(tipoTexto.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // ================================
    // API en castellano (recomendada)
    // ================================

    public boolean crearSector(Sector sector) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR)) {
            sentencia.setInt(1, sector.getEscola().getId());
            sentencia.setString(2, sector.getNom());
            sentencia.setBigDecimal(3, sector.getLatitud());
            sentencia.setBigDecimal(4, sector.getLongitud());
            sentencia.setString(5, sector.getAproximacio());
            sentencia.setInt(6, sector.getNumVies());
            sentencia.setString(7, sector.getPopularitat().name().toLowerCase());
            sentencia.setString(8, sector.getRestriccions());
            sentencia.setString(9, sector.getTipusSector().name().toLowerCase());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el sector: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarSector(Sector sector) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setInt(1, sector.getEscola().getId());
            sentencia.setString(2, sector.getNom());
            sentencia.setBigDecimal(3, sector.getLatitud());
            sentencia.setBigDecimal(4, sector.getLongitud());
            sentencia.setString(5, sector.getAproximacio());
            sentencia.setInt(6, sector.getNumVies());
            sentencia.setString(7, sector.getPopularitat().name().toLowerCase());
            sentencia.setString(8, sector.getRestriccions());
            sentencia.setString(9, sector.getTipusSector().name().toLowerCase());
            sentencia.setInt(10, sector.getId());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar el sector: " + e.getMessage());
            return false;
        }
    }

    public Sector buscarSectorPorId(int id) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return null;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_ID)) {
            sentencia.setInt(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearSector(resultado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el sector por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Sector> listarSectores() {
        List<Sector> sectores = new ArrayList<>();
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return sectores;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_TODOS);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                sectores.add(mapearSector(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar sectores: " + e.getMessage());
        }
        return sectores;
    }

    public List<Sector> listarSectoresPorEscuela(int idEscuela) {
        List<Sector> sectores = new ArrayList<>();
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return sectores;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_POR_ESCUELA)) {
            sentencia.setInt(1, idEscuela);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    sectores.add(mapearSector(resultado));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar sectores por escuela: " + e.getMessage());
        }
        return sectores;
    }

    public boolean eliminarSector(int id) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el sector: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // Compatibilidad: metodos antiguos usados en el proyecto
    // ======================================================

    public Sector llistarSector(int id) {
        return buscarSectorPorId(id);
    }

    public List<Sector> llistarTotsSectors() {
        return listarSectores();
    }

    // ==========================================
    // Helpers privados para reglas de negocio
    // ==========================================

    private boolean existeEscuela(int idEscuela) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_EXISTE_ESCUELA)) {
            sentencia.setInt(1, idEscuela);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al validar existencia de escuela: " + e.getMessage());
            return false;
        }
    }

    private boolean existeNombreEnEscuela(int idEscuela, String nombre, Integer idExcluir) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        int idExcluirSeguro = idExcluir == null ? 0 : idExcluir;
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_EXISTE_NOMBRE_EN_ESCUELA)) {
            sentencia.setInt(1, idEscuela);
            sentencia.setString(2, nombre);
            sentencia.setInt(3, idExcluirSeguro);
            sentencia.setInt(4, idExcluirSeguro);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar nombre duplicado de sector: " + e.getMessage());
        }
        return false;
    }

    private boolean hayViasIncompatiblesConTipo(int idSector, String tipoSectorTexto) {
        Connection conexion = obtenerConexionActiva();
        if (conexion == null) {
            return false;
        }

        String tipoSectorDb = tipoSectorTexto.toLowerCase();
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_CONTAR_VIAS_INCOMPATIBLES)) {
            sentencia.setInt(1, idSector);
            sentencia.setString(2, tipoSectorDb);
            sentencia.setString(3, tipoSectorDb);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar coherencia de tipo de sector con vias: " + e.getMessage());
        }
        return false;
    }

    private Connection obtenerConexionActiva() {
        conexio_db.comprobarConexion();
        Connection conexion = conexio_db.getConn();
        if (conexion == null) {
            System.err.println("ERROR: No hay conexion con la base de datos.");
        }
        return conexion;
    }

    private Sector mapearSector(ResultSet resultado) throws SQLException {
        Escola escuela = new Escola();
        escuela.setId(resultado.getInt("id_escola"));

        String nombreEscuela = obtenerCadenaOpcional(resultado, "escola_nom");
        if (nombreEscuela != null) {
            escuela.setNom(nombreEscuela);
            escuela.setPoblacio(obtenerCadenaOpcional(resultado, "escola_poblacio"));
            escuela.setAproximacio(obtenerCadenaOpcional(resultado, "escola_aproximacio"));
            escuela.setNumVies(resultado.getInt("escola_num_vies"));
            String popularidadEscuela = obtenerCadenaOpcional(resultado, "escola_popularitat");
            if (popularidadEscuela != null) {
                escuela.setPopularitat(Escola.Popularitat.valueOf(popularidadEscuela.toUpperCase()));
            }
        }

        Sector sector = new Sector();
        sector.setId(resultado.getInt("id"));
        sector.setEscola(escuela);
        sector.setNom(resultado.getString("nom"));
        sector.setLatitud(resultado.getBigDecimal("latitud"));
        sector.setLongitud(resultado.getBigDecimal("longitud"));
        sector.setAproximacio(resultado.getString("aproximacio"));
        sector.setNumVies(resultado.getInt("num_vies"));
        sector.setPopularitat(Sector.Popularitat.valueOf(resultado.getString("popularitat").toUpperCase()));
        sector.setRestriccions(resultado.getString("restriccions"));
        sector.setTipusSector(Sector.TipusSector.valueOf(resultado.getString("tipus_sector").toUpperCase()));
        return sector;
    }

    private String obtenerCadenaOpcional(ResultSet resultado, String columna) {
        try {
            return resultado.getString(columna);
        } catch (SQLException e) {
            return null;
        }
    }
}
