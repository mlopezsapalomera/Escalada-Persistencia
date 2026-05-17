package controller;

import model.dao.DAOFactory;
import model.dao.EscaladorDAO;
import model.entidades.Escalador;
import view.EscaladorView;

import java.util.List;
import java.util.Scanner;

public class EscaladorController {
    private final EscaladorDAO escaladorDAO;
    private final EscaladorView escaladorView;

    public EscaladorController() {
        // Aquesta línia ara hauria de funcionar correctament amb els imports correctes
        this.escaladorDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO();
        this.escaladorView = new EscaladorView();
    }

    public void gestionarEscaladors() {
        Scanner scanner = new Scanner(System.in);
        int opcio;

        do {
            escaladorView.mostrarMenu();
            String line = scanner.nextLine();
            try {
                opcio = Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                opcio = -1;
            }

            switch (opcio) {
                case 1:
                    crearEscalador(scanner);
                    break;
                case 2:
                    modificarEscalador(scanner);
                    break;
                case 3:
                    llistarUnEscalador(scanner);
                    break;
                case 4:
                    llistarTotsEscaladors();
                    break;
                case 6:
                    mostrarAgrupatsPerNivell();
                    break;
                case 5:
                    eliminarEscalador(scanner);
                    break;
                case 0:
                    System.out.println("Tornant al menú principal...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }

    private void crearEscalador(Scanner scanner) {
        Escalador escalador = escaladorView.dadesCrearEscalador(scanner);
        if (escaladorDAO.create(escalador)) {
            System.out.println("Escalador creat correctament.");
        } else {
            System.out.println("Error en crear l'escalador.");
        }
    }

    private void modificarEscalador(Scanner scanner) {
        System.out.print("Introdueix l'ID de l'escalador a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Escalador escalador = escaladorDAO.getById(id);
        if (escalador != null) {
            Escalador escaladorModificat = escaladorView.dadesModificarEscalador(scanner, escalador);
            if (escaladorDAO.update(escaladorModificat)) {
                System.out.println("Escalador modificat correctament.");
            } else {
                System.out.println("Error en modificar l'escalador.");
            }
        } else {
            System.out.println("No s'ha trobat cap escalador amb aquest ID.");
        }
    }

    private void llistarUnEscalador(Scanner scanner) {
        System.out.print("Introdueix l'ID de l'escalador a llistar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Escalador escalador = escaladorDAO.getById(id);
        if (escalador != null) {
            escaladorView.mostrarDetalls(escalador);
        } else {
            System.out.println("No s'ha trobat cap escalador amb aquest ID.");
        }
    }

    private void llistarTotsEscaladors() {
        List<Escalador> escaladors = escaladorDAO.getAll();
        escaladorView.mostrarLlista(escaladors);
    }

    private void eliminarEscalador(Scanner scanner) {
        System.out.print("Introdueix l'ID de l'escalador a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (escaladorDAO.delete(id)) {
            System.out.println("Escalador eliminat correctament.");
        } else {
            System.out.println("Error en eliminar l'escalador.");
        }
    }

    private void mostrarAgrupatsPerNivell() {
        java.util.Map<String, java.util.List<Escalador>> map = escaladorDAO.getEscaladorsGroupedByNivell();
        if (map.isEmpty()) { System.out.println("No hi ha escaladors."); return; }
        System.out.println("\n--- Escaladors agrupats per nivell ---");
        for (String nivell : map.keySet()) {
            System.out.println("Nivell: " + nivell);
            for (Escalador e : map.get(nivell)) {
                System.out.printf("  - ID:%d Nom:%s Alias:%s%n", e.getId(), e.getNom(), e.getAlias());
            }
        }
    }
}