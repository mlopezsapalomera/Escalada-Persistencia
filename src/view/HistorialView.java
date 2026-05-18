package view;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Vista de consola para operaciones del historial de ascensiones.
 */
public class HistorialView {

    /**
     * Solicita el id de escalador.
     * @param sc lector de entrada.
     * @return id válido o -1 si la entrada no es correcta.
     */
    public int llegirIdEscalador(Scanner sc) {
        System.out.print("ID escalador: ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) { return -1; }
    }

    /**
     * Solicita el id de vía.
     * @param sc lector de entrada.
     * @return id válido o -1 si la entrada no es correcta.
     */
    public int llegirIdVia(Scanner sc) {
        System.out.print("ID via: ");
        try { return Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { return -1; }
    }

    /**
     * Solicita una fecha o usa la fecha actual si se deja vacío o es inválida.
     * @param sc lector de entrada.
     * @return fecha de ascensión.
     */
    public Date llegirDataOAvui(Scanner sc) {
        System.out.print("Data ascensio (YYYY-MM-DD) [deixa buit = avui]: ");
        String s = sc.nextLine().trim();
        if (s.isEmpty()) return new Date(System.currentTimeMillis());
        try { return Date.valueOf(s); } catch (Exception e) { return new Date(System.currentTimeMillis()); }
    }

    /**
     * Muestra ascensiones filtradas por escalador.
     * @param rows filas devueltas por la consulta.
     */
    public void mostrarAscensosPerEscalador(List<Map<String, Object>> rows) {
        System.out.println("--- Ascensos (per escalador) ---");
        for (Map<String,Object> r : rows) {
            System.out.println(r.get("data") + " - " + r.get("nom_via") + " (" + r.get("grau") + ") [EscolaID:" + r.get("id_escola") + ", SectorID:" + r.get("id_sector") + "]");
        }
    }

    /**
     * Muestra ascensiones filtradas por vía.
     * @param rows filas devueltas por la consulta.
     */
    public void mostrarAscensosPerVia(List<Map<String, Object>> rows) {
        System.out.println("--- Ascensos (per via) ---");
        for (Map<String,Object> r : rows) {
            System.out.println(r.get("data") + " - " + r.get("nom_escalador") + " (ID:" + r.get("id_escalador") + ")");
        }
    }
}
