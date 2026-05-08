import controller.*;
import view.*;
import model.entidades.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inicialitzem vistes i controladors
        MenuView menu = new MenuView();
        EscolaView escolaView = new EscolaView();
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        EscaladorView escaladorView = new EscaladorView();

        int opcio;
        do {
            opcio = menu.mostrarMenuPrincipal();
            
            switch (opcio) {
                case 1:
                    // LLISTAR ESCOLES
                    List<Escola> escoles = escolaCtrl.llistarTotesEscoles();
                    escolaView.mostrarLlistatEscoles(escoles);
                    break;
                    
                case 2:
                    // PROVA DE CREACIÓ (Aquí podríem demanar dades per teclat)
                    System.out.println("Creant escola de prova...");
                    Escola nova = new Escola("Siurana", "Cornudella", "Pàrquing del poble", 800, Escola.Popularitat.ALTA);
                    escolaCtrl.crearEscola(nova);
                    break;

                case 3:
                    // LLISTAR ESCALADORS
                    escaladorView.mostrarLlistat(escaladorCtrl.llistarTotsEscaladors());
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