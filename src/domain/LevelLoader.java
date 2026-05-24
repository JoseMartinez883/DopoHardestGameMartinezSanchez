package domain;

import java.util.*;
import java.nio.file.*;
import java.io.IOException;

/**
 * Cargador de los niveles del juego
 */
public final class LevelLoader {
    private LevelLoader() {}

    private static final Map<String, LevelAction> COMMAND_REGISTRY = new HashMap<>();

    @FunctionalInterface
    private interface LevelAction {
        void execute(String[] parts, Board board, Map<String, PlayerSkin> skinRegistry) throws Exception;
    }

    static {
        COMMAND_REGISTRY.put("TIME", (parts, board, reg) -> board.setTimeLimit(Integer.parseInt(parts[1])));
        COMMAND_REGISTRY.put("FLOOR", LevelLoader::parseFloor);
        COMMAND_REGISTRY.put("WALL", LevelLoader::parseWall);
        COMMAND_REGISTRY.put("ZONE", LevelLoader::parseZone);
        COMMAND_REGISTRY.put("COIN", LevelLoader::parseCoin);
        COMMAND_REGISTRY.put("ENEMY", LevelLoader::parseEnemy);
        COMMAND_REGISTRY.put("ITEM", LevelLoader::parseItem);
    }

    public static Board loadLevel(java.io.File file, Map<String, PlayerSkin> registry) throws DopoException {
        List<String> lines;
        try {
            lines = Files.readAllLines(file.toPath(), java.nio.charset.StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            throw new DopoException(DopoException.LEVEL_NOT_FOUND + ": " + file.getName(), e);
        }
        return parseLevelLines(lines, registry);
    }

    public static Board loadLevel(String levelConfig, Map<String, PlayerSkin> registry) throws DopoException {
        String filePath = "levels/" + levelConfig + ".txt";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath), java.nio.charset.StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            throw new DopoException(DopoException.LEVEL_NOT_FOUND + ": " + levelConfig, e);
        }
        return parseLevelLines(lines, registry);
    }

    private static Board parseLevelLines(List<String> lines, Map<String, PlayerSkin> registry) throws DopoException {
        Board board = new Board(800, 600);

        
        int lineNumber = 0;
        for (String line : lines) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            try {
                LevelAction action = COMMAND_REGISTRY.get(command);
                if (action == null) {
                    throw new IllegalArgumentException(DopoException.UNKNOWN_COMMAND + command);
                }
                action.execute(parts, board, registry);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new DopoException(DopoException.formatLevelError(lineNumber, command, "Faltan parámetros"), e);
            } catch (Exception e) {
                throw new DopoException(DopoException.formatLevelError(lineNumber, command, e.getMessage()), e);
            }
        }

        if (board.getSpawnZones().isEmpty()) {
            throw new DopoException(DopoException.MISSING_INITIAL_ZONE);
        }
        if (board.getGoals().isEmpty()) {
            throw new DopoException(DopoException.MISSING_FINAL_ZONE);
        }

        return board;
    }

    private static void parseFloor(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        if (parts.length < 6) throw new IllegalArgumentException("Faltan parámetros para FLOOR.");
        double x = Double.parseDouble(parts[2]);
        double y = Double.parseDouble(parts[3]);
        double w = Double.parseDouble(parts[4]);
        double h = Double.parseDouble(parts[5]);
        if (w <= 0 || h <= 0) throw new IllegalArgumentException(DopoException.INVALID_DIMENSION);
        board.addVisual(new CheckeredFloor(new Position(x, y), w, h));
    }

    private static void parseWall(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        if (parts.length < 6) throw new IllegalArgumentException("Faltan parámetros para WALL.");
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double w = Double.parseDouble(parts[3]);
        double h = Double.parseDouble(parts[4]);
        boolean visible = Boolean.parseBoolean(parts[5]);
        if (w <= 0 || h <= 0) throw new IllegalArgumentException(DopoException.INVALID_DIMENSION);
        Wall wall = new Wall(new Position(x, y), w, h);
        wall.setVisible(visible);
        board.addVisual(wall);
        board.addSolid(wall); 
    }

    private static void parseZone(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        if (parts.length < 6) throw new IllegalArgumentException("Faltan parámetros para ZONE.");
        String type = parts[1].toUpperCase();
        double x = Double.parseDouble(parts[2]);
        double y = Double.parseDouble(parts[3]);
        double w = Double.parseDouble(parts[4]);
        double h = Double.parseDouble(parts[5]);
        
        if (w <= 0 || h <= 0) throw new IllegalArgumentException(DopoException.INVALID_DIMENSION);
        
        if (type.equals("INITIAL")) {
            InitialZone z = new InitialZone(new Position(x, y), w, h);
            board.addVisual(z);
            board.addSpawnZone(z);
        } else if (type.equals("FINAL")) {
            FinalZone z = new FinalZone(new Position(x, y), w, h);
            board.addVisual(z);
            board.addGoal(z);
        } else if (type.equals("INTERMEDIATE")) {
            IntermediateZone z = new IntermediateZone(new Position(x, y), w, h);
            board.addVisual(z);
            board.addInteractable(z); 
        } else {
            throw new IllegalArgumentException("Tipo de zona desconocido: " + type);
        }
    }

    private static void parseCoin(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        String type = parts[1].toUpperCase();
        if (type.equals("SKIN")) {
            if (parts.length < 5) throw new IllegalArgumentException("Faltan parámetros para COIN SKIN.");
            String skinType = parts[2].toUpperCase();
            double x = Double.parseDouble(parts[3]);
            double y = Double.parseDouble(parts[4]);
            PlayerSkin skin = reg.get(skinType);
            if (skin == null) {
                throw new IllegalArgumentException("Tipo de skin desconocido o no registrado: " + skinType);
            }
            SkinCoin coin = new SkinCoin(new Position(x, y), skin);
            board.addVisual(coin);
            board.addInteractable(coin);
            board.addCollectable(coin);
        } else if (type.equals("YELLOW")) {
            if (parts.length < 4) throw new IllegalArgumentException("Faltan parámetros para COIN YELLOW.");
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            YellowCoin coin = new YellowCoin(new Position(x, y));
            board.addVisual(coin);
            board.addInteractable(coin);
            board.addCollectable(coin);
        } else {
            throw new IllegalArgumentException("Tipo de moneda desconocido: " + type);
        }
    }

    private static void parseEnemy(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        String type = parts[1].toUpperCase();
        if (type.equals("BASIC")) {
            if (parts.length < 6) throw new IllegalArgumentException("Faltan parámetros para ENEMY BASIC.");
            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            String dir = parts[4];
            if (!dir.equalsIgnoreCase("H") && !dir.equalsIgnoreCase("V")) {
                throw new IllegalArgumentException("Dirección de enemigo básico inválida: " + dir);
            }
            boolean isH = dir.equalsIgnoreCase("H");
            double speed = Double.parseDouble(parts[5]);
            if (speed < 0) throw new IllegalArgumentException(DopoException.INVALID_SPEED);
      
            Enemy e = new Enemy(new Position(x, y), speed, new LinearMovement(isH));
            board.addVisual(e);
            board.addInteractable(e);
            board.addTickable(e);
        } else if (type.equals("PATROL")) {
            if (parts.length < 7) throw new IllegalArgumentException("Faltan parámetros para ENEMY PATROL.");
            double cx = Double.parseDouble(parts[2]);
            double cy = Double.parseDouble(parts[3]);
            double r = Double.parseDouble(parts[4]);
            double angle = Double.parseDouble(parts[5]);
            double speed = Double.parseDouble(parts[6]);
            if (r < 0) throw new IllegalArgumentException(DopoException.INVALID_RADIUS);
            if (speed < 0) throw new IllegalArgumentException(DopoException.INVALID_SPEED);
            
            double rad = Math.toRadians(angle);
            Position pos = new Position((cx + r * Math.cos(rad)) - Enemy.VISUAL_RADIUS, (cy + r * Math.sin(rad)) - Enemy.VISUAL_RADIUS);
            
            Enemy e = new Enemy(pos, speed, new CircularMovement(cx, cy, r, angle, speed));
            board.addVisual(e);
            board.addInteractable(e);
            board.addTickable(e);
        } else {
            throw new IllegalArgumentException("Tipo de enemigo desconocido: " + type);
        }
    }
    private static void parseItem(String[] parts, Board board, Map<String, PlayerSkin> reg) {
        String type = parts[1].toUpperCase();
        double x = Double.parseDouble(parts[2]);
        double y = Double.parseDouble(parts[3]);
        if (type.equals("BOMB")) {
            Bomb b = new Bomb(new Position(x, y));
            board.addVisual(b);
            board.addInteractable(b);
            board.addCollectable(b);
        } else if (type.equals("HEALTH")) {
            HealthFountain h = new HealthFountain(new Position(x, y));
            board.addVisual(h);
            board.addInteractable(h);
            board.addCollectable(h);
        } else {
            throw new IllegalArgumentException("Tipo de item desconocido: " + type);
        }
    }
}
