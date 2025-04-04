import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JFrame {

    private static final int TILE_SIZE = 25;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ALL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);
    
    private final int[] x = new int[ALL_TILES];
    private final int[] y = new int[ALL_TILES];
    
    private int bodyParts = 3;
    private int applesEaten;
    private int appleX;
    private int appleY;
    
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        random = new Random();
        this.setTitle("Jogo da Cobrinha");
        this.setSize(WIDTH, HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(new GamePanel());
        this.pack();
        this.setVisible(true);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setLocationRelativeTo(null);
        
        startGame();
    }

    private void startGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.pack();
        this.setLocationRelativeTo(null);
        newApple();
        running = true;
        timer = new Timer(100, e -> {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        });
        timer.start();
    }

    private void newApple() {
        appleX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
        appleY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
    }

    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        
        switch (direction) {
            case 'U' -> y[0] -= TILE_SIZE;
            case 'D' -> y[0] += TILE_SIZE;
            case 'L' -> x[0] -= TILE_SIZE;
            case 'R' -> x[0] += TILE_SIZE;
        }
    }

    private void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    private void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }
        
        if (!running) {
            timer.stop();
        }
    }

    private class GamePanel extends JPanel implements KeyListener {
        
        public GamePanel() {
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            this.addKeyListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (running) {
                g.setColor(Color.RED);
                g.fillOval(appleX, appleY, TILE_SIZE, TILE_SIZE);
                
                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(new Color(45, 180, 0));
                    }
                    g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
                }
        
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Pontos: " + applesEaten, 10, 20);
            } else {
                gameOver(g);
            }
        }

        private void gameOver(Graphics g) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Pontos: " + applesEaten, (WIDTH - metrics.stringWidth("Pontos: " + applesEaten)) / 2, HEIGHT / 2 + 50);
            
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Pressione ESPAÇO para jogar novamente", (WIDTH - metrics.stringWidth("Pressione ESPAÇO para jogar novamente")) / 2, HEIGHT / 2 + 100);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        resetGame();
                    }
                    break;
            }
        }

        private void resetGame() {
            bodyParts = 3;
            applesEaten = 0;
            direction = 'R';
            running = true;
            
            for (int i = 0; i < bodyParts; i++) {
                x[i] = 0;
                y[i] = 0;
            }
            
            newApple();
            timer.start();
            repaint();
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}