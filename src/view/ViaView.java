package view;

import model.entidades.*;
import java.util.List;
import java.util.Scanner;

public class ViaView {

    public void mostrarMenu() {
        System.out.println("\n--- Gestió de Vies ---");
        System.out.println("1. Crear nova Via");
        System.out.println("2. Llistar totes les Vies");
        System.out.println("3. Llistar vies disponibles per Escola");
        System.out.println("4. Modificar una Via");
        System.out.println("5. Eliminar una Via");
        System.out.println("6. Cercar vies per dificultat (rang)");
        System.out.println("7. Cercar vies per estat");
        System.out.println("8. Mostrar vies que han passat a 'Apte' recentment");
        System.out.println("9. Mostrar vies més llargues d'una escola");
        System.out.println("0. Tornar");
        System.out.print("Selecciona: ");
    }

    public Via dadesNovaVia(Scanner sc, List<Escola> escoles, List<Sector> sectors, List<Escalador> escaladors) {
        Via v = new Via();

        System.out.println("\n--- CREAR NOVA VIA ---");
        
        // 1. Triar Escalador (Creador)
        System.out.println("Qui ha creat la via?");
        for (Escalador e : escaladors) System.out.println("["+e.getId()+"] " + e.getNom());
        int idEsc = readInt(sc, "Introdueix l'ID de l'escalador creador: ");
        Escalador esc = new Escalador(); esc.setId(idEsc);
        v.setCreadaPer(esc);

        // 2. Triar Sector
        System.out.println("A quin sector pertany?");
        for (Sector s : sectors) System.out.println("["+s.getId()+"] " + s.getNom());
        int idSec = readInt(sc, "Introdueix l'ID del sector: ");
        Sector sec = new Sector(); sec.setId(idSec);
        v.setSector(sec);
        sc.nextLine(); // Buffer

        // 3. Dades bàsiques
        System.out.print("Nom de la via: ");
        v.setNom(sc.nextLine());
        
        System.out.print("Grau global (ex: 6a, 7b...): ");
        String grau;
        while (true) {
            grau = sc.nextLine().trim();
            if (grau.matches("^[4-9](?:a|a\\+|b|b\\+|c|c\\+)?$")) break;
            System.out.print("Grau no vàlid. Torna-ho a intentar (ex: 6a, 7b+): ");
        }
        v.setGrauGlobal(grau);

        System.out.print("Orientació (N, NE, NO, S, SE, SO, E, O): ");
        while (true) {
            String o = sc.nextLine().trim().toUpperCase();
            try {
                v.setOrientacio(Via.Orientacio.valueOf(o));
                break;
            } catch (IllegalArgumentException ex) {
                System.out.print("Orientació no vàlida. Introdueix una de (N, NE, NO, SE, SO, E, O, S): ");
            }
        }

        System.out.print("Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel): ");
        int tipus = readInt(sc, "Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel): ");
        if(tipus == 1) v.setEstil(Via.Estil.ESPORTIVA);
        else if(tipus == 2) v.setEstil(Via.Estil.CLASSICA);
        else v.setEstil(Via.Estil.GEL);

        // 4. Estat i Data (Requeriment PDF)
        System.out.print("Estat (1-Apte, 2-Construcció, 3-Tancada): ");
        int est = readInt(sc, "Estat (1-Apte, 2-Construcció, 3-Tancada): ");
        if(est == 1) v.setEstat(Via.Estat.APTE);
        else {
            if(est == 2) v.setEstat(Via.Estat.CONSTRUCCIO);
            else v.setEstat(Via.Estat.TANCADA);

            System.out.print("Fins a quina data estarà així? (YYYY-MM-DD): ");
            while (true) {
                try {
                    v.setDataFinalitzacioEstat(java.sql.Date.valueOf(sc.nextLine().trim()));
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.print("Data no vàlida. Introdueix YYYY-MM-DD: ");
                }
            }
        }

        // 5. Si és Esportiva, demanem detalls extres
        if (v.getEstil() == Via.Estil.ESPORTIVA) {
            int llarg = readInt(sc, "Llargada total (5-30 metres): ");
            while (llarg < 5 || llarg > 30) {
                llarg = readInt(sc, "Llargada ha de ser entre 5 i 30. Torna-ho a introduir: ");
            }
            v.setLlargadaTotal(llarg);
            System.out.print("Ancoratges (spits, parabolts, químics): ");
            while (true) {
                String a = sc.nextLine().trim().toLowerCase();
                if (a.equals("spits") || a.equals("parabolts") || a.equals("químics") || a.equals("quimics")) { v.setAncoratges(a); break; }
                System.out.print("Ancoratge no vàlid. Tria entre (spits, parabolts, químics): ");
            }
        }

        return v;
    }

    public Via dadesModificarVia(Via v, Scanner sc, List<Escola> escoles, List<Sector> sectors, List<Escalador> escaladors) {
        System.out.println("\n--- MODIFICAR VIA (deixa buit per mantenir) ---");
        System.out.println("ID: " + v.getId());

        System.out.print("Nom [" + v.getNom() + "]: ");
        String s = sc.nextLine().trim(); if (!s.isEmpty()) v.setNom(s);

        System.out.print("Grau global [" + v.getGrauGlobal() + "]: ");
        String grau = sc.nextLine().trim(); if (!grau.isEmpty()) v.setGrauGlobal(grau);

        System.out.print("Orientació [" + (v.getOrientacio()!=null?v.getOrientacio().name():"-") + "]: ");
        String o = sc.nextLine().trim(); if (!o.isEmpty()) { try { v.setOrientacio(Via.Orientacio.valueOf(o.toUpperCase())); } catch (IllegalArgumentException ignored) {} }

        System.out.print("Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel) [" + (v.getEstil()!=null?v.getEstil().name():"-") + "]: ");
        String t = sc.nextLine().trim(); if (!t.isEmpty()) {
            try {
                int tipus = Integer.parseInt(t);
                if(tipus == 1) v.setEstil(Via.Estil.ESPORTIVA);
                else if(tipus == 2) v.setEstil(Via.Estil.CLASSICA);
                else v.setEstil(Via.Estil.GEL);
            } catch (Exception ignored) {}
        }

        System.out.print("Estat (1-Apte,2-Construcció,3-Tancada) [" + (v.getEstat()!=null?v.getEstat().name():"-") + "]: ");
        String es = sc.nextLine().trim(); if (!es.isEmpty()) {
            try { int est = Integer.parseInt(es); if (est==1) v.setEstat(Via.Estat.APTE); else if (est==2) v.setEstat(Via.Estat.CONSTRUCCIO); else v.setEstat(Via.Estat.TANCADA); } catch (Exception ignored) {}
        }

        if (v.getEstil() == Via.Estil.ESPORTIVA) {
            System.out.print("Llargada total [" + v.getLlargadaTotal() + "]: ");
            String L = sc.nextLine().trim(); if (!L.isEmpty()) { try { v.setLlargadaTotal(Integer.parseInt(L)); } catch(Exception ignored) {} }
            System.out.print("Ancoratges [" + (v.getAncoratges()!=null?v.getAncoratges():"-") + "]: ");
            String a = sc.nextLine().trim(); if (!a.isEmpty()) v.setAncoratges(a);
        }

        return v;
    }

    public int readViaId(Scanner sc) {
        while (true) {
            System.out.print("ID via: ");
            String l = sc.nextLine().trim();
            try { return Integer.parseInt(l); } catch (Exception ex) { System.out.print("Entrada invàlida. "); }
        }
    }

    public boolean confirmacio(Scanner sc, String missatge) {
        System.out.print(missatge + " (s/n): ");
        String r = sc.nextLine().trim().toLowerCase();
        return r.equals("s") || r.equals("si");
    }

    private int readInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.print("Entrada no vàlida. Torna-ho a intentar. ");
            }
        }
    }
}