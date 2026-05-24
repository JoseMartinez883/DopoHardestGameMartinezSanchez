package domain;

import java.util.List;

/**
 * La clase fachada
 * Centraliza el estado global, el ciclo de vida del juego y coordina a los diferentes modos de juego
 */
import java.io.Serializable;

public class DOPOGame implements Serializable {
    private Board board;
    private GameState state;
    private GameMode gameMode;
    private GameTimer timer;

    private static final java.util.Map<String, PlayerSkin> SKIN_REGISTRY = new java.util.LinkedHashMap<>();
    static {
        SKIN_REGISTRY.put("RED", new RedSkin());
        SKIN_REGISTRY.put("BLUE", new BlueSkin());
        SKIN_REGISTRY.put("GREEN", new GreenSkin());
    }

    public static PlayerSkin getSkinByIndex(int index) {
        return new java.util.ArrayList<>(SKIN_REGISTRY.values()).get(index);
    }

    public static PlayerSkin getSkinByName(String name) {
        PlayerSkin skin = SKIN_REGISTRY.get(name.toUpperCase());
        if (skin == null) throw new IllegalArgumentException("Skin no existe: " + name);
        return skin;
    }

    public DOPOGame() {
        this.state = new ReadyState();
    }

    private String currentLevelName = "level1";

    // Inicialización interna de la partida
    private void initGameSession(String configPath, List<Player> players, int remainingTime, boolean loadBoardFromDisk) throws DopoException {
        try {
            this.state = new PlayingState();
            this.currentLevelName = configPath;
            
            if (loadBoardFromDisk) {
                // Cambio del Plan - Sección 5.1
                this.board = LevelLoader.loadLevel(configPath, SKIN_REGISTRY);
            } else if (this.board == null) {
                throw new DopoException("No hay un tablero cargado para restaurar la partida");
            }
            int timeLimit = (remainingTime > 0) ? remainingTime : this.board.getTimeLimit();
            this.timer = new GameTimer(timeLimit);
            this.gameMode = new GameMode(players, this.board);
            this.gameMode.initGame();
        } catch (DopoException e) {
            Log.record(e);
            throw e;
        }
    }

    private List<Player> buildPlayers(String mode, int skinIndex1, int borderR1, int borderG1, int borderB1, 
                                       int skinIndex2, int borderR2, int borderG2, int borderB2) {
        PlayerSkin s1 = getSkinByIndex(skinIndex1);
        ElementColor c1 = new ElementColor(borderR1, borderG1, borderB1);
        PlayerSkin s2 = getSkinByIndex(skinIndex2);
        ElementColor c2 = new ElementColor(borderR2, borderG2, borderB2);
        return PlayerFactory.createPlayers(mode, s1, c1, s2, c2, getAvailableSkins());
    }

    /**
     * Inicia una partida cargando el nivel especificado desde un archivo
     * @param configPath Nombre del nivel
     * @param mode Modalidad de juego 
     * @param skinIndex1 Índice de skin del jugador 1
     * @param borderR1 Componente roja del borde del jugador 1
     * @param borderG1 Componente verde del borde del jugador 1
     * @param borderB1 Componente azul del borde del jugador 1
     * @param skinIndex2 Índice de skin del jugador 2
     * @param borderR2 Componente roja del borde del jugador 2
     * @param borderG2 Componente verde del borde del jugador 2
     * @param borderB2 Componente azul del borde del jugador 2
     * @throws DopoException si ocurre un error al cargar los recursos del nivel
     */
    public void startGame(String configPath, String mode, 
                          int skinIndex1, int borderR1, int borderG1, int borderB1, 
                          int skinIndex2, int borderR2, int borderG2, int borderB2) throws DopoException {
        List<Player> players = buildPlayers(mode, skinIndex1, borderR1, borderG1, borderB1, skinIndex2, borderR2, borderG2, borderB2);
        initGameSession(configPath, players, -1, true);
    }

    public String getCurrentLevelName() { 
        return currentLevelName; 
    }

    /** 
     * Pausa la ejecución del juego
     */
    public void pauseGame() {
        if (state.isPlaying()) state = new PausedState();
    }

    /**
     *  Reanuda el Game Loop
     */
    public void resumeGame() {
        if (state.isPaused()) state = new PlayingState();
    }

    public void togglePause() {
        this.state = this.state.togglePause();
    }

    /**
     * Termina la partida 
     */
    public void endGame() {
        this.state = new GameOverState();
    }

    /**
     * Recibe la acción del teclado del usuario y la asigna al jugador correcto
     * @param playerId ID del jugador
     * @param direction Dirección a moverse
     */
    public void movePlayer(int playerId, Direction direction) {
        this.state = this.state.movePlayer(this.gameMode, playerId, direction);
    }

    public void tick() {
        this.state = this.state.tick(this.gameMode, this.timer);
    }

    public java.util.List<java.util.Map<String, String>> getObjectsData() {
        java.util.List<java.util.Map<String, String>> list = new java.util.ArrayList<>();
        if (board != null) {
            for (BoardElement e : board.getVisualElements()) {
                java.util.Map<String, String> map = e.toVisualMap();
                if (map != null && !map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    public java.util.List<java.util.Map<String, String>> getPlayersData() {
        java.util.List<java.util.Map<String, String>> list = new java.util.ArrayList<>();
        if (gameMode != null) {
            for (Player p : gameMode.getPlayers()) {
                java.util.Map<String, String> map = p.toVisualMap();
                if (map != null && !map.isEmpty()) {
                    list.add(map);
                }
            }
        }
        return list;
    }

    public int getPlayerDeaths(int playerIdx) {
        if (gameMode != null && gameMode.getPlayers().size() > playerIdx) {
            return gameMode.getPlayers().get(playerIdx).getDeaths();
        }
        return 0;
    }

    public int getPlayerCollectedCoins(int playerIdx) {
        if (gameMode != null && gameMode.getPlayers().size() > playerIdx) {
            return gameMode.getPlayers().get(playerIdx).getCollectedElementsCount();
        }
        return 0;
    }

    public int getPlayerTotalCoinsRequired(int playerIdx) {
        if (gameMode != null && gameMode.getPlayers().size() > playerIdx) {
            return gameMode.getPlayers().get(playerIdx).getTotalElementsRequired();
        }
        return 0;
    }

    public void setPlayerDirection(int playerIdx, int dx, int dy) {
        if (gameMode != null && gameMode.getPlayers().size() > playerIdx) {
            gameMode.getPlayers().get(playerIdx).setCurrentDirection(Direction.fromVector(dx, dy));
        }
    }

    public PlayerSkin[] getAvailableSkins() {
        return SKIN_REGISTRY.values().toArray(new PlayerSkin[0]);
    }

    public int getTimeRemaining() {
        return (timer != null) ? timer.getRemainingTime() : 0;
    }

    public boolean isPlaying() { return state.isPlaying(); }
    public boolean isPaused() { return state.isPaused(); }
    public boolean isGameOver() { return state.isGameOver(); }
    public boolean isVictory() { return state.isVictory(); }
    public boolean isReady() { return state.isReady(); }

    public Board getBoard() { 
        return board; 
    }

    public void setBoard(Board board) { 
        this.board = board; 
    }

    public void save(java.io.File file) throws DopoException {
        try {
            DOPOPersistence.save(file, this);
        } catch (DopoException e) {
            Log.record(e);
            throw e;
        }
    }

    public static DOPOGame open(java.io.File file) throws DopoException {
        try {
            return DOPOPersistence.open(file);
        } catch (DopoException e) {
            Log.record(e);
            throw e;
        }
    }

    public void loadLevelAndRestore(java.io.File file, String mode,
                                    int skinIndex1, int borderR1, int borderG1, int borderB1,
                                    int skinIndex2, int borderR2, int borderG2, int borderB2) throws DopoException {
        try {
            this.board = LevelLoader.loadLevel(file, SKIN_REGISTRY);
            List<Player> players = buildPlayers(mode, skinIndex1, borderR1, borderG1, borderB1, skinIndex2, borderR2, borderG2, borderB2);
            initGameSession(file.getName().replace(".txt", ""), players, -1, false);
        } catch (DopoException e) {
            Log.record(e);
            throw e;
        }
    }

    public GameState getState() { return state; }
    public GameMode getGameMode() { return gameMode; }
    public GameTimer getTimer() { return timer; }
}
