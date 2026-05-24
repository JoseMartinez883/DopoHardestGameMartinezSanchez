package domain;

/**
 * Representa un Checkpoint o Zona Intermedia
 * Cuando el jugador entra en ella, se convierte en su nuevo punto de reaparición
 */
public class IntermediateZone extends InitialZone implements Interactable {
    
    /**
     * Crea un punto de control en el tablero
     * @param position Coordenada superior izquierda
     * @param width Ancho de la zona segura
     * @param height Alto de la zona segura
     */
    public IntermediateZone(Position position, double width, double height) {
        super(position, width, height);
    }

    /**
     * Actualiza el punto de reaparición del objeto que entra en contacto con esta zona
     * @param other El ente que está tocando el checkpoint
     */
    @Override
    public void applyCollisionEffect(Interactable other) {
        other.setSpawnPoint(getSpawnPoint());
    }
}
