package domain;

/**
 * Representa la zona final que sirve como meta del nivel
 * Si el jugador entra aquí tras recoger todas las monedas, gana el nivel
 */
public class FinalZone extends Zone implements Goal {
    /**
     * Crea una zona final
     * @param position Coordenada superior izquierda
     * @param width    Ancho de la meta
     * @param height   Alto de la meta
     */
    public FinalZone(Position position, double width, double height) {
        super(position, width, height);
    }

    /**
     * Verifica si el jugador ha llegado a la meta
     * @param pos La posición actual del jugador
     * @return true si el jugador está pisando esta zona
     */
    @Override
    public boolean isReachedBy(Position pos) {
        return contains(pos);
    }
}