package presentation;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * VictoryDialog muestra la pantalla de victoria al completar un nivel,
 * ofreciendo opciones para pasar al siguiente nivel, reiniciar o volver al menú.
 */
public final class VictoryDialog extends JDialog {
    private final V2GUI gui;

    public VictoryDialog(V2GUI gui) {
        super(gui, "¡Victoria!", true);
        this.gui = gui;
        
        gui.getGameLoopController().stop();
        
        String nextLevel = null;
        File lastLoadedFile = gui.getLastLoadedFile();
        if (lastLoadedFile != null) {
            String fileName = lastLoadedFile.getName();
            if (fileName.startsWith("level") && fileName.endsWith(".txt")) {
                try {
                    String numStr = fileName.substring(5, fileName.length() - 4);
                    int num = Integer.parseInt(numStr);
                    if (num < 18) {
                        nextLevel = "level" + (num + 1);
                    }
                } catch (NumberFormatException e) {
                    // No es un número secuencial estándar
                }
            }
        }
        
        setSize(300, 360);
        setLocationRelativeTo(gui);
        setUndecorated(true);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createLineBorder(new Color(153, 255, 153), 3));
        panel.setLayout(null);
        
        JLabel titleLabel = new JLabel("¡VICTORIA!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titleLabel.setForeground(new Color(153, 255, 153));
        
        Font btnFont = new Font("Comic Sans MS", Font.BOLD, 16);
        Color btnBg = new Color(30, 30, 30);
        Color btnFg = Color.WHITE;
        Color neonColor = new Color(153, 255, 153);
        
        int btnWidth = 240;
        int btnHeight = 45;
        int btnX = (300 - btnWidth) / 2;
        
        if (nextLevel != null) {
            titleLabel.setBounds(0, 30, 300, 40);
            panel.add(titleLabel);
            
            final String next = nextLevel;
            JButton btnNext = new JButton("Siguiente Nivel");
            btnNext.setFont(btnFont);
            btnNext.setBackground(btnBg);
            btnNext.setForeground(btnFg);
            btnNext.setFocusPainted(false);
            btnNext.setBorder(BorderFactory.createLineBorder(neonColor, 2));
            btnNext.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnNext.setBounds(btnX, 100, btnWidth, btnHeight);
            btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnNext.setBackground(neonColor);
                    btnNext.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnNext.setBackground(btnBg);
                    btnNext.setForeground(btnFg);
                }
            });
            btnNext.addActionListener(e -> {
                dispose();
                gui.endGame();
                gui.launchGame(next);
            });
            panel.add(btnNext);
            
            JButton btnRetry = new JButton("Reiniciar");
            btnRetry.setFont(btnFont);
            btnRetry.setBackground(btnBg);
            btnRetry.setForeground(btnFg);
            btnRetry.setFocusPainted(false);
            btnRetry.setBorder(BorderFactory.createLineBorder(neonColor, 2));
            btnRetry.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRetry.setBounds(btnX, 160, btnWidth, btnHeight);
            btnRetry.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnRetry.setBackground(neonColor);
                    btnRetry.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnRetry.setBackground(btnBg);
                    btnRetry.setForeground(btnFg);
                }
            });
            btnRetry.addActionListener(e -> {
                dispose();
                gui.endGame();
                gui.reloadLastFile();
            });
            panel.add(btnRetry);
            
            JButton btnMenu = new JButton("Salir");
            btnMenu.setFont(btnFont);
            btnMenu.setBackground(btnBg);
            btnMenu.setForeground(btnFg);
            btnMenu.setFocusPainted(false);
            btnMenu.setBorder(BorderFactory.createLineBorder(neonColor, 2));
            btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMenu.setBounds(btnX, 220, btnWidth, btnHeight);
            btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnMenu.setBackground(neonColor);
                    btnMenu.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnMenu.setBackground(btnBg);
                    btnMenu.setForeground(btnFg);
                }
            });
            btnMenu.addActionListener(e -> {
                dispose();
                gui.endGame();
                gui.showPanel("LevelSelection");
            });
            panel.add(btnMenu);
        } else {
            titleLabel.setBounds(0, 40, 300, 40);
            panel.add(titleLabel);
            
            JButton btnRetry = new JButton("Reiniciar");
            btnRetry.setFont(btnFont);
            btnRetry.setBackground(btnBg);
            btnRetry.setForeground(btnFg);
            btnRetry.setFocusPainted(false);
            btnRetry.setBorder(BorderFactory.createLineBorder(neonColor, 2));
            btnRetry.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnRetry.setBounds(btnX, 130, btnWidth, btnHeight);
            btnRetry.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnRetry.setBackground(neonColor);
                    btnRetry.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnRetry.setBackground(btnBg);
                    btnRetry.setForeground(btnFg);
                }
            });
            btnRetry.addActionListener(e -> {
                dispose();
                gui.endGame();
                gui.reloadLastFile();
            });
            panel.add(btnRetry);
            
            JButton btnMenu = new JButton("Salir");
            btnMenu.setFont(btnFont);
            btnMenu.setBackground(btnBg);
            btnMenu.setForeground(btnFg);
            btnMenu.setFocusPainted(false);
            btnMenu.setBorder(BorderFactory.createLineBorder(neonColor, 2));
            btnMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMenu.setBounds(btnX, 190, btnWidth, btnHeight);
            btnMenu.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnMenu.setBackground(neonColor);
                    btnMenu.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnMenu.setBackground(btnBg);
                    btnMenu.setForeground(btnFg);
                }
            });
            btnMenu.addActionListener(e -> {
                dispose();
                gui.endGame();
                gui.showPanel("LevelSelection");
            });
            panel.add(btnMenu);
        }
        
        add(panel);
    }
}
