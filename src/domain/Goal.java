package domain;

/**
 * Define un elemento que puede actuar como meta
 */
import java.io.Serializable;

public interface Goal extends Serializable {
    boolean isReachedBy(Position pos);
}
