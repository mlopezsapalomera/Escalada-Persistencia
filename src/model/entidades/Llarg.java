package model.entidades;

/**
 * Entidad que representa un largo de una vía.
 */
public class Llarg {
    private int numeroLlarg;
    private int llargada;
    private String dificultat;
    private Via.Orientacio orientacio;

    /**
     * Constructor vacío.
     */
    public Llarg() {
    }

    /**
     * Constructor principal de largo.
     * @param llargada longitud del largo en metros.
     * @param dificultat grado o dificultad del largo.
     * @param orientacio orientación del largo.
     */
    public Llarg(int llargada, String dificultat, Via.Orientacio orientacio) {
        this.llargada = llargada;
        this.dificultat = dificultat;
        this.orientacio = orientacio;
    }

    /** @return número de largo dentro de la vía. */
    public int getNumeroLlarg() { return numeroLlarg; }
    /** @return longitud del largo. */
    public int getLlargada() { return llargada; }
    /** @return dificultad del largo. */
    public String getDificultat() { return dificultat; }
    /** @return orientación del largo. */
    public Via.Orientacio getOrientacio() { return orientacio; }

    /** @param numeroLlarg número del largo en la vía. */
    public void setNumeroLlarg(int numeroLlarg) { this.numeroLlarg = numeroLlarg; }
    /** @param metres longitud del largo en metros. */
    public void setMetres(int metres) { this.llargada = metres; }
    /** @return longitud del largo en metros. */
    public int getMetres() { return llargada; }
    /** @param grau dificultad o grado del largo. */
    public void setGrau(String grau) { this.dificultat = grau; }
    /** @return grado del largo. */
    public String getGrau() { return dificultat; }
    /** @param orientacio orientación del largo. */
    public void setOrientacio(Via.Orientacio orientacio) { this.orientacio = orientacio; }
}