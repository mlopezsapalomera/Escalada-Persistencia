package view;
import model.entidades.*;
import java.util.Arrays;
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
        int idEsc = llegirEnter(sc, "Introdueix l'ID de l'escalador creador: ");
        Escalador esc = new Escalador(); esc.setId(idEsc);
        v.setCreadaPer(esc);

        // 2. Triar Sector
        System.out.println("A quin sector pertany?");
        for (Sector s : sectors) System.out.println("["+s.getId()+"] " + s.getNom());
        int idSec = llegirEnter(sc, "Introdueix l'ID del sector: ");
        Sector sec = null;
        for (Sector s : sectors) { if (s.getId() == idSec) { sec = s; break; } }
        if (sec == null) { sec = new Sector(); sec.setId(idSec); }
        v.setSector(sec);

        // 3. Dades bàsiques
        System.out.print("Nom de la via: ");
        v.setNom(sc.nextLine()); // readInt ja neteja el buffer ara, així que pilla el nom bé
        
        System.out.print("Grau global (ex: 6a, 7b...): ");
        String grau;
        while (true) {
            grau = sc.nextLine().trim();
            if (grauValid(grau)) break;
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

        int tipus = llegirEnter(sc, "Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel): ");
        if(tipus == 1) v.setEstil(Via.Estil.ESPORTIVA);
        else if(tipus == 2) v.setEstil(Via.Estil.CLASSICA);
        else v.setEstil(Via.Estil.GEL);

        // 4. Estat i Data (Requeriment PDF)
        int est = llegirEnter(sc, "Estat (1-Apte, 2-Construcció, 3-Tancada): ");
        if (est == 1) v.setEstat(Via.Estat.APTE);
        else {
            if (est == 2) v.setEstat(Via.Estat.CONSTRUCCIO);
            else v.setEstat(Via.Estat.TANCADA);

            System.out.print("Fins a quina data estarà així? Introdueix YYYY-MM-DD o número de dies des d'avui: ");
            while (true) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) { System.out.print("Introdueix una data o nombre de dies: "); continue; }
                // Si és un número, interpretem com a dies a partir d'avui
                try {
                    int dies = Integer.parseInt(line);
                    long millis = System.currentTimeMillis() + (long)dies * 24 * 60 * 60 * 1000;
                    v.setDataFinalitzacioEstat(new java.sql.Date(millis));
                    break;
                } catch (NumberFormatException nfe) {
                    try {
                        v.setDataFinalitzacioEstat(java.sql.Date.valueOf(line));
                        break;
                    } catch (IllegalArgumentException ex) {
                        System.out.print("Entrada no vàlida. Introdueix YYYY-MM-DD o dies (ex: 7): ");
                    }
                }
            }
        }

        // 5. ENTRADA DE DADES EXTRA SEGONS L'ESTIL
        if (v.getEstil() == Via.Estil.ESPORTIVA) {
            int llarg = llegirEnter(sc, "Llargada total (5-30 metres): ");
            while (llarg < 5 || llarg > 30) {
                llarg = llegirEnter(sc, "Llargada ha de ser entre 5 i 30. Torna-ho a introduir: ");
            }
            v.setLlargadaTotal(llarg);
            
            System.out.print("Ancoratges (spits, parabolts, químics): ");
            while (true) {
                String a = sc.nextLine().trim().toLowerCase();
                if (a.equals("spits") || a.equals("parabolts") || a.equals("químics") || a.equals("quimics")) { 
                    v.setAncoratges(a); 
                    break; 
                }
                System.out.print("Ancoratge no vàlid. Tria entre (spits, parabolts, químics): ");
            }
        } else if (v.getEstil() == Via.Estil.CLASSICA || v.getEstil() == Via.Estil.GEL) {
            int numLlargs = llegirEnter(sc, "Quants llargs (L) té aquesta via?: ");
            v.setLlistaLlargs(new java.util.ArrayList<>());

            for (int i = 1; i <= numLlargs; i++) {
                System.out.println("-> Dades del Llarg L" + i + ":");
                Llarg llarg = new Llarg();
                llarg.setNumeroLlarg(i);

                // metres: validar rang raonable
                int metres = llegirEnterRange(sc, "   Metres del llarg L" + i + ": ", 1, 500);
                llarg.setMetres(metres);

                // grau: validar format (6a, 6a+, 7b...)
                System.out.print("   Grau del llarg L" + i + ": ");
                String grauLlarg;
                while (true) {
                    grauLlarg = sc.nextLine().trim();
                    if (grauValid(grauLlarg)) break;
                    System.out.print("   Grau no vàlid. Torna-ho a intentar (ex: 6a, 7b+): ");
                }
                llarg.setGrau(grauLlarg);

                v.getLlistaLlargs().add(llarg);
            }
        }

        return v;
    }

    // Lista de grados coincidente con la base de datos
    private static final List<String> GRADE_ORDER = Arrays.asList(
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
            "8c","8c+","9a","9a+","9b","9b+","9c","9c+"
    );

    private boolean grauValid(String grau) {
        if (grau == null) return false;
        String g = grau.trim().toLowerCase().replaceAll("\\s+", "");
        return GRADE_ORDER.contains(g);
    }

    public Via dadesModificarVia(Via v, Scanner sc, List<Escola> escoles, List<Sector> sectors, List<Escalador> escaladors) {
        System.out.println("\n--- MODIFICAR VIA (deixa buit per mantenir) ---");
        System.out.println("ID Via a modificar: " + v.getId());

        System.out.print("Nom [" + v.getNom() + "]: ");
        String s = sc.nextLine().trim(); if (!s.isEmpty()) v.setNom(s);

        System.out.print("Grau global [" + v.getGrauGlobal() + "]: ");
        String grau = sc.nextLine().trim(); if (!grau.isEmpty()) {
            if (grauValid(grau)) v.setGrauGlobal(grau); else System.out.println("Grau invàlid. S'ignora el canvi.");
        }

        System.out.print("Orientació [" + (v.getOrientacio() != null ? v.getOrientacio().name() : "-") + "]: ");
        String o = sc.nextLine().trim(); if (!o.isEmpty()) { try { v.setOrientacio(Via.Orientacio.valueOf(o.toUpperCase())); } catch (IllegalArgumentException ignored) {} }

        System.out.print("Tipus de via (1-Esportiva, 2-Clàssica, 3-Gel) [" + (v.getEstil() != null ? v.getEstil().name() : "-") + "]: ");
        String t = sc.nextLine().trim(); if (!t.isEmpty()) {
            try {
                int tipus = Integer.parseInt(t);
                if (tipus == 1) v.setEstil(Via.Estil.ESPORTIVA);
                else if (tipus == 2) v.setEstil(Via.Estil.CLASSICA);
                else if (tipus == 3) v.setEstil(Via.Estil.GEL);
            } catch (Exception ignored) {}
        }

        System.out.print("Estat (1-Apte, 2-Construcció, 3-Tancada) [" + (v.getEstat() != null ? v.getEstat().name() : "-") + "]: ");
        String es = sc.nextLine().trim(); if (!es.isEmpty()) {
            try { 
                int est = Integer.parseInt(es); 
                if (est == 1) v.setEstat(Via.Estat.APTE); 
                else if (est == 2) v.setEstat(Via.Estat.CONSTRUCCIO); 
                else if (est == 3) v.setEstat(Via.Estat.TANCADA); 
            } catch (Exception ignored) {}
        }

        if (v.getEstat() == Via.Estat.CONSTRUCCIO || v.getEstat() == Via.Estat.TANCADA) {
            System.out.print("Data finalització estat (AAAA-MM-DD) o dies a partir d'avui [" + (v.getDataFinalitzacioEstat() != null ? v.getDataFinalitzacioEstat().toString() : "buit") + "]: ");
            String dataStr = sc.nextLine().trim();
            if (!dataStr.isEmpty()) {
                // Acceptar nombre de dies o data
                try {
                    int dies = Integer.parseInt(dataStr);
                    long millis = System.currentTimeMillis() + (long)dies * 24 * 60 * 60 * 1000;
                    v.setDataFinalitzacioEstat(new java.sql.Date(millis));
                } catch (NumberFormatException nfe) {
                    try { v.setDataFinalitzacioEstat(java.sql.Date.valueOf(dataStr)); } catch (Exception ignored) {}
                }
            }
        }

        if (v.getEstil() == Via.Estil.ESPORTIVA) {
            System.out.print("Llargada total [" + v.getLlargadaTotal() + "]: ");
            String L = sc.nextLine().trim(); if (!L.isEmpty()) { try { v.setLlargadaTotal(Integer.parseInt(L)); } catch(Exception ignored) {} }
            System.out.print("Ancoratges [" + (v.getAncoratges() != null ? v.getAncoratges() : "-") + "]: ");
            String a = sc.nextLine().trim(); if (!a.isEmpty()) v.setAncoratges(a);
            
        } else if (v.getEstil() == Via.Estil.CLASSICA || v.getEstil() == Via.Estil.GEL) {
            System.out.print("Vols redefinir els llargs (L) d'aquesta via? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("s") || resp.equals("si")) {
                int numLlargs = llegirEnter(sc, "Quants llargs (L) té ara aquesta via?: ");
                v.setLlistaLlargs(new java.util.ArrayList<>());

                for (int i = 1; i <= numLlargs; i++) {
                    System.out.println("-> Dades del Llarg L" + i + ":");
                    Llarg llarg = new Llarg();
                    llarg.setNumeroLlarg(i);
                    
                    int metres = llegirEnterRange(sc, "   Metres del llarg L" + i + ": ", 1, 500);
                    llarg.setMetres(metres);

                    System.out.print("   Grau del llarg L" + i + ": ");
                    String grauLlarg;
                    while (true) {
                        grauLlarg = sc.nextLine().trim();
                        if (grauLlarg.isEmpty()) { System.out.print("   Grau no vàlid. Torna-ho a intentar (ex: 6a, 7b+): "); continue; }
                        if (grauValid(grauLlarg)) break;
                        System.out.print("   Grau no vàlid. Torna-ho a intentar (ex: 6a, 7b+): ");
                    }
                    llarg.setGrau(grauLlarg);
                    
                    v.getLlistaLlargs().add(llarg);
                }
            }
        }

        return v;
    }

    public int llegirIdVia(Scanner sc) {
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

    
    private int llegirEnter(Scanner sc, String prompt) {
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

    private int llegirEnterRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int v = llegirEnter(sc, prompt);
            if (v < min || v > max) {
                System.out.print("Valor fora de rang (" + min + "-" + max + "). Torna-ho a intentar. ");
                continue;
            }
            return v;
        }
    }
}