package domain;

import java.io.Serializable;
import java.util.Map;

public abstract class BoardElement implements Serializable {
    protected Position position;
    protected boolean isActive;

    public BoardElement(Position position) {
        this.position = position;
        this.isActive = true;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Devuelve una representación visual genérica del objeto como un diccionario de datos.
     * Permite un desacoplamiento absoluto de capas, 100% libre de instanceof y OCP-compliant.
     */
    public abstract Map<String, String> toVisualMap();
}
