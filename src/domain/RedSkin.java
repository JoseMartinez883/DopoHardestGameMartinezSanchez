package domain;
public class RedSkin implements PlayerSkin {
    private final ElementColor color = new ElementColor(255, 0, 0);
    public double getSpeed() { return 2.4; }
    public double getSize() { return 1.0; }
    public String getName() { return "RedSkin"; }
    public HitResult onHit(Player player) { return HitResult.DEAD; }
    public void onRespawn() {}
    public ElementColor getColor() { return color; }
}
