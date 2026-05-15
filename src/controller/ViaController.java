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
                    // Necessitem dades per als desplegables de la vista
                    List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
                    // L'ideal seria que el llistarTotsSectors també estigués al DAO, però de moment usem el mètode del controlador
                    List<Sector> sectors = new SectorController().llistarTotsSectors();
                    List<Escalador> escaladors = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO().getAll();
                    
                    if (sectors.isEmpty() || escaladors.isEmpty()) {
                        System.out.println("Error: Necessites tenir almenys un Sector i un Escalador creats!");
                        break;
                    }

                    Via novaVia = viaView.dadesNovaVia(sc, escoles, sectors, escaladors);
                    if (crearVia(novaVia)) {
                        System.out.println("Via guardada amb èxit!");
                    }
                    break;
                case 2:
                    System.out.println("Llistar vies pendent d'implementar...");
                    break;
            }
        } while (opcio != 0);
    }

    public boolean crearVia(Via v) {
        // SQL basat en la teva taula 'vies'
        String sqlGeneral = "INSERT INTO vies (id_sector, id_escola, id_creador, nom, grau_global, orientacio, estat, data_finalitzacio_estat, tipus_roca, tipus_via, restriccions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false); // Inici transacció

            try (PreparedStatement pstmt = conn.prepareStatement(sqlGeneral, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, v.getSector().getId());
                // Busquem l'ID de l'escola a través de l'objecte sector que hem triat
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

                    // 2. Segon pas: Inserir detalls segons el tipus
                    if (v.getEstil() == Via.Estil.ESPORTIVA) {
                        insertarDetallEsportiva(v, conn);
                    } else if (v.getEstil() == Via.Estil.CLASSICA || v.getEstil() == Via.Estil.GEL) {
                        // Si has implementat la llista de llargs, la guardem
                        if (v.getLlistaLlargs() != null && !v.getLlistaLlargs().isEmpty()) {
                            insertarLlargs(v, conn);
                        }
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
        String sql = "INSERT INTO detalls_esportiva (id_via, llargada, ancoratge) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, v.getId());
            pstmt.setInt(2, v.getLlargadaTotal());
            pstmt.setString(3, v.getAncoratges()); // 'spits', 'parabolts', 'químics'
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
}