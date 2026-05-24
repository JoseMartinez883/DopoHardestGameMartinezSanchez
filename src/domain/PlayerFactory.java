package domain;

import java.util.List;
import java.util.ArrayList;

/**
 * Fábrica encargada de la instanciación de Jugadores en el Dominio.
 * Cumple con SRP (Single Responsibility Principle) al aislar la creación de entidades de negocio.
 * Cumple con OCP (Open/Closed Principle) al permitir soportar modos como IA en el futuro
 * sin modificar la GUI ni la fachada del juego.
 */
public class PlayerFactory {

    public static List<Player> createPlayers(String mode, 
                                             PlayerSkin skin1, ElementColor border1, 
                                             PlayerSkin skin2, ElementColor border2, 
                                             PlayerSkin[] availableSkins) {
        List<Player> players = new ArrayList<>();
        
        // Jugador 1: Humano en coordenadas iniciales del dominio (0, 0)
        players.add(new HumanPlayer(new Position(0, 0), skin1, border1, 1));
        
        // Jugador 2: Si el modo es PvP, se crea un segundo Humano
        if ("PvP".equalsIgnoreCase(mode)) {
            PlayerSkin s2 = (skin2 != null) ? skin2 : availableSkins[1];
            players.add(new HumanPlayer(new Position(0, 0), s2, border2, 2));
        }
        
        return players;
    }
}
