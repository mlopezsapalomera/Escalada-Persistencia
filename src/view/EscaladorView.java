package view;

import model.entidades.Escalador;
import java.util.List;

public class EscaladorView {
    public void mostrarLlistat(List<Escalador> llista) {
        System.out.println("\n--- LLISTAT D'ESCALADORS ---");
        for (Escalador e : llista) {
            System.out.printf("ID: %d | Alias: %-10s | Nivell: %-5s | Estil: %s%n",
                e.getId(), e.getAlias(), e.getNivellMaxim(), e.getEstilPreferit());
        }
    }
}