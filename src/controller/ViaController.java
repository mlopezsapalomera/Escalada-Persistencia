package controller;

import model.entidades.*;
import model.persistencia.conexio_db;
import java.sql.*;

public class ViaController {

    public boolean crearVia(Via v) {
        // 1. Insertem a la taula 'vies' (He posat els noms exactes de la teva BD)
        // He afegit id_escola perquè a la teva BD és NOT NULL
        String sqlVia = "INSERT INTO vies (id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, tipus_roca, tipus_via, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlVia, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, v.getSector().getId());
                pstmt.setInt(2, v.getSector().getEscola().getId()); // Agafem l'ID de l'escola a través del sector
                pstmt.setInt(3, v.getCreadaPer().getId());
                pstmt.setString(4, v.getNom());
                
                // Si és esportiva usem la seva dificultat, si no, la global
                String grau = (v.getEstil() == Via.Estil.ESPORTIVA) ? v.getDificultatEsportiva() : "N/A";
                pstmt.setString(5, grau);
                
                pstmt.setString(6, v.getOrientacio().name()); 
                pstmt.setString(7, v.getEstat().name().toLowerCase());
                pstmt.setString(8, v.getTipusRoca());
                pstmt.setString(9, v.getEstil().name().toLowerCase());
                pstmt.setString(10, v.getRestriccions());

                int rows = pstmt.executeUpdate();
                if (rows == 0) throw new SQLException("Error al crear la via.");

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    v.setId(rs.getInt(1));

                    // 2. Detalls específics
                    if (v.getEstil() == Via.Estil.ESPORTIVA) {
                        insertarDetallEsportiva(v, conn);
                    } else if (!v.getLlistaLlargs().isEmpty()) {
                        insertarLlargs(v, conn);
                    }
                }
                
                conn.commit();
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Error en la transacció: " + ex.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    private void insertarDetallEsportiva(Via v, Connection conn) throws SQLException {
        // La teva taula es diu 'detalls_esportiva' i el camp 'ancoratge'
        String sql = "INSERT INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, v.getId());
            pstmt.setInt(2, v.getLlargadaTotal()); 
            pstmt.setString(3, v.getAncoratges()); // Ha de ser 'spits', 'parabolts' o 'químics'
            pstmt.executeUpdate();
        }
    }

    private void insertarLlargs(Via v, Connection conn) throws SQLException {
        // La teva taula 'llargs' té: id_via, ordre_llarg, llargada, grau, ancoratge
        String sql = "INSERT INTO llargs (id_via, ordre_llarg, llargada, grau, ancoratge) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int numLlarg = 1;
            for (Llarg llarg : v.getLlistaLlargs()) {
                pstmt.setInt(1, v.getId());
                pstmt.setInt(2, numLlarg++);
                pstmt.setInt(3, llarg.getLlargada());
                pstmt.setString(4, llarg.getDificultat());
                pstmt.setString(5, v.getAncoratges()); 
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }
}