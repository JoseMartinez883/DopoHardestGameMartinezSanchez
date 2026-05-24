package domain;

/**
 * manejo de errores del juego
 */
public class DopoException extends Exception {

    public static final String LEVEL_NOT_FOUND = "Error: No se encontró el archivo del nivel.";
    public static final String PARSE_ERROR = "Error: Formato de nivel inválido en la línea: ";
    public static final String UNKNOWN_COMMAND = "Error: Comando desconocido en el archivo de nivel: ";
    public static final String MISSING_INITIAL_ZONE = "Error: El nivel debe contener al menos una zona de inicio (INITIAL).";
    public static final String MISSING_FINAL_ZONE = "Error: El nivel debe contener al menos una zona final (FINAL).";
    public static final String INVALID_DIMENSION = "Error: Las dimensiones de elementos deben ser mayores a cero.";
    public static final String INVALID_SPEED = "Error: La velocidad de movimiento no puede ser negativa.";
    public static final String INVALID_RADIUS = "Error: El radio de patrulla no puede ser negativo.";

    public DopoException(String message) {
        super(message);
    }

    public DopoException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String formatLevelError(int line, String command, String cause) {
        return String.format("Error en lnea %d [Comando %s]: %s", line, command, cause);
    }
}
