package domain;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

/**
 * Class for recording unexpected errors in a log file.
 * Intended for the development team, users should never see the raw log content.
 * @author DOPO
 * @version ECI 2026
 */
public class Log {
    /**
     * @param name Name used for the logger and the log file (Tower.log)
     * @param UNEXPECTED_ERROR Message shown to the user when an unexpected error occurs.
     */
    public static String name = "Tower";
    public static final String UNEXPECTED_ERROR = "Ha ocurrido un error inesperado. Por favor contacte al equipo de desarrollo.";
    
    /**
     * Records an unexpected error in the log file (Tower.log).
     * This information is intended for the development team only.
     * @param e the exception to record
     */
    public static void record(Exception e) {
        try {
            Logger logger = Logger.getLogger(name);
            logger.setUseParentHandlers(false);
            FileHandler file = new FileHandler(name + ".log", true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, e.toString(), e);
            }
            file.close();
        } catch (Exception oe) {
            oe.printStackTrace();
            System.exit(0);
        }
    }
}
