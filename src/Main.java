import controller.EscolaController;
import model.entidades.Escola;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Test de Funcionamiento: EscolaController ---");

        // Instanciamos el controlador que acabas de rellenar
        EscolaController control = new EscolaController();

        // 1. CREAMOS un objeto de prueba
        // Usamos el constructor: nom, poblacio, aproximacio, numVies, popularitat
        Escola testEscola = new Escola("Siurana", "Cornudella", "15 minuts", 1200, Escola.Popularitat.ALTA);
        
        System.out.println("\nIntentando insertar escuela...");
        if (control.crearEscola(testEscola)) {
            System.out.println("[OK] ¡Escuela guardada en la base de datos!");
        } else {
            System.err.println("[ERROR] No se pudo guardar.");
        }

        // 2. LISTAMOS todo lo que haya en la tabla para ver si aparece
        System.out.println("\n--- Listado Actual en DB ---");
        List<Escola> lista = control.llistarTotesEscoles();
        
        if (lista.isEmpty()) {
            System.out.println("La tabla está vacía.");
        } else {
            for (Escola e : lista) {
                System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNom() + " | Población: " + e.getPoblacio());
            }
        }
    }
}