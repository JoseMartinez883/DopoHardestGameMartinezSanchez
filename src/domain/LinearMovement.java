package domain;

/**
 * Estrategia de movimiento 
 */
public class LinearMovement implements MovementStrategy {
    private boolean horizontal;
    private int directionSign = 1;

    public LinearMovement(boolean horizontal) {
        this.horizontal = horizontal;
    }

    @Override
    public void move(MovableElement element, Board board) {
        double currentX = element.getPosition().getX();
        double currentY = element.getPosition().getY();
        double speed = element.getSpeed(); 
        
        Position nextPos;
        if (horizontal) {
            nextPos = new Position(currentX + (directionSign * speed), currentY);
        } else {
            nextPos = new Position(currentX, currentY + (directionSign * speed));
        }

        if (board != null && !board.isWalkable(nextPos, 20.0)) {
            directionSign *= -1;
        } else {
            element.setPosition(nextPos);
        }
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}
