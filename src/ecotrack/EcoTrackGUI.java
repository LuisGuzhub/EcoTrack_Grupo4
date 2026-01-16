package ecotrack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EcoTrackGUI extends JFrame {

    private ListaCircular listaResiduos;
    private ColaPrioridad colaRutas;
    private PilaReciclaje pilaReciclaje;
    private Utilidad utilidad;
    private String rutaResiduos;
    private String rutaZonas; // Para persistencia de zonas

    private JTextArea areaTexto;

    public EcoTrackGUI() {
        super("EcoTrack - Gestión de Residuos Urbanos");

        // Look & Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // ========= LÓGICA =========
        listaResiduos = new ListaCircular();
        colaRutas = new ColaPrioridad();
        pilaReciclaje = new PilaReciclaje();
        utilidad = new Utilidad();
        rutaResiduos = Paths.get("data", "residuos.txt").toString();
        rutaZonas = Paths.get("data", "Zonas.txt").toString();

        // Cargar datos (Persistencia de inicio) 
        GestorArchivos.cargarResiduos(listaResiduos, rutaResiduos);
        GestorArchivos.cargarZonas(utilidad, rutaZonas);

        // ========= UI =========
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 248, 250));

        // Encabesado
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
        JButton btnGuardar = crearBoton("Guardar todo");
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
            GestorArchivos.guardarTodo(listaResiduos, utilidad.getMapaZonas(), rutaResiduos, rutaZonas);
            mostrarSeccion("Guardado", "✔ Datos y zonas guardados exitosamente.");
        });

        btnSalir.addActionListener(e -> {
            GestorArchivos.guardarTodo(listaResiduos, utilidad.getMapaZonas(), rutaResiduos, rutaZonas);
            dispose();
        });

        // ========= VENTANA =========
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 550);
        setLocationRelativeTo(null);
        mostrarSeccion("Bienvenido a EcoTrack", "Sistema listo para gestionar residuos urbanos.");
        setVisible(true);
    }

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
            if (r.getId() == id) return r;
        }
        return null;
    }

    private void accionRegistrar(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "ID del residuo:");
            if (idStr == null) return;
            int id = Integer.parseInt(idStr.trim());

            String nombre = JOptionPane.showInputDialog(this, "Nombre:");
            if (nombre == null || nombre.isBlank()) return;

            String tipo = JOptionPane.showInputDialog(this, "Tipo:");
            if (tipo == null || tipo.isBlank()) return;

            String pesoStr = JOptionPane.showInputDialog(this, "Peso (kg):");
            if (pesoStr == null) return;
            double peso = Double.parseDouble(pesoStr.replace(",", "."));

            String fecha = JOptionPane.showInputDialog(this, "Fecha (dd/mm/aaaa):");
            String zona = JOptionPane.showInputDialog(this, "Zona:");
            String prioStr = JOptionPane.showInputDialog(this, "Prioridad (1=Alta, 2=Media, 3=Baja):");
            int prioridad = Integer.parseInt(prioStr.trim());

            Residuo r = new Residuo(id, nombre, tipo, peso, fecha, zona, prioridad);
            listaResiduos.insertar(r);
            utilidad.construirDesdeResiduos(listaResiduos);
            mostrarSeccion("Residuo registrado", r.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResiduos() {
        String[] opciones = {"Hacia adelante", "Hacia atrás"};
        int sel = JOptionPane.showOptionDialog(this, "Sentido de iteración:", "Iteradores", 0, 3, null, opciones, opciones[0]);
        if (sel == -1) return;

        StringBuilder sb = new StringBuilder();
        Iterator<Residuo> it = (sel == 0) ? listaResiduos.iteradorAdelante() : listaResiduos.iteradorAtras();
        
        while (it.hasNext()) {
            sb.append(it.next()).append("\n");
        }
        mostrarSeccion("Lista de residuos", sb.length() > 0 ? sb.toString() : "Lista vacía.");
    }

    private void ordenarResiduos() {
        String[] opciones = {"Por peso", "Por tipo", "Por prioridad ambiental"};
        String sel = (String) JOptionPane.showInputDialog(this, "Criterio:", "Ordenar", 3, null, opciones, opciones[0]);
        if (sel == null) return;

        Comparator<Residuo> comp;
        switch (sel) {
            case "Por peso": comp = new ComparadorPeso(); break;
            case "Por tipo": comp = new ComparadorTipo(); break;
            default: comp = new ComparadorPrioridad(); break;
        }

        listaResiduos.ordenar(comp); // Uso del método de ordenamiento propio 
        mostrarResiduos();
    }

    private void gestionarCola() {
        String[] opciones = {"Agregar", "Atender", "Ver"};
        int op = JOptionPane.showOptionDialog(this, "Acción:", "Cola", 0, 3, null, opciones, opciones[0]);
        if (op == 0) {
            String idStr = JOptionPane.showInputDialog(this, "ID a encolar:");
            if (idStr != null) {
                Residuo r = buscarResiduoPorId(Integer.parseInt(idStr));
                if (r != null) colaRutas.encolar(r);
            }
        } else if (op == 1) {
            Residuo r = colaRutas.desencolar();
            if (r != null) mostrarSeccion("Atendido", r.toString());
        } else if (op == 2) {
            mostrarSeccion("Cola de prioridad", colaRutas.generarReporteCola());
        }
    }

    private void gestionarPila() {
        String[] opciones = {"Enviar a pila", "Procesar tope", "Ver pila"};
        int op = JOptionPane.showOptionDialog(this, "Acción:", "Pila", 0, 3, null, opciones, opciones[0]);
        if (op == 0) {
            String idStr = JOptionPane.showInputDialog(this, "ID a apilar:");
            if (idStr != null) {
                Residuo r = buscarResiduoPorId(Integer.parseInt(idStr));
                if (r != null) pilaReciclaje.apilar(r);
            }
        } else if (op == 1) {
            Residuo r = pilaReciclaje.desapilar();
            if (r != null) mostrarSeccion("Procesado de Pila", r.toString());
        } else if (op == 2) {
            mostrarSeccion("Pila de reciclaje", "Contenido en consola o reporte.");
        }
    }

    private void mostrarZonas() {
        utilidad.construirDesdeResiduos(listaResiduos);
        mostrarSeccion("Análisis de zonas", utilidad.generarReporteZonas());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcoTrackGUI::new);
    }
}