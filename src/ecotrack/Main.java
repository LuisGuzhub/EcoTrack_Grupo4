package ecotrack;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA ECO-TRACK ===");
        System.out.println("Gestión Inteligente de Residuos Urbanos");
        System.out.println("----------------------------------------");

        // Estructuras principales
        ListaCircular listaResiduos = new ListaCircular();
        ColaPrioridad colaRutas = new ColaPrioridad();
        PilaReciclaje pilaReciclaje = new PilaReciclaje();
        Utilidad utilidad = new Utilidad();

        // Archivo de residuos
        String rutaResiduos = Paths.get("data", "residuos.txt").toString();

        // Cargar datos iniciales (si existen)
        GestorArchivos.cargarResiduos(listaResiduos, rutaResiduos);

        // Zonas de ejemplo (puedes luego cargarlas desde archivo)
        utilidad.agregarZona(new Zona("Centro", 8, 3));
        utilidad.agregarZona(new Zona("Norte", 2, 6));
        utilidad.agregarZona(new Zona("Sur", 5, 5));

        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== MENÚ ECO-TRACK =====");
            System.out.println("1. Registrar residuo");
            System.out.println("2. Mostrar residuos (lista circular)");
            System.out.println("3. Encolar residuo para recolección (cola de prioridad)");
            System.out.println("4. Mostrar cola de prioridad");
            System.out.println("5. Despachar siguiente residuo de la cola");
            System.out.println("6. Enviar residuo a centro de reciclaje (pila)");
            System.out.println("7. Procesar residuo del centro de reciclaje");
            System.out.println("8. Ver estado de zonas y zona crítica");
            System.out.println("9. Ordenar residuos (peso / tipo / prioridad)");
            System.out.println("10. Guardar residuos en archivo");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero(scanner);

            switch (opcion) {
                case 1:
                    registrarResiduo(scanner, listaResiduos);
                    break;

                case 2:
                    System.out.println("\n--- Lista de residuos ---");
                    listaResiduos.mostrarAdelante();
                    break;

                case 3:
                    Residuo rEnc = seleccionarResiduo(scanner, listaResiduos);
                    if (rEnc != null) {
                        colaRutas.encolar(rEnc);
                    }
                    break;

                case 4:
                    colaRutas.mostrarCola();
                    break;

                case 5:
                    colaRutas.desencolar();
                    break;

                case 6:
                    Residuo rPila = seleccionarResiduo(scanner, listaResiduos);
                    if (rPila != null) {
                        pilaReciclaje.apilar(rPila);
                    }
                    break;

                case 7:
                    pilaReciclaje.desapilar();
                    break;

                case 8:
                    System.out.println("\n--- Estado de las Zonas ---");
                    utilidad.mostrarZonas();
                    Zona critica = utilidad.obtenerZonaCritica();
                    if (critica != null) {
                        System.out.println("Zona crítica actual: " + critica.getNombre()
                                + " (Utilidad " + critica.calcularUtilidad() + ")");
                    }
                    break;

                case 9:
                    ordenarResiduos(scanner, listaResiduos);
                    break;

                case 10:
                    GestorArchivos.guardarResiduos(listaResiduos, rutaResiduos);
                    break;

                case 0:
                    System.out.println("Guardando antes de salir...");
                    GestorArchivos.guardarResiduos(listaResiduos, rutaResiduos);
                    salir = true;
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }

        scanner.close();
        System.out.println("Sistema Eco-Track finalizado.");
    }

    // ================= FUNCIONES AUXILIARES =================

    private static int leerEntero(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // limpiar salto de línea
        return val;
    }

    private static double leerDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Ingrese un valor numérico válido: ");
            scanner.next();
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }

    // Registrar nuevo residuo y agregarlo a la lista circular
    private static void registrarResiduo(Scanner scanner, ListaCircular lista) {
        System.out.println("\n--- Registro de residuo ---");

        System.out.print("ID: ");
        int id = leerEntero(scanner);

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Tipo (Orgánico, Plástico, Vidrio, etc.): ");
        String tipo = scanner.nextLine();

        System.out.print("Peso (kg): ");
        double peso = leerDouble(scanner);

        System.out.print("Fecha de recolección (dd/mm/aaaa): ");
        String fecha = scanner.nextLine();

        System.out.print("Zona: ");
        String zona = scanner.nextLine();

        System.out.print("Prioridad ambiental (1=Alta, 2=Media, 3=Baja): ");
        int prioridad = leerEntero(scanner);

        Residuo r = new Residuo(id, nombre, tipo, peso, fecha, zona, prioridad);
        lista.insertar(r);
        System.out.println("Residuo registrado y agregado a la lista.");
    }

    // Permite elegir un residuo existente por ID desde la lista circular
    private static Residuo seleccionarResiduo(Scanner scanner, ListaCircular lista) {
        System.out.print("Ingrese el ID del residuo: ");
        int idBuscado = leerEntero(scanner);

        Iterator<Residuo> it = lista.iteradorAdelante();
        while (it.hasNext()) {
            Residuo r = it.next();
            if (r.getId() == idBuscado) {
                return r;
            }
        }
        System.out.println("No se encontró residuo con ese ID.");
        return null;
    }

    // Ordenar residuos según criterio usando comparadores
    private static void ordenarResiduos(Scanner scanner, ListaCircular lista) {
        List<Residuo> aux = new ArrayList<>();
        Iterator<Residuo> it = lista.iteradorAdelante();
        while (it.hasNext()) {
            aux.add(it.next());
        }

        if (aux.isEmpty()) {
            System.out.println("No hay residuos para ordenar.");
            return;
        }

        System.out.println("\nCriterio de ordenamiento:");
        System.out.println("1. Por peso");
        System.out.println("2. Por tipo");
        System.out.println("3. Por prioridad ambiental");
        System.out.print("Seleccione: ");

        int op = leerEntero(scanner);

        switch (op) {
            case 1:
                aux.sort(new ComparadorPeso());
                System.out.println("\n--- Residuos ordenados por peso ---");
                break;
            case 2:
                aux.sort(new ComparadorTipo());
                System.out.println("\n--- Residuos ordenados por tipo ---");
                break;
            case 3:
                aux.sort(new ComparadorPrioridad());
                System.out.println("\n--- Residuos ordenados por prioridad ---");
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }

        for (Residuo r : aux) {
            System.out.println(r);
        }
    }
}
