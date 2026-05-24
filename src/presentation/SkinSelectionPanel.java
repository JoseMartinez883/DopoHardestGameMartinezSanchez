package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de selección de apariencias (Skins) de manera 100% desacoplada del Dominio.
 * Utiliza índices numéricos y representación local AWT/Swing pura.
 */
public final class SkinSelectionPanel extends JPanel {
    private V2GUI gui;
    private int selectedSkinIndex = 0;
    private boolean selectingForP2 = false;
    private final String[] skinNames = {"Red", "Blue", "Green"};
    private final Color[] skinColors = {
        new Color(255, 0, 0),     // Red Skin color (Pure Red)
        new Color(0, 0, 255),     // Blue Skin color (Pure Blue)
        new Color(0, 255, 0)      // Green Skin color (Pure Green)
    };

    public SkinSelectionPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null);
        setBackground(new Color(172, 251, 239)); 

        CustomButton backBtn = new CustomButton("Back");
        backBtn.setBounds(30, 20, 120, 45);
        backBtn.addActionListener(e -> {
            if (selectingForP2) {
                selectingForP2 = false;
                selectedSkinIndex = 0;
                repaint();
            } else {
                gui.showPanel("Play");
            }
        });

        CustomButton startBtn = new CustomButton("CONTINUE");
        startBtn.setBounds(275, 450, 250, 60);
        startBtn.addActionListener(e -> {
            if (!selectingForP2) {
                gui.setSelectedSkinIndex(selectedSkinIndex);
                if (gui.isPvP()) {
                    selectingForP2 = true;
                    selectedSkinIndex = 1; 
                    repaint();
                } else {
                    ColorCustomizationPanel cp = gui.getColorPanel();
                    cp.setForP2(false);
                    gui.showPanel("ColorSelection");
                }
            } else {
                gui.setSelectedSkinIndexP2(selectedSkinIndex);
                selectingForP2 = false;
                ColorCustomizationPanel cp = gui.getColorPanel();
                cp.setForP2(false); 
                gui.showPanel("ColorSelection");
            }
        });

        int xPos = 200;
        for (int i = 0; i < skinNames.length; i++) {
            final int index = i;
            JButton hitbox = new JButton();
            hitbox.setBounds(xPos, 250, 80, 80);
            hitbox.setOpaque(false);
            hitbox.setContentAreaFilled(false);
            hitbox.setBorderPainted(false);
            hitbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            hitbox.addActionListener(e -> { 
                selectedSkinIndex = index; 
                repaint(); 
            });
            add(hitbox);
            xPos += 160;
        }

        add(backBtn);
        add(startBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
        String title = selectingForP2 ? "Player 2: Choose Skin" : "Choose your Skin";
        g2d.drawString(title, 180, 120);

        int xPos = 200;
        for (int i = 0; i < skinNames.length; i++) {
            Color skinColor = skinColors[i];
            
            if (i == selectedSkinIndex) {
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(xPos - 10, 240, 100, 100);

                drawCubeWithCustomBorder(g2d, skinColor, Color.BLACK, xPos, 250);
            } else {
                drawCube(g2d, skinColor, xPos, 250);
            }
            xPos += 160;
        }
    }

    private void drawCube(Graphics2D g2d, Color color, int x, int y) {
        drawCubeWithCustomBorder(g2d, color, Color.BLACK, x, y);
    }

    private void drawCubeWithCustomBorder(Graphics2D g2d, Color bodyColor, Color borderColor, int x, int y) {
        g2d.setColor(bodyColor);
        g2d.fillRect(x, y, 80, 80);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(x, y, 80, 80);
    }
}
