package controller;

import model.entidades.Escalador;
import model.persistencia.conexio_db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EscaladorController {

    // 1. CREAR
    public boolean crearEscalador(Escalador e) {
        String sql = "INSERT INTO escaladors (nom, alias, edat, nivell_maxim, estil_preferit) VALUES (?, ?, ?, ?, ?)";
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getNom());
            pstmt.setString(2, e.getAlias());
            pstmt.setInt(3, e.getEdat());
            pstmt.setString(4, e.getNivellMaxim());
            pstmt.setString(5, e.getEstilPreferit().name().toLowerCase()); 

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al crear l'escalador: " + ex.getMessage());
            return false;
        }
    }

    // 2. MODIFICAR
    public boolean modificarEscalador(Escalador e) {
        String sql = "UPDATE escaladors SET nom=?, alias=?, edat=?, nivell_maxim=?, estil_preferit=? WHERE id=?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setString(1, e.getNom());
            pstmt.setString(2, e.getAlias());
            pstmt.setInt(3, e.getEdat());
            pstmt.setString(4, e.getNivellMaxim());
            pstmt.setString(5, e.getEstilPreferit().name().toLowerCase());
            pstmt.setInt(6, e.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al modificar l'escalador: " + ex.getMessage());
            return false;
        }
    }

    // 3. LLISTAR UN PER ID
    public Escalador llistarEscalador(int id) {
        String sql = "SELECT * FROM escaladors WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Escalador e = new Escalador();
                    e.setId(rs.getInt("id"));
                    e.setNom(rs.getString("nom"));
                    e.setAlias(rs.getString("alias"));
                    e.setEdat(rs.getInt("edat"));
                    e.setNivellMaxim(rs.getString("nivell_maxim"));
                    e.setEstilPreferit(Escalador.Estil.valueOf(rs.getString("estil_preferit").toUpperCase()));
                    return e;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al cercar l'escalador: " + ex.getMessage());
        }
        return null;
    }

    // 4. LLISTAR TOTS
    public List<Escalador> llistarTotsEscaladors() {
        String sql = "SELECT * FROM escaladors";
        List<Escalador> llista = new ArrayList<>();
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Escalador e = new Escalador();
                e.setId(rs.getInt("id"));
                e.setNom(rs.getString("nom"));
                e.setAlias(rs.getString("alias"));
                e.setEdat(rs.getInt("edat"));
                e.setNivellMaxim(rs.getString("nivell_maxim"));
                e.setEstilPreferit(Escalador.Estil.valueOf(rs.getString("estil_preferit").toUpperCase()));
                llista.add(e);
            }
        } catch (SQLException ex) {
            System.err.println("Error al llistar escaladors: " + ex.getMessage());
        }
        return llista;
    }

    // 5. ELIMINAR
    public boolean eliminarEscalador(int id) {
        String sql = "DELETE FROM escaladors WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar l'escalador: " + ex.getMessage());
            return false;
        }
    }
}