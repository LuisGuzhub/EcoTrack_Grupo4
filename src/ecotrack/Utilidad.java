package ecotrack;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Utilidad {
    private Map<String, Zona> mapaZonas = new LinkedHashMap<>(); // [cite: 86]

    public void agregarZona(Zona z) { mapaZonas.put(z.getNombre(), z); }
    public Map<String, Zona> getMapaZonas() { return mapaZonas; }

    public void construirDesdeResiduos(ListaCircular lista) {
        mapaZonas.clear();
        Iterator<Residuo> it = lista.iteradorAdelante();
        while (it.hasNext()) {
            Residuo r = it.next();
            Zona z = mapaZonas.getOrDefault(r.getZona(), new Zona(r.getZona(), 0, 0));
            z.setPendientes(z.getPendientes() + 1);
            mapaZonas.put(r.getZona(), z);
        }
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

    public String generarReporteZonas() {
        StringBuilder sb = new StringBuilder("--- Estado Ambiental de Zonas ---\n");
        for (Zona z : mapaZonas.values()) sb.append(z).append("\n");
        return sb.toString();
    }
}