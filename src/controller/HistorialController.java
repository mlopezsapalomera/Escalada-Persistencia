package controller;
import model.dao.DAOFactory;
import model.dao.HistorialDAO;
import view.HistorialView;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controlador de operaciones de historial de ascensiones.
 */
public class HistorialController {

    private final HistorialDAO historialDAO = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirHistorialDAO();
    private final HistorialView view = new HistorialView();
    private final Scanner scanner;

    /**
     * Construye el controlador con un scanner compartido.
     * @param scanner lector de entrada por consola.
     */
    public HistorialController(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Gestiona una operación de historial según opción introducida.
     */
    public void gestionarHistorial() {
        System.out.println("--- Gestió Historial ---");
        System.out.println("1. Afegir ascensió");
        System.out.println("2. Llistar ascensos per escalador");
        System.out.println("3. Llistar ascensos per via");
        System.out.print("Opció: ");
        String line = scanner.nextLine();
        int op = -1;
        try { op = Integer.parseInt(line.trim()); } catch (Exception ignored) {}
        switch (op) {
            case 1:
                int idE = view.llegirIdEscalador(scanner);
                int idV = view.llegirIdVia(scanner);
                Date d = view.llegirDataOAvui(scanner);
                if (idE <= 0 || idV <= 0) { System.out.println("IDs invàlids."); break; }
                boolean ok = historialDAO.afegirAscensio(idE, idV, d);
                System.out.println(ok ? "Ascensió afegida." : "Error afegint ascensió.");
                break;
            case 2:
                int idEsc = view.llegirIdEscalador(scanner);
                if (idEsc <= 0) { System.out.println("ID invàlid."); break; }
                List<Map<String,Object>> rows = historialDAO.obtenirAscensosPerEscalador(idEsc);
                view.mostrarAscensosPerEscalador(rows);
                break;
            case 3:
                int idVia = view.llegirIdVia(scanner);
                if (idVia <= 0) { System.out.println("ID invàlid."); break; }
                List<Map<String,Object>> rows2 = historialDAO.obtenirAscensosPerVia(idVia);
                view.mostrarAscensosPerVia(rows2);
                break;
            default:
                System.out.println("Opció no vàlida.");
        }
    }
}
