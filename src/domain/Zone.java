package domain;

/**
 * Representa una zona en el juego
 */
public abstract class Zone extends BoardElement {
    protected double width;
    protected double height;

    /**
     * Constructor base para una zona
     * @param position Coordenada superior izquierda de la zona
     * @param width    Ancho de la zona
     * @param height   Alto de la zona
     */
    public Zone(Position position, double width, double height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    /**
     * Verifica si una posición dada se encuentra dentro de esta zona
     * @param pos La posición a verificar
     * @return true si la posición está dentro, false si no
     */
    public boolean contains(Position pos) {
        return pos.getX() >= position.getX() && pos.getX() <= position.getX() + width &&
        pos.getY() >= position.getY() && pos.getY() <= position.getY() + height;
    }

    /** @return El ancho de la zona */
    public double getWidth() { return width; }
    
    /** @return El alto de la zona */
    public double getHeight() { return height; }

    public Hitbox getHitbox() {
        return new RectangularHitbox(position, width, height);
    }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = new java.util.HashMap<>();
        if (isActive) {
            data.put("shape", "RECT");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            data.put("w", String.valueOf((int)width));
            data.put("h", String.valueOf((int)height));
            data.put("r", "153");
            data.put("g", "255");
            data.put("b", "153");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
