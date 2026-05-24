package domain;

/**
 * Representa una fuente de vida que otorga un escudo extra al jugador
 */
public class HealthFountain extends CollectableElement {
    public HealthFountain(Position position) {
        super(position);
    }

    @Override
    public boolean isRequiredForVictory() {
        return false;
    }

    /**
     * Al chocar proporciona un escudo curativo al objeto impactado y se consume
     * @param other El objeto que recibe la curación
     */
    @Override
    public void applyCollisionEffect(Interactable other) {
        if (isActive() && !collected) {
            if (other.receiveHeal()) {
                this.collected = true;
                this.deactivate();
            }
        }
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
            data.put("r", "255"); // Pink
            data.put("g", "105");
            data.put("b", "180");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
