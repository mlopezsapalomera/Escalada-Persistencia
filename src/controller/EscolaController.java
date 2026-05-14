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
            opcio = scanner.nextInt();
            scanner.nextLine(); // Consumir newline

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
        Escola escola = escolaView.dadesCrearEscola(scanner);
        if (escolaDAO.create(escola)) {
            System.out.println("Escola creada correctament.");
        } else {
            System.out.println("Error en crear l'escola.");
        }
    }

    private void modificarEscola(Scanner scanner) {
        System.out.print("Introdueix l'ID de l'escola a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
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
        int id = scanner.nextInt();
        scanner.nextLine();
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
        int id = scanner.nextInt();
        scanner.nextLine();
        if (escolaDAO.delete(id)) {
            System.out.println("Escola eliminada correctament.");
        } else {
            System.out.println("Error en eliminar l'escola.");
        }
    }
}