package view;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HistorialView {

    public int llegirIdEscalador(Scanner sc) {
        System.out.print("ID escalador: ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) { return -1; }
    }

    public int llegirIdVia(Scanner sc) {
        System.out.print("ID via: ");
        try { return Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { return -1; }
    }

    public Date llegirDataOAvui(Scanner sc) {
        System.out.print("Data ascensio (YYYY-MM-DD) [deixa buit = avui]: ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return new Date(System.currentTimeMillis());
        try { return Date.valueOf(s); } catch (Exception e) { return new Date(System.currentTimeMillis()); }
    }

    public void mostrarAscensosPerEscalador(List<Map<String, Object>> rows) {
        System.out.println("--- Ascensos (per escalador) ---");
        for (Map<String,Object> r : rows) {
            System.out.println(r.get("data") + " - " + r.get("nom_via") + " (" + r.get("grau") + ") [EscolaID:" + r.get("id_escola") + ", SectorID:" + r.get("id_sector") + "]");
        }
    }

    public void mostrarAscensosPerVia(List<Map<String, Object>> rows) {
        System.out.println("--- Ascensos (per via) ---");
        for (Map<String,Object> r : rows) {
            System.out.println(r.get("data") + " - " + r.get("nom_escalador") + " (ID:" + r.get("id_escalador") + ")");
        }
    }
}
