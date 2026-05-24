package domain;

/**
 * Abstracción de cualquier elemento del tablero que posea la capacidad de desplazarse
 * Gestiona la velocidad base y delega la lógica de movimiento concreta a sus subclases
 */
public abstract class MovableElement extends BoardElement {
    protected double speed;

    /**
     * Construye un nuevo elemento móvil
     * @param position Coordenadas iniciales en el tablero
     * @param speed Rapidez con la que se moverá el elemento
     */
    public MovableElement(Position position, double speed) {
        super(position);
        this.speed = speed;
    }

    /**
     * Define el comportamiento de movimiento del elemento en el ciclo de actualización
     * @param board El tablero actual para poder consultar colisiones y límites físicos
     */
    public abstract void move(Board board);

    /**
     * Obtiene la velocidad actual
     * @return El valor de rapidez del elemento
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Actualiza la velocidad de movimiento
     * @param speed El nuevo valor de rapidez a asignar
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
