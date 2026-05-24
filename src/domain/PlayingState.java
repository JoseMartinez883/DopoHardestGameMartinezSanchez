package domain;

public class PlayingState implements GameState {
    @Override
    public GameState tick(GameMode mode, GameTimer timer) {
        mode.tick();
        timer.tick();
        
        if (mode.checkVictory()) {
            return new VictoryState();
        } else if (mode.isGameOver() || timer.isTimeUp()) {
            return new GameOverState();
        }
        return this;
    }
    
    @Override
    public GameState movePlayer(GameMode mode, int playerId, Direction dir) {
        if (mode != null && mode.getPlayers().size() > playerId) {
            mode.getPlayers().get(playerId).setCurrentDirection(dir);
        }
        return this;
    }
    
    @Override
    public GameState togglePause() {
        return new PausedState();
    }
    
    @Override public boolean isPlaying() { return true; }
}
