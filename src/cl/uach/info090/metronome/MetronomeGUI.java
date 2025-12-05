package cl.uach.info090.metronome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MetronomeGUI
 *
 * Clase Singleton que representa la interfaz gráfica del metrónomo.
 * También actúa como el programa principal (contiene el método static main).
 * Contiene el display y el hilo de pulso.
 *
 * @author Rayen Aburto
 */
public class MetronomeGUI extends JFrame implements ActionListener {

    // Atributos privados
	/** Instancia Singleton */
    private static MetronomeGUI instance = null; 
    /** Objeto de Pulse, un hilo de ejecución */
    private Pulse pulse;
    /** Panel de visualización */
    private final SimpleMetronomeDisplay metronomeDisplay; 
    
    // Componentes de la GUI
    /** Selector (JComboBox) para elegir los Beats Per Minute (BPM) */
    private final JComboBox<Integer> bpmSelector;
    /** Botón para iniciar o detener la reproducción del metrónomo */
    private final JButton playStopButton;
    /** Bandera que indica si el metrónomo está actualmente en ejecución */
    private boolean isPlaying = false;

    /**
     * Constructor privado para asegurar el patrón Singleton.
     * Inicializa la interfaz gráfica y los objetos Pulse y SimpleMetronomeDisplay.
     */
    private MetronomeGUI() {
        // Inicializar componentes del metrónomo
        metronomeDisplay = new SimpleMetronomeDisplay(); // El panel especial
        // Al construir Pulse, se le pasa el display para que lo controle 
        pulse = new Pulse(metronomeDisplay); 
        
        // --- Configuración de la Ventana (JFrame)
        this.setTitle("Metronome"); // Título 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // --- Panel de Controles (Top Panel)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.add(new JLabel("BPM :")); // Etiqueta BPM 
        
        // Selector de BPM (JComboBox)
        Integer[] bpms = {50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 120, 140, 160}; // Más opciones para mejorar las limitaciones
        bpmSelector = new JComboBox<>(bpms);
        bpmSelector.setSelectedItem(60); // Valor inicial
        controlPanel.add(bpmSelector);
        
        // Botón Play/Stop
        playStopButton = new JButton("Play/Stop");
        playStopButton.addActionListener(this); // La clase MetronomeGUI actúa como ActionListener 
        controlPanel.add(playStopButton);

        // --- Layout Principal
        this.setLayout(new BorderLayout());
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(metronomeDisplay, BorderLayout.CENTER); // La zona de abajo de la interfaz

        this.pack(); // Ajustar el tamaño
        this.setLocationRelativeTo(null); // Centrar en pantalla
    }

    /**
     * Devuelve la única instancia de MetronomeGUI (patrón Singleton).
     *
     * @return La instancia única de MetronomeGUI.
     */
    public static MetronomeGUI getInstance() {
        if (instance == null) {
            instance = new MetronomeGUI();
        }
        return instance;
    }

    /**
     * Maneja los eventos de la interfaz
     *
     * @param e El evento de acción disparado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playStopButton) {
            // Lógica del botón Play/Stop
            if (isPlaying) {
                // Detener el metrónomo
                pulse.stopPulse(); 
                // Crear una nueva instancia de Pulse para la próxima vez (un Thread solo se puede iniciar una vez)
                pulse = new Pulse(metronomeDisplay); 
                isPlaying = false;
                playStopButton.setText("Play/Stop");
            } else {
                // Iniciar/Reanudar el metrónomo
                // Obtener y establecer el nuevo BPM
                Integer selectedBPM = (Integer) bpmSelector.getSelectedItem();
                if (selectedBPM != null) {
                    pulse.setBpm(selectedBPM);
                }
                
                // Iniciar el hilo de ejecución
                pulse.start();
                isPlaying = true;
                playStopButton.setText("Stop");
            }
        }
    }

    /**
     * Método main estático, punto de entrada del programa.
     * Inicializa y muestra la interfaz gráfica.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Uso de SwingUtilities.invokeLater para asegurar que la GUI se inicializa en el 
        // Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MetronomeGUI gui = MetronomeGUI.getInstance();
            gui.setVisible(true);
        });
    }
}
