import controller.*;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Inicialització de vistes i controladors
        MenuView menu = new MenuView();
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        SectorController sectorCtrl = new SectorController();
        ViaController viaCtrl = new ViaController(); // Nou controlador de vies

        int opcio;
        do {
            opcio = menu.mostrarMenuPrincipal();
            
            switch (opcio) {
                case 1:
                    escolaCtrl.gestionarEscoles();
                    break;
                case 2:
                    sectorCtrl.gestionarSectors();
                    break;
                case 3:
                    // Activem la gestió de vies
                    viaCtrl.gestionarVies();
                    break;
                case 4:
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