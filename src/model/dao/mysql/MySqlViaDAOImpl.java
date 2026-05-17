package model.dao.mysql;

import model.dao.ViaDAO;
import model.entidades.Via;
import model.entidades.Llarg;
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

        // Unicitat: dins d'una escola no pot haver-hi dues vies amb el mateix nom
        String checkSql = "SELECT COUNT(*) AS cnt FROM vies WHERE id_escola = ? AND LOWER(nom) = LOWER(?)";
        try (PreparedStatement pc = conn.prepareStatement(checkSql)) {
            pc.setInt(1, v.getSector().getEscola().getId());
            pc.setString(2, v.getNom());
            try (ResultSet rc = pc.executeQuery()) {
                if (rc.next() && rc.getInt("cnt") > 0) {
                    System.out.println("Error: ja existeix una via amb aquest nom a la mateixa escola.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error comprovant unicitat de via: " + e.getMessage());
            return false;
        }

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGeneral, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, v.getSector().getId());
                pstmt.setInt(2, v.getSector().getEscola().getId());
                pstmt.setInt(3, v.getCreadaPer().getId());
                pstmt.setString(4, v.getNom());
                pstmt.setString(5, v.getGrauGlobal());
                pstmt.setString(6, v.getOrientacio() != null ? v.getOrientacio().name() : null);
                pstmt.setString(7, v.getEstat() != null ? v.getEstat().name().toLowerCase() : "apte");
                pstmt.setDate(8, v.getDataFinalitzacioEstat());
                pstmt.setString(9, v.getTipusRoca());
                pstmt.setString(10, v.getEstil() != null ? v.getEstil().name().toLowerCase() : null);
                pstmt.setString(11, v.getRestriccions());

                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        v.setId(rs.getInt(1));
                        if (v.getEstil() == Via.Estil.ESPORTIVA) {
                            insertarDetallEsportiva(v, conn);
                        } else if (v.getLlistaLlargs() != null && !v.getLlistaLlargs().isEmpty()) {
                            insertarLlargs(v, conn);
                        }
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Error al crear via (rollback): " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
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
                pstmt.setString(5, llarg.getOrientacio() != null ? llarg.getOrientacio().name() : null);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    @Override
    public Via getById(int id) {
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Via v = mapResultSetToVia(rs);
                    // load subtype details
                    if (v.getEstil() == Via.Estil.ESPORTIVA) {
                        String sqlEsp = "SELECT * FROM detalls_esportiva WHERE id_via = ?";
                        try (PreparedStatement p2 = conexio_db.getConn().prepareStatement(sqlEsp)) {
                            p2.setInt(1, id);
                            try (ResultSet r2 = p2.executeQuery()) {
                                if (r2.next()) {
                                    v.setLlargadaTotal(r2.getInt("llargada"));
                                    v.setAncoratges(r2.getString("ancoratge"));
                                }
                            }
                        }
                    } else {
                        String sqlLlarg = "SELECT * FROM llargs WHERE id_via = ? ORDER BY ordre_llarg";
                        try (PreparedStatement p3 = conexio_db.getConn().prepareStatement(sqlLlarg)) {
                            p3.setInt(1, id);
                            try (ResultSet r3 = p3.executeQuery()) {
                                List<Llarg> llist = new ArrayList<>();
                                while (r3.next()) {
                                    Llarg l = new Llarg(r3.getInt("llargada"), r3.getString("grau"), Via.Orientacio.valueOf(r3.getString("ancoratge") != null ? r3.getString("ancoratge").toUpperCase() : "N"));
                                    llist.add(l);
                                }
                                v.setLlistaLlargs(llist);
                            }
                        }
                    }
                    return v;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtenir via per ID: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Via> getAll() {
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies";
        List<Via> llista = new ArrayList<>();
        conexio_db.comprobarConexion();
        try (Statement stmt = conexio_db.getConn().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Via v = mapResultSetToVia(rs);
                llista.add(v);
            }
        } catch (SQLException ex) {
            System.err.println("Error al llistar vies: " + ex.getMessage());
        }
        return llista;
    }

    @Override
    public boolean update(Via via) {
        String sql = "UPDATE vies SET id_sector = ?, id_escola = ?, id_creador = ?, nom = ?, grau_global = ?, orientacio = ?, estat = ?, data_finalitzacio_estat = ?, tipus_roca = ?, tipus_via = ?, restriccions = ? WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, via.getSector().getId());
            pstmt.setInt(2, via.getSector().getEscola().getId());
            pstmt.setInt(3, via.getCreadaPer().getId());
            pstmt.setString(4, via.getNom());
            pstmt.setString(5, via.getGrauGlobal());
            pstmt.setString(6, via.getOrientacio() != null ? via.getOrientacio().name() : null);
            pstmt.setString(7, via.getEstat() != null ? via.getEstat().name().toLowerCase() : "apte");
            pstmt.setDate(8, via.getDataFinalitzacioEstat());
            pstmt.setString(9, via.getTipusRoca() != null ? via.getTipusRoca().toLowerCase() : null);
            pstmt.setString(10, via.getEstil() != null ? via.getEstil().name().toLowerCase() : null);
            pstmt.setString(11, via.getRestriccions());
            pstmt.setInt(12, via.getId());

            int updated = pstmt.executeUpdate();
            if (updated == 0) return false;

            // subtype handling: delete and reinsert llargs/detalls for simplicity
            if (via.getEstil() == Via.Estil.ESPORTIVA) {
                String delL = "DELETE FROM llargs WHERE id_via = ?";
                try (PreparedStatement d = conexio_db.getConn().prepareStatement(delL)) {
                    d.setInt(1, via.getId());
                    d.executeUpdate();
                }
                String upEsp = "REPLACE INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
                try (PreparedStatement p2 = conexio_db.getConn().prepareStatement(upEsp)) {
                    p2.setInt(1, via.getId());
                    p2.setInt(2, via.getLlargadaTotal());
                    p2.setString(3, via.getAncoratges());
                    p2.executeUpdate();
                }
            } else {
                String del = "DELETE FROM detalls_esportiva WHERE id_via = ?";
                try (PreparedStatement d2 = conexio_db.getConn().prepareStatement(del)) {
                    d2.setInt(1, via.getId());
                    d2.executeUpdate();
                }
                String delL = "DELETE FROM llargs WHERE id_via = ?";
                try (PreparedStatement d3 = conexio_db.getConn().prepareStatement(delL)) {
                    d3.setInt(1, via.getId());
                    d3.executeUpdate();
                }
                String sqlLlarg = "INSERT INTO llargs (id_via, ordre_llarg, llargada, grau, ancoratge) VALUES (?, ?, ?, ?, ?)";
                int ordre = 1;
                for (Llarg l : via.getLlistaLlargs()) {
                    try (PreparedStatement p4 = conexio_db.getConn().prepareStatement(sqlLlarg)) {
                        p4.setInt(1, via.getId());
                        p4.setInt(2, ordre++);
                        p4.setInt(3, l.getLlargada());
                        p4.setString(4, l.getDificultat());
                        p4.setString(5, l.getOrientacio() != null ? l.getOrientacio().name() : null);
                        p4.executeUpdate();
                    }
                }
            }

            return true;
        } catch (SQLException ex) {
            System.err.println("Error al actualitzar via: " + ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM vies WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar via: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Via> getDisponiblesPerEscola(int idEscola) {
        List<Via> disponibles = new ArrayList<>();
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies WHERE id_escola = ? AND estat = 'apte'";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Via v = mapResultSetToVia(rs);
                    disponibles.add(v);
                }
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return disponibles;
    }

    @Override
    public List<Via> buscarPorDificultat(String minGrau, String maxGrau) {
        List<Via> res = new ArrayList<>();
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies WHERE grau_global >= ? AND grau_global <= ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setString(1, minGrau);
            ps.setString(2, maxGrau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(mapResultSetToVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> buscarPorEstat(String estat) {
        List<Via> res = new ArrayList<>();
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies WHERE estat = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setString(1, estat.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(mapResultSetToVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> getViesQueHanPassatAPteRecentment(int dies) {
        List<Via> res = new ArrayList<>();
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT * FROM vies WHERE estat = 'apte' AND data_finalitzacio_estat IS NOT NULL AND data_finalitzacio_estat >= DATE_SUB(CURDATE(), INTERVAL ? DAY)";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, dies);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(mapResultSetToVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> getViesMesLlarguesPerEscola(int idEscola, int limit) {
        List<Via> res = new ArrayList<>();
        // ensure estados are up-to-date
        refreshEstados();
        String sql = "SELECT v.* FROM vies v LEFT JOIN detalls_esportiva d ON v.id = d.id_via LEFT JOIN (SELECT id_via, SUM(llargada) AS total_llarg FROM llargs GROUP BY id_via) L ON v.id = L.id_via WHERE v.id_escola = ? ORDER BY COALESCE(d.llargada, L.total_llarg) DESC LIMIT ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(mapResultSetToVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    // Helper to map common fields
    private Via mapResultSetToVia(ResultSet rs) throws SQLException {
        Via v = new Via();
        v.setId(rs.getInt("id"));
        // set sector (only id)
        model.entidades.Sector s = new model.entidades.Sector();
        s.setId(rs.getInt("id_sector"));
        model.entidades.Escola e = new model.entidades.Escola();
        e.setId(rs.getInt("id_escola"));
        s.setEscola(e);
        v.setSector(s);

        v.setNom(rs.getString("nom"));
        v.setGrauGlobal(rs.getString("grau_global"));
        String orient = rs.getString("orientacio");
        if (orient != null) v.setOrientacio(Via.Orientacio.valueOf(orient.toUpperCase()));
        String estat = rs.getString("estat");
        if (estat != null) v.setEstat(Via.Estat.valueOf(estat.toUpperCase()));
        v.setDataFinalitzacioEstat(rs.getDate("data_finalitzacio_estat"));
        v.setTipusRoca(rs.getString("tipus_roca"));
        String tipus = rs.getString("tipus_via");
        if (tipus != null) v.setEstil(Via.Estil.valueOf(tipus.toUpperCase()));

        // creador id only
        model.entidades.Escalador cr = new model.entidades.Escalador();
        cr.setId(rs.getInt("id_creador"));
        v.setCreadaPer(cr);

        v.setRestriccions(rs.getString("restriccions"));

        return v;
    }

    @Override
    public void refreshEstados() {
        String sql = "UPDATE vies SET estat = 'apte', data_finalitzacio_estat = NULL WHERE data_finalitzacio_estat IS NOT NULL AND data_finalitzacio_estat <= CURDATE()";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al refrescar estados de vias: " + e.getMessage());
        }
    }
}