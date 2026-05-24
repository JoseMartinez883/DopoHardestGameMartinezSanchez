package domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Entidad principal que representa al usuario dentro del tablero
 * Mantiene un inventario de coleccionables, estado de vida y puede recibir daños o curaciones
 */
public abstract class Player extends MovableElement implements Interactable {
    public static final double BASE_SIZE = 20.0;
    public static final int INVULNERABILITY_TICKS = 60;

    protected int deaths;
    protected int collectedElementsCount;
    private List<Collectable> collectedList = new ArrayList<>();
    protected int totalElementsRequired;
    protected int shieldPoints; 
    protected PlayerSkin currentSkin;
    protected PlayerSkin originalSkin;
    protected ElementColor borderColor;
    protected Position spawnPoint;
    protected double spawnOffsetX = 0.0;
    protected Direction intendedDirection = Direction.NONE;
    protected int invulnerabilityTimer = 0;
    protected int extraShields = 0; // Vidas extra de las Fuentes de Vida

    /**
     * Crea un nuevo jugador
     * @param position Coordenada inicial de aparición
     * @param skin La apariencia y habilidades especiales que tendrá el jugador
     * @param borderColor El color del borde del cuadrado del jugador
     */
    public Player(Position position, PlayerSkin skin, ElementColor borderColor) {
        super(position, skin.getSpeed());
        this.originalSkin = skin;
        this.currentSkin = skin;
        this.borderColor = borderColor;
        this.deaths = 0;
        this.collectedElementsCount = 0;
        this.spawnPoint = new Position(position.getX(), position.getY());
    }

    public void addShield() {
        this.extraShields++;
    }

    public HitResult receiveHit() {
        if (invulnerabilityTimer > 0) {
            return HitResult.SURVIVED_SHIELD;
        }

        if (extraShields > 0) {
            extraShields--;
            invulnerabilityTimer = INVULNERABILITY_TICKS;
            return HitResult.SURVIVED_SHIELD;
        }

        HitResult result = currentSkin.onHit(this);
        if (result == HitResult.SURVIVED_SHIELD) {
            invulnerabilityTimer = INVULNERABILITY_TICKS; 
        } else if (result == HitResult.DEAD) {
            die();
        }
        return result;
    }

    public void decreaseInvulnerability() {
        if (invulnerabilityTimer > 0) invulnerabilityTimer--;
    }

    public void die() {
        this.deaths++;
        resetCollectedElements();
        respawn();
    }

    public void respawn() {
        this.position = new Position(spawnPoint.getX(), spawnPoint.getY());
        this.currentSkin = originalSkin;
        this.currentSkin.onRespawn();
        this.invulnerabilityTimer = 0;
    }

    public void incrementElements(Collectable c) {
        collectedElementsCount++;
        collectedList.add(c);
    }

    public void setSpawnOffsetX(double offset) {
        this.spawnOffsetX = offset;
    }

    public void setSpawnPoint(Position spawnPoint) {
        this.spawnPoint = new Position(spawnPoint.getX() + spawnOffsetX, spawnPoint.getY());
    }

    public Position getSpawnPoint() {
        return spawnPoint;
    }



    public void setCurrentDirection(Direction direction) {
        this.intendedDirection = direction;
    }

    public Position calculateNextPosition() {
        double currentSpeed = currentSkin.getSpeed();
        double dx = intendedDirection.getDx() * currentSpeed;
        double dy = intendedDirection.getDy() * currentSpeed;
        return this.position.translate(dx, dy);
    }

    @Override
    public void move(Board board) {
        this.position = calculateNextPosition();
    }

    public int getDeaths() { return deaths; }

    public int getCollectedElementsCount() { return collectedElementsCount; }

    public boolean isInvulnerable() { return invulnerabilityTimer > 0; }

    public void resetCollectedElements() {
        for (Collectable c : collectedList) {
            c.resetIfCollectable();
        }
        collectedList.clear();
        this.collectedElementsCount = 0;
    }

    public ElementColor getBorderColor() { return borderColor; }

    public PlayerSkin getCurrentSkin() { return currentSkin; }

    public void setCurrentSkin(PlayerSkin skin) {
        this.currentSkin = skin;
        this.speed = skin.getSpeed();
    }

    public void revertToOriginalSkin() {
        this.currentSkin = originalSkin;
        this.currentSkin.onRespawn();
        this.speed = originalSkin.getSpeed();
    }

    public void setTotalElementsRequired(int val) { this.totalElementsRequired = val; }

    public int getTotalElementsRequired() { return totalElementsRequired; }
    


    /**
     * Delega la colisión al otro objeto
     * @param other El objeto impactado
     */
    @Override
    public void onCollision(Interactable other) {
        other.applyCollisionEffect(this);
    }

    /**
     * Define qué ocurre si otro objeto choca contra el jugador.
     * Obliga a las clases concretas (HumanPlayer) a implementarlo.
     * @param other El objeto causante del choque
     */
    @Override
    public abstract void applyCollisionEffect(Interactable other);



    /**
     * Reacción del jugador al sufrir daño externo
     */
    @Override
    public boolean takeDamage() {
        this.receiveHit();
        return true;
    }

    /**
     * Reacción del jugador al intentar recoger un objeto
     * Incrementa la cuenta interna y activa el efecto de dicho objeto
     * @param item El coleccionable a almacenar
     */
    @Override
    public void collectItem(Collectable item) {
        this.incrementElements(item);
        item.applyCollectEffect(this);
    }

    /**
     * Envuelve al jugador con una apariencia nueva
     * @param skin El nuevo aspecto
     */
    @Override
    public void applySkin(PlayerSkin skin) {
        this.setCurrentSkin(skin);
    }

    @Override
    public void revertSkin() {
        this.revertToOriginalSkin();
    }

    /**
     * Aumenta la salud o escudos del jugador
     */
    @Override
    public boolean receiveHeal() {
        this.addShield();
        return true;
    }

    public Hitbox getHitbox() {
        return new RectangularHitbox(position, BASE_SIZE, BASE_SIZE);
    }

    @Override
    public Map<String, String> toVisualMap() {
        Map<String, String> data = new HashMap<>();
        if (isActive) {
            data.put("shape", "RECT");
            data.put("x", String.valueOf((int)position.getX()));
            data.put("y", String.valueOf((int)position.getY()));
            
            int size = (int)(BASE_SIZE * currentSkin.getSize());
            data.put("w", String.valueOf(size));
            data.put("h", String.valueOf(size));
            
            data.put("isInvulnerable", String.valueOf(isInvulnerable()));
            
            ElementColor skinColor = currentSkin.getColor();
            data.put("r", String.valueOf(skinColor.getR()));
            data.put("g", String.valueOf(skinColor.getG()));
            data.put("b", String.valueOf(skinColor.getB()));
            
            data.put("br", String.valueOf(borderColor.getR()));
            data.put("bg", String.valueOf(borderColor.getG()));
            data.put("bb", String.valueOf(borderColor.getB()));
        }
        return data;
    }
}
