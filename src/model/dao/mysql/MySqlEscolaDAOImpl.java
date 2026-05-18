package model.dao.mysql;
import model.dao.EscolaDAO;
import model.entidades.Escola;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlEscolaDAOImpl implements EscolaDAO {

    @Override
    public boolean crear(Escola escola) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        // Unicitat: no permetre dues escoles amb el mateix nom
        String checkSql = "SELECT COUNT(*) AS cnt FROM escoles WHERE LOWER(nom) = LOWER(?)";
        try (PreparedStatement pc = conn.prepareStatement(checkSql)) {
            pc.setString(1, escola.getNom());
            try (ResultSet rc = pc.executeQuery()) {
                if (rc.next() && rc.getInt("cnt") > 0) {
                    System.out.println("Error: ja existeix una escola amb aquest nom.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error comprovant unicitat d'escola: " + e.getMessage());
            return false;
        }

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
    public Escola obtenirPerId(int id) {
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
                    String pop = rs.getString("popularitat");
                    if (pop != null) pop = pop.toUpperCase();
                    else pop = "MITJANA";
                    escola.setPopularitat(Escola.Popularitat.valueOf(pop));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir l'escola per ID: " + e.getMessage());
        }
        return escola;
    }

    @Override
    public List<Escola> obtenirTots() {
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
                String pop = rs.getString("popularitat");
                if (pop != null) pop = pop.toUpperCase();
                else pop = "MITJANA";
                escola.setPopularitat(Escola.Popularitat.valueOf(pop));
                escoles.add(escola);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenir totes les escoles: " + e.getMessage());
        }
        return escoles;
    }

    @Override
    public List<Escola> obtenirEscolesAmbRestriccionsActives() {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        String sql = "SELECT DISTINCT e.* FROM escoles e JOIN sectors s ON s.id_escola = e.id WHERE s.restriccions IS NOT NULL AND s.restriccions <> '' UNION SELECT DISTINCT e.* FROM escoles e JOIN vies v ON v.id_escola = e.id WHERE v.restriccions IS NOT NULL AND v.restriccions <> ''";
        List<Escola> res = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Escola escola = new Escola();
                escola.setId(rs.getInt("id"));
                escola.setNom(rs.getString("nom"));
                escola.setLloc(rs.getString("lloc"));
                escola.setAproximacio(rs.getString("aproximacio"));
                escola.setNumVies(rs.getInt("num_vies"));
                String pop = rs.getString("popularitat");
                if (pop != null) pop = pop.toUpperCase(); else pop = "MITJANA";
                escola.setPopularitat(Escola.Popularitat.valueOf(pop));
                res.add(escola);
            }
        } catch (SQLException e) { System.err.println("Error al llistar escoles amb restriccions: " + e.getMessage()); }
        return res;
    }

    @Override
    public boolean actualitzar(Escola escola) {
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
    public boolean eliminar(int id) {
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        // Check for dependent sectors
        String check = "SELECT COUNT(*) AS cnt FROM sectors WHERE id_escola = ?";
        try (PreparedStatement pc = conn.prepareStatement(check)) {
            pc.setInt(1, id);
            try (ResultSet rc = pc.executeQuery()) {
                if (rc.next() && rc.getInt("cnt") > 0) {
                    System.out.println("No es pot eliminar l'escola: existeixen sectors associats.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error comprovant dependències d'escola: " + ex.getMessage());
            return false;
        }

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
