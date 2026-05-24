package domain;

/**
 * Representa una moneda dentro del juego
 */
public abstract class Coin extends CollectableElement {
    /**
     * Construye una nueva moneda
     * @param position La coordenada exacta en el tablero
     */
    public Coin(Position position) {
        super(position);
    }

    /**
     * Marca la moneda como recolectada, la desactiva y ejecuta su efecto concreto
     * @param collector El objeto que guardó la moneda en su inventario
     */
    @Override
    public void applyCollectEffect(Interactable collector) {
        this.collected = true;
        this.deactivate();
        onCollect(collector);
    }

    /**
     * Comportamiento polimórfico que define qué ocurre exactamente al recolectarse
     * @param collector Quien disparó el evento de recolección
     */
    protected abstract void onCollect(Interactable collector);
}
