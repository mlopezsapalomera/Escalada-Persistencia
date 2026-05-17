package view;

import model.entidades.Escola;
import java.util.List;
import java.util.Scanner;

public class EscolaView {

    public void mostrarMenu() {
        System.out.println("\n--- Gestió d'Escoles ---");
        System.out.println("1. Crear escola");
        System.out.println("2. Modificar escola");
        System.out.println("3. Llistar una escola");
        System.out.println("4. Llistar totes les escoles");
        System.out.println("5. Eliminar escola");
        System.out.println("6. Llistar escoles amb restriccions actives");
        System.out.println("0. Tornar al menú principal");
        System.out.print("Selecciona una opció: ");
    }

    public void mostrarLlista(List<Escola> escoles) {
        if (escoles.isEmpty()) {
            System.out.println("No hi ha escoles per mostrar.");
        } else {
            System.out.println("\n--- Llista d'Escoles ---");
            for (Escola e : escoles) {
                System.out.printf("ID: %d, Nom: %s, Lloc: %s, Popularitat: %s%n",
                        e.getId(), e.getNom(), e.getLloc(), e.getPopularitat());
            }
        }
    }

    public void mostrarDetalls(Escola escola) {
        System.out.println("\n--- Detalls de l'Escola ---");
        System.out.println("ID: " + escola.getId());
        System.out.println("Nom: " + escola.getNom());
        System.out.println("Lloc: " + escola.getLloc());
        System.out.println("Aproximació: " + escola.getAproximacio());
        System.out.println("Número de vies: " + escola.getNumVies());
        System.out.println("Popularitat: " + escola.getPopularitat());
    }

    public Escola dadesCrearEscola(Scanner scanner) {
        String nom = "";
        while (nom.trim().isEmpty()) {
            System.out.print("Nom: ");
            if (!scanner.hasNextLine()) return null;
            nom = scanner.nextLine();
        }

        System.out.print("Lloc (Població): ");
        String lloc = scanner.hasNextLine() ? scanner.nextLine() : "";

        System.out.print("Aproximació: ");
        String aproximacio = scanner.hasNextLine() ? scanner.nextLine() : "";
        System.out.print("Popularitat (BAIXA, MITJANA, ALTA): ");
        String popInput = scanner.nextLine().trim().toUpperCase();
        Escola.Popularitat popularitat;
        try {
            popularitat = Escola.Popularitat.valueOf(popInput);
        } catch (IllegalArgumentException ex) {
            System.out.println("Popularitat no vàlida, s'estableix MITJANA per defecte.");
            popularitat = Escola.Popularitat.MITJANA;
        }

        // El número de vías se inicializa a 0 por defecto en la BBDD.
        return new Escola(nom, lloc, aproximacio, 0, popularitat);
    }

    public Escola dadesModificarEscola(Scanner scanner, Escola escola) {
        System.out.print("Nou nom [" + escola.getNom() + "]: ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) escola.setNom(nom);

        System.out.print("Nou lloc [" + escola.getLloc() + "]: ");
        String lloc = scanner.nextLine();
        if (!lloc.isEmpty()) escola.setLloc(lloc);

        System.out.print("Nova aproximació [" + escola.getAproximacio() + "]: ");
        String aproximacio = scanner.nextLine();
        if (!aproximacio.isEmpty()) escola.setAproximacio(aproximacio);

        System.out.print("Nova popularitat [" + escola.getPopularitat() + "] (BAIXA, MITJANA, ALTA): ");
        String popStr = scanner.nextLine();
        if (!popStr.isEmpty()) escola.setPopularitat(Escola.Popularitat.valueOf(popStr.toUpperCase()));

        return escola;
    }
}