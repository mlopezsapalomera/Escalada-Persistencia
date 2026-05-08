package view;

import model.entidades.Escola;
import java.util.List;

public class EscolaView {
    public void mostrarEscola(Escola e) {
        System.out.println("----- DETALLS ESCOLA -----");
        System.out.println("ID: " + e.getId());
        System.out.println("Nom: " + e.getNom());
        System.out.println("Població: " + e.getPoblacio());
        System.out.println("Popularitat: " + e.getPopularitat());
        System.out.println("--------------------------");
    }

    public void mostrarLlistatEscoles(List<Escola> escoles) {
        System.out.println("\n==== LLISTAT D'ESCOLES DISPONIBLES ====");
        for (Escola e : escoles) {
            System.out.printf("[%d] %-20s | %-15s | Pop: %s%n", 
                e.getId(), e.getNom(), e.getPoblacio(), e.getPopularitat());
        }
    }
}