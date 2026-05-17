package view;

import model.entidades.Escola;
import model.entidades.Sector;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class SectorView {

    public void mostrarMenu() {
        System.out.println("\n--- Gestió de Sectors ---");
        System.out.println("1. Crear sector");
        System.out.println("2. Llistar tots els sectors");
        System.out.println("3. Mostrar sectors amb més de X vies disponibles");
        System.out.println("0. Tornar al menú principal");
        System.out.print("Selecciona una opció: ");
    }

    public Sector dadesCrearSector(Scanner scanner, List<Escola> escoles) {
        System.out.println("\n--- CREAR NOU SECTOR ---");

        // 1. Mostrar les Escoles disponibles perquè esculli
        System.out.println("Escoles disponibles:");
        for (Escola e : escoles) {
            System.out.println("[" + e.getId() + "] " + e.getNom());
        }
        System.out.print("Introdueix l'ID de l'escola a la qual pertany: ");
        int idEscola = scanner.nextInt();
        scanner.nextLine(); // Netejar el buffer

        // Creem un objecte Escola només amb l'ID per poder-lo relacionar
        Escola escolaEscollida = new Escola();
        escolaEscollida.setId(idEscola);

        // 2. Demanar la resta de dades del sector
        System.out.print("Nom del sector: ");
        String nom = scanner.nextLine();

        System.out.print("Latitud (ex: 41.5833): ");
        BigDecimal lat = new BigDecimal(scanner.nextLine());

        System.out.print("Longitud (ex: 1.8333): ");
        BigDecimal lon = new BigDecimal(scanner.nextLine());

        System.out.print("Aproximació: ");
        String aprox = scanner.nextLine();

        System.out.print("Popularitat (baixa, mitjana, alta): ");
        Sector.Popularitat pop = Sector.Popularitat.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Restriccions (o 'Cap'): ");
        String restriccions = scanner.nextLine();

        System.out.print("Tipus de sector (gel, mixte_roca): ");
        Sector.TipusSector tipus = Sector.TipusSector.valueOf(scanner.nextLine().toUpperCase());

        return new Sector(escolaEscollida, nom, lat, lon, aprox, 0, pop, restriccions, tipus);
    }

    public void mostrarLlista(List<Sector> sectors) {
        if (sectors.isEmpty()) {
            System.out.println("No hi ha sectors creats.");
        } else {
            System.out.println("\n--- Llista de Sectors ---");
            for (Sector s : sectors) {
                System.out.printf("ID: %d | Nom: %-15s | ID Escola: %d | Tipus: %s%n",
                        s.getId(), s.getNom(), s.getEscola().getId(), s.getTipusSector());
            }
        }
    }
}