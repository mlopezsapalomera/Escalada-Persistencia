package view;

import java.util.Scanner;

public class MenuView {
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n***********************************");
        System.out.println("* MENU PILLAM LTD. CO - ESCALADA  *");
        System.out.println("***********************************");
        System.out.println("1. Gestionar Escoles");
        System.out.println("2. Gestionar Sectors");
        System.out.println("3. Gestionar Vies");
        System.out.println("4. Gestionar Escaladors");
        System.out.println("5. Gestionar Historial");
        System.out.println("0. Sortir");
        System.out.println("***********************************");
        System.out.print("Selecciona una opció: ");
        
        // Llegim l'opció de l'usuari amb validació per evitar InputMismatchException
        String line = sc.nextLine();
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1; // Opció invàlida
        }
    }
}