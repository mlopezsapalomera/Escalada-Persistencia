import controller.*;
import view.MenuView;

public class Main {
    public static void main(String[] args) {
        // Mode interactiu per defecte. No s'injecten inputs automàtics aquí.

        // Inicialització de vistes i controladors
        MenuView menu = new MenuView();
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        SectorController sectorCtrl = new SectorController();
        ViaController viaCtrl = new ViaController();
        HistorialController historialCtrl = new HistorialController();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            model.persistencia.conexio_db.desconectar();
        }));

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
                    viaCtrl.gestionarVies();
                    break;
                case 4:
                    escaladorCtrl.gestionarEscaladors();
                    break;
                case 5:
                    historialCtrl.gestionarHistorial(new java.util.Scanner(System.in)); 
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