package ecotrack;

public class Zona {
    private String nombre;
    private int residuosRecolectados;
    private int residuosPendientes;

    public Zona(String nombre, int recolectados, int pendientes) {
        this.nombre = nombre;
        this.residuosRecolectados = recolectados;
        this.residuosPendientes = pendientes;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRecolectados() {
        return residuosRecolectados;
    }

    public int getPendientes() {
        return residuosPendientes;
    }

    public void setRecolectados(int valor) {
        this.residuosRecolectados = valor;
    }

    public void setPendientes(int valor) {
        this.residuosPendientes = valor;
    }

    public int calcularUtilidad() {
        return residuosRecolectados - residuosPendientes;
    }

    @Override
    public String toString() {
        return nombre + " | Recolectados: " + residuosRecolectados +
                " | Pendientes: " + residuosPendientes +
                " | Utilidad: " + calcularUtilidad();
    }
}
