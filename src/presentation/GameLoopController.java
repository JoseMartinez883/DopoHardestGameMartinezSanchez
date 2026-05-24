package presentation;

import javax.swing.Timer;

/**
 * Controlador del Bucle de Juego
 */
public final class GameLoopController {
    private final V2GUI gui;
    private final GamePanel gamePanel;
    private final Timer loopTimer;
    private final Runnable onStateChange;

    public GameLoopController(V2GUI gui, GamePanel gamePanel, Runnable onStateChange) {
        this.gui = gui;
        this.gamePanel = gamePanel;
        this.onStateChange = onStateChange;

        // Ajustado a 10ms para resolver el bug de redondeo de Windows y lograr 60+ FPS
        this.loopTimer = new Timer(10, e -> update());
    }

    private void update() {
        if (gui.isGamePlaying()) {
            gui.tickGame();
            gamePanel.repaint();
        } else if (gui.isGameVictory() || gui.isGameOver()) {
            stop();
            onStateChange.run();
        }
    }

    public void start() {
        loopTimer.start();
    }

    public void stop() {
        loopTimer.stop();
    }
}
