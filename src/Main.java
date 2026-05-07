import javax.swing.SwingUtilities;
import view.VistaEscuela;

public class Main {
    public static void main(String[] args) {
        // Punto de entrada de la interfaz grafica.
        SwingUtilities.invokeLater(() -> new VistaEscuela().setVisible(true));
    }
}