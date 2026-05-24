package domain;

/**
 * Representa una moneda de skin en el juego
 */
public class SkinCoin extends Coin {
    private PlayerSkin associatedSkin;

    /**
     * Crea una moneda de skin en la posición indicada con la skin que otorga
     */
    public SkinCoin(Position position, PlayerSkin associatedSkin) {
        super(position);
        this.associatedSkin = associatedSkin;
    }

    /**
     * Envuelve al objeto recolector con la nueva apariencia asociada a esta moneda
     * @param collector El objeto que recogió la moneda
     */
    @Override
    protected void onCollect(Interactable collector) {
        collector.applySkin(associatedSkin);
    }



    public PlayerSkin getAssociatedSkin() {
        return associatedSkin;
    }

    @Override
    public java.util.Map<String, String> toVisualMap() {
        java.util.Map<String, String> data = super.toVisualMap();
        if (data != null && !data.isEmpty()) {
            ElementColor color = associatedSkin.getColor();
            data.put("r", String.valueOf(color.getR()));
            data.put("g", String.valueOf(color.getG()));
            data.put("b", String.valueOf(color.getB()));
        }
        return data;
    }

    @Override
    public void reset() {
        super.reset();
        if (associatedSkin != null) {
            associatedSkin.onRespawn();
        }
    }
}
