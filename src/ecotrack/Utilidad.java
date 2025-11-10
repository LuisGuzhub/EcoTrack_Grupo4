package ecotrack;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utilidad {

    // Usamos LinkedHashMap para mantener el orden de inserción (se ve más prolijo
    // al mostrar)
    private Map<String, Zona> mapaZonas = new LinkedHashMap<>();

    public void agregarZona(Zona z) {
        mapaZonas.put(z.getNombre(), z);
    }

    public Zona obtenerZona(String nombre) {
        return mapaZonas.get(nombre);
    }

    public Zona obtenerZonaCritica() {
        Zona critica = null;
        for (Zona z : mapaZonas.values()) {
            if (critica == null || z.calcularUtilidad() < critica.calcularUtilidad()) {
                critica = z;
            }
        }
        return critica;
    }

    /**
     * Construye / reconstruye las zonas automáticamente a partir de los residuos de
     * la lista.
     * - Recolectados = cantidad de residuos en esa zona.
     * - Pendientes = 0 por defecto (puedes luego cargar desde zonas.txt si
     * quieres).
     */
    public void construirZonasDesdeResiduos(ListaCircular lista) {
        mapaZonas.clear();

        if (lista == null)
            return;

        Iterator<Residuo> it = lista.iteradorAdelante();
        while (it.hasNext()) {
            Residuo r = it.next();
            String nombreZona = r.getZona();

            Zona zona = mapaZonas.get(nombreZona);
            if (zona == null) {
                zona = new Zona(nombreZona, 0, 0);
                mapaZonas.put(nombreZona, zona);
            }

            zona.setRecolectados(zona.getRecolectados() + 1);
            // si en el futuro manejas "pendientes", aquí también los ajustarías
        }
    }

    // Para consola clásica (si sigues usando el main por texto)
    public void mostrarZonas() {
        System.out.print(generarReporteZonas());
    }

    // Para GUI: devuelve un texto formateado
    public String generarReporteZonas() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Estado de las Zonas ---\n");

        if (mapaZonas.isEmpty()) {
            sb.append("No hay zonas registradas.\n");
            return sb.toString();
        }

        for (Zona z : mapaZonas.values()) {
            sb.append(z).append("\n");
        }

        Zona critica = obtenerZonaCritica();
        if (critica != null) {
            sb.append("\nZona crítica actual: ")
                    .append(critica.getNombre())
                    .append(" (Utilidad ")
                    .append(critica.calcularUtilidad())
                    .append(")\n");
        }

        return sb.toString();
    }
}