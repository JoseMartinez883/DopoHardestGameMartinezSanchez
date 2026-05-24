package domain;
public class BlueSkin implements PlayerSkin {
    private final ElementColor color = new ElementColor(0, 0, 255);
    public double getSpeed() { return 3.6; }
    public double getSize() { return 1.5; }
    public String getName() { return "BlueSkin"; }
    public HitResult onHit(Player player) { return HitResult.DEAD; }
    public void onRespawn() {}
    public ElementColor getColor() { return color; }
}
