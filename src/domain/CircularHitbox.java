package domain;

/**
 * Implementación de Hitbox para formas circulares 
 */
public class CircularHitbox implements Hitbox {
    private Position center;
    private double radius;

    public CircularHitbox(Position center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(Position p, double w, double h) {
        double pCenterX = p.getX() + (w / 2.0);
        double pCenterY = p.getY() + (h / 2.0);

        double dx = center.getX() - pCenterX;
        double dy = center.getY() - pCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < (radius + (w / 2.0));
    }
}
