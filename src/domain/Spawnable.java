package domain;

/**
 * Define un elemento que puede proporcionar un punto de aparición para el jugador
 */
import java.io.Serializable;

public interface Spawnable extends Serializable {
    Position getSpawnPoint();
}
