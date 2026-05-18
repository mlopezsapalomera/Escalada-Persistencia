import controller.*;
import view.MenuView;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Modo interactivo por defecto. No se inyectan entradas automáticas aquí.

        // Inicialización de vistas y controladores
        MenuView menuVista = new MenuView(sc);
        EscolaController controladorEscola = new EscolaController(sc);
        EscaladorController controladorEscalador = new EscaladorController(sc);
        SectorController controladorSector = new SectorController(sc);
        ViaController controladorVia = new ViaController(sc);
        HistorialController controladorHistorial = new HistorialController(sc);

        // Añadimos un hook para desconectar la base de datos al terminar la aplicación
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