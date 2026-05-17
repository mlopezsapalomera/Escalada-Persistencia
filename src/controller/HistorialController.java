package controller;

import model.dao.DAOFactory;
import model.dao.HistorialDAO;
import view.HistorialView;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HistorialController {

    private final HistorialDAO historialDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getHistorialDAO();
    private final HistorialView view = new HistorialView();

    public void gestionarHistorial(Scanner sc) {
        System.out.println("--- Gestió Historial ---");
        System.out.println("1. Afegir ascensió");
        System.out.println("2. Llistar ascensos per escalador");
        System.out.println("3. Llistar ascensos per via");
        System.out.print("Opció: ");
        String line = sc.nextLine();
        int op = -1;
        try { op = Integer.parseInt(line.trim()); } catch (Exception ignored) {}
        switch (op) {
            case 1:
                int idE = view.readEscaladorId(sc);
                int idV = view.readViaId(sc);
                Date d = view.readDateOrToday(sc);
                if (idE <= 0 || idV <= 0) { System.out.println("IDs invàlids."); break; }
                boolean ok = historialDAO.addAscensio(idE, idV, d);
                System.out.println(ok ? "Ascensió afegida." : "Error afegint ascensió.");
                break;
            case 2:
                int idEsc = view.readEscaladorId(sc);
                if (idEsc <= 0) { System.out.println("ID invàlid."); break; }
                List<Map<String,Object>> rows = historialDAO.getAscensosByEscalador(idEsc);
                view.mostrarAscensosPerEscalador(rows);
                break;
            case 3:
                int idVia = view.readViaId(sc);
                if (idVia <= 0) { System.out.println("ID invàlid."); break; }
                List<Map<String,Object>> rows2 = historialDAO.getAscensosByVia(idVia);
                view.mostrarAscensosPerVia(rows2);
                break;
            default:
                System.out.println("Opció no vàlida.");
        }
    }
}
