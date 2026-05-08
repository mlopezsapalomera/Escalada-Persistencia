import controller.*;
import model.entidades.*;
import view.*; // Importem les noves vistes
import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Inicialitzem Controladors
        EscolaController escolaCtrl = new EscolaController();
        EscaladorController escaladorCtrl = new EscaladorController();
        SectorController sectorCtrl = new SectorController();
        ViaController viaCtrl = new ViaController();

        // 2. Inicialitzem Vistes
        EscolaView escolaView = new EscolaView();
        ViaView viaView = new ViaView();

        System.out.println("--- SISTEMA DE GESTIÓ D'ESCALADA PILLAM LTD. Co. ---");

        // --- EXEMPLE DE FLUX SOLIDIFICAT ---

        // A. Crear Escola i mostrar-la
        Escola montserrat = new Escola("Montserrat", "Monistrol", "A-2 sortida Montserrat", 500, Escola.Popularitat.ALTA);
        escolaCtrl.crearEscola(montserrat);
        
        List<Escola> escoles = escolaCtrl.llistarTotesEscoles();
        escolaView.mostrarLlistatEscoles(escoles);

        if (!escoles.isEmpty()) {
            Escola eDb = escoles.get(0);
            
            // B. Crear Sector (necessari per a la via)
            Sector vNova = new Sector();
            vNova.setEscola(eDb);
            vNova.setNom("Vinya Nova");
            vNova.setTipusSector(Sector.TipusSector.MIXTE_ROCA);
            sectorCtrl.crearSector(vNova);
            
            // Recuperem el sector de la DB per tenir l'ID
            Sector sDb = sectorCtrl.llistarTotsSectors().get(0);

            // C. Crear Escalador
            escaladorCtrl.crearEscalador(new Escalador("Biel", "Bielix", 20, "7b+", Escalador.Estil.ESPORTIVA));
            Escalador bielDb = escaladorCtrl.llistarTotsEscaladors().get(0);

            // D. Crear Via
            Via via = new Via();
            via.setNom("L'esperó de la discòrdia");
            via.setSector(sDb);
            via.setCreadaPer(bielDb);
            via.setEstil(Via.Estil.ESPORTIVA);
            via.setEstat(Via.Estat.APTE);
            via.setLlargadaTotal(25);
            via.setDificultatEsportiva("6b+");
            via.setAncoratges("parabolts");
            via.setTipusRoca("conglomerat");

            if (viaCtrl.crearVia(via)) {
                viaView.mostrarMissatgeExit("Via '" + via.getNom() + "' guardada a la base de dades.");
            } else {
                viaView.mostrarError("No s'ha pogut crear la via.");
            }
        }
    }
}