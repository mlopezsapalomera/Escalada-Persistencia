package controller;
import model.dao.DAOFactory;
import model.dao.EscolaDAO;
import model.entidades.Escola;
import view.EscolaView;

import java.util.List;
import java.util.Scanner;

public class EscolaController {
    private final EscolaDAO escolaDAO;
    private final EscolaView escolaView;
    private final Scanner scanner;

    public EscolaController(Scanner scanner) {
        this.scanner = scanner;
        this.escolaDAO = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscolaDAO();
        this.escolaView = new EscolaView();
    }

    public void gestionarEscoles() {
        int opcio;

        do {
            escolaView.mostrarMenu();
            if (!scanner.hasNextLine()) {
                System.out.println("Entrada finalitzada.");
                break;
            }
            String line;
            try {
                line = scanner.nextLine();
            } catch (java.util.NoSuchElementException ex) {
                System.out.println("Entrada finalitzada.");
                break;
            }
            try {
                opcio = Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                opcio = -1;
            }

            switch (opcio) {
                case 1:
                    crearEscola();
                    break;
                case 2:
                    modificarEscola();
                    break;
                case 3:
                    llistarUnaEscola();
                    break;
                case 4:
                    llistarTotesEscoles();
                    break;
                case 6:
                    llistarEscolesAmbRestriccions();
                    break;
                case 5:
                    eliminarEscola();
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }

    private void crearEscola() {
        Escola escola = null;
        try {
            escola = escolaView.dadesCrearEscola(scanner);
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Entrada finalitzada.");
            return;
        }
        if (escolaDAO.crear(escola)) {
            System.out.println("Escola creada correctament.");
        } else {
            System.out.println("Error en crear l'escola.");
        }
        
    }

    private void modificarEscola() {
        System.out.print("Introdueix l'ID de l'escola a modificar: ");
        int id;
        try {
            if (!scanner.hasNextLine()) { System.out.println("Entrada finalitzada."); return; }
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("ID invàlid.");
            return;
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Entrada finalitzada.");
            return;
        }
        Escola escola = escolaDAO.obtenirPerId(id);
        if (escola != null) {
            Escola escolaModificada = escolaView.dadesModificarEscola(scanner, escola);
                if (escolaDAO.actualitzar(escolaModificada)) {
                System.out.println("Escola modificada correctament.");
            } else {
                System.out.println("Error en modificar l'escola.");
            }
        } else {
            System.out.println("No s'ha trobat cap escola amb aquest ID.");
        }
    }

    private void llistarUnaEscola() {
        System.out.print("Introdueix l'ID de l'escola a llistar: ");
        int id;
        try {
            if (!scanner.hasNextLine()) { System.out.println("Entrada finalitzada."); return; }
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("ID invàlid.");
            return;
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Entrada finalitzada.");
            return;
        }
        Escola escola = escolaDAO.obtenirPerId(id);
        if (escola != null) {
            escolaView.mostrarDetalls(escola);
        } else {
            System.out.println("No s'ha trobat cap escola amb aquest ID.");
        }
            return;
    }

    private void llistarTotesEscoles() {
        List<Escola> escoles = escolaDAO.obtenirTots();
        escolaView.mostrarLlista(escoles);
    }

    private void eliminarEscola() {
        System.out.print("Introdueix l'ID de l'escola a eliminar: ");
        int id;
        try {
            if (!scanner.hasNextLine()) { System.out.println("Entrada finalitzada."); return; }
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("ID invàlid.");
            return;
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Entrada finalitzada.");
            return;
        }
        if (escolaDAO.eliminar(id)) {
            System.out.println("Escola eliminada correctament.");
        } else {
            System.out.println("Error en eliminar l'escola.");
        }
        
    }

    private void llistarEscolesAmbRestriccions() {
        java.util.List<Escola> res = escolaDAO.obtenirEscolesAmbRestriccionsActives();
        if (res.isEmpty()) System.out.println("No hi ha escoles amb restriccions actives.");
        else escolaView.mostrarLlista(res);
    }
}