package domain;

/**
 * Representa un bloque o pared en el juego
 */
public class Wall extends BoardElement implements Solid {
    private double width, height;
    private boolean visible = true;

    /**
     * Crea un muro visible por defecto
     */
    public Wall(Position position, double width, double height) {
        this(position, width, height, true);
    }

    /**
     * Crea un muro 
     * @param position Coordenadas
     * @param width    Ancho de la pared
     * @param height   Alto de la pared
     * @param visible  Si debe ser renderizado por el motor gráfico
     */
    public Wall(Position position, double width, double height, boolean visible) {
        super(position);
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Verifica si otro objeto intenta cruzar la pared
     * @param pos La posición actual del objeto que intenta moverse
     * @param size El tamaño de dicho objeto
     * @return true si la pared está bloqueando el paso
     */
    @Override
    public boolean blocksPosition(Position pos, double size) {
        double px = pos.getX();
        double py = pos.getY();
        return px < position.getX() + width && 
               px + size > position.getX() &&
               py < position.getY() + height &&
               py + size > position.getY();
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = new java.util.HashMap<>();
        if (visible && isActive) {
            data.put("shape", "RECT");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            data.put("w", String.valueOf((int)width));
            data.put("h", String.valueOf((int)height));
            data.put("r", "140");
            data.put("g", "150");
            data.put("b", "220");
            data.put("br", "0");
            data.put("bg", "0");
            data.put("bb", "0");
        }
        return data;
    }
}
