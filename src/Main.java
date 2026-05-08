import controller.EscolaController;
import controller.EscaladorController;
import controller.SectorController;
import model.entidades.Escola;
import model.entidades.Escalador;
import model.entidades.Sector;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciem els controladors
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        SectorController sectorCtrl = new SectorController();

        System.out.println("=== TEST DE FUNCIONAMENT DE L'APLICACIÓ ===");

        // --- PART 1: TEST ESCOLA ---
        System.out.println("\n1. Creant Escola...");
        Escola esc = new Escola("Montserrat", "Monistrol", "30 minuts", 500, Escola.Popularitat.ALTA);
        if (escolaCtrl.crearEscola(esc)) {
            System.out.println("[OK] Escola creada.");
        }

        // --- PART 2: TEST ESCALADOR ---
        System.out.println("\n2. Creant Escalador...");
        Escalador e = new Escalador("Biel Soler", "BIEL", 20, "7b", Escalador.Estil.ESPORTIVA);
        if (escaladorCtrl.crearEscalador(e)) {
            System.out.println("[OK] Escalador creat.");
        }

        // --- PART 3: TEST SECTOR (Necessita una Escola) ---
        System.out.println("\n3. Creant Sector...");
        // Busquem la primera escola de la llista per saber el seu ID
        List<Escola> escoles = escolaCtrl.llistarTotesEscoles();
        if (!escoles.isEmpty()) {
            Escola escolaDesti = escoles.get(0); // Agafem la primera que trobem
            
            Sector sec = new Sector();
            sec.setEscola(escolaDesti); // Li assignem l'escola
            sec.setNom("Sector La Vinya Nova");
            sec.setLatitud(new BigDecimal("41.5833"));
            sec.setLongitud(new BigDecimal("1.8333"));
            sec.setAproximacio("15 minuts");
            sec.setNumVies(45);
            sec.setPopularitat(Sector.Popularitat.ALTA);
            sec.setRestriccions("Cap");
            sec.setTipusSector(Sector.TipusSector.MIXTE_ROCA);

            if (sectorCtrl.crearSector(sec)) {
                System.out.println("[OK] Sector creat dins de: " + escolaDesti.getNom());
            }
        } else {
            System.err.println("[ERROR] No hi ha escoles a la DB per crear un sector.");
        }

        // --- PART 4: LLISTATS FINALS ---
        System.out.println("\n=== LLISTAT FINAL A LA BASE DE DADES ===");
        
        System.out.println("\n--- ESCOLES ---");
        for (Escola es : escolaCtrl.llistarTotesEscoles()) {
            System.out.println("ID: " + es.getId() + " | Nom: " + es.getNom());
        }

        System.out.println("\n--- ESCALADORS ---");
        for (Escalador escador : escaladorCtrl.llistarTotsEscaladors()) {
            System.out.println("ID: " + escador.getId() + " | Nom: " + escador.getNom() + " | Alies: " + escador.getAlias());
        }

        System.out.println("\n--- SECTORS ---");
        for (Sector s : sectorCtrl.llistarTotsSectors()) {
            System.out.println("Sector: " + s.getNom() + " | Pertany a Escola ID: " + s.getEscola().getId());
        }
    }
}