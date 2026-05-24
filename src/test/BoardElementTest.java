package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardElementTest {
    private Board board;
    private HumanPlayer player;

    @Before
    public void setUp() {
        board = new Board(800, 600);
        player = new HumanPlayer(new Position(50, 50), new RedSkin(), new domain.ElementColor(0, 0, 0), 1);
    }

        @Test
    public void testYellowCoinCollection() {
        YellowCoin coin = new YellowCoin(new Position(50, 50));
        assertTrue(coin.isActive());
        assertEquals(0, player.getCollectedElementsCount());
        board.addCollectable(coin);
        player.onCollision(coin);
        assertEquals(1, player.getCollectedElementsCount());
        assertFalse(coin.isActive());
    }

        @Test
    public void testSkinCoinCollectionBlue() {
        SkinCoin skinCoin = new SkinCoin(new Position(50, 50), new BlueSkin());
        board.addCollectable(skinCoin);
        player.onCollision(skinCoin);
        assertEquals(BlueSkin.class, player.getCurrentSkin().getClass());
        assertFalse(skinCoin.isActive());
    }

        @Test
    public void testSkinCoinVisualColorMatch() {
        SkinCoin skinCoin = new SkinCoin(new Position(50, 50), new BlueSkin());
        java.util.Map<String, String> visualData = skinCoin.toVisualMap();
        String r = visualData.get("r");
        String g = visualData.get("g");
        String b = visualData.get("b");
        
        assertEquals("0", r);
        assertEquals("0", g);
        assertEquals("255", b);
    }

        @Test
    public void testInitialZoneCentering() {
        InitialZone zone = new InitialZone(new Position(10, 20), 80, 100);
        Position spawn = zone.getSpawnPoint();
        assertEquals(50.0, spawn.getX(), 0.001);
        assertEquals(70.0, spawn.getY(), 0.001);
    }

        @Test
    public void testIntermediateZoneCheckpoint() {
        IntermediateZone checkpoint = new IntermediateZone(new Position(200, 200), 100, 100);
        GameMode gameMode = new GameMode(Arrays.asList(player), board);
        player.setPosition(new Position(250, 250));
        player.onCollision(checkpoint);
        assertEquals(250.0, player.getSpawnPoint().getX(), 0.001);
        assertEquals(250.0, player.getSpawnPoint().getY(), 0.001);
        assertTrue(checkpoint.contains(new Position(250, 250)));
        assertFalse(checkpoint.contains(new Position(301, 301)));
        assertEquals(RectangularHitbox.class, checkpoint.getHitbox().getClass());
    }

        @Test
    public void testWallCollisionObstruction() {
        Wall wall = new Wall(new Position(30, 30), 40, 40);
        board.addSolid(wall);
        assertFalse(board.isWalkable(new Position(40, 40), Player.BASE_SIZE));
        assertTrue(board.isWalkable(new Position(10, 10), Player.BASE_SIZE));
        assertEquals(30.0, wall.getPosition().getX(), 0.001);
        assertEquals(30.0, wall.getPosition().getY(), 0.001);
        assertEquals(40.0, wall.getWidth(), 0.001);
        assertEquals(40.0, wall.getHeight(), 0.001);
        assertTrue(wall.isVisible());
        wall.setVisible(true);
        java.util.Map<String, String> visualMap = wall.toVisualMap();
        assertNotNull(visualMap);
        assertEquals("RECT", visualMap.get("shape"));
        assertEquals("30", visualMap.get("x"));
        assertEquals("30", visualMap.get("y"));
        assertEquals("40", visualMap.get("w"));
        assertEquals("40", visualMap.get("h"));
        assertEquals("140", visualMap.get("r"));
        assertEquals("150", visualMap.get("g"));
        assertEquals("220", visualMap.get("b"));
        assertEquals("0", visualMap.get("br"));
        wall.setVisible(false);
        assertFalse(wall.isVisible());
        assertTrue(wall.blocksPosition(new Position(30, 30), Player.BASE_SIZE));
        assertFalse(wall.blocksPosition(new Position(0, 0), Player.BASE_SIZE));
    }

        @Test
    public void testWallBlockingPath() {
        Wall wall = new Wall(new Position(100, 100), 50, 50);
        board.addSolid(wall);
        player.setPosition(new Position(90, 110));
        assertFalse(board.isWalkable(new Position(110, 110), Player.BASE_SIZE));
    }

        @Test
    public void testHitboxBoundaryCollisions() {
        RectangularHitbox hitbox = new RectangularHitbox(new Position(100, 100), 50, 50);
        assertTrue(hitbox.collidesWith(new Position(120, 120), 20, 20));
        assertFalse(hitbox.collidesWith(new Position(200, 200), 20, 20));
        assertFalse(hitbox.collidesWith(new Position(150, 100), 20, 20));
    }



        @Test
    public void testCollectableElementBaseMethods() {
        CollectableElement dummy = new CollectableElement(new Position(120, 120)) {
            @Override
            public void applyCollisionEffect(Interactable other) {
                this.collected = true;
                this.deactivate();
            }
        };
        assertTrue(dummy.isRequiredForVictory());
        assertFalse(dummy.isCollected());
        assertNotNull(dummy.getHitbox());
        assertTrue(dummy.getHitbox() instanceof CircularHitbox);
        player.onCollision(dummy);
        assertTrue(dummy.isCollected());
        player.onCollision(dummy);
        assertTrue(dummy.isCollected());
    }
        @Test
    public void testBombExplosionLogic() {
        Bomb bomb = new Bomb(new Position(100, 100));
        assertFalse(bomb.isRequiredForVictory());
        
        java.util.Map<String, String> visual = bomb.toVisualMap();
        assertEquals("OVAL", visual.get("shape"));
        assertEquals("128", visual.get("r"));
        
        bomb.applyCollisionEffect(player);
        assertEquals(1, player.getDeaths());
        assertFalse(bomb.isActive());
    }

        @Test
    public void testHealthFountainHealingLogic() {
        HealthFountain fountain = new HealthFountain(new Position(100, 100));
        assertFalse(fountain.isRequiredForVictory());
        
        java.util.Map<String, String> visual = fountain.toVisualMap();
        assertEquals("OVAL", visual.get("shape"));
        assertEquals("255", visual.get("r"));
        
        fountain.applyCollisionEffect(player);
        player.receiveHit();
        assertEquals(0, player.getDeaths());
        assertFalse(fountain.isActive());
    }

        @Test
    public void testInteractableAndCollectableDefaultsOnRealObjects() {
        Enemy enemy = new Enemy(new Position(0,0), 2.0, new LinearMovement(true));
        
        assertFalse(enemy.takeDamage());
        assertFalse(enemy.receiveHeal());
        
        YellowCoin coin = new YellowCoin(new Position(0,0));
        coin.applyCollectEffect(enemy); 
        
        enemy.collectItem(coin); 
        enemy.applySkin(new domain.RedSkin()); 
        enemy.revertSkin(); 
        enemy.setSpawnPoint(new Position(10,10)); 
    }
}


