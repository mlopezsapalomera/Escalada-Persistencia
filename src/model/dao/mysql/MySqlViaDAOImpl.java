package model.dao.mysql;

import model.dao.ViaDAO;
import model.entidades.*;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlViaDAOImpl implements ViaDAO {

    @Override
    public boolean create(Via v) {
        String sqlGeneral = "INSERT INTO vies (id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, data_finalitzacio_estat, tipus_roca, tipus_via, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGeneral, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, v.getSector().getId());
                pstmt.setInt(2, v.getSector().getEscola().getId());
                pstmt.setInt(3, v.getCreadaPer().getId());
                pstmt.setString(4, v.getNom());
                pstmt.setString(5, v.getGrauGlobal());
                pstmt.setString(6, v.getOrientacio().name());
                pstmt.setString(7, v.getEstat().name().toLowerCase());
                pstmt.setDate(8, v.getDataFinalitzacioEstat());
                pstmt.setString(9, v.getTipusRoca());
                pstmt.setString(10, v.getEstil().name().toLowerCase());
                pstmt.setString(11, v.getRestriccions());

                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                
                if (rs.next()) {
                    v.setId(rs.getInt(1));
                    if (v.getEstil() == Via.Estil.ESPORTIVA) {
                        insertarDetallEsportiva(v, conn);
                    } else if (v.getLlistaLlargs() != null && !v.getLlistaLlargs().isEmpty()) {
                        insertarLlargs(v, conn);
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) { return false; }
    }

    private void insertarDetallEsportiva(Via v, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, v.getId());
            pstmt.setInt(2, v.getLlargadaTotal());
            pstmt.setString(3, v.getAncoratges());
            pstmt.executeUpdate();
        }
    }

    private void insertarLlargs(Via v, Connection conn) throws SQLException {
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

    @Override
    public List<Via> getDisponiblesPerEscola(int idEscola) {
        List<Via> disponibles = new ArrayList<>();
        String sql = "SELECT nom, grau_global FROM vies WHERE id_escola = ? AND estat = 'apte'";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Via v = new Via();
                v.setNom(rs.getString("nom"));
                v.setGrauGlobal(rs.getString("grau_global"));
                disponibles.add(v);
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return disponibles;
    }

    @Override public Via getById(int id) { return null; }
    @Override public List<Via> getAll() { return new ArrayList<>(); }
    @Override public boolean update(Via via) { return false; }
    @Override public boolean delete(int id) { return false; }
}