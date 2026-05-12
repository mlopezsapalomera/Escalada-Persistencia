package controller;

import model.entidades.*;
import model.persistencia.conexio_db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViaController {

    // ==========================================
    // 1. CREAR VIA (amb transacció)
    // ==========================================
    public boolean crearVia(Via v) {
        String sqlVia = "INSERT INTO vies " +
                "(id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, tipus_roca, tipus_via, restriccions) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) {
            System.err.println("ERROR: No hi ha connexió amb la base de dades.");
            return false;
        }

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sqlVia, Statement.RETURN_GENERATED_KEYS)) {

                // id_sector
                pstmt.setInt(1, v.getSector().getId());

                // id_escola (a través del sector)
                pstmt.setInt(2, v.getSector().getEscola().getId());

                // id_creador
                pstmt.setInt(3, v.getCreadaPer().getId());

                // nom
                pstmt.setString(4, v.getNom());

                // grau_global: dificultat si és esportiva, "N/A" si no
                String grau = (v.getEstil() == Via.Estil.ESPORTIVA)
                        ? v.getDificultatEsportiva()
                        : "N/A";
                pstmt.setString(5, grau);

                // orientacio → minúscules per coincidir amb l'ENUM de la BD
                pstmt.setString(6, v.getOrientacio().name().toLowerCase());

                // estat → minúscules ('apte', 'construccio', 'tancada')
                pstmt.setString(7, v.getEstat().name().toLowerCase());

                // tipus_roca → minúscules
                pstmt.setString(8, v.getTipusRoca().name().toLowerCase());

                // tipus_via → minúscules ('esportiva', 'classica', 'gel')
                pstmt.setString(9, v.getEstil().name().toLowerCase());

                // restriccions
                pstmt.setString(10, v.getRestriccions());

                int files = pstmt.executeUpdate();
                if (files == 0) throw new SQLException("No s'ha pogut crear la via.");

                // Recuperem l'ID generat i l'assignem a l'objecte
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        v.setId(rs.getInt(1));
                    }
                }

                // Inserim els detalls específics segons el tipus
                if (v.getEstil() == Via.Estil.ESPORTIVA) {
                    insertarDetallEsportiva(v, conn);
                } else {
                    // Clàssica o Gel: inserim els llargs si n'hi ha
                    if (!v.getLlistaLlargs().isEmpty()) {
                        insertarLlargs(v, conn);
                    }
                }

                conn.commit();
                System.out.println("LOG: Via '" + v.getNom() + "' creada amb ID " + v.getId());
                return true;

            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Error en la transacció al crear la via: " + ex.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException ex) {
            System.err.println("Error al gestionar la connexió: " + ex.getMessage());
            return false;
        }
    }

    // ==========================================
    // 2. MODIFICAR VIA
    // ==========================================
    public boolean modificarVia(Via v) {
        String sql = "UPDATE vies SET nom=?, grau_global=?, orientacio=?, estat=?, " +
                "tipus_roca=?, tipus_via=?, restriccions=? WHERE id=?";

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, v.getNom());

            String grau = (v.getEstil() == Via.Estil.ESPORTIVA)
                    ? v.getDificultatEsportiva()
                    : "N/A";
            pstmt.setString(2, grau);
            pstmt.setString(3, v.getOrientacio().name().toLowerCase());
            pstmt.setString(4, v.getEstat().name().toLowerCase());
            pstmt.setString(5, v.getTipusRoca().name().toLowerCase());
            pstmt.setString(6, v.getEstil().name().toLowerCase());
            pstmt.setString(7, v.getRestriccions());
            pstmt.setInt(8, v.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al modificar la via: " + ex.getMessage());
            return false;
        }
    }

    // ==========================================
    // 3. LLISTAR UNA VIA PER ID
    // ==========================================
    public Via llistarVia(int id) {
        String sql = "SELECT * FROM vies WHERE id = ?";

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVia(rs);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error al cercar la via: " + ex.getMessage());
        }
        return null;
    }

    // ==========================================
    // 4. LLISTAR TOTES LES VIES
    // ==========================================
    public List<Via> llistarTotesVies() {
        String sql = "SELECT * FROM vies";
        List<Via> llista = new ArrayList<>();

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return llista;

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                llista.add(mapResultSetToVia(rs));
            }

        } catch (SQLException ex) {
            System.err.println("Error al llistar les vies: " + ex.getMessage());
        }
        return llista;
    }

    // ==========================================
    // 5. LLISTAR VIES PER SECTOR
    // ==========================================
    public List<Via> llistarViesPerSector(int idSector) {
        String sql = "SELECT * FROM vies WHERE id_sector = ?";
        List<Via> llista = new ArrayList<>();

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return llista;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idSector);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    llista.add(mapResultSetToVia(rs));
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error al llistar vies per sector: " + ex.getMessage());
        }
        return llista;
    }

    // ==========================================
    // 6. ELIMINAR VIA
    // ==========================================
    public boolean eliminarVia(int id) {
        // ON DELETE CASCADE a la BD elimina automàticament
        // els detalls_esportiva i llargs associats
        String sql = "DELETE FROM vies WHERE id = ?";

        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al eliminar la via: " + ex.getMessage());
            return false;
        }
    }

    // ==========================================
    // MÈTODES PRIVATS D'AJUDA
    // ==========================================

    /**
     * Converteix una fila del ResultSet en un objecte Via.
     * Assigna només els IDs de sector, escola i creador (sense fer JOIN).
     */
    private Via mapResultSetToVia(ResultSet rs) throws SQLException {
        Via v = new Via();
        v.setId(rs.getInt("id"));
        v.setNom(rs.getString("nom"));
        v.setDificultatEsportiva(rs.getString("grau_global"));
        v.setOrientacio(Via.Orientacio.valueOf(rs.getString("orientacio").toUpperCase()));
        v.setEstat(Via.Estat.valueOf(rs.getString("estat").toUpperCase()));
        v.setTipusRoca(Via.TipusRoca.valueOf(rs.getString("tipus_roca").toUpperCase()));
        v.setEstil(Via.Estil.valueOf(rs.getString("tipus_via").toUpperCase()));
        v.setRestriccions(rs.getString("restriccions"));

        // Assignem objectes buits amb l'ID (sense fer JOIN addicional)
        Sector sector = new Sector();
        sector.setId(rs.getInt("id_sector"));
        Escola escola = new Escola();
        escola.setId(rs.getInt("id_escola"));
        sector.setEscola(escola);
        v.setSector(sector);

        Escalador creador = new Escalador();
        creador.setId(rs.getInt("id_creador"));
        v.setCreadaPer(creador);

        return v;
    }

    /**
     * Insereix els detalls d'una via esportiva a la taula detalls_esportiva.
     * L'ancoratge "químics" necessita l'accent tal com és a la BD.
     */
    private void insertarDetallEsportiva(Via v, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";

        // Obtenim l'ancoratge correctament si la via és ViaEsportiva
        String ancoratgeDb;
        if (v instanceof ViaEsportiva) {
            ancoratgeDb = ((ViaEsportiva) v).getAncoratge().toDb();
        } else {
            ancoratgeDb = v.getAncoratges() != null ? v.getAncoratges().toLowerCase() : null;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, v.getId());
            pstmt.setInt(2, v.getLlargadaTotal());
            pstmt.setString(3, ancoratgeDb);
            pstmt.executeUpdate();
        }
    }

    /**
     * Insereix els llargs d'una via clàssica o de gel a la taula llargs.
     * Usa addBatch() per eficiència.
     */
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
}