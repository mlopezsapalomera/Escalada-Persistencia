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

    public EscolaController() {
        this.escolaDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO();
        this.escolaView = new EscolaView();
    }

    public void gestionarEscoles() {
        Scanner scanner = new Scanner(System.in);
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
                    crearEscola(scanner);
                    break;
                case 2:
                    modificarEscola(scanner);
                    break;
                case 3:
                    llistarUnaEscola(scanner);
                    break;
                case 4:
                    llistarTotesEscoles();
                    break;
                case 6:
                    llistarEscolesAmbRestriccions();
                    break;
                case 5:
                    eliminarEscola(scanner);
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }

    private void crearEscola(Scanner scanner) {
        if (!scanner.hasNextLine()) { System.out.println("Entrada finalitzada."); return; }
        Escola escola = null;
        try {
            escola = escolaView.dadesCrearEscola(scanner);
        } catch (java.util.NoSuchElementException ex) {
            System.out.println("Entrada finalitzada.");
            return;
        }
        if (escolaDAO.create(escola)) {
            System.out.println("Escola creada correctament.");
        } else {
            System.out.println("Error en crear l'escola.");
        }
    }

    private void modificarEscola(Scanner scanner) {
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
        Escola escola = escolaDAO.getById(id);
        if (escola != null) {
            Escola escolaModificada = escolaView.dadesModificarEscola(scanner, escola);
            if (escolaDAO.update(escolaModificada)) {
                System.out.println("Escola modificada correctament.");
            } else {
                System.out.println("Error en modificar l'escola.");
            }
        } else {
            System.out.println("No s'ha trobat cap escola amb aquest ID.");
        }
    }

    private void llistarUnaEscola(Scanner scanner) {
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
        Escola escola = escolaDAO.getById(id);
        if (escola != null) {
            escolaView.mostrarDetalls(escola);
        } else {
            System.out.println("No s'ha trobat cap escola amb aquest ID.");
        }
    }

    private void llistarTotesEscoles() {
        List<Escola> escoles = escolaDAO.getAll();
        escolaView.mostrarLlista(escoles);
    }

    private void eliminarEscola(Scanner scanner) {
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
        if (escolaDAO.delete(id)) {
            System.out.println("Escola eliminada correctament.");
        } else {
            System.out.println("Error en eliminar l'escola.");
        }
    }

    private void llistarEscolesAmbRestriccions() {
        java.util.List<Escola> res = escolaDAO.getEscolesAmbRestriccionsActives();
        if (res.isEmpty()) System.out.println("No hi ha escoles amb restriccions actives.");
        else escolaView.mostrarLlista(res);
    }
}