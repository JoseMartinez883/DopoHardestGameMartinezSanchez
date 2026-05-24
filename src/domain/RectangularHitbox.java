package domain;

/**
 * Implementación de Hitbox para formas rectangulares 
 */
public class RectangularHitbox implements Hitbox {
    private Position position;
    private double width;
    private double height;

    public RectangularHitbox(Position position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean collidesWith(Position p, double w, double h) {
        // Lógica de intersección de rectángulos
        return p.getX() < position.getX() + width &&
               p.getX() + w > position.getX() &&
               p.getY() < position.getY() + height &&
               p.getY() + h > position.getY();
    }
}
