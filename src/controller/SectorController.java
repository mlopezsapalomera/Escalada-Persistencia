package controller;

import model.entidades.Sector;
import model.entidades.Escola;
import model.dao.DAOFactory;
import model.dao.SectorDAO;
import view.SectorView;

import java.util.List;
import java.util.Scanner;

public class SectorController {

    private final SectorDAO sectorDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getSectorDAO();
    private final SectorView sectorView = new SectorView();

    public void gestionarSectors() {
        Scanner scanner = new Scanner(System.in);
        int opcio;
        do {
            sectorView.mostrarMenu();
            opcio = scanner.nextInt();
            scanner.nextLine();
            switch (opcio) {
                case 1:
                    List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
                    if (escoles.isEmpty()) {
                        System.out.println("Error: No pots crear un sector sense crear primer una Escola!");
                        break;
                    }
                    Sector s = sectorView.dadesCrearSector(scanner, escoles);
                    if (sectorDAO.create(s)) {
                        System.out.println("Sector creat correctament!");
                    } else {
                        System.out.println("Error en crear el sector.");
                    }
                    break;
                case 2:
                    sectorView.mostrarLlista(sectorDAO.getAll());
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }
    
    // Mètode públic per si altres controladors necessiten la llista de sectors
    public List<Sector> llistarTotsSectors() {
        return sectorDAO.getAll();
    }
}