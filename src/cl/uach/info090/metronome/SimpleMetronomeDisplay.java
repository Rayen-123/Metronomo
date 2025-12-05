package cl.uach.info090.metronome;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;

/**
 *
 * Representa el panel dónde se muestran gráfica y sonoramente los beats del metrónomo
 * Contiene 4 círculos para representar un compás 4/4 y se encarga de la generación del sonido
 *
 * @author Rayen Aburto
 */
public class SimpleMetronomeDisplay extends JPanel implements MetronomeDisplay {
	
	/** Compás de 4 tiempos (4/4) por defecto */
    private final int NUM_BEATS = 4;
    /** Almacena el índice del beat actual dentro del compás (0 a 3) */
    private int currentBeat = 0;
    /** Arreglo de componentes visuales (círculos) que representan cada beat */
    private final JComponent[] beatIndicators = new JComponent[NUM_BEATS];

    /**
     * Constructor de SimpleMetronomeDisplay
     * Inicializa los componentes visuales del display (los 4 círculos)
     */
    public SimpleMetronomeDisplay() {
        // Configuración básica del panel
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Centrar y espaciar círculos
        this.setPreferredSize(new Dimension(350, 100)); // Tamaño sugerido

        // Inicializar los indicadores de beat (círculos)
        for (int i = 0; i < NUM_BEATS; i++) {
            // Se usa un JPanel con un tamaño fijo para simular un círculo
            JPanel circle = new JPanel();
            circle.setPreferredSize(new Dimension(50, 50));
            circle.setBackground(Color.LIGHT_GRAY); // Color de fondo normal
            beatIndicators[i] = circle;
            this.add(circle);
        }
    }

    /**
     * Genera un tono de onda sinusoidal corto (un 'beep' controlado) usando
     * javax.sound.sampled.
     */
    private void playSound() {
        try {
            float sampleRate = 1000; // Frecuencia de muestreo
            int frequency = 800; // Frecuencia del tono (A5)
            double duration = 0.05; // Duración en segundos (50 ms, muy corto)
            int numSamples = (int) (duration * sampleRate);
            
            // Especificaciones del formato de audio
            AudioFormat format = new AudioFormat(sampleRate, 8, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[numSamples];
            
            // Generar la onda sinusoidal (8-bit mono)
            for (int i = 0; i < numSamples; i++) {
                double angle = 2.0 * Math.PI * i * frequency / sampleRate;
                // El factor 127 es para escalar la onda a un rango de 8 bits sin signo (0-255)
                buffer[i] = (byte) (Math.sin(angle) * 127 + 128); 
            }

            line.write(buffer, 0, buffer.length);
            line.drain(); // Esperar a que la reproducción termine
            line.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            // Si falla la generación de tono, se recurre al beep del sistema.
            Toolkit.getDefaultToolkit().beep(); 
        }
    }
    /**
     * Invocado por el hilo Pulse en cada beat.
     * Actualiza la representación visual del beat y de emite el sonido.
     *
     */
    @Override
    public void tick() {
        // Detener el beat anterior (restaurar color normal)
        // Se usa un operador ternario para manejar el índice: 
        // Si currentBeat es 0, el beat anterior es el último (NUM_BEATS - 1).
        int prevBeat = (currentBeat == 0) ? NUM_BEATS - 1 : currentBeat - 1;
        beatIndicators[prevBeat].setBackground(Color.LIGHT_GRAY); // Vuelve al color de fondo normal

        // Encender el beat actual (oscurecer el background)
        beatIndicators[currentBeat].setBackground(Color.DARK_GRAY); // Oscurece su background
        
        // Emitir el sonido (Sincronización!)
        // Se ejecuta inmediatamente después de la actualización visual,
        // garantizando la sincronización.
        playSound();

        // Actualizar el índice del beat
        currentBeat = (currentBeat + 1) % NUM_BEATS;
    }
}