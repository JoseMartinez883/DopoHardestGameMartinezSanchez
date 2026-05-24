package domain;

public class ReadyState implements GameState {
    @Override public GameState tick(GameMode mode, GameTimer timer) { return this; }
    @Override public GameState movePlayer(GameMode mode, int playerId, Direction dir) { return this; }
    @Override public GameState togglePause() { return this; }
    
    @Override public boolean isReady() { return true; }
}
