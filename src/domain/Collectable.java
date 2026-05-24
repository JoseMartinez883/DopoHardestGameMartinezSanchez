package domain;

import java.io.Serializable;

/**
 * Interfaz que define el contrato para cualquier objeto que pueda almacenarse en un inventario
 * Funciona de la mano con el doble despacho para activar efectos al momento de la recolección
 */
public interface Collectable extends Serializable {
    /**
     * Determina si recoger este elemento suma al progreso necesario para ganar el nivel
     * @return true si es obligatorio para la victoria
     */
    boolean isRequiredForVictory();
    
    /**
     * Devuelve el objeto a su estado inicial en el tablero si el jugador pierde una vida
     */
    void resetIfCollectable();
    
    /**
     * Ejecuta el beneficio o penalización específica sobre el ente que lo recogió
     * @param collector El objeto que guardó el coleccionable en su inventario
     */
    default void applyCollectEffect(Interactable collector) {}
}
