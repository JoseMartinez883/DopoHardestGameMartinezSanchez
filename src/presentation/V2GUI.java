package presentation;

import javax.swing.*;
import java.awt.*;
import domain.DOPOGame;

/**
 * V2GUI es la ventana principal (JFrame) del juego.
 * Administra el layout general, las transiciones de paneles y guarda el estado
 * de la configuración visual del usuario, delegando responsabilidades de renderizado,
 * diálogos y persistencia a controladores independientes.
 */
public final class V2GUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private DOPOGame game;
    private GamePanel gamePanel;
    private ColorCustomizationPanel colorPanel;
    private GameLoopController gameLoopController;
    
    private int selectedSkinIndex = 0;
    private int selectedSkinIndexP2 = 1;
    private Color selectedBorderColorP1 = Color.BLACK;
    private Color selectedBorderColorP2 = Color.BLACK;
    private boolean isPvP = false;
    private java.io.File lastLoadedFile;
    
    private final GameRenderer gameRenderer;
    private final GameFileController gameFileController;

    public java.io.File getLastLoadedFile() { return lastLoadedFile; }
    public void setLastLoadedFile(java.io.File file) { this.lastLoadedFile = file; }
    public void setPvP(boolean pvp) { this.isPvP = pvp; }
    public boolean isPvP() { return this.isPvP; }

    public void setSelectedBorderColorP1(Color color) { 
        this.selectedBorderColorP1 = color; 
    }
    public void setSelectedBorderColorP2(Color color) { 
        this.selectedBorderColorP2 = color; 
    }
    public Color getSelectedBorderColorP1() { return selectedBorderColorP1; }
    public Color getSelectedBorderColorP2() { return selectedBorderColorP2; }
    
    public void setSelectedSkinIndex(int index) {
        this.selectedSkinIndex = index;
    }
    public void setSelectedSkinIndexP2(int index) {
        this.selectedSkinIndexP2 = index;
    }
    public int getSelectedSkinIndex() { return selectedSkinIndex; }
    public int getSelectedSkinIndexP2() { return selectedSkinIndexP2; }
    
    public Color getSelectedSkinColorP1() {
        if (selectedSkinIndex == 0) return new Color(255, 102, 102); // Red
        if (selectedSkinIndex == 1) return new Color(102, 178, 255); // Blue
        return new Color(102, 255, 178); // Green
    }
    public Color getSelectedSkinColorP2() {
        if (selectedSkinIndexP2 == 0) return new Color(255, 102, 102); // Red
        if (selectedSkinIndexP2 == 1) return new Color(102, 178, 255); // Blue
        return new Color(102, 255, 178); // Green
    }
    
    public ColorCustomizationPanel getColorPanel() { return colorPanel; }
    public GamePanel getGamePanel() { return gamePanel; }

    public V2GUI() {
        game = new DOPOGame();
        selectedSkinIndex = 0;
        selectedSkinIndexP2 = 1;
        gameRenderer = new GameRenderer();
        gameFileController = new GameFileController();

        setTitle("The DOPO Hardest Game Martinez-Sanchez");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        MainMenuPanel mainMenu = new MainMenuPanel(this);
        mainContainer.add(mainMenu, "MainMenu");

        PlayPanel playPanel = new PlayPanel(this);
        mainContainer.add(playPanel, "Play");

        SettingsPanel settingsPanel = new SettingsPanel(this);
        mainContainer.add(settingsPanel, "Settings");

        SkinSelectionPanel skinPanel = new SkinSelectionPanel(this);
        mainContainer.add(skinPanel, "SkinSelection");

        colorPanel = new ColorCustomizationPanel(this);
        mainContainer.add(colorPanel, "ColorSelection");

        LevelSelectionPanel levelSelection = new LevelSelectionPanel(this);
        mainContainer.add(levelSelection, "LevelSelection");

        gamePanel = new GamePanel(this);
        mainContainer.add(gamePanel, "Game");

        add(mainContainer);
        
        gameLoopController = new GameLoopController(this, gamePanel, () -> {
            if (this.isGameVictory()) {
                this.showVictoryDialog();
            } else if (this.isGameOver()) {
                this.showGameOverDialog();
            }
        });
    }

    private void showVictoryDialog() {
        new VictoryDialog(this).setVisible(true);
    }

    private void showGameOverDialog() {
        new GameOverDialog(this).setVisible(true);
    }

    public DOPOGame getGame() { return game; }
    public GameLoopController getGameLoopController() { return gameLoopController; }

    public boolean isGamePlaying() { return game != null && game.isPlaying(); }
    public boolean isGameVictory() { return game != null && game.isVictory(); }
    public boolean isGameOver() { return game != null && game.isGameOver(); }
    public boolean isGameLoaded() { return game != null; }

    public int getTimeRemaining() { return (game != null) ? game.getTimeRemaining() : 0; }
    public void tickGame() { if (game != null) game.tick(); }
    public void movePlayer(int playerIdx, int dx, int dy) {
        if (game != null) {
            game.setPlayerDirection(playerIdx, dx, dy);
        }
    }
    
    public void loadGameFromSave(java.io.File file) throws Exception {
        this.game = DOPOGame.open(file);
    }
    
    public void saveGameToFile(java.io.File file) throws Exception {
        if (game != null) game.save(file);
    }
    
    public void loadCustomLevelAndRestore(java.io.File file, String modeStr, int skinIndex1, int borderR1, int borderG1, int borderB1, int skinIndex2, int borderR2, int borderG2, int borderB2) throws Exception {
        if (game != null) game.loadLevelAndRestore(file, modeStr, skinIndex1, borderR1, borderG1, borderB1, skinIndex2, borderR2, borderG2, borderB2);
    }
    
    public void startGame(String levelConfig, String modeStr, int skinIndex1, int borderR1, int borderG1, int borderB1, int skinIndex2, int borderR2, int borderG2, int borderB2) throws Exception {
        if (game != null) game.startGame(levelConfig, modeStr, skinIndex1, borderR1, borderG1, borderB1, skinIndex2, borderR2, borderG2, borderB2);
    }

    public java.util.List<java.util.Map<String, String>> getGameObjectsData() {
        return game != null ? game.getObjectsData() : new java.util.ArrayList<>();
    }

    public java.util.List<java.util.Map<String, String>> getGamePlayersData() {
        return game != null ? game.getPlayersData() : new java.util.ArrayList<>();
    }

    public int getGamePlayerDeaths(int playerIdx) {
        return game != null ? game.getPlayerDeaths(playerIdx) : 0;
    }

    public int getGamePlayerCollectedCoins(int playerIdx) {
        return game != null ? game.getPlayerCollectedCoins(playerIdx) : 0;
    }

    public int getGamePlayerTotalCoinsRequired(int playerIdx) {
        return game != null ? game.getPlayerTotalCoinsRequired(playerIdx) : 0;
    }

    public void drawGame(Graphics2D g2d, int panelWidth, int panelHeight, boolean isPaused) {
        gameRenderer.drawGame(g2d, this, panelWidth, panelHeight, isPaused);
    }

    public void pauseGame() { if (game != null) game.pauseGame(); }
    public void resumeGame() { if (game != null) game.resumeGame(); }
    public void endGame() { if (game != null) game.endGame(); }

    public void showPanel(String name) {
        if (!"Game".equals(name) && gameLoopController != null) {
            gameLoopController.stop();
        }
        cardLayout.show(mainContainer, name);
    }

    public void setGame(domain.DOPOGame newGame) {
        this.game = newGame;
    }

    public void openGame() {
        gameFileController.openGame(this);
    }

    public void saveGame() {
        gameFileController.saveGame(this);
    }

    public void openCustomLevel() {
        gameFileController.openCustomLevel(this);
    }

    public void reloadLastFile() {
        gameFileController.reloadLastFile(this);
    }

    public void launchGame(String levelConfig) {
        gameFileController.launchGame(this, levelConfig);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new V2GUI().setVisible(true);
        });
    }
}
