package controller;

import model.entidades.Escola;
import model.persistencia.conexio_db; // Ja corregit a persistencia
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EscolaController {

    // ==========================================
    // 1. CREAR (Insertar)
    // ==========================================
    public boolean crearEscola(Escola escola) {
    String sql = "INSERT INTO escoles (nom, poblacio, aproximacio, num_vies, popularitat) VALUES (?, ?, ?, ?, ?)";
    
    conexio_db.comprobarConexion();
    Connection conn = conexio_db.getConn();

    // --- AÑADE ESTO PARA EVITAR CRASHES ---
    if (conn == null) {
        System.err.println("ERROR: No hay conexión con la base de datos.");
        return false;
    }
    // --------------------------------------

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, escola.getNom());
            pstmt.setString(2, escola.getPoblacio());
            pstmt.setString(3, escola.getAproximacio());
            pstmt.setInt(4, escola.getNumVies());
            pstmt.setString(5, escola.getPopularitat().name().toLowerCase());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear la escuela: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 2. MODIFICAR (Update)
    // ==========================================
    public boolean modificarEscola(Escola escola) {
        String sql = "UPDATE escoles SET nom=?, poblacio=?, aproximacio=?, num_vies=?, popularitat=? WHERE id=?";
        
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, escola.getNom());
            pstmt.setString(2, escola.getPoblacio());
            pstmt.setString(3, escola.getAproximacio());
            pstmt.setInt(4, escola.getNumVies());
            pstmt.setString(5, escola.getPopularitat().name().toLowerCase());
            pstmt.setInt(6, escola.getId());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al modificar la escuela: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. LLISTAR UN (Select by ID)
    // ==========================================
    public Escola llistarEscola(int id) {
        String sql = "SELECT * FROM escoles WHERE id=?";
        Escola escola = null;

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    escola = new Escola();
                    escola.setId(rs.getInt("id"));
                    escola.setNom(rs.getString("nom"));
                    escola.setPoblacio(rs.getString("poblacio"));
                    escola.setAproximacio(rs.getString("aproximacio"));
                    escola.setNumVies(rs.getInt("num_vies"));
                    // Convertim el text de la BDD ('alta') al format de l'Enum ('ALTA')
                    escola.setPopularitat(Escola.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la escuela: " + e.getMessage());
        }
        return escola;
    }

    // ==========================================
    // 4. LLISTAR TOTS (Select All)
    // ==========================================
    public List<Escola> llistarTotesEscoles() {
        String sql = "SELECT * FROM escoles";
        List<Escola> llistaEscoles = new ArrayList<>();

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Escola escola = new Escola();
                escola.setId(rs.getInt("id"));
                escola.setNom(rs.getString("nom"));
                escola.setPoblacio(rs.getString("poblacio"));
                escola.setAproximacio(rs.getString("aproximacio"));
                escola.setNumVies(rs.getInt("num_vies"));
                escola.setPopularitat(Escola.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                
                llistaEscoles.add(escola);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar las escuelas: " + e.getMessage());
        }
        return llistaEscoles;
    }

    // ==========================================
    // 5. ELIMINAR (Delete)
    // ==========================================
    public boolean eliminarEscola(int id) {
        String sql = "DELETE FROM escoles WHERE id=?";
        
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar la escuela: " + e.getMessage());
            return false;
        }
    }
}