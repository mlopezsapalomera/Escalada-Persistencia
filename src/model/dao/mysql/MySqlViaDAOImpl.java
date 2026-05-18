package model.dao.mysql;
import model.dao.ViaDAO;
import model.entidades.Via;
import model.entidades.Llarg;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implementación MySQL del DAO de vías.
public class MySqlViaDAOImpl implements ViaDAO {
    @Override
    public boolean crear(Via v) {
        // Inserta la vía y sus datos derivados (detall esportiva o llargs) en transacción.
        String sqlGeneral = "INSERT INTO vies (id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, data_finalitzacio_estat, tipus_roca, tipus_via, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        // Unicidad: dentro de una escuela no puede repetirse el nombre de vía.
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
                            inserirDetallEsportiva(v, conn);
                        } else if (v.getLlistaLlargs() != null && !v.getLlistaLlargs().isEmpty()) {
                            inserirLlargs(v, conn);
                        }
                        // Actualiza contadores de vías en escuela y sector.
                        String incEscola = "UPDATE escoles SET num_vies = num_vies + 1 WHERE id = ?";
                        String incSector = "UPDATE sectors SET num_vies = num_vies + 1 WHERE id = ?";
                        try (PreparedStatement pIncE = conn.prepareStatement(incEscola)) {
                            pIncE.setInt(1, v.getSector().getEscola().getId());
                            pIncE.executeUpdate();
                        }
                        try (PreparedStatement pIncS = conn.prepareStatement(incSector)) {
                            pIncS.setInt(1, v.getSector().getId());
                            pIncS.executeUpdate();
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

    private void inserirDetallEsportiva(Via v, Connection conn) throws SQLException {
        // Guarda los datos específicos de una vía deportiva.
        String sql = "INSERT INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, v.getId());
            pstmt.setInt(2, v.getLlargadaTotal());
            pstmt.setString(3, v.getAncoratges());
            pstmt.executeUpdate();
        }
    }

    private void inserirLlargs(Via v, Connection conn) throws SQLException {
        // Inserta los llargs de vías clásicas/gel en batch para eficiencia.
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
    public Via obtenirPerId(int id) {
        // Antes de leer, sincroniza cambios automáticos de estado por fecha.
        actualitzarEstats();
        String sql = "SELECT v.*, d.llargada AS det_llargada, d.ancoratge AS det_ancoratge FROM vies v LEFT JOIN detalls_esportiva d ON v.id = d.id_via WHERE v.id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Via v = convertirResultSetEnVia(rs);
                    if (v.getEstil() == Via.Estil.ESPORTIVA) {
                        v.setLlargadaTotal(rs.getInt("det_llargada"));
                        v.setAncoratges(rs.getString("det_ancoratge"));
                    } else {
                        String sqlL = "SELECT * FROM llargs WHERE id_via = ? ORDER BY ordre_llarg";
                        try (PreparedStatement ps2 = conexio_db.getConn().prepareStatement(sqlL)) {
                            ps2.setInt(1, id);
                            try (ResultSet rs2 = ps2.executeQuery()) {
                                List<Llarg> llista = new ArrayList<>();
                                while (rs2.next()) {
                                    Llarg l = new Llarg();
                                    l.setNumeroLlarg(rs2.getInt("ordre_llarg"));
                                    l.setMetres(rs2.getInt("llargada"));
                                    l.setGrau(rs2.getString("grau"));
                                    String an = rs2.getString("ancoratge");
                                    if (an != null) {
                                        try { l.setOrientacio(Via.Orientacio.valueOf(an.toUpperCase())); } catch (IllegalArgumentException ignored) {}
                                    }
                                    llista.add(l);
                                }
                                v.setLlistaLlargs(llista);
                            }
                        }
                    }
                    return v;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obteniendo via por id: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Via> obtenirTots() {
        // Sincroniza estados y lista todas las vías.
        actualitzarEstats();
        String sql = "SELECT * FROM vies";
        List<Via> llista = new ArrayList<>();
        conexio_db.comprobarConexion();
        try (Statement stmt = conexio_db.getConn().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Via v = convertirResultSetEnVia(rs);
                llista.add(v);
            }
        } catch (SQLException ex) {
            System.err.println("Error al llistar vies: " + ex.getMessage());
        }
        return llista;
    }

    @Override
    public boolean actualitzar(Via via) {
        // Actualiza la vía, sus detalles específicos y contadores si cambia de ubicación.
        String sql = "UPDATE vies SET id_sector = ?, id_escola = ?, id_creador = ?, nom = ?, grau_global = ?, orientacio = ?, estat = ?, data_finalitzacio_estat = ?, tipus_roca = ?, tipus_via = ?, restriccions = ? WHERE id = ?";
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        // Lee estado y ubicación actuales para ajustar fechas y contadores.
        String currentSql = "SELECT estat, data_finalitzacio_estat, id_sector, id_escola FROM vies WHERE id = ?";
        String currentEstat = null;
        java.sql.Date currentDate = null;
        int currentSectorId = -1;
        int currentEscolaId = -1;
        try (PreparedStatement psCur = conn.prepareStatement(currentSql)) {
            psCur.setInt(1, via.getId());
            try (ResultSet rsCur = psCur.executeQuery()) {
                if (rsCur.next()) {
                    currentEstat = rsCur.getString("estat");
                    currentDate = rsCur.getDate("data_finalitzacio_estat");
                    currentSectorId = rsCur.getInt("id_sector");
                    currentEscolaId = rsCur.getInt("id_escola");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error llegint estat/ubicacio actual de la via: " + ex.getMessage());
            return false;
        }

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, via.getSector().getId());
                pstmt.setInt(2, via.getSector().getEscola().getId());
                pstmt.setInt(3, via.getCreadaPer().getId());
                pstmt.setString(4, via.getNom());
                pstmt.setString(5, via.getGrauGlobal());
                pstmt.setString(6, via.getOrientacio() != null ? via.getOrientacio().name() : null);
                pstmt.setString(7, via.getEstat() != null ? via.getEstat().name().toLowerCase() : "apte");

                // Si pasa a APTE sin fecha, registra fecha de transición cuando corresponda.
                java.sql.Date dateToSet = via.getDataFinalitzacioEstat();
                boolean wasNotApte = currentEstat != null && !"apte".equalsIgnoreCase(currentEstat);
                if (via.getEstat() == Via.Estat.APTE) {
                    if (dateToSet == null) {
                        if (wasNotApte) dateToSet = new java.sql.Date(System.currentTimeMillis());
                        else dateToSet = currentDate; // conservar fecha si ya estaba en APTE
                    }
                }

                pstmt.setDate(8, dateToSet);
                pstmt.setString(9, via.getTipusRoca() != null ? via.getTipusRoca().toLowerCase() : null);
                pstmt.setString(10, via.getEstil() != null ? via.getEstil().name().toLowerCase() : null);
                pstmt.setString(11, via.getRestriccions());
                pstmt.setInt(12, via.getId());

                int updated = pstmt.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    return false;
                }

                // Ajusta contadores si cambia de escuela o sector.
                int newSectorId = via.getSector().getId();
                int newEscolaId = via.getSector().getEscola().getId();
                String incEscola = "UPDATE escoles SET num_vies = num_vies + 1 WHERE id = ?";
                String incSector = "UPDATE sectors SET num_vies = num_vies + 1 WHERE id = ?";
                String decEscola = "UPDATE escoles SET num_vies = GREATEST(num_vies - 1, 0) WHERE id = ?";
                String decSector = "UPDATE sectors SET num_vies = GREATEST(num_vies - 1, 0) WHERE id = ?";

                if (currentEscolaId != -1 && currentEscolaId != newEscolaId) {
                    try (PreparedStatement d = conn.prepareStatement(decEscola)) { d.setInt(1, currentEscolaId); d.executeUpdate(); }
                    try (PreparedStatement i = conn.prepareStatement(incEscola)) { i.setInt(1, newEscolaId); i.executeUpdate(); }
                }
                if (currentSectorId != -1 && currentSectorId != newSectorId) {
                    try (PreparedStatement d = conn.prepareStatement(decSector)) { d.setInt(1, currentSectorId); d.executeUpdate(); }
                    try (PreparedStatement i = conn.prepareStatement(incSector)) { i.setInt(1, newSectorId); i.executeUpdate(); }
                }

                // Reescribe subtipo (detalles/llargs) para mantener consistencia.
                if (via.getEstil() == Via.Estil.ESPORTIVA) {
                    String delL = "DELETE FROM llargs WHERE id_via = ?";
                    try (PreparedStatement d = conn.prepareStatement(delL)) {
                        d.setInt(1, via.getId());
                        d.executeUpdate();
                    }
                    String upEsp = "REPLACE INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
                    try (PreparedStatement p2 = conn.prepareStatement(upEsp)) {
                        p2.setInt(1, via.getId());
                        p2.setInt(2, via.getLlargadaTotal());
                        p2.setString(3, via.getAncoratges());
                        p2.executeUpdate();
                    }
                } else {
                    String del = "DELETE FROM detalls_esportiva WHERE id_via = ?";
                    try (PreparedStatement d2 = conn.prepareStatement(del)) {
                        d2.setInt(1, via.getId());
                        d2.executeUpdate();
                    }
                    String delL = "DELETE FROM llargs WHERE id_via = ?";
                    try (PreparedStatement d3 = conn.prepareStatement(delL)) {
                        d3.setInt(1, via.getId());
                        d3.executeUpdate();
                    }
                    String sqlLlarg = "INSERT INTO llargs (id_via, ordre_llarg, llargada, grau, ancoratge) VALUES (?, ?, ?, ?, ?)";
                    int ordre = 1;
                    for (Llarg l : via.getLlistaLlargs()) {
                        try (PreparedStatement p4 = conn.prepareStatement(sqlLlarg)) {
                            p4.setInt(1, via.getId());
                            p4.setInt(2, ordre++);
                            p4.setInt(3, l.getLlargada());
                            p4.setString(4, l.getDificultat());
                            p4.setString(5, l.getOrientacio() != null ? l.getOrientacio().name() : null);
                            p4.executeUpdate();
                        }
                    }
                }

                conn.commit();
                return true;
            }
        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("Error al actualitzar via: " + ex.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Elimina vía en transacción y decrementa contadores de escuela/sector.
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        String select = "SELECT id_escola, id_sector FROM vies WHERE id = ?";
        String del = "DELETE FROM vies WHERE id = ?";
        String decEscola = "UPDATE escoles SET num_vies = GREATEST(num_vies - 1, 0) WHERE id = ?";
        String decSector = "UPDATE sectors SET num_vies = GREATEST(num_vies - 1, 0) WHERE id = ?";

        int idEscola = -1, idSector = -1;
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idEscola = rs.getInt("id_escola");
                    idSector = rs.getInt("id_sector");
                } else {
                    return false; // vía no encontrada
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error leyendo vía antes de eliminar: " + ex.getMessage());
            return false;
        }

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pDel = conn.prepareStatement(del)) {
                pDel.setInt(1, id);
                int deleted = pDel.executeUpdate();
                if (deleted == 0) {
                    conn.rollback();
                    return false;
                }
            }

            if (idEscola != -1) {
                try (PreparedStatement pE = conn.prepareStatement(decEscola)) { pE.setInt(1, idEscola); pE.executeUpdate(); }
            }
            if (idSector != -1) {
                try (PreparedStatement pS = conn.prepareStatement(decSector)) { pS.setInt(1, idSector); pS.executeUpdate(); }
            }

            conn.commit();
            return true;
        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("Error al eliminar vía transaccionalmente: " + ex.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    @Override
    public List<Via> obtenirViesDisponiblesPerEscola(int idEscola) {
        List<Via> disponibles = new ArrayList<>();
        // Devuelve solo vías en estado APTE para una escuela.
        actualitzarEstats();
        String sql = "SELECT * FROM vies WHERE id_escola = ? AND estat = 'apte'";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Via v = convertirResultSetEnVia(rs);
                    disponibles.add(v);
                }
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return disponibles;
    }

    @Override
    public List<Via> cercarPerDificultat(String minGrau, String maxGrau) {
        // Busca por rango de grado usando el orden definido en la base de datos.
        List<Via> res = new ArrayList<>();
        actualitzarEstats();

        // Lista de grados según orden técnico acordado en BD.
        String gradeList = "'4','4+','5','5+','6a','6a+','6b','6b+','6c','6c+','7a','7a+','7b','7b+','7c','7c+','8a','8a+','8b','8b+','8c','8c+','9a','9a+','9b','9b+','9c','9c+'";

        // Valida que ambos grados existan en el catálogo.
        String checkSql = "SELECT FIELD(?, " + gradeList + ") AS p1, FIELD(?, " + gradeList + ") AS p2";
        conexio_db.comprobarConexion();
        try (PreparedStatement cp = conexio_db.getConn().prepareStatement(checkSql)) {
            cp.setString(1, minGrau);
            cp.setString(2, maxGrau);
            try (ResultSet cr = cp.executeQuery()) {
                if (cr.next()) {
                    int p1 = cr.getInt("p1");
                    int p2 = cr.getInt("p2");
                    if (p1 == 0 || p2 == 0) {
                        System.err.println("Grau mínim o màxim no vàlid: " + minGrau + " - " + maxGrau);
                        return res;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return res;
        }

        // Permite min/max en cualquier orden usando FIELD + LEAST/GREATEST.
        String sql = "SELECT * FROM vies WHERE FIELD(grau_global, " + gradeList + ") BETWEEN LEAST(FIELD(?, " + gradeList + "), FIELD(?, " + gradeList + ")) AND GREATEST(FIELD(?, " + gradeList + "), FIELD(?, " + gradeList + "))";
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setString(1, minGrau);
            ps.setString(2, maxGrau);
            ps.setString(3, minGrau);
            ps.setString(4, maxGrau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(convertirResultSetEnVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> cercarPerEstat(String estat) {
        List<Via> res = new ArrayList<>();
        // Sincroniza estados y filtra por estado solicitado.
        actualitzarEstats();
        String sql = "SELECT * FROM vies WHERE estat = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setString(1, estat.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(convertirResultSetEnVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> obtenirViesQueHanPassatAPteRecentment(int dies) {
        List<Via> res = new ArrayList<>();
        // Lista vías que pasaron a APTE dentro del rango de días.
        actualitzarEstats();
        String sql = "SELECT * FROM vies WHERE estat = 'apte' AND data_finalitzacio_estat IS NOT NULL AND data_finalitzacio_estat >= DATE_SUB(CURDATE(), INTERVAL ? DAY)";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, dies);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(convertirResultSetEnVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    @Override
    public List<Via> obtenirViesMesLlarguesPerEscola(int idEscola, int limit) {
        List<Via> res = new ArrayList<>();
        // Obtiene las vías más largas de una escuela (deportiva o suma de llargs).
        actualitzarEstats();
        String sql = "SELECT v.* FROM vies v LEFT JOIN detalls_esportiva d ON v.id = d.id_via LEFT JOIN (SELECT id_via, SUM(llargada) AS total_llarg FROM llargs GROUP BY id_via) L ON v.id = L.id_via WHERE v.id_escola = ? ORDER BY COALESCE(d.llargada, L.total_llarg) DESC LIMIT ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.setInt(1, idEscola);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) res.add(convertirResultSetEnVia(rs));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return res;
    }

    // Mapea campos comunes de ResultSet a entidad Via.
    private Via convertirResultSetEnVia(ResultSet rs) throws SQLException {
        Via v = new Via();
        v.setId(rs.getInt("id"));
        // Relación mínima: sector y escuela por ID.
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

        // Relación mínima del creador por ID.
        model.entidades.Escalador cr = new model.entidades.Escalador();
        cr.setId(rs.getInt("id_creador"));
        v.setCreadaPer(cr);

        v.setRestriccions(rs.getString("restriccions"));

        return v;
    }

    @Override
    public void actualitzarEstats() {
        // No borra la fecha para mantener trazabilidad de "pasó a APTE recientemente".
        String sql = "UPDATE vies SET estat = 'apte' WHERE data_finalitzacio_estat IS NOT NULL AND data_finalitzacio_estat <= CURDATE()";
        conexio_db.comprobarConexion();
        try (PreparedStatement ps = conexio_db.getConn().prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estados de vias: " + e.getMessage());
        }
    }
}