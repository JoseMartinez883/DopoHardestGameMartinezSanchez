package domain;

public class GreenSkin implements PlayerSkin {
    private boolean resistanceUsed = false;

    public double getSpeed() { return resistanceUsed ? (2.4 * 0.7) : 2.4; }
    public double getSize() { return 1.0; }
    public String getName() { return "GreenSkin"; }
    public HitResult onHit(Player player) {
        if (!resistanceUsed) {
            resistanceUsed = true;
            return HitResult.SURVIVED_SHIELD;
        }
        return HitResult.DEAD;
    }
    public void resetResistance() { resistanceUsed = false; }
    public void onRespawn() { resistanceUsed = false; }
    public boolean isResistanceUsed() { return resistanceUsed; }
    private final ElementColor color = new ElementColor(0, 255, 0);
    public ElementColor getColor() { return color; }
}
