package controller;

import model.entidades.Sector;
import model.entidades.Escola;
import model.persistencia.conexio_db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import view.SectorView;
import model.dao.DAOFactory;
import java.util.Scanner;

// (Deixa els imports de dalt que ja tenies)

public class SectorController {

    private SectorView sectorView = new SectorView();

    public void gestionarSectors() {
        Scanner scanner = new Scanner(System.in);
        int opcio;

        do {
            sectorView.mostrarMenu();
            opcio = scanner.nextInt();
            scanner.nextLine();

            switch (opcio) {
                case 1:
                    // Obtenim les escoles usant el DAO que tens súper ben muntat
                    List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
                    
                    if (escoles.isEmpty()) {
                        System.out.println("Error: No pots crear un sector sense crear primer una Escola!");
                        break;
                    }
                    
                    Sector s = sectorView.dadesCrearSector(scanner, escoles);
                    if (crearSector(s)) {
                        System.out.println("Sector creat correctament!");
                    } else {
                        System.out.println("Error en crear el sector.");
                    }
                    break;
                case 2:
                    sectorView.mostrarLlista(llistarTotsSectors());
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }

    public boolean crearSector(Sector s) {
        String sql = "INSERT INTO sectors (id_escola, nom, latitud, longitud, aproximacio, num_vies, popularitat, restriccions, tipus_sector) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        conexio_db.comprobarConexion();
        Connection conn = conexio_db.getConn();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, s.getEscola().getId()); // Clau estrangera a Escola
            pstmt.setString(2, s.getNom());
            pstmt.setBigDecimal(3, s.getLatitud());
            pstmt.setBigDecimal(4, s.getLongitud());
            pstmt.setString(5, s.getAproximacio());
            pstmt.setInt(6, s.getNumVies());
            pstmt.setString(7, s.getPopularitat().name().toLowerCase());
            pstmt.setString(8, s.getRestriccions());
            pstmt.setString(9, s.getTipusSector().name().toLowerCase());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al crear el sector: " + ex.getMessage());
            return false;
        }
    }

    public List<Sector> llistarTotsSectors() {
        String sql = "SELECT * FROM sectors";
        List<Sector> llista = new ArrayList<>();
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Sector s = new Sector();
                s.setId(rs.getInt("id"));
                s.setNom(rs.getString("nom"));
                s.setLatitud(rs.getBigDecimal("latitud"));
                s.setLongitud(rs.getBigDecimal("longitud"));
                s.setPopularitat(Sector.Popularitat.valueOf(rs.getString("popularitat").toUpperCase()));
                s.setTipusSector(Sector.TipusSector.valueOf(rs.getString("tipus_sector").toUpperCase()));
                
                // Assignem l'escola (només el ID de moment)
                Escola e = new Escola();
                e.setId(rs.getInt("id_escola"));
                s.setEscola(e);
                
                llista.add(s);
            }
        } catch (SQLException ex) {
            System.err.println("Error al llistar sectors: " + ex.getMessage());
        }
        return llista;
    }
    
    // Mètode per eliminar sector
    public boolean eliminarSector(int id) {
        String sql = "DELETE FROM sectors WHERE id = ?";
        conexio_db.comprobarConexion();
        try (PreparedStatement pstmt = conexio_db.getConn().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("Error al eliminar sector: " + ex.getMessage());
            return false;
        }
    }
}
