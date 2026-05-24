package domain;

/**
 * Define el comportamiento físico de cualquier forma en el juego.
 */
import java.io.Serializable;

public interface Hitbox extends Serializable {
 
    boolean collidesWith(Position p, double w, double h);
}
