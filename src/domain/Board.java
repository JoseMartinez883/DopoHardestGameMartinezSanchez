package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Almacena de forma todos los elementos
 */
public class Board implements Serializable {
    public static final double COLLISION_TOLERANCE = 8.0;
    private int width;
    private int height;
    
    private PhysicsEngine physics;
    
    // Listas extraídas de VisualEngine, UpdateEngine y GameRuleEngine
    private List<BoardElement> renderingElements;
    private List<Tickable> tickables;
    private List<Collectable> collectables;
    private List<Spawnable> spawnZones;
    private List<Goal> goals;
    private int timeLimit = 50;

    /**
     * Instancia un tablero vacío 
     * @param width  Ancho del área de juego
     * @param height Alto del área de juego
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.physics = new PhysicsEngine(width, height);
        
        this.renderingElements = new ArrayList<>();
        this.tickables = new ArrayList<>();
        this.collectables = new ArrayList<>();
        this.spawnZones = new ArrayList<>();
        this.goals = new ArrayList<>();
    }

    public void addVisual(BoardElement element) { renderingElements.add(element); }
    public void addSolid(Solid solid) { physics.addSolid(solid); }
    public void addInteractable(Interactable interactable) { physics.addInteractable(interactable); }
    public void addCollectable(Collectable collectable) { collectables.add(collectable); }
    public void addSpawnZone(Spawnable zone) { spawnZones.add(zone); }
    public void addGoal(Goal goal) { goals.add(goal); }
    public void addTickable(Tickable tickable) { tickables.add(tickable); }
    
    /** 
     * Vacía completamente el nivel actual (usado al cargar un nuevo nivel o resetear) 
     */


    /**
     * Revisa físicamente si un elemento en movimiento ha chocado contra las "hitboxes" de otros elementos
     * @param element El objeto en movimiento
     * @param elementSizeX Ancho del objeto
     * @param elementSizeY Alto del objeto
     * @return Una lista con todo aquello que fue tocado en ese fotograma
     */
    public List<Interactable> checkCollision(MovableElement element, double elementSizeX, double elementSizeY) {
        return physics.checkCollision(element, elementSizeX, elementSizeY);
    }

    /**
     * Chequea si es posible caminar hacia un punto
     * @param position Coordenada que se desea pisar
     * @param size Tamaño del que quiere caminar
     * @return true si el espacio está libre
     */
    public boolean isWalkable(Position position, double size) {
        return physics.isWalkable(position, size);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public List<BoardElement> getVisualElements() { return renderingElements; }
    public List<Solid> getSolids() { return physics.getSolids(); }
    public List<Interactable> getInteractables() { return physics.getInteractables(); }
    public List<Collectable> getCollectables() { return collectables; }
    public List<Goal> getGoals() { return goals; }
    public List<Spawnable> getSpawnZones() { return spawnZones; }
    public List<Tickable> getTickables() { return tickables; }
    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
}
