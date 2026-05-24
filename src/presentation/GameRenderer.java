package presentation;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * GameRenderer maneja el registro de renderizadores para cada tipo de figura
 * y se encarga del renderizado gráfico del juego y de su HUD en pantalla.
 */
public final class GameRenderer {
    private final Map<String, ElementRenderer> renderers = new HashMap<>();

    public GameRenderer() {
        initializeGameRenderers();
    }

    private void initializeGameRenderers() {
        renderers.put("RECT", (data, g2d) -> {
            int x = Integer.parseInt(data.get("x"));
            int y = Integer.parseInt(data.get("y"));
            int w = Integer.parseInt(data.get("w"));
            int h = Integer.parseInt(data.get("h"));
            int r = Integer.parseInt(data.get("r"));
            int g = Integer.parseInt(data.get("g"));
            int b = Integer.parseInt(data.get("b"));
            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(x, y, w, h);
            
            if (data.containsKey("br")) {
                int br = Integer.parseInt(data.get("br"));
                int bg = Integer.parseInt(data.get("bg"));
                int bb = Integer.parseInt(data.get("bb"));
                g2d.setColor(new Color(br, bg, bb));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x, y, w, h);
            }
        });
        
        renderers.put("OVAL", (data, g2d) -> {
            int x = Integer.parseInt(data.get("x"));
            int y = Integer.parseInt(data.get("y"));
            int w = Integer.parseInt(data.get("w"));
            int h = Integer.parseInt(data.get("h"));
            int r = Integer.parseInt(data.get("r"));
            int g = Integer.parseInt(data.get("g"));
            int b = Integer.parseInt(data.get("b"));
            g2d.setColor(new Color(r, g, b));
            g2d.fillOval(x, y, w, h);
            
            if (data.containsKey("br")) {
                int br = Integer.parseInt(data.get("br"));
                int bg = Integer.parseInt(data.get("bg"));
                int bb = Integer.parseInt(data.get("bb"));
                g2d.setColor(new Color(br, bg, bb));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawOval(x, y, w, h);
            }
        });
        
        renderers.put("CHECKER", (data, g2d) -> {
            int cx = Integer.parseInt(data.get("x"));
            int cy = Integer.parseInt(data.get("y"));
            int cw = Integer.parseInt(data.get("w"));
            int ch = Integer.parseInt(data.get("h"));
            int r = Integer.parseInt(data.get("r"));
            int g = Integer.parseInt(data.get("g"));
            int b = Integer.parseInt(data.get("b"));
            
            g2d.setColor(new Color(r, g, b));
            g2d.fillRect(cx, cy, cw, ch);
            g2d.setColor(new Color(230, 230, 255));
            for(int x = 0; x < cw; x += 25) {
                for(int y = 0; y < ch; y += 25) {
                    if(((x/25) + (y/25)) % 2 == 0) g2d.fillRect(cx + x, cy + y, 25, 25);
                }
            }
            if (data.containsKey("br")) {
                int br = Integer.parseInt(data.get("br"));
                int bg = Integer.parseInt(data.get("bg"));
                int bb = Integer.parseInt(data.get("bb"));
                g2d.setColor(new Color(br, bg, bb));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(cx, cy, cw, ch);
            }
        });
    }

    /**
     * Dibuja los elementos del juego y el HUD en el panel.
     */
    public void drawGame(Graphics2D g2d, V2GUI gui, int panelWidth, int panelHeight, boolean isPaused) {
        g2d.setColor(new Color(180, 180, 255));
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        if (gui.isGameLoaded()) {
            for (Map<String, String> data : gui.getGameObjectsData()) {
                String shape = data.get("shape");
                ElementRenderer r = renderers.get(shape);
                if (r != null) r.render(data, g2d);
            }

            for (Map<String, String> data : gui.getGamePlayersData()) {
                String invulnerableStr = data.get("isInvulnerable");
                boolean isInvulnerable = Boolean.parseBoolean(invulnerableStr);
                if (isInvulnerable && System.currentTimeMillis() % 200 < 100) {
                    continue;
                }

                String shape = data.get("shape");
                ElementRenderer r = renderers.get(shape);
                if (r != null) r.render(data, g2d);
            }
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        int secondsLeft = (gui.isGameLoaded()) ? gui.getTimeRemaining() : 0;
        g2d.drawString("Time: " + secondsLeft + "s", 10, 20);
        
        if (gui.isGameLoaded()) {
            int numPlayers = gui.getGamePlayersData().size();
            if (numPlayers > 0) {
                if (numPlayers > 1) {
                    g2d.drawString("P1 Deaths: " + gui.getGamePlayerDeaths(0) + " | Coins: " + gui.getGamePlayerCollectedCoins(0) + "/" + gui.getGamePlayerTotalCoinsRequired(0), 110, 20);
                    g2d.drawString("P2 Deaths: " + gui.getGamePlayerDeaths(1) + " | Coins: " + gui.getGamePlayerCollectedCoins(1) + "/" + gui.getGamePlayerTotalCoinsRequired(1), 340, 20);
                } else {
                    g2d.drawString("Deaths: " + gui.getGamePlayerDeaths(0), 120, 20);
                    g2d.drawString("Coins: " + gui.getGamePlayerCollectedCoins(0) + "/" + gui.getGamePlayerTotalCoinsRequired(0), 230, 20);
                }
            }
        }
        
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Press Esc/P to Pause | Ctrl+S to Save", 550, 20);

        if (isPaused) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }
    }
}
