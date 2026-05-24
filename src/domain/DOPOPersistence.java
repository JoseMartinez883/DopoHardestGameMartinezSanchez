package domain;

import java.io.*;

/**
 * Clase encargada de la lógica de guardado y carga de archivos binarios (serialización).
 */
public class DOPOPersistence {

    /**
     * Guarda el estado completo de la partida usando serialización de objetos en disco.
     */
    public static void save(File file, DOPOGame game) throws DopoException {
        try (ObjectOutputStream oos = new ObjectOutputStream(java.nio.file.Files.newOutputStream(file.toPath()))) {
            oos.writeObject(game);
        } catch (IOException e) {
            throw new DopoException("Error al guardar la partida por serialización binaria: " + e.getMessage());
        }
    }

    /**
     * Abre y restaura el estado completo de la partida usando deserialización de objetos desde el disco.
     */
    public static DOPOGame open(File file) throws DopoException {
        try (ObjectInputStream ois = new ObjectInputStream(java.nio.file.Files.newInputStream(file.toPath()))) {
            return (DOPOGame) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new DopoException("Error al cargar la partida por serialización binaria: " + e.getMessage());
        }
    }
}
