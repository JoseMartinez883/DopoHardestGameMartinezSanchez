package presentation;

import java.awt.*;
import javax.swing.*;

public final class LevelSelectionPanel extends JPanel {
    private V2GUI gui;

    public LevelSelectionPanel(V2GUI gui) {
        this.gui = gui;
        setLayout(new BorderLayout());
        setBackground(new Color(200, 200, 255)); 
        JPanel gridPanel = new JPanel(new GridLayout(3, 6, 15, 15));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        for (int i = 1; i <= 18; i++) {
            gridPanel.add(createLevelThumbnail(i));
        }

        JPanel bottomNav = new JPanel(new BorderLayout());
        bottomNav.setOpaque(false);
        bottomNav.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        CustomButton backBtn = new CustomButton("<< BACK");
        backBtn.setPreferredSize(new Dimension(150, 45));
        backBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        backBtn.addActionListener(e -> gui.showPanel("SkinSelection"));

        CustomButton menuBtn = new CustomButton("BACK TO MENU");
        menuBtn.setPreferredSize(new Dimension(180, 45));
        menuBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        menuBtn.addActionListener(e -> gui.showPanel("MainMenu"));

        CustomButton customLevelBtn = new CustomButton("LOAD LEVEL (.txt)");
        customLevelBtn.setPreferredSize(new Dimension(220, 45));
        customLevelBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        customLevelBtn.addActionListener(e -> gui.openCustomLevel());

        CustomButton nextBtn = new CustomButton("NEXT >>");
        nextBtn.setPreferredSize(new Dimension(150, 45));
        nextBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

        bottomNav.add(backBtn, BorderLayout.WEST);
        
        JPanel centerNav = new JPanel();
        centerNav.setOpaque(false);
        centerNav.add(menuBtn);
        centerNav.add(customLevelBtn);
        bottomNav.add(centerNav, BorderLayout.CENTER);

        bottomNav.add(nextBtn, BorderLayout.EAST);

        add(gridPanel, BorderLayout.CENTER);
        add(bottomNav, BorderLayout.SOUTH);
    }

    private JPanel createLevelThumbnail(int levelNum) {
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setOpaque(false);

        JLabel label = new JLabel("LEVEL " + levelNum, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        container.add(label, BorderLayout.NORTH);

        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(new Color(170, 170, 255));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        imagePlaceholder.setLayout(new BorderLayout());
        
        JButton thumbBtn = new JButton();
        thumbBtn.setOpaque(false);
        thumbBtn.setContentAreaFilled(false);
        thumbBtn.setBorderPainted(false);
        thumbBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        thumbBtn.addActionListener(e -> {
            gui.launchGame("level" + levelNum);
        });

        imagePlaceholder.add(thumbBtn, BorderLayout.CENTER);
        container.add(imagePlaceholder, BorderLayout.CENTER);
        
        return container;
    }
}
