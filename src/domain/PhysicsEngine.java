package domain;

import java.util.ArrayList;
import java.util.List;

/**
* Gestion la fisica del juego 
 */
import java.io.Serializable;

public class PhysicsEngine implements Serializable {
    public static final double COLLISION_TOLERANCE = 8.0;

    private int boardWidth;
    private int boardHeight;
    private List<Solid> solids;
    private List<Interactable> interactables;

    public PhysicsEngine(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.solids = new ArrayList<>();
        this.interactables = new ArrayList<>();
    }

    public void addSolid(Solid solid) { solids.add(solid); }
    public void addInteractable(Interactable interactable) { interactables.add(interactable); }

    public void clear() {
        solids.clear();
        interactables.clear();
    }

    public List<Interactable> checkCollision(MovableElement element, double elementSizeX, double elementSizeY) {
        List<Interactable> hits = new ArrayList<>();
        Position pPos = element.getPosition();

        for (Interactable c : interactables) {
            if (c != element) {
                // El motor ya no hace cálculos, delega al Hitbox del objeto
                if (c.getHitbox().collidesWith(pPos, elementSizeX, elementSizeY)) { 
                    hits.add(c);
                }
            }
        }
        return hits;
    }

    public boolean isWalkable(Position position, double size) {
        for (Solid s : solids) {
            if (s.blocksPosition(position, size)) {
                return false;
            }
        }
        return position.getX() >= 0 && position.getX() + size <= boardWidth && position.getY() >= 0 && position.getY() + size <= boardHeight;
    }

    public List<Solid> getSolids() { return solids; }
    public List<Interactable> getInteractables() { return interactables; }
}
