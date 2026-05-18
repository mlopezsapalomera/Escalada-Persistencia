package view;
import java.util.Scanner;

/**
 * Vista principal del menú de la aplicación.
 */
public class MenuView {
    private final Scanner sc;

    /**
     * Construye la vista del menú principal con un scanner compartido.
     * @param sc lector de entrada por consola.
     */
    public MenuView(Scanner sc) {
        this.sc = sc;
    }

    /**
     * Muestra el menú principal y devuelve la opción seleccionada.
     * @return número de opción o -1 si la entrada no es válida.
     */
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
        
        String line = sc.nextLine();
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1; // Opció invàlida
        }
    }
}