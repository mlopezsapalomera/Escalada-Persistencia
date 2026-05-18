package model.dao.mysql;
import model.dao.EscaladorDAO;
import model.entidades.Escalador;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlEscaladorDAOImpl implements EscaladorDAO {

    @Override
    public boolean crear(Escalador escalador) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "INSERT INTO escaladors (nom, alias, edat, nivell, nom_via_nivell_maxim, estil_preferit) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, escalador.getNom());
            ps.setString(2, escalador.getAlias());
            ps.setInt(3, escalador.getEdat());
            ps.setString(4, escalador.getNivell());
            ps.setString(5, escalador.getNomViaNivellMaxim());
            ps.setString(6, escalador.getEstilPreferit().name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        escalador.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en crear l'escalador: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Escalador obtenirPerId(int id) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "SELECT * FROM escaladors WHERE id = ?";
        Escalador escalador = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    escalador = new Escalador();
                    escalador.setId(rs.getInt("id"));
                    escalador.setNom(rs.getString("nom"));
                    escalador.setAlias(rs.getString("alias"));
                    escalador.setEdat(rs.getInt("edat"));
                    escalador.setNivell(rs.getString("nivell"));
                    escalador.setNomViaNivellMaxim(rs.getString("nom_via_nivell_maxim"));
                    String estilStr = rs.getString("estil_preferit");
                    if (estilStr != null) estilStr = estilStr.toUpperCase();
                    escalador.setEstilPreferit(Escalador.Estil.valueOf(estilStr));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir l'escalador per ID: " + e.getMessage());
        }
        return escalador;
    }

    @Override
    public List<Escalador> obtenirTots() {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "SELECT * FROM escaladors";
        List<Escalador> escaladors = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Escalador escalador = new Escalador();
                escalador.setId(rs.getInt("id"));
                escalador.setNom(rs.getString("nom"));
                escalador.setAlias(rs.getString("alias"));
                escalador.setEdat(rs.getInt("edat"));
                escalador.setNivell(rs.getString("nivell"));
                escalador.setNomViaNivellMaxim(rs.getString("nom_via_nivell_maxim"));
                    String estilStr = rs.getString("estil_preferit");
                    if (estilStr != null) estilStr = estilStr.toUpperCase();
                    escalador.setEstilPreferit(Escalador.Estil.valueOf(estilStr));
                escaladors.add(escalador);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir tots els escaladors: " + e.getMessage());
        }
        return escaladors;
    }

    @Override
    public java.util.Map<String, java.util.List<Escalador>> obtenirEscaladorsAgrupatsPerNivell() {
        java.util.Map<String, java.util.List<Escalador>> map = new java.util.HashMap<>();
        conexio_db.comprobarConexion();
        String sql = "SELECT * FROM escaladors ORDER BY nivell";
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Escalador e = new Escalador();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setAlias(rs.getString("alias"));
                e.setEdat(rs.getInt("edat"));
                String nivell = rs.getString("nivell");
                e.setNivell(nivell);
                e.setNomViaNivellMaxim(rs.getString("nom_via_nivell_maxim"));
                String estilStr = rs.getString("estil_preferit"); if (estilStr != null) estilStr = estilStr.toUpperCase(); e.setEstilPreferit(Escalador.Estil.valueOf(estilStr));
                map.computeIfAbsent(nivell, k -> new java.util.ArrayList<>()).add(e);
            }
        } catch (SQLException ex) { System.err.println("Error agrupant escaladors: " + ex.getMessage()); }
        return map;
    }

    @Override
    public boolean actualitzar(Escalador escalador) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "UPDATE escaladors SET nom = ?, alias = ?, edat = ?, nivell = ?, nom_via_nivell_maxim = ?, estil_preferit = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, escalador.getNom());
            ps.setString(2, escalador.getAlias());
            ps.setInt(3, escalador.getEdat());
            ps.setString(4, escalador.getNivell());
            ps.setString(5, escalador.getNomViaNivellMaxim());
            ps.setString(6, escalador.getEstilPreferit().name());
            ps.setInt(7, escalador.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en actualitzar l'escalador: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "DELETE FROM escaladors WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en eliminar l'escalador: " + e.getMessage());
        }
        return false;
    }
}
