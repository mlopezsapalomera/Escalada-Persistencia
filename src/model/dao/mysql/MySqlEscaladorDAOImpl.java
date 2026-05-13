package model.dao.mysql;

import model.dao.EscaladorDAO;
import model.entidades.Escalador;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlEscaladorDAOImpl implements EscaladorDAO {

    @Override
    public boolean create(Escalador escalador) {
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
    public Escalador getById(int id) {
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
                    escalador.setEstilPreferit(Escalador.Estil.valueOf(rs.getString("estil_preferit")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir l'escalador per ID: " + e.getMessage());
        }
        return escalador;
    }

    @Override
    public List<Escalador> getAll() {
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
                escalador.setEstilPreferit(Escalador.Estil.valueOf(rs.getString("estil_preferit")));
                escaladors.add(escalador);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir tots els escaladors: " + e.getMessage());
        }
        return escaladors;
    }

    @Override
    public boolean update(Escalador escalador) {
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
    public boolean delete(int id) {
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
