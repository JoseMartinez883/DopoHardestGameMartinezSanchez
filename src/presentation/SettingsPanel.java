package presentation;

import javax.swing.*;
import java.awt.*;

public final class SettingsPanel extends JPanel {
    private V2GUI gui;

    public SettingsPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null);
        setBackground(new Color(255, 239, 213));

        CustomButton backBtn = new CustomButton("Back");
        backBtn.setBounds(30, 20, 120, 45);
        backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        backBtn.addActionListener(e -> gui.showPanel("MainMenu"));

        CustomButton graphicsBtn = new CustomButton("Graphics");
        graphicsBtn.setBounds(275, 200, 250, 50);

        CustomButton soundBtn = new CustomButton("Sound");
        soundBtn.setBounds(275, 270, 250, 50);

        CustomButton controlsBtn = new CustomButton("Controls");
        controlsBtn.setBounds(275, 340, 250, 50);

        add(backBtn);
        add(graphicsBtn);
        add(soundBtn);
        add(controlsBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
        g2d.drawString("Settings & Tweaks", 195, 120);
    }
}
