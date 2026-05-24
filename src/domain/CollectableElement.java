package domain;

/**
 * Clase base para cualquier elemento del juego que pueda ser recogido físicamente
 * Maneja el estado de recolección y la lógica básica de colisión para ser atrapado
 */
public abstract class CollectableElement extends BoardElement implements Collectable, Interactable {

    protected boolean collected;

    public CollectableElement(Position position) {
        super(position);
        this.collected = false;
    }


    public void reset() {
        this.collected = false;
        this.isActive = true;
    }

    @Override
    public void resetIfCollectable() {
        reset();
    }

    @Override
    public boolean isRequiredForVictory() { return true; }

    @Override
    public Hitbox getHitbox() {
        return new CircularHitbox(
            new Position(position.getX() + 10, position.getY() + 10), 
            10
        );
    }

    /**
     * Delega el proceso de recolección al objeto que chocó contra este elemento
     * Si el elemento está activo, le pide al otro objeto que intente guardarlo
     * @param other El objeto que hizo contacto físico
     */
    @Override
    public void applyCollisionEffect(Interactable other) {
        if (isActive() && !collected) {
            other.collectItem(this);
        }
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = new java.util.HashMap<>();
        if (isActive && !collected) {
            data.put("shape", "OVAL");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            data.put("w", "20");
            data.put("h", "20");
            data.put("r", "255");
            data.put("g", "215");
            data.put("b", "0");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
