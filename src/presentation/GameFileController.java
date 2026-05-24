package presentation;

import javax.swing.*;
import java.io.File;
import java.awt.Color;

/**
 * GameFileController se encarga de la lógica de interfaz de usuario relacionada con
 * la persistencia de partidas y carga de archivos de niveles.
 */
public final class GameFileController {

    public void openGame(V2GUI gui) {
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }
        JFileChooser chooser = new JFileChooser(savesDir);
        if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                gui.loadGameFromSave(file);
                
                gui.setLastLoadedFile(file);
                gui.showPanel("Game");
                gui.getGamePanel().requestFocusInWindow();
                gui.getGameLoopController().start();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gui, ex.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveGame(V2GUI gui) {
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }
        JFileChooser chooser = new JFileChooser(savesDir);
        if (chooser.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
            try {
                gui.saveGameToFile(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(gui, "Tablero guardado exitosamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gui, ex.getMessage(), "Error al Guardar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void openCustomLevel(V2GUI gui) {
        File customDir = new File("levels/custom");
        if (!customDir.exists()) {
            customDir.mkdirs();
        }
        JFileChooser chooser = new JFileChooser(customDir);
        if (chooser.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String modeStr = gui.isPvP() ? "PvP" : "SinglePlayer";
                
                Color cP1 = gui.getSelectedBorderColorP1();
                Color cP2 = gui.getSelectedBorderColorP2();
                
                gui.loadCustomLevelAndRestore(file, modeStr, 
                                         gui.getSelectedSkinIndex(), cP1.getRed(), cP1.getGreen(), cP1.getBlue(),
                                         gui.getSelectedSkinIndexP2(), cP2.getRed(), cP2.getGreen(), cP2.getBlue());
                
                gui.setLastLoadedFile(file);
                gui.showPanel("Game");
                gui.getGamePanel().requestFocusInWindow();
                gui.getGameLoopController().start();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(gui, ex.getMessage(), "Error al cargar nivel", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void reloadLastFile(V2GUI gui) {
        File lastLoadedFile = gui.getLastLoadedFile();
        if (lastLoadedFile == null) return;
        try {
            boolean isSave = false;
            String name = lastLoadedFile.getName().toLowerCase();
            if (name.endsWith(".dopo") || name.endsWith(".dat")) {
                isSave = true;
            }

            if (isSave) {
                gui.loadGameFromSave(lastLoadedFile);
                
                gui.showPanel("Game");
                gui.getGamePanel().requestFocusInWindow();
                gui.getGameLoopController().start();
            } else {
                String modeStr = gui.isPvP() ? "PvP" : "SinglePlayer";
                Color cP1 = gui.getSelectedBorderColorP1();
                Color cP2 = gui.getSelectedBorderColorP2();
                
                gui.loadCustomLevelAndRestore(lastLoadedFile, modeStr, 
                                         gui.getSelectedSkinIndex(), cP1.getRed(), cP1.getGreen(), cP1.getBlue(),
                                         gui.getSelectedSkinIndexP2(), cP2.getRed(), cP2.getGreen(), cP2.getBlue());
                
                gui.showPanel("Game");
                gui.getGamePanel().requestFocusInWindow();
                gui.getGameLoopController().start();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, ex.getMessage(), "Error al reiniciar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void launchGame(V2GUI gui, String levelConfig) {
        try {
            File file = new File("levels/" + levelConfig + ".txt");
            if (file.exists()) {
                gui.setLastLoadedFile(file);
            } else {
                gui.setLastLoadedFile(null);
            }
            
            String modeStr = gui.isPvP() ? "PvP" : "SinglePlayer";
            Color cP1 = gui.getSelectedBorderColorP1();
            Color cP2 = gui.getSelectedBorderColorP2();
            
            gui.startGame(levelConfig, modeStr, 
                           gui.getSelectedSkinIndex(), cP1.getRed(), cP1.getGreen(), cP1.getBlue(),
                           gui.getSelectedSkinIndexP2(), cP2.getRed(), cP2.getGreen(), cP2.getBlue());
            gui.showPanel("Game");
            gui.getGamePanel().requestFocusInWindow();
            gui.getGameLoopController().start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(gui, ex.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
}
