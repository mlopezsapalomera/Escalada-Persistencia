package model.dao.mysql;

import model.dao.SectorDAO;
import model.entidades.Escola;
import model.entidades.Sector;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlSectorDAOImpl implements SectorDAO {

    @Override
    public boolean create(Sector s) {
        String sql = "INSERT INTO sectors (id_escola, nom, latitud, longitud, aproximacio, num_vies, popularitat, restriccions, tipus_sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al crear el sector: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Sector> getAll() {
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
    public boolean delete(int id) {
        String sql = "DELETE FROM sectors WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar sector: " + ex.getMessage());
            return false;
        }
    }

    // Mètodes obligatoris per la interfície, deixats buits temporalment si no es fan servir ara
    @Override public Sector getById(int id) { return null; }
    @Override public boolean update(Sector sector) { return false; }
}