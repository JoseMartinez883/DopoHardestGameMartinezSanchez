package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domain.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public class PersistenceLogTest {
    private Board board;
    private HumanPlayer player;

    @Before
    public void setUp() {
        board = new Board(800, 600);
        player = new HumanPlayer(new Position(100, 100), new RedSkin(), new domain.ElementColor(0, 0, 0), 1);
    }

        @Test
    public void testDeepSerializationIntegrity() {
        try {
            DOPOGame originalGame = new DOPOGame();
            originalGame.startGame("level1", "SinglePlayer", 0, 255, 0, 0, 1, 0, 0, 255);
            Player gamePlayer = originalGame.getGameMode().getPlayers().get(0);
            gamePlayer.setPosition(new Position(150, 250));
            gamePlayer.onCollision(new YellowCoin(new Position(10, 10)));
            File tempFile = File.createTempFile("dopo_test_save", ".dopo");
            tempFile.deleteOnExit(); // Se asegura de borrarlo al terminar la JVM
            DOPOPersistence.save(tempFile, originalGame);
            DOPOGame loadedGame = DOPOPersistence.open(tempFile);
            assertNotNull("La partida cargada no Deberia ser nula", loadedGame);
            List<java.util.Map<String, String>> playersData = loadedGame.getPlayersData();
            assertEquals("Deberia haber exactamente 1 jugador", 1, playersData.size());
            assertEquals("La coordenada X del jugador debe ser igual", "150", playersData.get(0).get("x"));
            assertEquals("La coordenada Y del jugador debe ser igual", "250", playersData.get(0).get("y"));
            assertEquals("Las monedas recolectadas deben coincidir", 1, loadedGame.getPlayerCollectedCoins(0));
            
        } catch (Exception e) {
            fail("Excepción inesperada en prueba de persistencia profunda: " + e.getMessage());
        }
    }

        @Test
    public void testLevelLoaderValidationInvalidWallDimension() {
        List<String> badLines = Arrays.asList(
            "TIME 60",
            "WALL 10 10 0 50 true"
        );
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            parseMethod.invoke(null, badLines, new java.util.HashMap<>());
            fail("Deberia haber fallado por dimensión inválida (ancho <= 0)");
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            assertEquals(DopoException.class, cause.getClass());
            assertTrue("El mensaje debe reportar dimensión inválida", cause.getMessage().contains(DopoException.INVALID_DIMENSION));
        }
    }

        @Test
    public void testLevelLoaderValidationNegativeEnemySpeed() {
        List<String> badLines = Arrays.asList(
            "TIME 60",
            "ENEMY BASIC 100 100 H -2.5"
        );
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            parseMethod.invoke(null, badLines, new java.util.HashMap<>());
            fail("Deberia haber fallado por velocidad negativa");
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            assertEquals(DopoException.class, cause.getClass());
            assertTrue("El mensaje debe reportar velocidad inválida", cause.getMessage().contains(DopoException.INVALID_SPEED));
        }
    }

        @Test
    public void testLevelLoaderValidationNegativePatrolRadius() {
        List<String> badLines = Arrays.asList(
            "TIME 60",
            "ENEMY PATROL 100 100 -50 0 2.0"
        );
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            parseMethod.invoke(null, badLines, new java.util.HashMap<>());
            fail("Deberia haber fallado por radio negativo");
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            assertEquals(DopoException.class, cause.getClass());
            assertTrue("El mensaje debe reportar radio inválido", cause.getMessage().contains(DopoException.INVALID_RADIUS));
        }
    }

        @Test
    public void testLevelLoaderValidationMissingZones() {
        List<String> badLines = Arrays.asList(
            "TIME 60",
            "ZONE FINAL 200 200 50 50"
        );
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            parseMethod.invoke(null, badLines, new java.util.HashMap<>());
            fail("Deberia haber fallado por falta de zona inicial");
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            assertEquals(DopoException.class, cause.getClass());
            assertTrue("El mensaje debe reportar falta de zona inicial", cause.getMessage().contains(DopoException.MISSING_INITIAL_ZONE));
        }
    }

        @Test
    public void testLevelLoaderExceptionOnMalformedInput() {
        List<String> malformedLines = Arrays.asList(
            "TIME 60",
            "INVALIDO_CMD 100 100"
        );
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            parseMethod.invoke(null, malformedLines, new java.util.HashMap<>());
            fail("Deberia haber fallado por comando no reconocido");
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            assertEquals(DopoException.class, cause.getClass());
        }
    }

        @Test
    public void testLoggerCreationAndIntegrity() {
        try {
            LevelLoader.loadLevel("nivel_fantasma_inexistente", new java.util.HashMap<>());
            fail("Deberia haber lanzado DopoException por nivel no encontrado");
        } catch (DopoException ex) {
            Log.record(ex);
            File logFile = new File("Tower.log");
            assertTrue("El archivo de logs 'Tower.log' debe haberse creado físicamente", logFile.exists());
            assertTrue("El archivo de logs no debe estar vacío", logFile.length() > 0);
        }
    }

        @Test
    public void testLoggerAppendPerformance() {
        File logFile = new File("Tower.log");
        if (logFile.exists()) {
            logFile.delete();
        }
        Log.record(new DopoException("ERROR_TEST_1"));
        long initialLength = logFile.length();
        Log.record(new DopoException("ERROR_TEST_2"));
        long finalLength = logFile.length();
        assertTrue("El archivo de logs debe crecer al agregar registros", finalLength > initialLength);
    }

        @Test
    public void testGameFacadeDecouplingLoadCustomLevel() {
        DOPOGame game = new DOPOGame();
        File dummyFile = new File("levels/nivel_no_existente_custom.txt");
        try {
            game.loadLevelAndRestore(dummyFile, "SinglePlayer", 0, 0, 0, 0, 0, 0, 0, 0);
            fail("Deberia haber lanzado DopoException por archivo inexistente");
        } catch (DopoException ex) {
            assertTrue("Debe contener el mensaje de archivo no encontrado", ex.getMessage().contains(DopoException.LEVEL_NOT_FOUND));
        }
    }

        @Test
    public void testDOPOGameSaveOpenExceptionLogging() {
        DOPOGame game = new DOPOGame();
        File invalidFile = new File("directorio_fantasma_inexistente/save.dopo");
        
        try {
            game.save(invalidFile);
            fail("Deberia haber fallado al guardar en un directorio inválido");
        } catch (DopoException ex) {
            assertNotNull("La excepción no debe ser nula", ex);
        }

        try {
            DOPOGame.open(invalidFile);
            fail("Deberia haber fallado al abrir un archivo inexistente");
        } catch (DopoException ex) {
            assertNotNull("La excepción no debe ser nula", ex);
        }
    }

        @Test
    public void testLevelLoaderValidationExtended() {
        try {
            java.lang.reflect.Method parseMethod = LevelLoader.class.getDeclaredMethod("parseLevelLines", List.class, java.util.Map.class);
            parseMethod.setAccessible(true);
            List<String> intermediateZoneLines = Arrays.asList(
                "TIME 60",
                "ZONE INITIAL 0 0 10 10",
                "ZONE FINAL 100 100 10 10",
                "ZONE INTERMEDIATE 50 50 30 30"
            );
            Board b1 = (Board) parseMethod.invoke(null, intermediateZoneLines, new java.util.HashMap<>());
            assertFalse(b1.getInteractables().isEmpty());
            List<String> unknownZoneLines = Arrays.asList(
                "TIME 60",
                "ZONE UNKNOWN 50 50 30 30"
            );
            try {
                parseMethod.invoke(null, unknownZoneLines, new java.util.HashMap<>());
                fail("Deberia haber fallado por tipo de zona desconocido");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Tipo de zona desconocido"));
            }
            List<String> validSkinCoins = Arrays.asList(
                "TIME 60",
                "ZONE INITIAL 0 0 10 10",
                "ZONE FINAL 100 100 10 10",
                "COIN SKIN BLUE 20 20",
                "COIN SKIN GREEN 30 30",
                "COIN SKIN RED 40 40"
            );
            java.util.Map<String, PlayerSkin> testReg = new java.util.HashMap<>();
            testReg.put("BLUE", new BlueSkin());
            testReg.put("GREEN", new GreenSkin());
            testReg.put("RED", new RedSkin());
            Board b2 = (Board) parseMethod.invoke(null, validSkinCoins, testReg);
            assertEquals(3, b2.getCollectables().size());
            List<String> unknownSkinCoin = Arrays.asList(
                "TIME 60",
                "COIN SKIN PURPLE 20 20"
            );
            try {
                parseMethod.invoke(null, unknownSkinCoin, new java.util.HashMap<>());
                fail("Deberia haber fallado por tipo de skin desconocido");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Tipo de skin desconocido"));
            }
            List<String> incompleteSkinCoin = Arrays.asList(
                "TIME 60",
                "COIN SKIN BLUE 20"
            );
            try {
                parseMethod.invoke(null, incompleteSkinCoin, new java.util.HashMap<>());
                fail("Deberia haber fallado por parámetros incompletos en COIN SKIN");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Faltan par"));
            }
            List<String> validYellowCoin = Arrays.asList(
                "TIME 60",
                "ZONE INITIAL 0 0 10 10",
                "ZONE FINAL 100 100 10 10",
                "COIN YELLOW 10 10"
            );
            Board b3 = (Board) parseMethod.invoke(null, validYellowCoin, new java.util.HashMap<>());
            assertEquals(1, b3.getCollectables().size());
            List<String> incompleteYellowCoin = Arrays.asList(
                "TIME 60",
                "COIN YELLOW 10"
            );
            try {
                parseMethod.invoke(null, incompleteYellowCoin, new java.util.HashMap<>());
                fail("Deberia haber fallado por parámetros incompletos en COIN YELLOW");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Faltan par"));
            }
            List<String> unknownCoinType = Arrays.asList(
                "TIME 60",
                "COIN BRONZE 10 10"
            );
            try {
                parseMethod.invoke(null, unknownCoinType, new java.util.HashMap<>());
                fail("Deberia haber fallado por tipo de moneda desconocido");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Tipo de moneda desconocido"));
            }
            List<String> invalidEnemyBasicDir = Arrays.asList(
                "TIME 60",
                "ENEMY BASIC 10 10 X 2.0"
            );
            try {
                parseMethod.invoke(null, invalidEnemyBasicDir, new java.util.HashMap<>());
                fail("Deberia haber fallado por dirección básica inválida");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Dirección de enemigo básico inválida"));
            }
            List<String> validEnemyPatrol = Arrays.asList(
                "TIME 60",
                "ZONE INITIAL 0 0 10 10",
                "ZONE FINAL 100 100 10 10",
                "ENEMY PATROL 150 150 50 0 2.0"
            );
            Board b4 = (Board) parseMethod.invoke(null, validEnemyPatrol, new java.util.HashMap<>());
            assertFalse(b4.getTickables().isEmpty());
            List<String> negativePatrolSpeed = Arrays.asList(
                "TIME 60",
                "ENEMY PATROL 150 150 50 0 -2.0"
            );
            try {
                parseMethod.invoke(null, negativePatrolSpeed, new java.util.HashMap<>());
                fail("Deberia haber fallado por velocidad negativa en PATROL");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains(DopoException.INVALID_SPEED));
            }
            List<String> unknownEnemyType = Arrays.asList(
                "TIME 60",
                "ENEMY ALIEN 10 10"
            );
            try {
                parseMethod.invoke(null, unknownEnemyType, new java.util.HashMap<>());
                fail("Deberia haber fallado por tipo de enemigo desconocido");
            } catch (Exception e) {
                assertTrue(e.getCause().getMessage().contains("Tipo de enemigo desconocido"));
            }

        } catch (Exception e) {
            fail("Excepción inesperada en test de reflexión del LevelLoader: " + e.getMessage());
        }
    }

        @Test
    public void testDOPOGameLifecycleAndRestore() {
        DOPOGame game = new DOPOGame();
        try {
            game.startGame("level1", "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
            assertEquals("level1", game.getCurrentLevelName());
            assertNotNull(game.getBoard());
            try {
                game.startGame("nivel_fantasma_inexistente", "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
                fail("Deberia haber fallado por nivel no encontrado");
            } catch (DopoException ex) {
                assertTrue(ex.getMessage().contains(DopoException.LEVEL_NOT_FOUND));
            }
            game.loadLevelAndRestore(new File("levels/level1.txt"), "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
            assertEquals("level1", game.getCurrentLevelName());
            assertNotNull(game.getBoard());
            try {
                game.loadLevelAndRestore(new File("levels/nivel_no_existente_custom.txt"), "InvalidMode", 0, 0, 0, 0, 0, 0, 0, 0);
                fail("Deberia haber fallado por archivo inexistente");
            } catch (DopoException ex) {
                assertNotNull(ex);
            }

        } catch (Exception e) {
            fail("Excepción inesperada en prueba de ciclo de vida de DOPOGame: " + e.getMessage());
        }
    }

        @Test
    public void testDOPOGameGettersAndFallbacks() {
        DOPOGame game = new DOPOGame();
        try {
            game.startGame("level1", "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
            assertNotNull(game.getAvailableSkins());
            assertEquals(3, game.getAvailableSkins().length);
            assertNotNull(game.getGameMode());
            assertNotNull(game.getTimer());
            Board customBoard = new Board(500, 500);
            game.setBoard(customBoard);
            assertEquals(customBoard, game.getBoard());
            assertEquals(0, game.getPlayerDeaths(0));
            assertEquals(0, game.getPlayerCollectedCoins(0));
            assertTrue(game.getPlayerTotalCoinsRequired(0) >= 0);
            assertEquals(0, game.getPlayerDeaths(99));
            assertEquals(0, game.getPlayerCollectedCoins(99));
            assertEquals(0, game.getPlayerTotalCoinsRequired(99));
            game.setPlayerDirection(0, 0, 1); // SOUTH
            game.setPlayerDirection(99, 1, 0);

        } catch (Exception e) {
            fail("Excepción inesperada en prueba de getters y fallbacks: " + e.getMessage());
        }
    }

        @Test
    public void testDOPOGameFacadeExtremelyRigorous() {
        DOPOGame game = new DOPOGame();
        assertTrue(game.isReady());
        assertFalse(game.isPlaying());
        assertFalse(game.isPaused());
        assertFalse(game.isGameOver());
        assertFalse(game.isVictory());

        assertNull(game.getBoard());
        assertNull(game.getGameMode());
        assertNull(game.getTimer());
        game.movePlayer(1, Direction.NORTH); // No debe explotar, retorna inmediato
        game.tick(); // No debe explotar, retorna inmediato
        assertTrue(game.getObjectsData().isEmpty());
        assertTrue(game.getPlayersData().isEmpty());
        
        try {
            game.startGame("level1", "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
            assertTrue(game.isPlaying());
            assertFalse(game.isReady());
            assertFalse(game.getObjectsData().isEmpty());
            assertFalse(game.getPlayersData().isEmpty());
            game.pauseGame();
            assertTrue(game.isPaused());
            assertFalse(game.isPlaying());
            game.pauseGame();
            assertTrue(game.isPaused());
            game.togglePause();
            assertTrue(game.isPlaying());
            game.togglePause();
            assertTrue(game.isPaused());
            game.resumeGame();
            assertTrue(game.isPlaying());
            game.resumeGame();
            assertTrue(game.isPlaying());
            game.movePlayer(1, Direction.EAST);
            game.endGame();
            assertTrue(game.isGameOver());
            game.loadLevelAndRestore(new File("levels/level1.txt"), "SinglePlayer", 0, 0, 0, 0, 1, 255, 255, 255);
            assertTrue(game.isPlaying());
            java.lang.reflect.Field remainingField = GameTimer.class.getDeclaredField("remainingMs");
            remainingField.setAccessible(true);
            remainingField.set(game.getTimer(), 0L);
            game.tick();
            assertTrue(game.isGameOver());
            
        } catch (Exception e) {
            fail("Excepción inesperada en prueba rigurosa de fachada: " + e.getMessage());
        }
    }
}


