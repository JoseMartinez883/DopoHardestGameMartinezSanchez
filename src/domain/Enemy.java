package domain;

/**
 * Representa a los enemigos controlados por la computadora en el juego
 * Tienen la capacidad de dañar a otros elementos al chocar con ellos y reaccionan al daño desactivándose
 */
public class Enemy extends MovableElement implements Interactable, Tickable {
    protected MovementStrategy movementStrategy;
    public static final double VISUAL_RADIUS = 10.0;
    public static final double VISUAL_SIZE = VISUAL_RADIUS * 2;

    /**
     * Instancia un nuevo enemigo
     * @param position Coordenada donde aparecerá el enemigo
     * @param speed Rapidez de movimiento
     * @param movementStrategy El algoritmo o patrón de movimiento que usará
     */
    public Enemy(Position position, double speed, MovementStrategy movementStrategy) {
        super(position, speed);
        this.movementStrategy = movementStrategy;
    }

    @Override
    public void move(Board board) {
        movementStrategy.move(this, board);
    }



    @Override
    public void tick(Board board) {
        move(board);
        java.util.List<Interactable> hits = board.checkCollision(this, VISUAL_SIZE, VISUAL_SIZE);
        for (Interactable hit : hits) {
            onCollision(hit);
        }
    }

    @Override
    public Hitbox getHitbox() {
        return new CircularHitbox(
            new Position(position.getX() + VISUAL_RADIUS, position.getY() + VISUAL_RADIUS), VISUAL_RADIUS);
    }

    /**
     * Resuelve el choque inicial avisando al objeto con el que chocó
     * @param other El objeto impactado
     */
    @Override
    public void onCollision(Interactable other) {
        other.applyCollisionEffect(this);
    }

    /**
     * Define qué pasa cuando alguien choca físicamente contra este enemigo
     * Si el enemigo está activo le aplica daño al objeto que lo tocó
     * @param other El objeto que está haciendo contacto
     */
    @Override
    public void applyCollisionEffect(Interactable other) {
        if (isActive()) {
            other.takeDamage();
        }
    }

    @Override
    public boolean takeExplosionDamage() {
        this.deactivate(); // El enemigo muere si recibe daño por explosión
        return true;
    }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = new java.util.HashMap<>();
        if (isActive) {
            data.put("shape", "OVAL");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            data.put("w", String.valueOf((int)VISUAL_SIZE));
            data.put("h", String.valueOf((int)VISUAL_SIZE));
            data.put("r", "135"); // Light Sky Blue
            data.put("g", "206");
            data.put("b", "250");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
