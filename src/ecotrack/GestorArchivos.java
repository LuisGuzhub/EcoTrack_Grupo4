package ecotrack;

import java.io.*;
import java.util.Iterator;

public class GestorArchivos {

    // Guarda todos los residuos de la lista en un archivo de texto
    public static void guardarResiduos(ListaCircular lista, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {

            Iterator<Residuo> it = lista.iteradorAdelante();
            while (it.hasNext()) {
                Residuo r = it.next();
                // Formato: id;nombre;tipo;peso;fecha;zona;prioridad
                bw.write(r.getId() + ";" +
                        r.getNombre() + ";" +
                        r.getTipo() + ";" +
                        r.getPeso() + ";" +
                        r.getFechaRecoleccion() + ";" +
                        r.getZona() + ";" +
                        r.getPrioridad());
                bw.newLine();
            }

            System.out.println("Residuos guardados en: " + rutaArchivo);

        } catch (IOException e) {
            System.out.println("Error al guardar residuos: " + e.getMessage());
        }
    }

    // Carga residuos desde el archivo y los inserta en la lista circular
    public static void cargarResiduos(ListaCircular lista, String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("Archivo " + rutaArchivo + " no existe. No se cargan residuos.");
            return;
        }

        // Opcional: limpiar la lista si quieres empezar desde cero
        // (por ahora no la limpiamos para que veas qué se suma)

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Esperamos el formato id;nombre;tipo;peso;fecha;zona;prioridad
                String[] partes = linea.split(";");
                if (partes.length == 7) {
                    int id = Integer.parseInt(partes[0]);
                    String nombre = partes[1];
                    String tipo = partes[2];
                    double peso = Double.parseDouble(partes[3]);
                    String fecha = partes[4];
                    String zona = partes[5];
                    int prioridad = Integer.parseInt(partes[6]);

                    Residuo r = new Residuo(id, nombre, tipo, peso, fecha, zona, prioridad);
                    lista.insertar(r);
                }
            }
            System.out.println("Residuos cargados desde: " + rutaArchivo);

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al cargar residuos: " + e.getMessage());
        }
    }
}
