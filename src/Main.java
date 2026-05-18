import controller.*;
import view.MenuView;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación de gestión de escalada.
 */
public class Main {
    /**
     * Inicializa vistas y controladores y ejecuta el bucle principal del menú.
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        MenuView menuVista = new MenuView(sc);
        EscolaController controladorEscola = new EscolaController(sc);
        EscaladorController controladorEscalador = new EscaladorController(sc);
        SectorController controladorSector = new SectorController(sc);
        ViaController controladorVia = new ViaController(sc);
        HistorialController controladorHistorial = new HistorialController(sc);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            model.persistencia.conexio_db.desconectar();
        }));

        int opcioSeleccionada;
        do {
            opcioSeleccionada = menuVista.mostrarMenuPrincipal();

            switch (opcioSeleccionada) {
                case 1:
                    controladorEscola.gestionarEscoles();
                    break;
                case 2:
                    controladorSector.gestionarSectors();
                    break;
                case 3:
                    controladorVia.gestionarVies();
                    break;
                case 4:
                    controladorEscalador.gestionarEscaladors();
                    break;
                case 5:
                    controladorHistorial.gestionarHistorial();
                    break;
                case 0:
                    System.out.println("Adéu!");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (opcioSeleccionada != 0);
    }
}