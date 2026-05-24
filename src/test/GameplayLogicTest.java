package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class GameplayLogicTest {
    private Board board;
    private HumanPlayer player;
    private GameMode gameMode;

    @Before
    public void setUp() {
        board = new Board(800, 600);
        player = new HumanPlayer(new Position(100, 100), new RedSkin(), new domain.ElementColor(0, 0, 0), 1);
        List<Player> players = new ArrayList<>();
        players.add(player);
        gameMode = new GameMode(players, board);
    }

        @Test
    public void testEnemyCollisionDetection() {
        Enemy enemy = new Enemy(new Position(105, 105), 2.0, new LinearMovement(true));
        board.addVisual(enemy);
        board.addInteractable(enemy);
        board.addTickable(enemy);
        List<Interactable> hits = board.checkCollision(player, Player.BASE_SIZE, Player.BASE_SIZE);
        assertFalse("Deberia haberse detectado una colisión", hits.isEmpty());
        assertTrue("La lista debe contener al enemigo en colisión", hits.contains(enemy));
    }

        @Test
    public void testNoEnemyCollisionWhenFar() {
        Enemy distantEnemy = new Enemy(new Position(400, 400), 2.0, new LinearMovement(true));
        board.addVisual(distantEnemy);
        board.addInteractable(distantEnemy);
        List<Interactable> hits = board.checkCollision(player, Player.BASE_SIZE, Player.BASE_SIZE);
        assertTrue("No debe reportarse colisión si el enemigo está alejado", hits.isEmpty());
    }

        @Test
    public void testEnemyBasicLinearBounce() {
        Wall barrier = new Wall(new Position(130, 100), 20, 20);
        board.addSolid(barrier);
        Enemy linearEnemy = new Enemy(new Position(105, 100), 5.0, new LinearMovement(true));
        board.addVisual(linearEnemy);
        board.addTickable(linearEnemy);
        linearEnemy.tick(board);
        assertEquals(110.0, linearEnemy.getPosition().getX(), 0.001);
        linearEnemy.tick(board);
        linearEnemy.tick(board);
        assertEquals(105.0, linearEnemy.getPosition().getX(), 0.001);
    }

        @Test
    public void testEnemyCircularPatrolPath() {
        CircularMovement movement = new CircularMovement(150, 150, 50, 0, 10.0);
        Enemy circularEnemy = new Enemy(new Position(190, 140), 2.0, movement);
        assertEquals(190.0, circularEnemy.getPosition().getX(), 0.001);
        assertEquals(140.0, circularEnemy.getPosition().getY(), 0.001);
        assertEquals(150.0, movement.getCenterX(), 0.001);
        assertEquals(150.0, movement.getCenterY(), 0.001);
        assertEquals(50.0, movement.getRadius(), 0.001);
        assertEquals(0.0, movement.getAngleDegrees(), 0.001);
        assertEquals(10.0, movement.getSpeed(), 0.001);
        circularEnemy.tick(board);
        assertNotEquals(190.0, circularEnemy.getPosition().getX());
        assertNotEquals(140.0, circularEnemy.getPosition().getY());
    }

        @Test
    public void testGameTimerTickAndExpiration() throws InterruptedException {
        GameTimer timer = new GameTimer(1);
        assertEquals(1, timer.getRemainingTime());
        assertFalse(timer.isTimeUp());
        Thread.sleep(1100);
        timer.tick();
        assertEquals(0, timer.getRemainingTime());
        assertTrue(timer.isTimeUp());
    }

        @Test
    public void testGamePauseLogic() throws DopoException {
        DOPOGame game = new DOPOGame();
        game.startGame("level1", "SinglePlayer", 0, 255, 0, 0, 1, 0, 0, 255);
        game.pauseGame();
        assertTrue(game.isPaused());
        java.util.List<java.util.Map<String, String>> objectsData = game.getObjectsData();
        java.util.List<java.util.Map<String, String>> playersData = game.getPlayersData();
        assertNotNull(objectsData);
        assertNotNull(playersData);
    }

        @Test
    public void testPvPMutualPlayerCollision() {
        HumanPlayer p1 = new HumanPlayer(new Position(100, 100), new RedSkin(), new domain.ElementColor(255, 0, 0), 1);
        HumanPlayer p2 = new HumanPlayer(new Position(102, 100), new BlueSkin(), new domain.ElementColor(0, 0, 255), 2);
        List<Player> activePlayers = Arrays.asList(p1, p2);
        GameMode pvpMode = new GameMode(activePlayers, board);
        pvpMode.handlePlayerPlayerCollision(p1, p2);
        assertEquals(1, p1.getDeaths());
        assertEquals(1, p2.getDeaths());
        pvpMode.tick();
        assertFalse(pvpMode.isGameOver());
    }

        @Test
    public void testPvPPlayerCollisionReset() {
        HumanPlayer p1 = new HumanPlayer(new Position(100, 100), new RedSkin(), new domain.ElementColor(255, 0, 0), 1);
        HumanPlayer p2 = new HumanPlayer(new Position(102, 100), new BlueSkin(), new domain.ElementColor(0, 0, 255), 2);
        YellowCoin boardCoin = new YellowCoin(new Position(200, 200));
        board.addCollectable(boardCoin);
        p1.onCollision(boardCoin);
        p2.onCollision(new YellowCoin(new Position(0,0))); // dummy manual increment for testing
        List<Player> activePlayers = Arrays.asList(p1, p2);
        GameMode pvpMode = new GameMode(activePlayers, board);
        pvpMode.handlePlayerPlayerCollision(p1, p2);
        assertEquals(0, p1.getCollectedElementsCount());
        assertEquals(0, p2.getCollectedElementsCount());
        assertTrue(boardCoin.isActive());
    }

        @Test
    public void testVictoryConditionMet() {
        FinalZone goal = new FinalZone(new Position(300, 300), 50, 50);
        board.addGoal(goal);
        player.setTotalElementsRequired(1);
        YellowCoin requiredCoin = new YellowCoin(new Position(0, 0));
        player.onCollision(requiredCoin);
        player.setPosition(new Position(310, 310));
        assertTrue(gameMode.checkVictory());
        assertEquals(1, player.getTotalElementsRequired());
        assertTrue(requiredCoin.isRequiredForVictory());
        assertEquals(board, gameMode.getBoard());
    }

        @Test
    public void testVictoryConditionDenied() {
        FinalZone goal = new FinalZone(new Position(300, 300), 50, 50);
        board.addGoal(goal);
        player.setTotalElementsRequired(2);
        player.setPosition(new Position(310, 310));
        assertFalse(gameMode.checkVictory());
        assertFalse(gameMode.isGameOver());
    }

        @Test
    public void testEnemyHitResetsCollectedCoins() {
        player.onCollision(new YellowCoin(new Position(0, 0)));
        assertEquals(1, player.getCollectedElementsCount());
        Enemy testEnemy = new Enemy(new Position(100, 100), 2.0, new LinearMovement(true));
        player.onCollision(testEnemy);
        assertEquals(1, player.getDeaths());
        assertEquals(0, player.getCollectedElementsCount());
    }

        @Test
    public void testaticalLinearBounce() {
        Wall barrier = new Wall(new Position(100, 130), 20, 20);
        board.addSolid(barrier);
        Enemy verticalEnemy = new Enemy(new Position(100, 105), 5.0, new LinearMovement(false));
        verticalEnemy.tick(board);
        assertEquals(110.0, verticalEnemy.getPosition().getY(), 0.001);
        verticalEnemy.tick(board);
        verticalEnemy.tick(board);
        assertEquals(105.0, verticalEnemy.getPosition().getY(), 0.001);
    }

        @Test
    public void testGameStateEquality() {
        GameState playing = new PlayingState();
        GameState paused = new PausedState();
        
        assertTrue(playing.isPlaying());
        assertFalse(playing.isPaused());
        assertTrue(paused.isPaused());
        assertFalse(paused.isPlaying());
    }
        @Test
    public void testTerminalStatesLogic() {
        GameState over = new GameOverState();
        assertTrue(over.isGameOver());
        assertFalse(over.isVictory());
        assertEquals(over, over.tick(gameMode, new GameTimer(10)));
        assertEquals(over, over.movePlayer(gameMode, 1, Direction.NORTH));
        assertEquals(over, over.togglePause());

        GameState victory = new VictoryState();
        assertTrue(victory.isVictory());
        assertFalse(victory.isGameOver());
        assertEquals(victory, victory.tick(gameMode, new GameTimer(10)));
        assertEquals(victory, victory.movePlayer(gameMode, 1, Direction.NORTH));
        assertEquals(victory, victory.togglePause());
    }
}


