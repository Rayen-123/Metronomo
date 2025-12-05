package cl.uach.info090.metronome;

/**
 *
 * Clase que representa un hilo de ejecución (Thread) que se encarga de
 * "llevar el pulso" en la frecuencia especificada por el BPM
 * Itera estableciendo pausas e invocando el método tick del MetronomeDisplay
 *
 * @author Rayen Aburto
 */
public class Pulse extends Thread {
	
	/** Atributo para almacenar los Beats Por Minute */
    private int bpm;
    /** El display a notificar */
    private final MetronomeDisplay metronomeDisplay;
    /** Bandera para controlar el estado del hilo */
    private boolean isRunning; 

    /**
     * Constructor de la clase Pulse
     *
     * @param o El objeto MetronomeDisplay que será notificado en cada beat
     */
    public Pulse(MetronomeDisplay o) {
        this.metronomeDisplay = o;
        this.bpm = 60; // Valor por defecto
        this.isRunning = true;
    }

    /**
     * Lógica principal del hilo. Calcula la pausa y llama a tick() de forma continua
     *
     */
    @Override
    public void run() {
        // El hilo está en ejecución todo el tiempo 
        while (isRunning) {
            try {
                // Cálculo del intervalo de pausa en milisegundos:
                // Intervalo (ms) = 60000 / BPM
                int delay = (int) (60000.0 / bpm);

                // Notificar al display (Visual + Sonido)
                // Esto garantiza que el sonido y el gráfico están perfectamente sincronizados
                // porque ambos ocurren en la misma llamada de método tick()
                metronomeDisplay.tick();

                // Establecer la pausa de acuerdo con el bpm establecido 
                Thread.sleep(delay);

            } catch (InterruptedException e) {
                // Si el hilo es interrumpido
                Thread.currentThread().interrupt();
                isRunning = false;
            }
        }
    }

    /**
     * Establece la nueva frecuencia de pulso (BPM)
     *
     * @param bpm La nueva cantidad de Beats Por Minuto
     */
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    /**
     * Obtiene la frecuencia de pulso actual (BPM)
     *
     * @return La cantidad de Beats Por Minuto actual
     */
    public int getBpm() {
        return this.bpm;
    }
    
    /**
     * Detiene la ejecución del hilo de pulso.
     */
    public void stopPulse() {
        this.isRunning = false;
        this.interrupt(); // Interrumpir para salir del estado de sleep
    }
}