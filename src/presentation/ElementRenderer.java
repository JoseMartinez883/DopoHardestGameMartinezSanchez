package presentation;

import java.awt.Graphics2D;
import java.util.Map;

public interface ElementRenderer {
    void render(Map<String, String> data, Graphics2D g2d);
}
