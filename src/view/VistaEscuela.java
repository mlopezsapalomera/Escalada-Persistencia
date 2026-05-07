package view;

import controller.EscolaController;
import model.entidades.Escola;
import model.ResultadoCrud;
import java.util.Map;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Vista principal para gestionar escuelas.
 *
 * Objetivo:
 * - Crear, modificar, buscar, listar y eliminar escuelas.
 * - Mantener la interfaz simple para usarla como base del resto de vistas.
 */
public class VistaEscuela extends JFrame {

    private final EscolaController controladorEscuela = new EscolaController();

    // Campos del formulario.
    private final JTextField campoId = new JTextField();
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoPoblacion = new JTextField();
    private final JTextArea campoAproximacion = new JTextArea(4, 20);
    private final JTextField campoNumeroVias = new JTextField();
    private final JComboBox<Escola.Popularitat> comboPopularidad = new JComboBox<>(Escola.Popularitat.values());

    // Tabla de resultados.
    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Poblacion", "Aproximacion", "Numero vias", "Popularidad"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaEscuelas = new JTable(modeloTabla);

    public VistaEscuela() {
        configurarVentana();
        construirInterfaz();
        cargarEscuelas();
    }

    private void configurarVentana() {
        setTitle("Gestion de Escuelas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 16));
    }

    private void construirInterfaz() {
        add(crearCabecera(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));
        panel.setBackground(new Color(244, 247, 250));

        JLabel titulo = new JLabel("Gestion de Escuelas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("CRUD de escuelas conectado a la base de datos.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(90, 90, 90));

        JPanel panelTexto = new JPanel(new BorderLayout(0, 4));
        panelTexto.setOpaque(false);
        panelTexto.add(titulo, BorderLayout.NORTH);
        panelTexto.add(subtitulo, BorderLayout.SOUTH);

        panel.add(panelTexto, BorderLayout.WEST);

        JButton botonAbrirSectores = new JButton("Abrir sectores");
        botonAbrirSectores.addActionListener(e -> new VistaSector().setVisible(true));
        panel.add(botonAbrirSectores, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 16, 16, 0),
                BorderFactory.createTitledBorder("Datos de la escuela")
        ));

        GridBagConstraints restricciones = crearRestriccionesFormulario();
        agregarCampo(panel, restricciones, "ID", campoId, false);
        agregarCampo(panel, restricciones, "Nombre", campoNombre, true);
        agregarCampo(panel, restricciones, "Poblacion", campoPoblacion, true);
        agregarCampo(panel, restricciones, "Aproximacion", new JScrollPane(campoAproximacion), true);
        agregarCampo(panel, restricciones, "Numero de vias", campoNumeroVias, true);
        agregarCampo(panel, restricciones, "Popularidad", comboPopularidad, true);

        restricciones.gridwidth = 2;
        restricciones.weightx = 1.0;
        panel.add(crearPanelBotones(), restricciones);

        return panel;
    }

    private GridBagConstraints crearRestriccionesFormulario() {
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(6, 6, 6, 6);
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        return restricciones;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints restricciones, String etiqueta, Component campo, boolean editable) {
        restricciones.gridwidth = 1;
        restricciones.weightx = 0;
        panel.add(new JLabel(etiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1.0;

        if (campo instanceof JTextField campoTexto) {
            campoTexto.setColumns(18);
            campoTexto.setEditable(editable);
        }

        panel.add(campo, restricciones);

        restricciones.gridx = 0;
        restricciones.gridy++;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton botonCrear = new JButton("Crear");
        JButton botonModificar = new JButton("Modificar");
        JButton botonEliminar = new JButton("Eliminar");
        JButton botonBuscar = new JButton("Buscar ID");
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonLimpiar = new JButton("Limpiar");

        botonCrear.addActionListener(e -> crearEscuela());
        botonModificar.addActionListener(e -> modificarEscuela());
        botonEliminar.addActionListener(e -> eliminarEscuela());
        botonBuscar.addActionListener(e -> buscarEscuelaPorId());
        botonRefrescar.addActionListener(e -> cargarEscuelas());
        botonLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(botonCrear);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonRefrescar);
        panelBotones.add(botonLimpiar);

        return panelBotones;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 16, 16),
                BorderFactory.createTitledBorder("Listado de escuelas")
        ));

        tablaEscuelas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEscuelas.setRowHeight(24);
        tablaEscuelas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarFilaSeleccionada();
            }
        });

        panel.add(new JScrollPane(tablaEscuelas), BorderLayout.CENTER);
        return panel;
    }

    // ======== Acciones de botones ========
    private void crearEscuela() {
        Map<String, String> datos = obtenerCamposDelFormulario(false);
        ResultadoCrud resultado = controladorEscuela.validarYCrearEscuela(datos);

        procesarResultadoCrud(resultado);
    }

    private void modificarEscuela() {
        Map<String, String> datos = obtenerCamposDelFormulario(true);
        ResultadoCrud resultado = controladorEscuela.validarYModificarEscuela(datos);

        procesarResultadoCrud(resultado);
    }

    private void eliminarEscuela() {
        String idTexto = campoId.getText().trim();

        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que quieres eliminar esta escuela?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ResultadoCrud resultado = controladorEscuela.validarYEliminarEscuela(idTexto);
        procesarResultadoCrud(resultado);
    }

    private void buscarEscuelaPorId() {
        String idTexto = campoId.getText().trim();
        ResultadoCrud resultado = controladorEscuela.obtenerEscuelaParaEdicion(idTexto);

        if (!resultado.esExito()) {
            mostrarError(resultado.getMensaje());
            return;
        }

        Escola escuela = (Escola) resultado.getDatos();
        pintarEscuelaEnFormulario(escuela);
        seleccionarFilaPorId(escuela.getId());
    }

    // ======== Carga y pintado ========
    private void cargarEscuelas() {
        modeloTabla.setRowCount(0);

        List<Escola> escuelas = controladorEscuela.listarEscuelas();
        for (Escola escuela : escuelas) {
            modeloTabla.addRow(new Object[]{
                    escuela.getId(),
                    escuela.getNom(),
                    escuela.getPoblacio(),
                    escuela.getAproximacio(),
                    escuela.getNumVies(),
                    escuela.getPopularitat()
            });
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tablaEscuelas.getSelectedRow();
        if (fila < 0) {
            return;
        }

        campoId.setText(modeloTabla.getValueAt(fila, 0).toString());
        campoNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        campoPoblacion.setText(modeloTabla.getValueAt(fila, 2).toString());
        campoAproximacion.setText(modeloTabla.getValueAt(fila, 3).toString());
        campoNumeroVias.setText(modeloTabla.getValueAt(fila, 4).toString());
        comboPopularidad.setSelectedItem(modeloTabla.getValueAt(fila, 5));
    }

    private void pintarEscuelaEnFormulario(Escola escuela) {
        campoId.setText(String.valueOf(escuela.getId()));
        campoNombre.setText(escuela.getNom());
        campoPoblacion.setText(escuela.getPoblacio());
        campoAproximacion.setText(escuela.getAproximacio());
        campoNumeroVias.setText(String.valueOf(escuela.getNumVies()));
        comboPopularidad.setSelectedItem(escuela.getPopularitat());
    }

    private void seleccionarFilaPorId(int id) {
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            Object valor = modeloTabla.getValueAt(fila, 0);
            if (valor != null && Integer.toString(id).equals(valor.toString())) {
                tablaEscuelas.setRowSelectionInterval(fila, fila);
                tablaEscuelas.scrollRectToVisible(tablaEscuelas.getCellRect(fila, 0, true));
                return;
            }
        }
    }

    // ======== Obtener datos del formulario (sin validar) ========
    private Map<String, String> obtenerCamposDelFormulario(boolean incluirId) {
        Map<String, String> datos = new HashMap<>();

        if (incluirId) {
            datos.put("id", campoId.getText());
        }
        datos.put("nombre", campoNombre.getText());
        datos.put("poblacion", campoPoblacion.getText());
        datos.put("aproximacion", campoAproximacion.getText());
        datos.put("numeroVias", campoNumeroVias.getText());
        datos.put("popularidad", comboPopularidad.getSelectedItem().toString());

        return datos;
    }

    // ======== Procesamiento de resultados ========
    private void procesarResultadoCrud(ResultadoCrud resultado) {
        if (resultado.esExito()) {
            mostrarInfo(resultado.getMensaje());
            cargarEscuelas();
            limpiarFormulario();
        } else {
            mostrarError(resultado.getMensaje());
        }
    }

    private void limpiarFormulario() {
        campoId.setText("");
        campoNombre.setText("");
        campoPoblacion.setText("");
        campoAproximacion.setText("");
        campoNumeroVias.setText("");
        comboPopularidad.setSelectedIndex(0);
        tablaEscuelas.clearSelection();
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}