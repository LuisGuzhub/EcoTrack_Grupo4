package ecotrack;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EcoTrackGUI extends JFrame {

    private ListaCircular listaResiduos;
    private ColaPrioridad colaRutas;
    private PilaReciclaje pilaReciclaje;
    private Utilidad utilidad;
    private String rutaResiduos;

    private JTextArea areaTexto;

    public EcoTrackGUI() {
        super("EcoTrack - Gestión de Residuos Urbanos");

        // Look & Feel (Nimbus si está disponible)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }

        // ========= LÓGICA =========
        listaResiduos = new ListaCircular();
        colaRutas = new ColaPrioridad();
        pilaReciclaje = new PilaReciclaje();
        utilidad = new Utilidad();
        rutaResiduos = Paths.get("data", "residuos.txt").toString();

        // Cargar residuos existentes desde archivo
        GestorArchivos.cargarResiduos(listaResiduos, rutaResiduos);

        // Construir zonas automáticamente según los residuos cargados
        utilidad.construirZonasDesdeResiduos(listaResiduos);

        // ========= UI =========
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 248, 250));

        // Encabezado
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setBackground(new Color(25, 118, 210));

        JLabel titulo = new JLabel("EcoTrack", SwingConstants.LEFT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Gestión inteligente de residuos urbanos", SwingConstants.LEFT);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(220, 235, 255));

        header.add(titulo, BorderLayout.NORTH);
        header.add(subtitulo, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // Panel de botones lateral
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 8, 8));
        panelBotones.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelBotones.setBackground(new Color(250, 250, 250));

        JButton btnRegistrar = crearBoton("Registrar residuo");
        JButton btnMostrar = crearBoton("Mostrar residuos");
        JButton btnOrdenar = crearBoton("Ordenar residuos");
        JButton btnCola = crearBoton("Cola de prioridad");
        JButton btnPila = crearBoton("Centro reciclaje (pila)");
        JButton btnZonas = crearBoton("Ver zonas y crítica");
        JButton btnGuardar = crearBoton("Guardar residuos");
        JButton btnSalir = crearBoton("Salir");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnMostrar);
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnCola);
        panelBotones.add(btnPila);
        panelBotones.add(btnZonas);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.WEST);

        // Área de salida
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaTexto.setBorder(new EmptyBorder(12, 14, 12, 14));
        areaTexto.setBackground(Color.WHITE);
        areaTexto.setLineWrap(true);
        areaTexto.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // ========= ACCIONES =========

        btnRegistrar.addActionListener(this::accionRegistrar);
        btnMostrar.addActionListener(e -> mostrarResiduos());
        btnOrdenar.addActionListener(e -> ordenarResiduos());
        btnCola.addActionListener(e -> gestionarCola());
        btnPila.addActionListener(e -> gestionarPila());
        btnZonas.addActionListener(e -> mostrarZonas());
        btnGuardar.addActionListener(e -> {
            GestorArchivos.guardarResiduos(listaResiduos, rutaResiduos);
            mostrarSeccion("Guardado", "✔ Residuos guardados en " + rutaResiduos);
        });
        btnSalir.addActionListener(e -> {
            GestorArchivos.guardarResiduos(listaResiduos, rutaResiduos);
            dispose();
        });

        // ========= VENTANA =========
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 550);
        setLocationRelativeTo(null);

        mostrarSeccion(
                "Bienvenido a EcoTrack",
                "Residuos cargados desde: " + rutaResiduos +
                        "\nUsa el panel izquierdo para registrar, ordenar y analizar.");

        setVisible(true);
    }

    // ================= HELPERS UI =================

    private JButton crearBoton(String texto) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(new Color(236, 239, 241));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 12, 8, 12)));
        return b;
    }

    private void mostrarSeccion(String titulo, String contenido) {
        areaTexto.setText("");
        areaTexto.append("◆ " + titulo + "\n");
        areaTexto.append("────────────────────────────────────────────\n");
        areaTexto.append(contenido);
        areaTexto.append("\n");
        areaTexto.setCaretPosition(0);
    }

    private Residuo buscarResiduoPorId(int id) {
        Iterator<Residuo> it = listaResiduos.iteradorAdelante();
        while (it.hasNext()) {
            Residuo r = it.next();
            if (r.getId() == id)
                return r;
        }
        return null;
    }

    // ================= ACCIONES =================

    private void accionRegistrar(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID del residuo:");
            if (idStr == null)
                return;
            int id = Integer.parseInt(idStr.trim());

            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre == null || nombre.isBlank())
                return;

            String tipo = JOptionPane.showInputDialog(this, "Tipo (Orgánico, Plástico, Vidrio, etc.):");
            if (tipo == null || tipo.isBlank())
                return;

            String pesoStr = JOptionPane.showInputDialog(this, "Peso (kg):");
            if (pesoStr == null)
                return;
            pesoStr = pesoStr.replace(",", ".").trim();
            double peso = Double.parseDouble(pesoStr);

            String fecha = JOptionPane.showInputDialog(this, "Fecha (dd/mm/aaaa):");
            if (fecha == null || fecha.isBlank())
                return;

            String zona = JOptionPane.showInputDialog(this, "Zona:");
            if (zona == null || zona.isBlank())
                return;

            String prioStr = JOptionPane.showInputDialog(this, "Prioridad (1=Alta, 2=Media, 3=Baja):");
            if (prioStr == null)
                return;
            int prioridad = Integer.parseInt(prioStr.trim());

            Residuo r = new Residuo(id, nombre, tipo, peso, fecha, zona, prioridad);
            listaResiduos.insertar(r);

            // Recalcular zonas con los nuevos residuos
            utilidad.construirZonasDesdeResiduos(listaResiduos);

            mostrarSeccion("Residuo registrado", r.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Datos numéricos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResiduos() {
        StringBuilder sb = new StringBuilder();
        Iterator<Residuo> it = listaResiduos.iteradorAdelante();
        if (!it.hasNext()) {
            sb.append("No hay residuos registrados.");
        } else {
            while (it.hasNext()) {
                sb.append(it.next()).append("\n");
            }
        }
        mostrarSeccion("Lista de residuos", sb.toString());
    }

    private void ordenarResiduos() {
        List<Residuo> aux = new ArrayList<>();
        Iterator<Residuo> it = listaResiduos.iteradorAdelante();
        while (it.hasNext())
            aux.add(it.next());

        if (aux.isEmpty()) {
            mostrarSeccion("Ordenar residuos", "No hay residuos para ordenar.");
            return;
        }

        String[] opciones = { "Por peso", "Por tipo", "Por prioridad ambiental" };
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Criterio de ordenamiento:",
                "Ordenar residuos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);
        if (seleccion == null)
            return;

        switch (seleccion) {
            case "Por peso":
                aux.sort(new ComparadorPeso());
                break;
            case "Por tipo":
                aux.sort(new ComparadorTipo());
                break;
            case "Por prioridad ambiental":
                aux.sort(new ComparadorPrioridad());
                break;
        }

        StringBuilder sb = new StringBuilder();
        for (Residuo r : aux)
            sb.append(r).append("\n");

        mostrarSeccion("Residuos " + seleccion.toLowerCase(), sb.toString());
    }

    private void gestionarCola() {
        String[] opciones = { "Agregar a cola", "Atender siguiente", "Ver cola", "Cancelar" };
        int op = JOptionPane.showOptionDialog(
                this,
                "Acción sobre la cola de prioridad:",
                "Cola de prioridad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (op == 0) { // agregar
            String idStr = JOptionPane.showInputDialog(this, "ID del residuo a encolar:");
            if (idStr == null)
                return;
            try {
                int id = Integer.parseInt(idStr.trim());
                Residuo r = buscarResiduoPorId(id);
                if (r != null) {
                    colaRutas.encolar(r);
                    mostrarSeccion("Cola de prioridad", colaRutas.generarReporteCola());
                } else {
                    mostrarSeccion("Cola de prioridad", "No se encontró residuo con ID " + id);
                }
            } catch (NumberFormatException ex) {
                mostrarSeccion("Cola de prioridad", "ID inválido.");
            }
        } else if (op == 1) { // atender
            Residuo r = colaRutas.desencolar();
            if (r == null) {
                mostrarSeccion("Cola de prioridad", "No hay residuos en la cola.");
            } else {
                mostrarSeccion(
                        "Cola de prioridad",
                        "Atendido:\n" + r + "\n\n" + colaRutas.generarReporteCola());
            }
        } else if (op == 2) { // ver
            mostrarSeccion("Cola de prioridad", colaRutas.generarReporteCola());
        }
    }

    private void gestionarPila() {
        String[] opciones = { "Enviar a pila", "Procesar tope", "Ver pila", "Cancelar" };
        int op = JOptionPane.showOptionDialog(
                this,
                "Acción sobre el centro de reciclaje (pila):",
                "Pila de reciclaje",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (op == 0) { // enviar a pila
            String idStr = JOptionPane.showInputDialog(this, "ID del residuo a enviar a la pila:");
            if (idStr == null)
                return;
            try {
                int id = Integer.parseInt(idStr.trim());
                Residuo r = buscarResiduoPorId(id);
                if (r != null) {
                    pilaReciclaje.apilar(r);
                    mostrarSeccion("Pila de reciclaje", pilaReciclaje.generarReportePila());
                } else {
                    mostrarSeccion("Pila de reciclaje", "No se encontró residuo con ID " + id);
                }
            } catch (NumberFormatException ex) {
                mostrarSeccion("Pila de reciclaje", "ID inválido.");
            }
        } else if (op == 1) { // procesar tope
            Residuo r = pilaReciclaje.desapilar();
            if (r == null) {
                mostrarSeccion("Pila de reciclaje", "La pila está vacía.");
            } else {
                mostrarSeccion(
                        "Pila de reciclaje",
                        "Procesado desde la pila:\n" + r + "\n\n" + pilaReciclaje.generarReportePila());
            }
        } else if (op == 2) { // ver pila
            mostrarSeccion("Pila de reciclaje", pilaReciclaje.generarReportePila());
        }
    }

    private void mostrarZonas() {
        // Recalcular zonas por si hubo nuevos residuos
        utilidad.construirZonasDesdeResiduos(listaResiduos);
        String reporte = utilidad.generarReporteZonas();
        mostrarSeccion("Análisis de zonas", reporte);
    }

    // Punto de entrada solo para la GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcoTrackGUI::new);
    }
}
