package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * GameOverDialog muestra el diálogo tras fallar o perder en un nivel,
 * ofreciendo opciones para reiniciar o volver al menú.
 */
public final class GameOverDialog extends JDialog {
    private final V2GUI gui;

    public GameOverDialog(V2GUI gui) {
        super(gui, "Game Over", true);
        this.gui = gui;
        
        gui.getGameLoopController().stop();
        
        setSize(300, 360);
        setLocationRelativeTo(gui);
        setUndecorated(true);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 102, 102), 3));
        panel.setLayout(null);
        
        JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 102, 102));
        titleLabel.setBounds(0, 40, 300, 40);
        panel.add(titleLabel);
        
        Font btnFont = new Font("Comic Sans MS", Font.BOLD, 16);
        Color btnBg = new Color(30, 30, 30);
        Color btnFg = Color.WHITE;
        Color neonColor = new Color(255, 102, 102);
        
        int btnWidth = 240;
        int btnHeight = 45;
        int btnX = (300 - btnWidth) / 2;
        
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
        
        add(panel);
    }
}
