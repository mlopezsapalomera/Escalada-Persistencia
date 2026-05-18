package controller;

import model.entidades.*;
import model.dao.DAOFactory;
import model.dao.ViaDAO;
import view.ViaView;

import java.util.List;
import java.util.Scanner;

public class ViaController {
    
    private final ViaView viaView = new ViaView();
    private final ViaDAO viaDAO = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getViaDAO();

    public void gestionarVies() {
        Scanner sc = new Scanner(System.in);
        int opcio;
        do {
            viaView.mostrarMenu();
            String line = sc.nextLine();
            try {
                opcio = Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                opcio = -1;
            }
            switch (opcio) {
                case 1:
                    crearNovaVia(sc);
                    break;
                case 2:
                    llistarTotesVies();
                    break;
                case 3:
                    System.out.print("ID de l'escola: ");
                    int idEsc;
                    try {
                        idEsc = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("ID invàlid.");
                        break;
                    }
                    llistarViesDisponibles(idEsc);
                    break;
                case 6:
                    cercarPerDificultat(sc);
                    break;
                case 7:
                    cercarPerEstat(sc);
                    break;
                case 8:
                    llistarViesRecentmentApte(sc);
                    break;
                case 9:
                    llistarViesMesLlargues(sc);
                    break;
                case 4:
                    modificarVia(sc);
                    break;
                case 5:
                    eliminarVia(sc);
                    break;
                case 0:
                    System.out.println("Tornant...");
                    break;
            }
        } while (opcio != 0);
    }

    private void crearNovaVia(Scanner sc) {
        List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
        List<Sector> sectors = new SectorController().llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO().getAll();

        Via v = viaView.dadesNovaVia(sc, escoles, sectors, escaladors);
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

        if (viaDAO.create(v)) {
            System.out.println("Via guardada amb èxit!");
        } else {
            System.out.println("Error al guardar la via.");
        }
    }

    private void modificarVia(Scanner sc) {
        int id = viaView.readViaId(sc);
        Via v = viaDAO.getById(id);
        if (v == null) { System.out.println("Via no trobada."); return; }
        List<Escola> escoles = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscolaDAO().getAll();
        List<Sector> sectors = new SectorController().llistarTotsSectors();
        List<Escalador> escaladors = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getEscaladorDAO().getAll();
        Via updated = viaView.dadesModificarVia(v, sc, escoles, sectors, escaladors);
        if (viaDAO.update(updated)) System.out.println("Via actualitzada."); else System.out.println("Error actualitzant via.");
    }

    private void eliminarVia(Scanner sc) {
        int id = viaView.readViaId(sc);
        Via v = viaDAO.getById(id);
        if (v == null) { System.out.println("Via no trobada."); return; }
        boolean ok = viaView.confirmacio(sc, "Segur que vols eliminar la via '" + v.getNom() + "'?");
        if (!ok) { System.out.println("Eliminació cancel·lada."); return; }
        if (viaDAO.delete(id)) System.out.println("Via eliminada."); else System.out.println("Error eliminant via.");
    }

    private void llistarTotesVies() {
        viaDAO.refreshEstados();
        List<Via> vies = viaDAO.getAll();
        System.out.println("\n--- LLISTAT DE TOTES LES VIES ---");
        for (Via v : vies) {
            String estil = v.getEstil() != null ? v.getEstil().name() : "-";
            String estat = v.getEstat() != null ? v.getEstat().name() : "-";
            System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getGrauGlobal() + " - " + estil + " - " + estat + " (Escola ID: "+v.getSector().getEscola().getId()+", Sector ID: "+v.getSector().getId()+")");
        }
    }

    private void llistarViesDisponibles(int idEscola) {
        viaDAO.refreshEstados();
        List<Via> vies = viaDAO.getDisponiblesPerEscola(idEscola);
        System.out.println("\n--- VIES DISPONIBLES (APTE) ---");
        for (Via v : vies) {
            System.out.println("- " + v.getNom() + " (" + v.getGrauGlobal() + ")");
        }
    }

    private boolean validarGrau(String grau, Via.Estil estil) {
        if (!model.util.GradeUtils.isValid(grau)) {
            System.out.println("Format de grau no vàlid.");
            return false;
        }
        if (estil == Via.Estil.GEL) {
            // comparar amb ordre definit
            if (!model.util.GradeUtils.lessOrEqual(grau, "8b")) {
                System.out.println("Màxim grau en gel és 8b.");
                return false;
            }
        }
        return true;
    }

    private void cercarPerDificultat(Scanner sc) {
        viaDAO.refreshEstados();
        System.out.print("Grau mínim (ex: 6a): ");
        String min = sc.nextLine().trim();
        System.out.print("Grau màxim (ex: 7b): ");
        String max = sc.nextLine().trim();
        List<Via> res = viaDAO.buscarPorDificultat(min, max);
        System.out.println("\n--- Resultats cerca per dificultat ---");
        for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getGrauGlobal());
    }

    private void cercarPerEstat(Scanner sc) {
        viaDAO.refreshEstados();
        System.out.print("Estat (apte, construccio, tancada): ");
        String estat = sc.nextLine().trim();
        List<Via> res = viaDAO.buscarPorEstat(estat);
        System.out.println("\n--- Resultats cerca per estat: " + estat + " ---");
        for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - " + v.getEstat());
    }

    private void llistarViesRecentmentApte(Scanner sc) {
        viaDAO.refreshEstados();
        System.out.print("Nombre de dies enrere (ex: 7): ");
        try {
            int dies = Integer.parseInt(sc.nextLine().trim());
            List<Via> res = viaDAO.getViesQueHanPassatAPteRecentment(dies);
            System.out.println("\n--- Vies que han passat a Apte en els últims " + dies + " dies ---");
            for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - data: " + v.getDataFinalitzacioEstat());
        } catch (Exception ex) { System.out.println("Entrada invàlida."); }
    }

    private void llistarViesMesLlargues(Scanner sc) {
        viaDAO.refreshEstados();
        System.out.print("ID de l'escola: ");
        try {
            int idEscola = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Limita quantes vies mostrar (ex: 5): ");
            int limit = Integer.parseInt(sc.nextLine().trim());
            List<Via> res = viaDAO.getViesMesLlarguesPerEscola(idEscola, limit);
            System.out.println("\n--- Vies més llargues de l'escola ID " + idEscola + " ---");
            for (Via v : res) System.out.println("["+v.getId()+"] " + v.getNom() + " - llarg: " + v.getLlargadaTotal());
        } catch (Exception ex) { System.out.println("Entrada invàlida."); }
    }
}