import controller.*;
import model.entidades.*;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        SectorController sectorCtrl = new SectorController();
        ViaController viaCtrl = new ViaController();

        System.out.println("--- Verificant Requeriments Pillam Ltd. Co. ---");

        // 1. CREAR I RECUPERAR ESCOLA
        escolaCtrl.crearEscola(new Escola("Montserrat", "Monistrol", "A-2 sortida Montserrat", 500, Escola.Popularitat.ALTA));
        // Molt important: tornem a llistar per tenir l'objecte amb l'ID que ha posat la BBDD
        List<Escola> escoles = escolaCtrl.llistarTotesEscoles();
        if (escoles.isEmpty()) return;
        Escola eDb = escoles.get(0); 

        // 2. CREAR I RECUPERAR ESCALADOR
        escaladorCtrl.crearEscalador(new Escalador("Biel Soler", "Bielix", 20, "7b+", Escalador.Estil.ESPORTIVA));
        List<Escalador> escaladors = escaladorCtrl.llistarTotsEscaladors();
        if (escaladors.isEmpty()) return;
        Escalador bielDb = escaladors.get(0);

        // 3. CREAR I RECUPERAR SECTOR
        Sector vinyaNova = new Sector();
        vinyaNova.setEscola(eDb);
        vinyaNova.setNom("Vinya Nova");
        vinyaNova.setLatitud(new BigDecimal("41.5833"));
        vinyaNova.setLongitud(new BigDecimal("1.8333"));
        vinyaNova.setAproximacio("15 minuts des del pàrquing");
        vinyaNova.setNumVies(50);
        vinyaNova.setPopularitat(Sector.Popularitat.ALTA);
        vinyaNova.setRestriccions("Nidificació de febrer a juny");
        vinyaNova.setTipusSector(Sector.TipusSector.MIXTE_ROCA);

        sectorCtrl.crearSector(vinyaNova);
        // Recuperem el sector per tenir el seu ID
        List<Sector> sectors = sectorCtrl.llistarTotsSectors();
        if (sectors.isEmpty()) return;
        Sector sDb = sectors.get(0);

        // 4. CREAR LA VIA (Ara sí, amb tots els IDs de la BBDD)
        Via viaNova = new Via();
        viaNova.setNom("L'esperó de la discòrdia");
        viaNova.setSector(sDb); // Fem servir sDb que té ID
        viaNova.setEstil(Via.Estil.ESPORTIVA);
        viaNova.setEstat(Via.Estat.APTE);
        viaNova.setCreadaPer(bielDb); // Fem servir bielDb que té ID
        viaNova.setRestriccions("Cap");

        // Dades específiques segons PDF
        viaNova.setLlargadaTotal(25); // Entre 5 i 30m 
        viaNova.setDificultatEsportiva("6b+"); // [cite: 15]
        viaNova.setOrientacio(Via.Orientacio.S); // [cite: 16]
        viaNova.setAncoratges("parabolts"); // 
        viaNova.setTipusRoca("conglomerat"); // 

        if (viaCtrl.crearVia(viaNova)) {
            System.out.println("[OK] Via esportiva creada amb èxit!");
        }

        // 5. MOSTRAR RESULTATS
        System.out.println("\n--- LLISTAT D'ESCALADORS ---");
        escaladorCtrl.llistarTotsEscaladors().forEach(esc -> 
            System.out.println(esc.getAlias() + " - Nivell: " + esc.getNivellMaxim()));
    }
}