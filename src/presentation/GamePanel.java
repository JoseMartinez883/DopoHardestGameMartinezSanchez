package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GamePanel es una vista gráfica pura, 100% libre de imports y tipos del Dominio.
 * Delega el renderizado de los elementos del juego y el movimiento a la Fachada de Presentación (V2GUI).
 */
public final class GamePanel extends JPanel {
    private V2GUI gui;
    private boolean up, down, left, right;
    private boolean up2, down2, left2, right2;
    private boolean isPaused = false;
    private JPanel pauseMenuPanel;
    private JButton hudPauseBtn;
    
    private ImageIcon pauseIcon;
    private ImageIcon continueIcon;

    public GamePanel(V2GUI gui) {
        this.gui = gui;
        setLayout(null); 
        setFocusable(true);
        setBackground(Color.LIGHT_GRAY);

        try {
            java.io.File filePause = new java.io.File("resources/pause.png");
            if (filePause.exists()) {
                Image img = javax.imageio.ImageIO.read(filePause);
                Image scaled = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                pauseIcon = new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar resources/pause.png");
        }

        try {
            java.io.File fileCont = new java.io.File("resources/continue.png");
            if (fileCont.exists()) {
                Image img = javax.imageio.ImageIO.read(fileCont);
                Image scaled = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                continueIcon = new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar resources/continue.png");
        }

        hudPauseBtn = new JButton();
        hudPauseBtn.setBounds(750, 5, 32, 32);
        
        if (pauseIcon != null) {
            hudPauseBtn.setIcon(pauseIcon);
            hudPauseBtn.setBorderPainted(false);
            hudPauseBtn.setContentAreaFilled(false);
            hudPauseBtn.setFocusPainted(false);
        } else {
            hudPauseBtn.setText("||");
            hudPauseBtn.setFont(new Font("Arial", Font.BOLD, 12));
            hudPauseBtn.setBackground(new Color(30, 30, 30));
            hudPauseBtn.setForeground(Color.WHITE);
            hudPauseBtn.setBorder(BorderFactory.createLineBorder(new Color(153, 255, 153), 1));
        }
        hudPauseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hudPauseBtn.addActionListener(e -> togglePause());
        add(hudPauseBtn);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pause");
        getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isPaused) return; 
                
                if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
                    gui.saveGame();
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    gui.reloadLastFile();
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: up = true; break;
                    case KeyEvent.VK_S: down = true; break;
                    case KeyEvent.VK_A: left = true; break;
                    case KeyEvent.VK_D: right = true; break;
                    case KeyEvent.VK_UP: up2 = true; break;
                    case KeyEvent.VK_DOWN: down2 = true; break;
                    case KeyEvent.VK_LEFT: left2 = true; break;
                    case KeyEvent.VK_RIGHT: right2 = true; break;
                }
                updateDirection();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isPaused) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: up = false; break;
                    case KeyEvent.VK_S: down = false; break;
                    case KeyEvent.VK_A: left = false; break;
                    case KeyEvent.VK_D: right = false; break;
                    case KeyEvent.VK_UP: up2 = false; break;
                    case KeyEvent.VK_DOWN: down2 = false; break;
                    case KeyEvent.VK_LEFT: left2 = false; break;
                    case KeyEvent.VK_RIGHT: right2 = false; break;
                }
                updateDirection();
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                up = down = left = right = false;
                up2 = down2 = left2 = right2 = false;
                updateDirection();
            }
        });
    }

    public void togglePause() {
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    private void pauseGame() {
        isPaused = true;
        if (continueIcon != null) {
            hudPauseBtn.setIcon(continueIcon);
            hudPauseBtn.setBorderPainted(false);
            hudPauseBtn.setContentAreaFilled(false);
        } else {
            hudPauseBtn.setText(">");
            hudPauseBtn.setIcon(null);
        }
        
        gui.getGameLoopController().stop();
        up = down = left = right = false;
        up2 = down2 = left2 = right2 = false;
        updateDirection();
        showPauseMenu();
        repaint();
    }

    private void resumeGame() {
        isPaused = false;
        hidePauseMenu();
        if (pauseIcon != null) {
            hudPauseBtn.setIcon(pauseIcon);
            hudPauseBtn.setBorderPainted(false);
            hudPauseBtn.setContentAreaFilled(false);
        } else {
            hudPauseBtn.setText("||");
            hudPauseBtn.setIcon(null);
        }
        
        gui.getGameLoopController().start();
        repaint();
    }

    private void showPauseMenu() {
        if (pauseMenuPanel == null) {
            pauseMenuPanel = new JPanel();
            pauseMenuPanel.setLayout(null);
            pauseMenuPanel.setBackground(new Color(30, 30, 30, 240));
            pauseMenuPanel.setBorder(BorderFactory.createLineBorder(new Color(153, 255, 153), 3)); 
            
            int pWidth = 300;
            int pHeight = 360;
            pauseMenuPanel.setBounds((800 - pWidth) / 2, (600 - pHeight) / 2, pWidth, pHeight);

            JLabel titleLabel = new JLabel("JUEGO EN PAUSA", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
            titleLabel.setForeground(new Color(153, 255, 153)); 
            titleLabel.setBounds(0, 20, pWidth, 40);
            pauseMenuPanel.add(titleLabel);

            int startY = 80;
            int btnWidth = 240;
            int btnHeight = 45;
            int gap = 15;
            int btnX = (pWidth - btnWidth) / 2;

            JButton resumeBtn = crearBotonDePausa("Continuar", null, btnX, startY, btnWidth, btnHeight);
            resumeBtn.addActionListener(e -> resumeGame());
            pauseMenuPanel.add(resumeBtn);

            JButton restartBtn = crearBotonDePausa("Reiniciar", "restart_btn.png", btnX, startY + (btnHeight + gap), btnWidth, btnHeight);
            restartBtn.addActionListener(e -> {
                resumeGame();
                gui.reloadLastFile();
            });
            pauseMenuPanel.add(restartBtn);

            JButton saveBtn = crearBotonDePausa("Guardar Partida", "save_btn.png", btnX, startY + 2 * (btnHeight + gap), btnWidth, btnHeight);
            saveBtn.addActionListener(e -> gui.saveGame());
            pauseMenuPanel.add(saveBtn);

            JButton exitBtn = crearBotonDePausa("Salir de la Partida", "exit_btn.png", btnX, startY + 3 * (btnHeight + gap), btnWidth, btnHeight);
            exitBtn.addActionListener(e -> {
                gui.getGameLoopController().stop();
                isPaused = false;
                hidePauseMenu();
                gui.endGame();
                gui.showPanel("LevelSelection");
            });
            pauseMenuPanel.add(exitBtn);
        }
        add(pauseMenuPanel);
        revalidate();
        repaint();
    }

    private void hidePauseMenu() {
        if (pauseMenuPanel != null) {
            remove(pauseMenuPanel);
        }
        revalidate();
        repaint();
    }

    private JButton crearBotonDePausa(String text, String imageName, int x, int y, int w, int h) {
        JButton btn = new JButton();
        btn.setBounds(x, y, w, h);
        
        ImageIcon icon = null;
        if (imageName != null) {
            try {
                java.io.File file = new java.io.File("resources/" + imageName);
                if (file.exists()) {
                    Image img = javax.imageio.ImageIO.read(file);
                    Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaled);
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar la imagen " + imageName + ", usando respaldo.");
            }
        }

        if (icon != null) {
            btn.setIcon(icon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
        } else {
            btn.setText(text);
            btn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(30, 30, 30));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(153, 255, 153), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(153, 255, 153));
                    btn.setForeground(Color.BLACK);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(30, 30, 30));
                    btn.setForeground(Color.WHITE);
                }
            });
        }
        return btn;
    }

    private void updateDirection() {
        int dx1 = 0, dy1 = 0;
        if (up) dy1--;
        if (down) dy1++;
        if (left) dx1--;
        if (right) dx1++;
        gui.movePlayer(0, dx1, dy1);

        int dx2 = 0, dy2 = 0;
        if (up2) dy2--;
        if (down2) dy2++;
        if (left2) dx2--;
        if (right2) dx2++;
        gui.movePlayer(1, dx2, dy2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gui.isGamePlaying()) return;
        
        Graphics2D g2d = (Graphics2D) g;
        gui.drawGame(g2d, getWidth(), getHeight(), isPaused);
    }
}
