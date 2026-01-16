package ecotrack;

import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Corrección del error mostrado en imagen: System.in es el estándar 
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA ECO-TRACK ===");
        System.out.println("Gestión Inteligente de Residuos Urbanos [cite: 3]");
        System.out.println("----------------------------------------");

        // Estructuras principales (Implementaciones propias obligatorias) 
        ListaCircular listaResiduos = new ListaCircular();
        ColaPrioridad colaRutas = new ColaPrioridad();
        PilaReciclaje pilaReciclaje = new PilaReciclaje();
        Utilidad utilidad = new Utilidad();

        // Archivos de persistencia 
        String rutaResiduos = Paths.get("data", "residuos.txt").toString();
        String rutaZonas = Paths.get("data", "Zonas.txt").toString();

        // Cargar datos iniciales al iniciar el sistema 
        GestorArchivos.cargarResiduos(listaResiduos, rutaResiduos);
        GestorArchivos.cargarZonas(utilidad, rutaZonas);

        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENÚ ECO-TRACK =====");
            System.out.println("1. Registrar residuo");
            System.out.println("2. Mostrar residuos (lista circular doble)");
            System.out.println("3. Encolar para recolección (Prioridad)");
            System.out.println("4. Ver cola de prioridad");
            System.out.println("5. Despachar vehículo");
            System.out.println("6. Enviar a reciclaje (Pila)");
            System.out.println("7. Procesar centro de reciclaje");
            System.out.println("8. Ver estado de zonas y zona crítica");
            System.out.println("9. Ordenar residuos (Peso/Tipo/Prioridad)");
            System.out.println("10. Guardar estado del sistema");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    registrarResiduo(scanner, listaResiduos, utilidad);
                    break;
                case 2:
                    System.out.println("\n--- Lista de residuos (Recorrido Adelante) ---");
                    listaResiduos.mostrarAdelante(); // Método propio de tu ListaCircular
                    break;
                case 3:
                    Residuo rEnc = seleccionarResiduo(scanner, listaResiduos);
                    if (rEnc != null) colaRutas.encolar(rEnc);
                    break;
                case 4:
                    // método sincronizado con ColaPrioridad propia
                    System.out.print(colaRutas.generarReporteCola()); 
                    break;
                case 5:
                    colaRutas.desencolar(); // Despacho automático 
                    break;
                case 6:
                    Residuo rPila = seleccionarResiduo(scanner, listaResiduos);
                    if (rPila != null) pilaReciclaje.apilar(rPila); // LIFO 
                    break;
                case 7:
                    pilaReciclaje.desapilar();
                    break;
                case 8:
                    // Análisis de utilidad ambiental 
                    utilidad.construirDesdeResiduos(listaResiduos);
                    System.out.print(utilidad.generarReporteZonas());
                    break;
                case 9:
                    // Uso de comparadores obligatorios 
                    ordenarResiduos(scanner, listaResiduos);
                    break;
                case 10:
                    // Guardado manual de persistencia 
                    GestorArchivos.guardarTodo(listaResiduos, utilidad.getMapaZonas(), rutaResiduos, rutaZonas);
                    System.out.println("Sistema guardado.");
                    break;
                case 0:
                    System.out.println("Guardando antes de salir...");
                    GestorArchivos.guardarTodo(listaResiduos, utilidad.getMapaZonas(), rutaResiduos, rutaZonas);
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }

    // Método para manejar los 3 comparadores requeridos 
    private static void ordenarResiduos(Scanner scanner, ListaCircular lista) {
        System.out.println("\nCriterio de ordenamiento:");
        System.out.println("1. Por peso\n2. Por tipo\n3. Por prioridad ambiental");
        int op = leerEntero(scanner);

        Comparator<Residuo> comp;
        if (op == 1) comp = new ComparadorPeso();
        else if (op == 2) comp = new ComparadorTipo();
        else comp = new ComparadorPrioridad();

        lista.ordenar(comp); // Implementación de ordenamiento propia 
        System.out.println("Lista ordenada exitosamente.");
        lista.mostrarAdelante();
    }

    private static void registrarResiduo(Scanner scanner, ListaCircular lista, Utilidad utilidad) {
        System.out.println("\n--- Nuevo Registro ---");
        System.out.print("ID: "); int id = leerEntero(scanner);
        System.out.print("Nombre: "); String nom = scanner.nextLine();
        System.out.print("Tipo: "); String tipo = scanner.nextLine();
        System.out.print("Peso (kg): "); double peso = leerDouble(scanner);
        System.out.print("Fecha: "); String fec = scanner.nextLine();
        System.out.print("Zona: "); String zon = scanner.nextLine();
        System.out.print("Prioridad (1-3): "); int pri = leerEntero(scanner);

        Residuo r = new Residuo(id, nom, tipo, peso, fec, zon, pri);
        lista.insertar(r);
        utilidad.construirDesdeResiduos(lista);
    }

    private static Residuo seleccionarResiduo(Scanner scanner, ListaCircular lista) {
        System.out.print("Ingrese el ID del residuo: ");
        int idBuscado = leerEntero(scanner);
        Iterator<Residuo> it = lista.iteradorAdelante();
        while (it.hasNext()) {
            Residuo r = it.next();
            if (r.getId() == idBuscado) return r;
        }
        System.out.println("ID no encontrado.");
        return null;
    }

    private static int leerEntero(Scanner scanner) {
        while (!scanner.hasNextInt()) { 
            System.out.print("Entrada inválida. Ingrese número: ");
            scanner.next(); 
        }
        int val = scanner.nextInt(); 
        scanner.nextLine();
        return val;
    }

    private static double leerDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) { 
            System.out.print("Entrada inválida. Ingrese peso: ");
            scanner.next(); 
        }
        double val = scanner.nextDouble(); 
        scanner.nextLine();
        return val;
    }
}