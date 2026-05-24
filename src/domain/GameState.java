package domain;

import java.io.Serializable;

public interface GameState extends Serializable {
    GameState tick(GameMode mode, GameTimer timer);
    GameState movePlayer(GameMode mode, int playerId, Direction dir);
    GameState togglePause();
    
    default boolean isPlaying() { return false; }
    default boolean isPaused() { return false; }
    default boolean isGameOver() { return false; }
    default boolean isVictory() { return false; }
    default boolean isReady() { return false; }
}
