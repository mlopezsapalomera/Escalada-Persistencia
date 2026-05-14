package model.dao.mysql;

import model.dao.EscolaDAO;
import model.entidades.Escola;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlEscolaDAOImpl implements EscolaDAO {

    @Override
    public boolean create(Escola escola) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "INSERT INTO escoles (nom, lloc, aproximacio, popularitat) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, escola.getNom());
            ps.setString(2, escola.getLloc());
            ps.setString(3, escola.getAproximacio());
            ps.setString(4, escola.getPopularitat().name());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        escola.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en crear l'escola: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Escola getById(int id) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "SELECT * FROM escoles WHERE id = ?";
        Escola escola = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    escola = new Escola();
                    escola.setId(rs.getInt("id"));
                    escola.setNom(rs.getString("nom"));
                    escola.setLloc(rs.getString("lloc"));
                    escola.setAproximacio(rs.getString("aproximacio"));
                    escola.setNumVies(rs.getInt("num_vies"));
                    escola.setPopularitat(Escola.Popularitat.valueOf(rs.getString("popularitat")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir l'escola per ID: " + e.getMessage());
        }
        return escola;
    }

    @Override
    public List<Escola> getAll() {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "SELECT * FROM escoles";
        List<Escola> escoles = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Escola escola = new Escola();
                escola.setId(rs.getInt("id"));
                escola.setNom(rs.getString("nom"));
                escola.setLloc(rs.getString("lloc"));
                escola.setAproximacio(rs.getString("aproximacio"));
                escola.setNumVies(rs.getInt("num_vies"));
                escola.setPopularitat(Escola.Popularitat.valueOf(rs.getString("popularitat")));
                escoles.add(escola);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir totes les escoles: " + e.getMessage());
        }
        return escoles;
    }

    @Override
    public boolean update(Escola escola) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "UPDATE escoles SET nom = ?, lloc = ?, aproximacio = ?, popularitat = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, escola.getNom());
            ps.setString(2, escola.getLloc());
            ps.setString(3, escola.getAproximacio());
            ps.setString(4, escola.getPopularitat().name());
            ps.setInt(5, escola.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en actualitzar l'escola: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "DELETE FROM escoles WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en eliminar l'escola: " + e.getMessage());
        }
        return false;
    }
}
