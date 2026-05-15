package controller;

import model.entidades.*;
import model.dao.DAOFactory;
import model.dao.ViaDAO;
import view.ViaView;

import java.util.List;
import java.util.Scanner;

public class ViaController {
    
    private final ViaView viaView = new ViaView();
    private final ViaDAO viaDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getViaDAO();

    public void gestionarVies() {
        Scanner sc = new Scanner(System.in);
        int opcio;
        do {
            viaView.mostrarMenu();
            opcio = sc.nextInt(); sc.nextLine();
            switch (opcio) {
                case 1:
                    crearNovaVia(sc);
                    break;
                case 2:
                    System.out.println("Llistar totes les vies (pendent d'implementar)");
                    break;
                case 3:
                    System.out.print("ID de l'escola: ");
                    int idEsc = sc.nextInt();
                    llistarViesDisponibles(idEsc);
                    break;
                case 0:
                    System.out.println("Tornant...");
                    break;
            }
        } while (opcio != 0);
    }

    private void crearNovaVia(Scanner sc) {
        List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
        List<Sector> sectors = new SectorController().llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO().getAll();

        Via v = viaView.dadesNovaVia(sc, escoles, sectors, escaladors);
        
        if (validarGrau(v.getGrauGlobal(), v.getEstil())) {
            if (viaDAO.create(v)) {
                System.out.println("Via guardada amb èxit!");
            } else {
                System.out.println("Error al guardar la via.");
            }
        }
    }

    private void llistarViesDisponibles(int idEscola) {
        List<Via> vies = viaDAO.getDisponiblesPerEscola(idEscola);
        System.out.println("\n--- VIES DISPONIBLES (APTE) ---");
        for (Via v : vies) {
            System.out.println("- " + v.getNom() + " (" + v.getGrauGlobal() + ")");
        }
    }

    private boolean validarGrau(String grau, Via.Estil estil) {
        String regex = "^[4-9][abc]?\\+?$";
        if (!grau.matches(regex)) {
            System.out.println("Format de grau no vàlid.");
            return false;
        }
        if (estil == Via.Estil.GEL && grau.compareTo("8b") > 0) {
            System.out.println("Màxim grau en gel és 8b.");
            return false;
        }
        return true;
    }
}