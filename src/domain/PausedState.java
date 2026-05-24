package domain;

public class PausedState implements GameState {
    @Override public GameState tick(GameMode mode, GameTimer timer) { return this; }
    @Override public GameState movePlayer(GameMode mode, int playerId, Direction dir) { return this; }
    
    @Override
    public GameState togglePause() {
        return new PlayingState();
    }
    
    @Override public boolean isPaused() { return true; }
}
