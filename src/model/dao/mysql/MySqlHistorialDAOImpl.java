package model.dao.mysql;

import model.dao.HistorialDAO;
import model.persistencia.conexio_db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySqlHistorialDAOImpl implements HistorialDAO {

    @Override
    public boolean addAscensio(int idEscalador, int idVia, Date data) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "INSERT INTO historial_escaladors (id_escalador, id_via, data_ascensio) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEscalador);
            ps.setInt(2, idVia);
            ps.setDate(3, data);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error afegint ascensió: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAscensosByEscalador(int idEscalador) {
        List<Map<String, Object>> res = new ArrayList<>();
        conexio_db.comprobarConexion();
        String sql = "SELECT h.*, v.nom as nom_via, v.grau_global, v.id_sector, v.id_escola FROM historial_escaladors h JOIN vies v ON v.id = h.id_via WHERE h.id_escalador = ? ORDER BY h.data_ascensio DESC";
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscalador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id_via", rs.getInt("id_via"));
                    row.put("nom_via", rs.getString("nom_via"));
                    row.put("grau", rs.getString("grau_global"));
                    row.put("data", rs.getDate("data_ascensio"));
                    row.put("id_sector", rs.getInt("id_sector"));
                    row.put("id_escola", rs.getInt("id_escola"));
                    res.add(row);
                }
            }
        } catch (SQLException ex) { System.err.println("Error obtenint ascensos: " + ex.getMessage()); }
        return res;
    }

    @Override
    public List<Map<String, Object>> getAscensosByVia(int idVia) {
        List<Map<String, Object>> res = new ArrayList<>();
        conexio_db.comprobarConexion();
        String sql = "SELECT h.*, e.nom as nom_escalador FROM historial_escaladors h JOIN escaladors e ON e.id = h.id_escalador WHERE h.id_via = ? ORDER BY h.data_ascensio DESC";
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idVia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id_escalador", rs.getInt("id_escalador"));
                    row.put("nom_escalador", rs.getString("nom_escalador"));
                    row.put("data", rs.getDate("data_ascensio"));
                    res.add(row);
                }
            }
        } catch (SQLException ex) { System.err.println("Error obtenint ascensos per via: " + ex.getMessage()); }
        return res;
    }
}
