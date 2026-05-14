import controller.EscolaController;
import controller.EscaladorController;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Inicialitzem vistes i controladors
        MenuView menu = new MenuView();
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();

        int opcio;
        do {
            opcio = menu.mostrarMenuPrincipal();
            
            switch (opcio) {
                case 1:
                    // GESTIONAR ESCOLES
                    escolaCtrl.gestionarEscoles();
                    break;
                    
                case 2:
                     // GESTIONAR SECTORS (Pendent d'implementar)
                    System.out.println("Funcionalitat de sectors pendent d'implementar.");
                    break;

                case 3:
                    // GESTIONAR VIES (Pendent d'implementar)
                     System.out.println("Funcionalitat de vies pendent d'implementar.");
                    break;

                case 4:
                    // GESTIONAR ESCALADORS
                    escaladorCtrl.gestionarEscaladors();
                    break;

                case 0:
                    System.out.println("Adéu!");
                    break;
                    
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcio != 0);
    }
}