package model.dao.mysql;
import model.dao.SectorDAO;
import model.entidades.Escola;
import model.entidades.Sector;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implementación MySQL del DAO de sectores.
public class MySqlSectorDAOImpl implements SectorDAO {

    @Override
    public boolean crear(Sector s) {
        // Valida conexión y unicidad de nombre por escuela antes de insertar.
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        // Unicidad: dentro de una escuela no puede repetirse el nombre del sector.
        String checkSql = "SELECT COUNT(*) AS cnt FROM sectors WHERE id_escola = ? AND LOWER(nom) = LOWER(?)";
        try (PreparedStatement pc = conn.prepareStatement(checkSql)) {
            pc.setInt(1, s.getEscola().getId());
            pc.setString(2, s.getNom());
            try (ResultSet rc = pc.executeQuery()) {
                if (rc.next() && rc.getInt("cnt") > 0) {
                    System.out.println("Error: ja existeix un sector amb aquest nom a la mateixa escola.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error comprovant unicitat de sector: " + ex.getMessage());
            return false;
        }

        String sql = "INSERT INTO sectors (id_escola, nom, latitud, longitud, aproximacio, num_vies, popularitat, restriccions, tipus_sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.getEscola().getId());
            pstmt.setString(2, s.getNom());
            pstmt.setBigDecimal(3, s.getLatitud());
            pstmt.setBigDecimal(4, s.getLongitud());
            pstmt.setString(5, s.getAproximacio());
            pstmt.setInt(6, s.getNumVies());
            pstmt.setString(7, s.getPopularitat().name().toLowerCase());
            pstmt.setString(8, s.getRestriccions());
            pstmt.setString(9, s.getTipusSector().name().toLowerCase());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al crear el sector: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Sector> obtenirTots() {
        // Devuelve todos los sectores con su escuela referenciada por ID.
        String sql = "SELECT * FROM sectors";
        List<Sector> llista = new ArrayList<>();
        conexio_db.comprobarConexion();
        try (Statement stmt = conexio_db.getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sector s = new Sector();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setLatitud(rs.getBigDecimal("latitud"));
                s.setLongitud(rs.getBigDecimal("longitud"));
                s.setPopularitat(Sector.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                s.setTipusSector(Sector.TipusSector.valueOf(rs.getString("tipus_sector").toUpperCase()));
                
                Escola e = new Escola();
                e.setId(rs.getInt("id_escola"));
                s.setEscola(e);
                
                llista.add(s);
            }
        } catch (SQLException ex) {
            System.err.println("Error al llistar sectors: " + ex.getMessage());
        }
        return llista;
    }

    @Override
    public boolean eliminar(int id) {
        // Bloquea el borrado si el sector tiene vías asociadas.
        conexio_db.comprobarConexion();
        String check = "SELECT COUNT(*) AS cnt FROM vies WHERE id_sector = ?";
        try (PreparedStatement pc = conexio_db.getConn().prepareStatement(check)) {
            pc.setInt(1, id);
            try (java.sql.ResultSet rc = pc.executeQuery()) {
                if (rc.next() && rc.getInt("cnt") > 0) {
                    System.out.println("No es pot eliminar el sector: existeixen vies associades.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error comprovant dependències de sector: " + ex.getMessage());
            return false;
        }

        String sql = "DELETE FROM sectors WHERE id = ?";
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar sector: " + ex.getMessage());
            return false;
        }
    }

    // Obtiene un sector por ID con sus datos completos.
    @Override
    public Sector obtenirPerId(int id) {
        String sql = "SELECT * FROM sectors WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Sector s = new Sector();
                    s.setId(rs.getInt("id"));
                    s.setNom(rs.getString("nom"));
                    s.setLatitud(rs.getBigDecimal("latitud"));
                    s.setLongitud(rs.getBigDecimal("longitud"));
                    s.setAproximacio(rs.getString("aproximacio"));
                    s.setNumVies(rs.getInt("num_vies"));
                    s.setPopularitat(Sector.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                    s.setRestriccions(rs.getString("restriccions"));
                    s.setTipusSector(Sector.TipusSector.valueOf(rs.getString("tipus_sector").toUpperCase()));

                    Escola e = new Escola();
                    e.setId(rs.getInt("id_escola"));
                    s.setEscola(e);

                    return s;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenir sector per ID: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualitzar(Sector s) {
        // Antes de actualizar, valida compatibilidad entre tipo de sector y vías existentes.
        try {
            if (s.getTipusSector() == Sector.TipusSector.GEL) {
                String q = "SELECT COUNT(*) AS cnt FROM vies WHERE id_sector = ? AND tipus_via != 'gel'";
                try (PreparedStatement pc = conexio_db.getConn().prepareStatement(q)) {
                    pc.setInt(1, s.getId());
                    try (ResultSet rc = pc.executeQuery()) {
                        if (rc.next() && rc.getInt("cnt") > 0) {
                            System.out.println("No es pot canviar el tipus del sector a GEL: ja existeixen vies no GEL associades.");
                            return false;
                        }
                    }
                }
            } else if (s.getTipusSector() == Sector.TipusSector.MIXTE_ROCA) {
                String q = "SELECT COUNT(*) AS cnt FROM vies WHERE id_sector = ? AND tipus_via = 'gel'";
                try (PreparedStatement pc = conexio_db.getConn().prepareStatement(q)) {
                    pc.setInt(1, s.getId());
                    try (ResultSet rc = pc.executeQuery()) {
                        if (rc.next() && rc.getInt("cnt") > 0) {
                            System.out.println("No es pot canviar el tipus del sector a MIXTE_ROCA: ja existeixen vies de GEL associades.");
                            return false;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error comprovant compatibilitat del sector abans d'actualitzar: " + ex.getMessage());
            return false;
        }

        String sql = "UPDATE sectors SET id_escola = ?, nom = ?, latitud = ?, longitud = ?, aproximacio = ?, num_vies = ?, popularitat = ?, restriccions = ?, tipus_sector = ? WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, s.getEscola().getId());
            pstmt.setString(2, s.getNom());
            pstmt.setBigDecimal(3, s.getLatitud());
            pstmt.setBigDecimal(4, s.getLongitud());
            pstmt.setString(5, s.getAproximacio());
            pstmt.setInt(6, s.getNumVies());
            pstmt.setString(7, s.getPopularitat().name().toLowerCase());
            pstmt.setString(8, s.getRestriccions());
            pstmt.setString(9, s.getTipusSector().name().toLowerCase());
            pstmt.setInt(10, s.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al actualitzar sector: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Sector> obtenirSectorsAmbMesDeXViesDisponibles(int x) {
        // Consulta sectores con más de X vías en estado apto.
        List<Sector> res = new ArrayList<>();
        String sql = "SELECT s.* FROM sectors s JOIN vies v ON v.id_sector = s.id AND v.estat = 'apte' GROUP BY s.id HAVING COUNT(v.id) > ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, x);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Sector s = new Sector();
                    s.setId(rs.getInt("id"));
                    s.setNom(rs.getString("nom"));
                    s.setLatitud(rs.getBigDecimal("latitud"));
                    s.setLongitud(rs.getBigDecimal("longitud"));
                    s.setPopularitat(Sector.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                    s.setTipusSector(Sector.TipusSector.valueOf(rs.getString("tipus_sector").toUpperCase()));
                    Escola e = new Escola(); e.setId(rs.getInt("id_escola")); s.setEscola(e);
                    res.add(s);
                }
            }
        } catch (SQLException ex) { System.err.println("Error en consulta sectors: " + ex.getMessage()); }
        return res;
    }
}