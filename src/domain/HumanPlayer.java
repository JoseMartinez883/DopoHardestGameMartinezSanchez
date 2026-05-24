package domain;

public class HumanPlayer extends Player {
    private int playerId;

    public HumanPlayer(Position position, PlayerSkin skin, ElementColor borderColor, int playerId) {
        super(position, skin, borderColor);
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void applyCollisionEffect(Interactable other) {
        // HumanPlayer no aplica efectos a otros por defecto (salvo lógicas específicas de GameMode)
    }
}
