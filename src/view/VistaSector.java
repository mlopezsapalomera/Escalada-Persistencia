package view;

import controller.EscolaController;
import controller.SectorController;
import model.ResultadoCrud;
import model.entidades.Escola;
import model.entidades.Sector;

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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vista principal para gestionar sectores.
 *
 * Responsabilidades:
 * - Construir componentes Swing
 * - Obtener datos del formulario sin validar
 * - Delegar validacion y CRUD al controlador
 * - Mostrar resultados en tabla y mensajes
 */
public class VistaSector extends JFrame {

    private final SectorController controladorSector = new SectorController();
    private final EscolaController controladorEscuela = new EscolaController();

    private final JTextField campoId = new JTextField();
    private final JComboBox<OpcionEscuela> comboEscuela = new JComboBox<>();
    private final JTextField campoNombre = new JTextField();
    private final JTextField campoLatitud = new JTextField();
    private final JTextField campoLongitud = new JTextField();
    private final JTextArea campoAproximacion = new JTextArea(4, 20);
    private final JTextField campoNumeroVias = new JTextField();
    private final JComboBox<Sector.Popularitat> comboPopularidad = new JComboBox<>(Sector.Popularitat.values());
    private final JTextArea campoRestricciones = new JTextArea(3, 20);
    private final JComboBox<Sector.TipusSector> comboTipoSector = new JComboBox<>(Sector.TipusSector.values());

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Escuela", "Nombre", "Latitud", "Longitud", "Aproximacion", "Numero vias", "Popularidad", "Restricciones", "Tipo"},
            0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tablaSectores = new JTable(modeloTabla);

    public VistaSector() {
        configurarVentana();
        construirInterfaz();
        cargarEscuelasEnCombo();
        cargarSectores();
    }

    private void configurarVentana() {
        setTitle("Gestion de Sectores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1240, 760);
        setMinimumSize(new Dimension(1080, 680));
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

        JLabel titulo = new JLabel("Gestion de Sectores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("CRUD de sectores con validacion en controlador y filtro por escuela.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(90, 90, 90));

        JPanel panelTexto = new JPanel(new BorderLayout(0, 4));
        panelTexto.setOpaque(false);
        panelTexto.add(titulo, BorderLayout.NORTH);
        panelTexto.add(subtitulo, BorderLayout.SOUTH);

        panel.add(panelTexto, BorderLayout.WEST);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(390, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 16, 16, 0),
                BorderFactory.createTitledBorder("Datos del sector")
        ));

        GridBagConstraints restricciones = crearRestriccionesFormulario();
        agregarCampo(panel, restricciones, "ID", campoId, false);
        agregarCampo(panel, restricciones, "Escuela", comboEscuela, true);
        agregarCampo(panel, restricciones, "Nombre", campoNombre, true);
        agregarCampo(panel, restricciones, "Latitud", campoLatitud, true);
        agregarCampo(panel, restricciones, "Longitud", campoLongitud, true);
        agregarCampo(panel, restricciones, "Aproximacion", new JScrollPane(campoAproximacion), true);
        agregarCampo(panel, restricciones, "Numero de vias", campoNumeroVias, true);
        agregarCampo(panel, restricciones, "Popularidad", comboPopularidad, true);
        agregarCampo(panel, restricciones, "Restricciones", new JScrollPane(campoRestricciones), true);
        agregarCampo(panel, restricciones, "Tipo de sector", comboTipoSector, true);

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
        JButton botonFiltrar = new JButton("Filtrar escuela");
        JButton botonRefrescar = new JButton("Refrescar");
        JButton botonLimpiar = new JButton("Limpiar");

        botonCrear.addActionListener(e -> crearSector());
        botonModificar.addActionListener(e -> modificarSector());
        botonEliminar.addActionListener(e -> eliminarSector());
        botonBuscar.addActionListener(e -> buscarSectorPorId());
        botonFiltrar.addActionListener(e -> filtrarPorEscuelaSeleccionada());
        botonRefrescar.addActionListener(e -> cargarSectores());
        botonLimpiar.addActionListener(e -> limpiarFormulario());

        panelBotones.add(botonCrear);
        panelBotones.add(botonModificar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonFiltrar);
        panelBotones.add(botonRefrescar);
        panelBotones.add(botonLimpiar);

        return panelBotones;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 16, 16),
                BorderFactory.createTitledBorder("Listado de sectores")
        ));

        tablaSectores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSectores.setRowHeight(24);
        tablaSectores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarFilaSeleccionada();
            }
        });

        panel.add(new JScrollPane(tablaSectores), BorderLayout.CENTER);
        return panel;
    }

    // ======== Acciones de botones ========
    private void crearSector() {
        Map<String, String> datos = obtenerCamposDelFormulario(false);
        ResultadoCrud resultado = controladorSector.validarYCrearSector(datos);
        procesarResultadoCrud(resultado);
    }

    private void modificarSector() {
        Map<String, String> datos = obtenerCamposDelFormulario(true);
        ResultadoCrud resultado = controladorSector.validarYModificarSector(datos);
        procesarResultadoCrud(resultado);
    }

    private void eliminarSector() {
        String idTexto = campoId.getText().trim();

        int opcion = JOptionPane.showConfirmDialog(this, "Seguro que quieres eliminar este sector?", "Confirmacion", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ResultadoCrud resultado = controladorSector.validarYEliminarSector(idTexto);
        procesarResultadoCrud(resultado);
    }

    private void buscarSectorPorId() {
        String idTexto = campoId.getText().trim();
        ResultadoCrud resultado = controladorSector.obtenerSectorParaEdicion(idTexto);

        if (!resultado.esExito()) {
            mostrarError(resultado.getMensaje());
            return;
        }

        Sector sector = (Sector) resultado.getDatos();
        pintarSectorEnFormulario(sector);
        seleccionarFilaPorId(sector.getId());
    }

    private void filtrarPorEscuelaSeleccionada() {
        OpcionEscuela opcionEscuela = (OpcionEscuela) comboEscuela.getSelectedItem();
        if (opcionEscuela == null || opcionEscuela.getId() <= 0) {
            mostrarError("Debe seleccionar una escuela valida para filtrar.");
            return;
        }

        cargarSectoresPorEscuela(opcionEscuela.getId());
    }

    // ======== Carga y pintado ========
    private void cargarEscuelasEnCombo() {
        comboEscuela.removeAllItems();
        comboEscuela.addItem(new OpcionEscuela(0, "Seleccione una escuela"));

        List<Escola> escuelas = controladorEscuela.listarEscuelas();
        for (Escola escuela : escuelas) {
            comboEscuela.addItem(new OpcionEscuela(escuela.getId(), escuela.getNom()));
        }
    }

    private void cargarSectores() {
        modeloTabla.setRowCount(0);
        List<Sector> sectores = controladorSector.listarSectores();
        for (Sector sector : sectores) {
            modeloTabla.addRow(convertirSectorAFila(sector));
        }
    }

    private void cargarSectoresPorEscuela(int idEscuela) {
        modeloTabla.setRowCount(0);
        List<Sector> sectores = controladorSector.listarSectoresPorEscuela(idEscuela);
        for (Sector sector : sectores) {
            modeloTabla.addRow(convertirSectorAFila(sector));
        }
    }

    private Object[] convertirSectorAFila(Sector sector) {
        String nombreEscuela = sector.getEscola() != null && sector.getEscola().getNom() != null
                ? sector.getEscola().getNom()
                : "ID " + (sector.getEscola() != null ? sector.getEscola().getId() : "-");

        return new Object[]{
                sector.getId(),
                nombreEscuela,
                sector.getNom(),
                decimalATexto(sector.getLatitud()),
                decimalATexto(sector.getLongitud()),
                sector.getAproximacio(),
                sector.getNumVies(),
                sector.getPopularitat(),
                sector.getRestriccions(),
                sector.getTipusSector()
        };
    }

    private void cargarFilaSeleccionada() {
        int fila = tablaSectores.getSelectedRow();
        if (fila < 0) {
            return;
        }

        campoId.setText(modeloTabla.getValueAt(fila, 0).toString());
        campoNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
        campoLatitud.setText(modeloTabla.getValueAt(fila, 3).toString());
        campoLongitud.setText(modeloTabla.getValueAt(fila, 4).toString());
        campoAproximacion.setText(valorComoTexto(modeloTabla.getValueAt(fila, 5)));
        campoNumeroVias.setText(modeloTabla.getValueAt(fila, 6).toString());
        comboPopularidad.setSelectedItem(modeloTabla.getValueAt(fila, 7));
        campoRestricciones.setText(valorComoTexto(modeloTabla.getValueAt(fila, 8)));
        comboTipoSector.setSelectedItem(modeloTabla.getValueAt(fila, 9));

        // Se vuelve a buscar el sector para garantizar el id de escuela real.
        Integer id = parsearEntero(campoId.getText());
        if (id != null) {
            Sector sector = controladorSector.buscarSectorPorId(id);
            if (sector != null && sector.getEscola() != null) {
                seleccionarEscuelaEnCombo(sector.getEscola().getId());
            }
        }
    }

    private void pintarSectorEnFormulario(Sector sector) {
        campoId.setText(String.valueOf(sector.getId()));
        campoNombre.setText(sector.getNom());
        campoLatitud.setText(decimalATexto(sector.getLatitud()));
        campoLongitud.setText(decimalATexto(sector.getLongitud()));
        campoAproximacion.setText(valorComoTexto(sector.getAproximacio()));
        campoNumeroVias.setText(String.valueOf(sector.getNumVies()));
        comboPopularidad.setSelectedItem(sector.getPopularitat());
        campoRestricciones.setText(valorComoTexto(sector.getRestriccions()));
        comboTipoSector.setSelectedItem(sector.getTipusSector());

        if (sector.getEscola() != null) {
            seleccionarEscuelaEnCombo(sector.getEscola().getId());
        }
    }

    private void seleccionarEscuelaEnCombo(int idEscuela) {
        for (int i = 0; i < comboEscuela.getItemCount(); i++) {
            OpcionEscuela opcion = comboEscuela.getItemAt(i);
            if (opcion.getId() == idEscuela) {
                comboEscuela.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarFilaPorId(int id) {
        for (int fila = 0; fila < modeloTabla.getRowCount(); fila++) {
            Object valor = modeloTabla.getValueAt(fila, 0);
            if (valor != null && Integer.toString(id).equals(valor.toString())) {
                tablaSectores.setRowSelectionInterval(fila, fila);
                tablaSectores.scrollRectToVisible(tablaSectores.getCellRect(fila, 0, true));
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

        OpcionEscuela opcionEscuela = (OpcionEscuela) comboEscuela.getSelectedItem();
        String idEscuela = opcionEscuela == null ? "" : String.valueOf(opcionEscuela.getId());

        datos.put("idEscuela", idEscuela);
        datos.put("nombre", campoNombre.getText());
        datos.put("latitud", campoLatitud.getText());
        datos.put("longitud", campoLongitud.getText());
        datos.put("aproximacion", campoAproximacion.getText());
        datos.put("numeroVias", campoNumeroVias.getText());
        datos.put("popularidad", comboPopularidad.getSelectedItem().toString());
        datos.put("restricciones", campoRestricciones.getText());
        datos.put("tipoSector", comboTipoSector.getSelectedItem().toString());

        return datos;
    }

    // ======== Procesamiento de resultados ========
    private void procesarResultadoCrud(ResultadoCrud resultado) {
        if (resultado.esExito()) {
            mostrarInfo(resultado.getMensaje());
            cargarSectores();
            limpiarFormulario();
        } else {
            mostrarError(resultado.getMensaje());
        }
    }

    private void limpiarFormulario() {
        campoId.setText("");
        comboEscuela.setSelectedIndex(comboEscuela.getItemCount() > 0 ? 0 : -1);
        campoNombre.setText("");
        campoLatitud.setText("");
        campoLongitud.setText("");
        campoAproximacion.setText("");
        campoNumeroVias.setText("");
        comboPopularidad.setSelectedIndex(0);
        campoRestricciones.setText("");
        comboTipoSector.setSelectedIndex(0);
        tablaSectores.clearSelection();
    }

    private String decimalATexto(BigDecimal valor) {
        return valor == null ? "" : valor.stripTrailingZeros().toPlainString();
    }

    private String valorComoTexto(Object valor) {
        return valor == null ? "" : valor.toString();
    }

    private Integer parsearEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static final class OpcionEscuela {
        private final int id;
        private final String nombre;

        private OpcionEscuela(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}
