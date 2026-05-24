package domain;

/**
 * Define un elemento con el que el jugador puede interactuar al entrar en contacto
 */
import java.io.Serializable;

/**
 * Interfaz fundamental para el sistema de colisiones y doble despacho
 * Define a un elemento físico que tiene la "capacidad" de interactuar en el tablero
 * Los métodos por defecto permiten implementar un polimorfismo puro donde cada clase solo sobrescribe los efectos que le interesan
 */
public interface Interactable extends Serializable {
    Position getPosition();
    Hitbox getHitbox();
    boolean isActive();
    
    /**
     * Resuelve el choque inicial despachando el mensaje al otro objeto
     * @param other El objeto con el que se acaba de chocar
     */
    default void onCollision(Interactable other) {
        other.applyCollisionEffect(this);
    }
    
    /**
     * Aplica el efecto de la colisión usando la lógica específica de cada clase
     * @param other El objeto que originó la colisión
     */
    void applyCollisionEffect(Interactable other);
    
    /**
     * Capacidad para recibir daño al ser atacado o tocar algo peligroso
     * @return true si el daño tuvo efecto, false si fue ignorado
     */
    default boolean takeDamage() { return false; }
    
    /**
     * Capacidad para recibir daño por explosión
     * Por defecto actúa como un daño normal, pero permite a los objetos reaccionar distinto a las bombas
     * @return true si la explosión tuvo efecto
     */
    default boolean takeExplosionDamage() { return takeDamage(); }
    
    /**
     * Capacidad para recuperar salud o recibir escudos protectores
     * @return true si la curación fue consumida, false si fue ignorada
     */
    default boolean receiveHeal() { return false; }
    
    /**
     * Capacidad de almacenar objetos coleccionables en un inventario interno
     * @param item El coleccionable que se desea guardar
     */
    default void collectItem(Collectable item) {}
    
    /**
     * Capacidad para vestir una apariencia visual distinta de forma temporal
     * @param skin La nueva apariencia a vestir
     */
    default void applySkin(PlayerSkin skin) {}
    
    /**
     * Capacidad para quitarse cualquier apariencia temporal y volver a la original
     */
    default void revertSkin() {}
    
    /**
     * Capacidad para reasignar el punto de reaparición actual
     * @param spawnPoint Las nuevas coordenadas exactas donde renacer tras morir
     */
    default void setSpawnPoint(Position spawnPoint) {}
}
