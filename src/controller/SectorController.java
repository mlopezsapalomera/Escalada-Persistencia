package controller;
import model.entidades.Sector;
import model.entidades.Escola;
import model.dao.DAOFactory;
import model.dao.SectorDAO;
import view.SectorView;

import java.util.List;
import java.util.Scanner;

/**
 * Controlador de gestión de sectores.
 */
public class SectorController {

    private final SectorDAO sectorDAO = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirSectorDAO();
    private final SectorView sectorView = new SectorView();
    private final Scanner scanner;

    /**
     * Construye el controlador con scanner compartido.
     * @param scanner lector de entrada por consola.
     */
    public SectorController(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Ejecuta el bucle del menú de sectores.
     */
    public void gestionarSectors() {
        int opcio;
        do {
            sectorView.mostrarMenu();
            opcio = Integer.parseInt(scanner.nextLine().trim());
            switch (opcio) {
                case 1:
                    List<Escola> escoles = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscolaDAO().obtenirTots();
                    if (escoles.isEmpty()) {
                        System.out.println("Error: No pots crear un sector sense crear primer una Escola!");
                        break;
                    }
                    Sector s = sectorView.dadesCrearSector(scanner, escoles);
                    if (sectorDAO.crear(s)) {
                        System.out.println("Sector creat correctament!");
                    } else {
                        System.out.println("Error en crear el sector.");
                    }
                    break;
                case 2:
                    sectorView.mostrarLlista(sectorDAO.obtenirTots());
                    break;
                case 3:
                    mostrarSectorsAmbMesX();
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }
    
    /**
     * Devuelve todos los sectores para reutilizarlos desde otros controladores.
     * @return lista completa de sectores.
     */
    public List<Sector> llistarTotsSectors() {
        return sectorDAO.obtenirTots();
    }

    /**
     * Muestra sectores con más de X vías disponibles.
     */
    private void mostrarSectorsAmbMesX() {
        System.out.print("Mostra sectors amb més de quantes vies disponibles? X = ");
        try {
            int x = Integer.parseInt(scanner.nextLine().trim());
            java.util.List<Sector> res = sectorDAO.obtenirSectorsAmbMesDeXViesDisponibles(x);
            sectorView.mostrarLlista(res);
        } catch (Exception ex) { System.out.println("Entrada invàlida."); }
    }
}