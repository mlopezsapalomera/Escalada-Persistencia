package view;

import model.entidades.Escalador;

import java.util.List;
import java.util.Scanner;

public class EscaladorView {

    public void mostrarMenu() {
        System.out.println("\n--- Gestió d'Escaladors ---");
        System.out.println("1. Crear escalador");
        System.out.println("2. Modificar escalador");
        System.out.println("3. Llistar un escalador");
        System.out.println("4. Llistar tots els escaladors");
        System.out.println("5. Eliminar escalador");
        System.out.println("6. Mostrar escaladors agrupats per nivell");
        System.out.println("0. Tornar al menú principal");
        System.out.print("Selecciona una opció: ");
    }

    public void mostrarLlista(List<Escalador> escaladors) {
        if (escaladors.isEmpty()) {
            System.out.println("No hi ha escaladors per mostrar.");
        } else {
            System.out.println("\n--- Llista d'Escaladors ---");
            for (Escalador e : escaladors) {
                System.out.printf("ID: %d, Nom: %s, Àlies: %s, Nivell: %s, Estil: %s%n",
                        e.getId(), e.getNom(), e.getAlias(), e.getNivell(), e.getEstilPreferit());
            }
        }
    }

    public void mostrarDetalls(Escalador escalador) {
        System.out.println("\n--- Detalls de l'Escalador ---");
        System.out.println("ID: " + escalador.getId());
        System.out.println("Nom: " + escalador.getNom());
        System.out.println("Àlies: " + escalador.getAlias());
        System.out.println("Edat: " + escalador.getEdat());
        System.out.println("Nivell Màxim: " + escalador.getNivell());
        System.out.println("Via Nivell Màxim: " + escalador.getNomViaNivellMaxim());
        System.out.println("Estil Preferit: " + escalador.getEstilPreferit());
    }

    public Escalador dadesCrearEscalador(Scanner scanner) {
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Àlies: ");
        String alias = scanner.nextLine();
        System.out.print("Edat: ");
        int edat = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nivell (ex: 7a, 8b+): ");
        String nivell = scanner.nextLine();
        System.out.print("Nom de la via del nivell màxim: ");
        String nomVia = scanner.nextLine();
        System.out.print("Estil preferit (ESPORTIVA, CLASSICA, GEL): ");
        String estilInput = scanner.nextLine().trim().toUpperCase();
        Escalador.Estil estil;
        try {
            estil = Escalador.Estil.valueOf(estilInput);
        } catch (IllegalArgumentException ex) {
            System.out.println("Estil no vàlid, s'estableix per defecte a ESPORTIVA.");
            estil = Escalador.Estil.ESPORTIVA;
        }

        return new Escalador(nom, alias, edat, nivell, nomVia, estil);
    }

    public Escalador dadesModificarEscalador(Scanner scanner, Escalador escalador) {
        System.out.print("Nou nom [" + escalador.getNom() + "]: ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) escalador.setNom(nom);

        System.out.print("Nou àlies [" + escalador.getAlias() + "]: ");
        String alias = scanner.nextLine();
        if (!alias.isEmpty()) escalador.setAlias(alias);

        System.out.print("Nova edat [" + escalador.getEdat() + "]: ");
        String edatStr = scanner.nextLine();
        if (!edatStr.isEmpty()) escalador.setEdat(Integer.parseInt(edatStr));

        System.out.print("Nou nivell [" + escalador.getNivell() + "]: ");
        String nivell = scanner.nextLine();
        if (!nivell.isEmpty()) escalador.setNivell(nivell);

        System.out.print("Nova via del nivell Màxim [" + escalador.getNomViaNivellMaxim() + "]: ");
        String nomVia = scanner.nextLine();
        if (!nomVia.isEmpty()) escalador.setNomViaNivellMaxim(nomVia);

        System.out.print("Nou estil preferit [" + escalador.getEstilPreferit() + "] (ESPORTIVA, CLASSICA, GEL): ");
        String estilStr = scanner.nextLine();
        if (!estilStr.isEmpty()) escalador.setEstilPreferit(Escalador.Estil.valueOf(estilStr.toUpperCase()));

        return escalador;
    }
}