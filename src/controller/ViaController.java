package controller;
import model.entidades.*;
import model.dao.DAOFactory;
import model.dao.ViaDAO;
import view.ViaView;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ViaController {
    
    private final ViaView viaView = new ViaView();
    private final ViaDAO viaDAO = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirViaDAO();
    private final Scanner scanner;

    public ViaController(Scanner scanner) {
        this.scanner = scanner;
    }

    private static final List<String> GRADE_ORDER = Arrays.asList(
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
            "8c","8c+","9a","9a+","9b","9b+","9c","9c+"
    );

    private int rankGrade(String grau) {
        if (grau == null) return -1;
        String g = grau.trim().toLowerCase().replaceAll("\\s+", "");
        return GRADE_ORDER.indexOf(g);
    }

    private boolean grauValid(String grau) { return rankGrade(grau) != -1; }

    private boolean menorOigualGrau(String g1, String g2) {
        int r1 = rankGrade(g1);
        int r2 = rankGrade(g2);
        if (r1 == -1 || r2 == -1) return false;
        return r1 <= r2;
    }

    public void gestionarVies() {
        int opcio;
        do {
            viaView.mostrarMenu();
            String line = scanner.nextLine();
            try {
                opcio = Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                opcio = -1;
            }
            switch (opcio) {
                case 1:
                    crearNovaVia();
                    break;
                case 2:
                    llistarTotesVies();
                    break;
                case 3:
                    System.out.print("ID de l'escola: ");
                    int idEsc;
                    try {
                        idEsc = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("ID invàlid.");
                        break;
                    }
                    llistarViesDisponibles(idEsc);
                    break;
                case 6:
                    cercarPerDificultat();
                    break;
                case 7:
                    cercarPerEstat();
                    break;
                case 8:
                    llistarViesRecentmentApte();
                    break;
                case 9:
                    llistarViesMesLlargues();
                    break;
                case 4:
                    modificarVia();
                    break;
                case 5:
                    eliminarVia();
                    break;
                case 0:
                    System.out.println("Tornant...");
                    break;
            }
        } while (opcio != 0);
    }

    private void crearNovaVia() {
        List<Escola> escoles = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscolaDAO().obtenirTots();
        List<Sector> sectors = new SectorController(scanner).llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscaladorDAO().obtenirTots();

        Via v = viaView.dadesNovaVia(scanner, escoles, sectors, escaladors);
        // Validaciones básicas antes de persistir
        if (v == null) {
            System.out.println("Entrada cancel·lada o invàlida.");
            return;
        }

        // Comprovar creador
        boolean creadorOk = escaladors.stream().anyMatch(e -> e.getId() == (v.getCreadaPer() != null ? v.getCreadaPer().getId() : -1));
        if (!creadorOk) {
            System.out.println("Error: l'escalador creador no existeix. Dona'l d'alta primer.");
            return;
        }

        // Comprovar sector i escola
        if (v.getSector() == null || v.getSector().getId() == 0) {
            System.out.println("Error: sector invàlid.");
            return;
        }
        boolean sectorOk = sectors.stream().anyMatch(s -> s.getId() == v.getSector().getId());
        if (!sectorOk) {
            System.out.println("Error: el sector seleccionat no existeix.");
            return;
        }

        // Comprovar compatibilitat sector <-> tipus via
        try {
            Sector.TipusSector tipus = v.getSector().getTipusSector();
            if (tipus == Sector.TipusSector.GEL && v.getEstil() != Via.Estil.GEL) {
                System.out.println("Error: aquest sector és de gel; la via ha de ser de tipus GEL.");
                return;
            }
            if (tipus == Sector.TipusSector.MIXTE_ROCA && v.getEstil() == Via.Estil.GEL) {
                System.out.println("Error: aquest sector és de roca; no es poden afegir vies de GEL aquí.");
                return;
            }
        } catch (Exception ex) {
            // si el tipus no està definit, no bloquegem la inserció, però avisem
            System.out.println("Avís: no s'ha pogut verificar el tipus del sector (pot faltar informació). Continua amb precaució.");
        }

        if (!validarGrau(v.getGrauGlobal(), v.getEstil())) return;

        if (viaDAO.crear(v)) {
            System.out.println("Via guardada amb èxit!");
        } else {
            System.out.println("Error al guardar la via.");
        }
    }

    private void modificarVia() {
        int id = viaView.llegirIdVia(scanner);
        Via v = viaDAO.obtenirPerId(id);
        if (v == null) { System.out.println("Via no trobada."); return; }
        List<Escola> escoles = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscolaDAO().obtenirTots();
        List<Sector> sectors = new SectorController(scanner).llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.obtenirDAOFactory(DAOFactory.MYSQL).obtenirEscaladorDAO().obtenirTots();
        Via updated = viaView.dadesModificarVia(v, scanner, escoles, sectors, escaladors);
        if (viaDAO.actualitzar(updated)) System.out.println("Via actualitzada."); else System.out.println("Error actualitzant via.");
    }

    private void eliminarVia() {
        int id = viaView.llegirIdVia(scanner);
        Via v = viaDAO.obtenirPerId(id);
        if (v == null) { System.out.println("Via no trobada."); return; }
        boolean ok = viaView.confirmacio(scanner, "Segur que vols eliminar la via '" + v.getNom() + "'?\"");
        if (!ok) { System.out.println("Eliminació cancel·lada."); return; }
        if (viaDAO.eliminar(id)) System.out.println("Via eliminada."); else System.out.println("Error eliminant via.");
    }

    private void llistarTotesVies() {
        viaDAO.actualitzarEstats();
        List<Via> vies = viaDAO.obtenirTots();
        System.out.println("\n--- LLISTAT DE TOTES LES VIES ---");
        for (Via v : vies) {
            String estil = v.getEstil() != null ? v.getEstil().name() : "-";
            String estat = v.getEstat() != null ? v.getEstat().name() : "-";
            System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getGrauGlobal() + " - " + estil + " - " + estat + " (Escola ID: "+v.getSector().getEscola().getId()+", Sector ID: "+v.getSector().getId()+")");
        }
    }

    private void llistarViesDisponibles(int idEscola) {
        viaDAO.actualitzarEstats();
        List<Via> vies = viaDAO.obtenirViesDisponiblesPerEscola(idEscola);
        System.out.println("\n--- VIES DISPONIBLES (APTE) ---");
        for (Via v : vies) {
            System.out.println("- " + v.getNom() + " (" + v.getGrauGlobal() + ")");
        }
    }

    private boolean validarGrau(String grau, Via.Estil estil) {
        if (!grauValid(grau)) {
                System.out.println("Format de grau no vàlid.");
            return false;
        }
        if (estil == Via.Estil.GEL) {
            // comparar amb ordre definit
                if (!menorOigualGrau(grau, "8b")) {
                System.out.println("Màxim grau en gel és 8b.");
                return false;
            }
        }
        return true;
    }

    private void cercarPerDificultat() {
        viaDAO.actualitzarEstats();
        System.out.print("Grau mínim (ex: 6a): ");
        String min = scanner.nextLine().trim();
        System.out.print("Grau màxim (ex: 7b): ");
        String max = scanner.nextLine().trim();
        List<Via> res = viaDAO.cercarPerDificultat(min, max);
        System.out.println("\n--- Resultats cerca per dificultat ---");
        for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getGrauGlobal());
    }

    private void cercarPerEstat() {
        viaDAO.actualitzarEstats();
        System.out.print("Estat (apte, construccio, tancada): ");
        String estat = scanner.nextLine().trim();
        List<Via> res = viaDAO.cercarPerEstat(estat);
        System.out.println("\n--- Resultats cerca per estat: " + estat + " ---");
        for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getEstat());
    }

    private void llistarViesRecentmentApte() {
        viaDAO.actualitzarEstats();
        System.out.print("Nombre de dies enrere (ex: 7): ");
        try {
            int dies = Integer.parseInt(scanner.nextLine().trim());
            List<Via> res = viaDAO.obtenirViesQueHanPassatAPteRecentment(dies);
            System.out.println("\n--- Vies que han passat a Apte en els últims " + dies + " dies ---");
            for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - data: " + v.getDataFinalitzacioEstat());
        } catch (Exception ex) { System.out.println("Entrada invàlida."); }
    }

    private void llistarViesMesLlargues() {
        viaDAO.actualitzarEstats();
        System.out.print("ID de l'escola: ");
        try {
            int idEscola = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Limita quantes vies mostrar (ex: 5): ");
            int limit = Integer.parseInt(scanner.nextLine().trim());
            List<Via> res = viaDAO.obtenirViesMesLlarguesPerEscola(idEscola, limit);
            System.out.println("\n--- Vies més llargues de l'escola ID " + idEscola + " ---");
            for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - llarg: " + v.getLlargadaTotal());
        } catch (Exception ex) { System.out.println("Entrada invàlida."); }
    }
}