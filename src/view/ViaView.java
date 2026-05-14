package view;

import model.entidades.*;
import java.util.List;
import java.util.Scanner;

public class ViaView {

    public void mostrarMenu() {
        System.out.println("\n--- Gestió de Vies ---");
        System.out.println("1. Crear nova Via");
        System.out.println("2. Llistar totes les Vies");
        System.out.println("0. Tornar");
        System.out.print("Selecciona: ");
    }

    public Via dadesNovaVia(Scanner sc, List<Escola> escoles, List<Sector> sectors, List<Escalador> escaladors) {
        Via v = new Via();

        System.out.println("\n--- CREAR NOVA VIA ---");
        
        // 1. Triar Escalador (Creador)
        System.out.println("Qui ha creat la via?");
        for (Escalador e : escaladors) System.out.println("["+e.getId()+"] " + e.getNom());
        int idEsc = sc.nextInt();
        Escalador esc = new Escalador(); esc.setId(idEsc);
        v.setCreadaPer(esc);

        // 2. Triar Sector
        System.out.println("A quin sector pertany?");
        for (Sector s : sectors) System.out.println("["+s.getId()+"] " + s.getNom());
        int idSec = sc.nextInt();
        Sector sec = new Sector(); sec.setId(idSec);
        v.setSector(sec);
        sc.nextLine(); // Buffer

        // 3. Dades bàsiques
        System.out.print("Nom de la via: ");
        v.setNom(sc.nextLine());
        
        System.out.print("Grau global (ex: 6a, 7b...): ");
        v.setGrauGlobal(sc.nextLine());

        System.out.print("Orientació (N, NE, NO, S, SE, SO, E, O): ");
        v.setOrientacio(Via.Orientacio.valueOf(sc.nextLine().toUpperCase()));

        System.out.print("Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel): ");
        int tipus = sc.nextInt(); sc.nextLine();
        if(tipus == 1) v.setEstil(Via.Estil.ESPORTIVA);
        else if(tipus == 2) v.setEstil(Via.Estil.CLASSICA);
        else v.setEstil(Via.Estil.GEL);

        // 4. Estat i Data (Requeriment PDF)
        System.out.print("Estat (1-Apte, 2-Construcció, 3-Tancada): ");
        int est = sc.nextInt(); sc.nextLine();
        if(est == 1) v.setEstat(Via.Estat.APTE);
        else {
            if(est == 2) v.setEstat(Via.Estat.CONSTRUCCIO); // Recorda canviar l'Enum a Via.java!
            else v.setEstat(Via.Estat.TANCADA);
            
            System.out.print("Fins a quina data estarà així? (YYYY-MM-DD): ");
            v.setDataFinalitzacioEstat(java.sql.Date.valueOf(sc.nextLine()));
        }

        // 5. Si és Esportiva, demanem detalls extres
        if (v.getEstil() == Via.Estil.ESPORTIVA) {
            System.out.print("Llargada total (metres): ");
            v.setLlargadaTotal(sc.nextInt()); sc.nextLine();
            System.out.print("Ancoratges (spits, parabolts, químics): ");
            v.setAncoratges(sc.nextLine());
        }

        return v;
    }
}