package domain;

/**
 * define las 8 direcciones posibles de movimiento
 */
public enum Direction {
    NORTH(0, -1), SOUTH(0, 1), EAST(1, 0), WEST(-1, 0), 
    NORTH_EAST(1, -1), NORTH_WEST(-1, -1), SOUTH_EAST(1, 1), SOUTH_WEST(-1, 1), NONE(0, 0);

    private final double dx;
    private final double dy;

    /**
     * Crea una dirección con su equivalente matemático en un plano cartesiano
     * @param dx Vector en el eje X
     * @param dy Vector en el eje Y
     */
    Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public static Direction fromVector(int dx, int dy) {
        for (Direction d : values()) {
            if ((int)d.dx == dx && (int)d.dy == dy) return d;
        }
        return NONE;
    }
}
