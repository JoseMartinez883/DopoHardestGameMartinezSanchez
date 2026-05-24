package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setFont(new Font("Comic Sans MS", Font.BOLD, 22)); 
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(230, 230, 230));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(Color.LIGHT_GRAY);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(new Color(230, 230, 230));
            }
        });
    }
}
