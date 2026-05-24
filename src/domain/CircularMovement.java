package domain;

/**
 * Estrategia de movimiento circular
 */
public class CircularMovement implements MovementStrategy {
    private double centerX, centerY;
    private double radius;
    private double angle;
    private double angularSpeed;

    /**
     * @param centerX Centro de la órbita X
     * @param centerY Centro de la órbita Y
     * @param radius Distancia al centro
     * @param initialAngle Ángulo inicial (0 a 360)
     * @param speed Velocidad de rotación
     */
    public CircularMovement(double centerX, double centerY, double radius, double initialAngle, double speed) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = Math.toRadians(initialAngle);
        this.angularSpeed = speed * 0.01; 
    }

    @Override
    public void move(MovableElement element, Board board) {
        angle += angularSpeed; 
        double nextX = centerX + radius * Math.cos(angle);
        double nextY = centerY + radius * Math.sin(angle);
        element.setPosition(new Position(nextX - Enemy.VISUAL_RADIUS, nextY - Enemy.VISUAL_RADIUS));
    }

    public double getCenterX() { return centerX; }
    public double getCenterY() { return centerY; }
    public double getRadius() { return radius; }
    public double getAngleDegrees() { return Math.toDegrees(angle); }
    public double getSpeed() { return angularSpeed / 0.01; }
}
