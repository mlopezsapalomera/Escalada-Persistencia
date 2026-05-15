package controller;

import model.entidades.*;
import model.persistencia.conexio_db;
import view.ViaView;
import model.dao.DAOFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViaController {
    private ViaView viaView = new ViaView();

    public void gestionarVies() {
        Scanner sc = new Scanner(System.in);
        int opcio;
        do {
            viaView.mostrarMenu();
            opcio = sc.nextInt(); sc.nextLine();
            switch (opcio) {
                case 1:
                    crearNovaVia(sc);
                    break;
                case 2:
                    llistarViesDetallades();
                    break;
                case 3:
                    System.out.print("ID de l'escola per veure vies APTE: ");
                    int idEsc = sc.nextInt();
                    mostrarViesDisponiblesPerEscola(idEsc);
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
            }
        } while (opcio != 0);
    }

    private void crearNovaVia(Scanner sc) {
        // Carreguem dades necessàries per als selectors de la vista
        List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
        List<Sector> sectors = new SectorController().llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO().getAll();
        
        if (sectors.isEmpty() || escaladors.isEmpty()) {
            System.out.println("Error: Necessites sectors i escaladors creats prèviament.");
            return;
        }

        Via v = viaView.dadesNovaVia(sc, escoles, sectors, escaladors);
        
        // Validació del grau segons estil abans d'insertar
        if (validarGrau(v.getGrauGlobal(), v.getEstil())) {
            if (crearVia(v)) {
                System.out.println("Via guardada correctament!");
            }
        }
    }

    public boolean crearVia(Via v) {
        String sqlGeneral = "INSERT INTO vies (id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, data_finalitzacio_estat, tipus_roca, tipus_via, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();

        try {
            conn.setAutoCommit(false); // Inici transacció

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
                    } else if (!v.getLlistaLlargs().isEmpty()) {
                        insertarLlargs(v, conn);
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("Error en transacció: " + ex.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) { return false; }
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

    public void llistarViesDetallades() {
        String sql = "SELECT v.nom, v.grau_global, e.nom AS nom_escola, esc.nom AS nom_escalador " +
                     "FROM vies v JOIN escoles e ON v.id_escola = e.id JOIN escaladors esc ON v.id_creador = esc.id";
        conexio_db.comprobarConexion();
        try (Statement stmt = conexio_db.getConn().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- LLISTAT DE VIES ---");
            while (rs.next()) {
                System.out.printf("Via: %-15s | Grau: %-4s | Escola: %-15s | Creador: %s%n",
                    rs.getString("nom"), rs.getString("grau_global"), rs.getString("nom_escola"), rs.getString("nom_escalador"));
            }
        } catch (SQLException ex) { System.err.println("Error: " + ex.getMessage()); }
    }

    public void mostrarViesDisponiblesPerEscola(int idEscola) {
        String sql = "SELECT nom, grau_global FROM vies WHERE id_escola = ? AND estat = 'apte'";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, idEscola);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nVies disponibles (APTE):");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("nom") + " (" + rs.getString("grau_global") + ")");
                }
            }
        } catch (SQLException ex) { System.err.println("Error: " + ex.getMessage()); }
    }

    private boolean validarGrau(String grau, Via.Estil estil) {
        String regex = "^[4-9][abc]?\\+?$"; 
        if (!grau.matches(regex)) {
            System.out.println("Format de grau incorrecte (4 a 9c+).");
            return false;
        }
        if (estil == Via.Estil.GEL && grau.compareTo("8b") > 0) {
            System.out.println("Màxim grau en gel és 8b.");
            return false;
        }
        return true;
    }
}