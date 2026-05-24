package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domain.*;
import java.util.List;
import java.util.ArrayList;

public class PlayerSkinTest {
    private Board board;
    private HumanPlayer player;
    private GreenSkin greenSkin;
    private PlayerSkin[] availableSkins;

    @Before
    public void setUp() {
        board = new Board(800, 600);
        greenSkin = new GreenSkin();
        player = new HumanPlayer(new Position(100, 100), greenSkin, new ElementColor(0, 0, 0), 1);
        availableSkins = new PlayerSkin[] {
            new RedSkin(),
            new BlueSkin(),
            greenSkin
        };
    }

        @Test
    public void testPlayerDefaultInitialization() {
        Player tempPlayer = new HumanPlayer(new Position(100, 150), new RedSkin(), new ElementColor(255, 0, 0), 1);
        assertEquals(100.0, tempPlayer.getPosition().getX(), 0.001);
        assertEquals(150.0, tempPlayer.getPosition().getY(), 0.001);
        assertEquals(0, tempPlayer.getDeaths());
        assertEquals(0, tempPlayer.getCollectedElementsCount());
        assertFalse(tempPlayer.isInvulnerable());
    }

        @Test
    public void testFactorySinglePlayerMode() {
        List<Player> players = PlayerFactory.createPlayers(
            "SinglePlayer", 
            new RedSkin(), 
            new ElementColor(255, 0, 0), 
            new BlueSkin(), 
            new ElementColor(0, 0, 255), 
            availableSkins
        );
        assertEquals(1, players.size());
        assertEquals(HumanPlayer.class, players.get(0).getClass());
        assertEquals("RedSkin", players.get(0).getCurrentSkin().getName());
        assertEquals(1, ((HumanPlayer) players.get(0)).getPlayerId());
    }

        @Test
    public void testFactoryPvPMode() {
        List<Player> players = PlayerFactory.createPlayers(
            "PvP", 
            new RedSkin(), new ElementColor(255, 0, 0), 
            new BlueSkin(), new ElementColor(0, 0, 255), 
            availableSkins
        );
        assertEquals(2, players.size());
        assertEquals(HumanPlayer.class, players.get(0).getClass());
        assertEquals(HumanPlayer.class, players.get(1).getClass());
        assertEquals("RedSkin", players.get(0).getCurrentSkin().getName());
        assertEquals("BlueSkin", players.get(1).getCurrentSkin().getName());
    }

        @Test
    public void testPlayerMovementNorth() {
        player = new HumanPlayer(new Position(100, 100), new RedSkin(), new ElementColor(0, 0, 0), 1);
        Position next = player.getPosition().translate(0, -player.getCurrentSkin().getSpeed());
        assertEquals(100.0, next.getX(), 0.001);
        assertEquals(100.0 - 2.4, next.getY(), 0.001);
    }

        @Test
    public void testPlayerMovementDiagonal() {
        player = new HumanPlayer(new Position(100, 100), new RedSkin(), new ElementColor(0, 0, 0), 1);
        Position next = player.getPosition().translate(
            player.getCurrentSkin().getSpeed(), 
            -player.getCurrentSkin().getSpeed()
        );
        assertEquals(100.0 + 2.4, next.getX(), 0.001);
        assertEquals(100.0 - 2.4, next.getY(), 0.001);
    }

        @Test
    public void testPlayerMovementStepPrecision() {
        Position current = player.getPosition();
        double speed = player.getCurrentSkin().getSpeed();
        for (int i = 0; i < 10; i++) {
            current = current.translate(speed, 0);
        }
        player.setPosition(current);
        assertEquals(124.0, player.getPosition().getX(), 0.001);
        assertEquals(100.0, player.getPosition().getY(), 0.001);
        player.setSpeed(5.0);
        assertEquals(5.0, player.getSpeed(), 0.001);
    }

        @Test
    public void testDiagonalDisplacementLimit() {
        player = new HumanPlayer(new Position(0, 0), new RedSkin(), new ElementColor(0, 0, 0), 1);
        double speed = player.getCurrentSkin().getSpeed(); // 2.4
        Position next = player.getPosition().translate(speed, -speed);
        double distance = Math.sqrt(Math.pow(next.getX() - 0, 2) + Math.pow(next.getY() - 0, 2));
        assertTrue("La distancia diagonal calculada excede el límite fisico", distance <= speed * Math.sqrt(2));
    }

        @Test
    public void testRedSkinMortalHit() {
        player = new HumanPlayer(new Position(100, 100), new RedSkin(), new ElementColor(0, 0, 0), 1);
        HitResult result = player.receiveHit();
        assertEquals(HitResult.DEAD, result);
        assertEquals(1, player.getDeaths());
    }

        @Test
    public void testBlueSkinAttributes() {
        player = new HumanPlayer(new Position(100, 100), new BlueSkin(), new ElementColor(0, 0, 0), 1);
        assertEquals(3.6, player.getCurrentSkin().getSpeed(), 0.001);
        java.util.Map<String, String> data = player.toVisualMap();
        assertEquals("30", data.get("w"));
        assertEquals("30", data.get("h"));
        assertEquals("BlueSkin", player.getCurrentSkin().getName());
        assertEquals(HitResult.DEAD, player.getCurrentSkin().onHit(player));
    }

        @Test
    public void testGreenSkinFirstHitShield() {
        player = new HumanPlayer(new Position(100, 100), new GreenSkin(), new ElementColor(0, 0, 0), 1);
        assertEquals("GreenSkin", player.getCurrentSkin().getName());
        assertEquals(1.0, player.getCurrentSkin().getSize(), 0.001);
        ElementColor clydeColor = ((GreenSkin) player.getCurrentSkin()).getColor();
        assertEquals(0, clydeColor.getR());
        assertEquals(255, clydeColor.getG());
        assertEquals(0, clydeColor.getB());
        assertFalse(((GreenSkin) player.getCurrentSkin()).isResistanceUsed());
        HitResult result = player.receiveHit();
        assertEquals(HitResult.SURVIVED_SHIELD, result);
        assertEquals(0, player.getDeaths());
        assertEquals(2.4 * 0.7, player.getCurrentSkin().getSpeed(), 0.001);
        assertTrue(player.isInvulnerable());
        assertTrue(((GreenSkin) player.getCurrentSkin()).isResistanceUsed());
    }

        @Test
    public void testGreenSkinInvulnerabilityTimer() {
        player.receiveHit();
        assertTrue(player.isInvulnerable());
        for (int i = 0; i < Player.INVULNERABILITY_TICKS; i++) {
            player.decreaseInvulnerability();
        }
        assertFalse(player.isInvulnerable());
    }

        @Test
    public void testGreenSkinSecondHitFatal() {
        player.receiveHit();
        for (int i = 0; i < Player.INVULNERABILITY_TICKS; i++) {
            player.decreaseInvulnerability();
        }
        HitResult result = player.receiveHit();
        assertEquals(HitResult.DEAD, result);
        assertEquals(1, player.getDeaths());
    }

        @Test
    public void testGreenSkinRespawnResetsShield() {
        player.receiveHit();
        for (int i = 0; i < Player.INVULNERABILITY_TICKS; i++) {
            player.decreaseInvulnerability();
        }
        player.receiveHit();
        player.respawn();
        assertEquals(2.4, player.getCurrentSkin().getSpeed(), 0.001);
        assertFalse(player.isInvulnerable());
        ((GreenSkin) player.getCurrentSkin()).resetResistance();
        assertFalse(((GreenSkin) player.getCurrentSkin()).isResistanceUsed());
    }

        @Test
    public void testPlayerBoundsObstruction() {
        assertTrue(board.isWalkable(new Position(0, 0), Player.BASE_SIZE));
        assertFalse(board.isWalkable(new Position(-1, 100), Player.BASE_SIZE));
        assertFalse(board.isWalkable(new Position(100, -1), Player.BASE_SIZE));
        assertFalse(board.isWalkable(new Position(800 - Player.BASE_SIZE + 1, 100), Player.BASE_SIZE));
        assertFalse(board.isWalkable(new Position(100, 600 - Player.BASE_SIZE + 1), Player.BASE_SIZE));
    }

        @Test
    public void testPositionAlgebra() {
        Position pos = new Position(10.0, 20.0);
        pos.setX(30.0);
        pos.setY(40.0);
        assertEquals(30.0, pos.getX(), 0.001);
        assertEquals(40.0, pos.getY(), 0.001);
        Position other = new Position(33.0, 44.0);
        double distance = pos.distanceTo(other);
        assertEquals(5.0, distance, 0.001);
    }



        @Test
    public void testDirectionFromVectorParsing() {
        assertEquals(Direction.NORTH, Direction.fromVector(0, -1));
        assertEquals(Direction.SOUTH, Direction.fromVector(0, 1));
        assertEquals(Direction.EAST, Direction.fromVector(1, 0));
        assertEquals(Direction.WEST, Direction.fromVector(-1, 0));
        assertEquals(Direction.NONE, Direction.fromVector(0, 0));
        assertEquals(Direction.NONE, Direction.fromVector(99, 99));
    }
}



