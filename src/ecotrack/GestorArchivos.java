package ecotrack;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class GestorArchivos {

    // Método principal para guardar todo antes de cerrar la aplicación 
    public static void guardarTodo(ListaCircular lista, Map<String, Zona> zonas, String rRes, String rZon) {
        guardarResiduos(lista, rRes);
        guardarZonas(zonas, rZon);
    }

    private static void guardarResiduos(ListaCircular lista, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            Iterator<Residuo> it = lista.iteradorAdelante(); // Uso de iterador propio [cite: 85]
            while (it.hasNext()) {
                Residuo r = it.next();
                // Formato: ID, nombre, tipo, peso, fecha, zona, prioridad [cite: 57]
                bw.write(r.getId() + ";" + r.getNombre() + ";" + r.getTipo() + ";" + 
                         r.getPeso() + ";" + r.getFechaRecoleccion() + ";" + 
                         r.getZona() + ";" + r.getPrioridad());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static void guardarZonas(Map<String, Zona> zonas, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (Zona z : zonas.values()) {
                // Guarda estadísticas: nombre, recolectados y pendientes [cite: 65]
                bw.write(z.getNombre() + ";" + z.getRecolectados() + ";" + z.getPendientes());
                bw.newLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- MÉTODOS DE CARGA (Indispensables para el inicio del sistema ) ---

    public static void cargarResiduos(ListaCircular lista, String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length == 7) {
                    Residuo r = new Residuo(
                        Integer.parseInt(p[0]), p[1], p[2], 
                        Double.parseDouble(p[3]), p[4], p[5], 
                        Integer.parseInt(p[6])
                    );
                    lista.insertar(r); // Inserta en la lista propia [cite: 83]
                }
            }
        } catch (IOException | NumberFormatException e) { e.printStackTrace(); }
    }

    public static void cargarZonas(Utilidad utilidad, String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length == 3) {
                    String nombre = p[0];
                    int recolectados = Integer.parseInt(p[1]);
                    int pendientes = Integer.parseInt(p[2]);
                    utilidad.agregarZona(new Zona(nombre, recolectados, pendientes));
                }
            }
        } catch (IOException | NumberFormatException e) { e.printStackTrace(); }
    }
}
