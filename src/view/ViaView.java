package view;

import model.entidades.Via;
import java.util.List;

public class ViaView {
    public void mostrarMissatgeExit(String msg) {
        System.out.println("[SUCCESS] " + msg);
    }

    public void mostrarError(String msg) {
        System.err.println("[ERROR] " + msg);
    }

    public void mostrarLlistatVies(List<Via> vies) {
        System.out.println("\n==== RUTES D'ESCALADA ====");
        for (Via v : vies) {
            String detall = (v.getEstil() == Via.Estil.ESPORTIVA) 
                ? "Grau: " + v.getDificultatEsportiva() 
                : "Llargs: " + v.getLlistaLlargs().size();
            
            System.out.printf("Via: %-25s | Estil: %-10s | %s%n", 
                v.getNom(), v.getEstil(), detall);
        }
    }
}