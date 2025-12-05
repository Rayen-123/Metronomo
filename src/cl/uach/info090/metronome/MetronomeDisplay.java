package cl.uach.info090.metronome;

/**
 * Define la interfaz que cualquier clase de visualización de metrónomo debe implementar.
 * La clase Pulse invocará el método tick() en cada beat para sincronizar la interfaz.
 *
 * @author Rayen Aburto
 */
public interface MetronomeDisplay {
	/**
     * Invocado por el hilo Pulse en cada beat.
     * Es responsable de actualizar la representación visual del beat y de emitir el sonido.
     */
	public void tick(); // Método invocado por Pulse en cada beat
}