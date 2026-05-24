package domain;

import java.util.List;
import java.io.Serializable;

/**
 * Representa un modo de juego
 */
public class GameMode implements Serializable {
    protected List<Player> players;
    protected Board board;

    public GameMode(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
    }

    public void initGame() {
        if (players.isEmpty() || board.getSpawnZones().isEmpty()) return;
        
        // Cambio del Plan - Sección 4.2
        Spawnable spawnZone = board.getSpawnZones().get(0);
        Position center = spawnZone.getSpawnPoint();
        int totalElements = countRequiredElements();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            
            double offset = 0;
            if (players.size() > 1) {
                if (i == 0) offset = -17.5;
                else offset = 17.5;
            }
            
            p.setSpawnOffsetX(offset);
            p.setSpawnPoint(center);
            p.setPosition(p.getSpawnPoint());
            p.setTotalElementsRequired(totalElements);
        }
    }

    protected int countRequiredElements() {
        int count = 0;
        for (Collectable c : board.getCollectables()) {
            if (c.isRequiredForVictory()) count++;
        }
        return count;
    }

    public void tick() {
        for (Player p : players) {
            p.decreaseInvulnerability();
            double size = Player.BASE_SIZE * p.getCurrentSkin().getSize();
            Position nextPos = p.calculateNextPosition();
            if (board.isWalkable(nextPos, size)) { 
                p.setPosition(nextPos);
            }
            List<Interactable> collisions = board.checkCollision(p, size, size);
            for (Interactable i : collisions) {
                p.onCollision(i);
            }
        }
        
        if (players.size() > 1) {
            Player p1 = players.get(0);
            Player p2 = players.get(1);
            double size1 = Player.BASE_SIZE * p1.getCurrentSkin().getSize();
            double size2 = Player.BASE_SIZE * p2.getCurrentSkin().getSize();
            
            if (p1.getHitbox().collidesWith(p2.getPosition(), size2, size2)) {
                handlePlayerPlayerCollision(p1, p2);
            }
        }
        
        for (Tickable t : board.getTickables()) {
            t.tick(board);
        }
    }

    public boolean checkVictory() {
        int totalCollected = 0;
        for (Player p : players) {
            totalCollected += p.getCollectedElementsCount();
        }
        
        int required = 0;
        if (!players.isEmpty()) {
            required = players.get(0).getTotalElementsRequired();
        }

        // La victoria se habilita únicamente cuando TODOS los elementos del nivel han sido recolectados
        if (totalCollected == required) {
            for (Player p : players) {
                for (Goal v : board.getGoals()) {
                    if (v.isReachedBy(p.getPosition())) {
                        return true; // El jugador que esté pisando la meta gana la partida
                    }
                }
            }
        }
        return false;
    }
    


    public void handlePlayerPlayerCollision(Player player1, Player player2) {
        player1.die(); 
        player2.die(); 
        
        for (Player p : players) {
            p.resetCollectedElements();
        }
        
        for (Collectable c : board.getCollectables()) {
            c.resetIfCollectable();
        }
    }

    public boolean isGameOver() {
        return false; 
    }

    public List<Player> getPlayers() { return players; }
    public Board getBoard() { return board; }
}
