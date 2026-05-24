package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Pantalla para personalizar el borde del jugador sin importar ningún tipo de Dominio.
 * Utiliza clases AWT/Swing estándar para el manejo de colores.
 */
public final class ColorCustomizationPanel extends JPanel {
    private V2GUI gui;
    private Color currentColor = Color.BLACK;
    private boolean forP2 = false;

    private final Color[] presets = {
        Color.BLACK, Color.WHITE, Color.DARK_GRAY, Color.LIGHT_GRAY,
        new Color(128, 128, 128), new Color(165, 42, 42), 
        new Color(128, 0, 0), new Color(85, 107, 47),
        
        Color.RED, new Color(220, 20, 60), new Color(255, 69, 0),
        new Color(255, 165, 0), new Color(255, 127, 80), 
        new Color(255, 215, 0), Color.YELLOW, new Color(250, 128, 114),
        
        Color.BLUE, new Color(0, 0, 128), Color.GREEN, new Color(50, 205, 50),
        new Color(0, 255, 255), new Color(0, 128, 128), 
        new Color(135, 206, 235), new Color(64, 224, 208),
        
        new Color(128, 0, 128), new Color(138, 43, 226),
        new Color(75, 0, 130), new Color(238, 130, 238),
        new Color(255, 0, 255), new Color(255, 192, 203), 
        new Color(255, 105, 180), new Color(218, 112, 214)
    };

    public ColorCustomizationPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null);
        setOpaque(true);

        CustomButton backBtn = new CustomButton("<< BACK");
        backBtn.setBounds(30, 20, 120, 45);
        backBtn.addActionListener(e -> gui.showPanel("SkinSelection"));

        CustomButton nextBtn = new CustomButton("CONTINUE >>");
        nextBtn.setBounds(275, 450, 250, 60);
        nextBtn.addActionListener(e -> handleContinue());

        add(backBtn);
        add(nextBtn);

        // Generar Cuadrícula de Botones (8x4)
        int cols = 8;
        int cellSize = 42;
        int gap = 8;
        int startX = 400 - ((cols * (cellSize + gap)) / 2);
        int startY = 240;

        for (int i = 0; i < presets.length; i++) {
            final Color preset = presets[i];
            int row = i / cols;
            int col = i % cols;
            
            JButton btn = new JButton();
            btn.setBounds(startX + (col * (cellSize + gap)), startY + (row * (cellSize + gap)), cellSize, cellSize);
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                this.currentColor = preset;
                repaint();
            });
            add(btn);
        }
    }

    public void setForP2(boolean isP2) {
        this.forP2 = isP2;
        this.currentColor = Color.BLACK;
        repaint();
    }

    private void handleContinue() {
        if (!forP2) {
            gui.setSelectedBorderColorP1(currentColor);
            if (gui.isPvP()) {
                this.setForP2(true);
            } else {
                gui.showPanel("LevelSelection");
            }
        } else {
            gui.setSelectedBorderColorP2(currentColor);
            gui.showPanel("LevelSelection");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0, new Color(172, 251, 239), getWidth(), getHeight(), new Color(100, 200, 180));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
        String text = forP2 ? "PLAYER 2: BORDER" : "PLAYER 1: BORDER";
        g2d.drawString(text, 220, 80);

        int cubeSize = 80;
        int cubeX = 400 - (cubeSize / 2);
        int cubeY = 120;
        
        Color bodyColor = forP2 ? gui.getSelectedSkinColorP2() : gui.getSelectedSkinColorP1();

        g2d.setColor(bodyColor);
        g2d.fill(new RoundRectangle2D.Double(cubeX, cubeY, cubeSize, cubeSize, 10, 10));

        g2d.setColor(currentColor);
        g2d.setStroke(new BasicStroke(5));
        g2d.draw(new RoundRectangle2D.Double(cubeX, cubeY, cubeSize, cubeSize, 10, 10));

        int cols = 8;
        int cellSize = 42;
        int gap = 8;
        int startX = 400 - ((cols * (cellSize + gap)) / 2);
        int startY = 240;

        for (int i = 0; i < presets.length; i++) {
            Color preset = presets[i];
            int row = i / cols;
            int col = i % cols;
            int px = startX + (col * (cellSize + gap));
            int py = startY + (row * (cellSize + gap));

            g2d.setColor(preset);
            g2d.fillRoundRect(px, py, cellSize, cellSize, 8, 8);
            
            if (preset.equals(currentColor)) {
                g2d.setColor(Color.BLACK); 
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(px - 2, py - 2, cellSize + 4, cellSize + 4, 10, 10);
            } else {
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(px, py, cellSize, cellSize, 8, 8);
            }
        }
    }
}
