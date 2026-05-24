package domain;

import java.io.Serializable;

/**
 * Interfaz el movimiento
 */
public interface MovementStrategy extends Serializable {
    /**
     * Calcula y actualiza la posición de cualquier elemento móvil.
     * @param element El elemento que desea moverse.
     * @param board El tablero donde se encuentra el elemento.
     */
    void move(MovableElement element, Board board);
}
