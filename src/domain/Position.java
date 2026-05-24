package domain;

import java.io.Serializable;

/**
 * Representa una posicion bidimensional en el juego.
 */
public class Position implements Serializable {
    /**
     * @param x Coordenada horizontal (eje X)
     * @param y Coordena verticla (eje Y) 
    */

    protected double x;
    protected double y;

    /**
     * Instancia una nueva posicion geometrica (x, y).
     * @param x Coordenada en el eje horizontal.
     * @param y Coordenada en el eje vertical.
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retorna la coordenada del eje X.
     * @return La coordenada horizontal en formato double.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Retorna la coordenada del eje Y.
     * @return La coordenada vertical en formato double.
     */
    public double getY() { 
        return y; 
    }

    /**
     * Modifica la coordenada del eje X.
     * @param x Nueva coordenada horizontal.
     */
    public void setX(double x) { 
        this.x = x; 
    }

    /**
     * Modifica la coordenada del eje Y.
     * @param y Nueva coordenada vertical.
     */
    public void setY(double y) { 
        this.y = y; 
    }

    /**
     * Calcula la distancia entre esta posicion y otra posicion dada.
     * @param other La otra posicion de destino.
     * @return Distancia en linea recta como un valor double.
     */
    public double distanceTo(Position other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    /**
     * Realiza una traslacion
     * @param dx Delta de movimiento en el eje horizontal.
     * @param dy Delta de movimiento en el eje vertical.
     * @return Una nueva instancia de Position con las coordenadas trasladadas.
     */
    public Position translate(double dx, double dy) {
        return new Position(this.x + dx, this.y + dy);
    }
}

