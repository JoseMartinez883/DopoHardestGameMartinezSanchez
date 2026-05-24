package domain;
import java.io.Serializable;

public interface PlayerSkin extends Serializable {
    double getSpeed();
    double getSize();
    String getName();
    HitResult onHit(Player player);
    void onRespawn();
    ElementColor getColor();
}
