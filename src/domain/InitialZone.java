package domain;

/**
 * Representa la zona inicial donde el jugador comienza o reaparece al morir
 */
public class InitialZone extends Zone implements Spawnable {
    /**
     * Crea una zona inicial
     * @param position Coordenada superior izquierda
     * @param width    Ancho de la zona
     * @param height   Alto de la zona
     */
    public InitialZone(Position position, double width, double height) {
        super(position, width, height);
    }

    /**
     * Calcula el punto central de la zona para que el jugador aparezca ahí
     * @return Las coordenadas exactas del centro de la zona
     */
    @Override
    public Position getSpawnPoint() {
        double centerX = getPosition().getX() + (getWidth() / 2);
        double centerY = getPosition().getY() + (getHeight() / 2);
        return new Position(centerX, centerY);
    }
}
