package domain;

/**
 * Define un elemento que bloquea físicamente el paso de otros elementos
 */
import java.io.Serializable;

public interface Solid extends Serializable {
    Position getPosition();
    boolean blocksPosition(Position pos, double size);
}
