package presentation;

import javax.swing.*;
import java.awt.*;

public final class PlayPanel extends JPanel {
    private V2GUI gui;

    public PlayPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null);
        setBackground(new Color(109, 157, 216));

        CustomButton backBtn = new CustomButton("Back");
        backBtn.setBounds(30, 20, 120, 45);
        backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        backBtn.addActionListener(e -> gui.showPanel("MainMenu"));

        int btnWidth = 250;
        int btnHeight = 55;
        int centerX = 400 - (btnWidth / 2);

        CustomButton singlePlayerBtn = new CustomButton("1 Player");
        singlePlayerBtn.setBounds(centerX, 200, btnWidth, btnHeight);
        singlePlayerBtn.addActionListener(e -> {
            gui.setPvP(false);
            gui.showPanel("SkinSelection");
        });

        CustomButton pvpBtn = new CustomButton("Player VS Player");
        pvpBtn.setBounds(centerX, 275, btnWidth, btnHeight);
        pvpBtn.addActionListener(e -> {
            gui.setPvP(true);
            gui.showPanel("SkinSelection");
        });

        CustomButton coopBtn = new CustomButton("Machine Vs Player");
        coopBtn.setBounds(centerX, 350, btnWidth, btnHeight);

        add(backBtn);
        add(singlePlayerBtn);
        add(pvpBtn);
        add(coopBtn);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 45));
        g2d.drawString("Select Game Mode", 200, 120);
    }
}
