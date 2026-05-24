package domain;

/**
 * Representa una bomba que destruye a jugadores y enemigos al contacto
 */
public class Bomb extends CollectableElement {
    public Bomb(Position position) {
        super(position);
    }

    @Override
    public boolean isRequiredForVictory() {
        return false;
    }

    /**
     * Si la bomba está activa explota y aplica daño letal al objeto que la tocó
     * @param other El objeto que detonó la bomba al chocar
     */
    @Override
    public void applyCollisionEffect(Interactable other) {
        if (isActive() && !collected) {
            if (other.takeExplosionDamage()) {
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
            data.put("r", "128"); // Grey
            data.put("g", "128");
            data.put("b", "128");
            data.put("br", "255");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
