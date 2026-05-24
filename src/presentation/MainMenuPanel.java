package presentation;

import javax.swing.*;
import java.awt.*;

public final class MainMenuPanel extends JPanel {
    private V2GUI gui;

    public MainMenuPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null); 
        setBackground(new Color(173, 216, 240));

        int startY = 180;
        int btnWidth = 150;
        int btnHeight = 45;
        int gap = 15;

        CustomButton playBtn = new CustomButton("Play");
        playBtn.setBounds(120, startY, btnWidth, btnHeight);
        playBtn.addActionListener(e -> gui.showPanel("Play"));

        CustomButton loadBtn = new CustomButton("Load");
        loadBtn.setBounds(120, startY + (btnHeight + gap), btnWidth, btnHeight);
        loadBtn.addActionListener(e -> gui.openGame());

        CustomButton settingsBtn = new CustomButton("Settings");
        settingsBtn.setBounds(120, startY + 2 * (btnHeight + gap), btnWidth, btnHeight);
        settingsBtn.addActionListener(e -> gui.showPanel("Settings"));

        CustomButton quitBtn = new CustomButton("Quit");
        quitBtn.setBounds(120, startY + 3 * (btnHeight + gap), btnWidth, btnHeight);
        quitBtn.addActionListener(e -> System.exit(0));

        add(playBtn);
        add(loadBtn);
        add(settingsBtn);
        add(quitBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
        g2d.drawString("The Worlds...", 350, 160);

        g2d.setColor(new Color(65, 105, 225)); 
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
        g2d.drawString("HARDEST GAME", 330, 230);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        g2d.drawString("by", 580, 280);
        g2d.drawString("Martinez", 560, 315);
        g2d.drawString("Sanchez", 560, 350);
    }
}
