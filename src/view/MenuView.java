package view;

import java.util.Scanner;

public class MenuView {
    private Scanner sc = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n***********************************");
        System.out.println("* MENU PILLAM LTD. CO - ESCALADA  *");
        System.out.println("***********************************");
        System.out.println("1. Llistar Escoles");
        System.out.println("2. Crear una Escola nova");
        System.out.println("3. Llistar Escaladors");
        System.out.println("0. Sortir");
        System.out.println("***********************************");
        System.out.print("Selecciona una opció: ");
        
        // Llegim l'opció de l'usuari
        int opcio = sc.nextInt();
        sc.nextLine(); // Netegem el buffer del scanner
        return opcio;
    }
}