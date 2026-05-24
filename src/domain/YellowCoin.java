package domain;

/**
 * Representa una moneda amarilla en el juego
 */
public class YellowCoin extends Coin {
    /**
     * Crea una moneda en la posición indicada
     */
    public YellowCoin(Position position) {
        super(position);
    }

    /**
     * Restaura la apariencia original del jugador al ser recolectada
     * @param collector El objeto que recogió la moneda amarilla
     */
    @Override
    protected void onCollect(Interactable collector) {
        collector.revertSkin();
    }


}
