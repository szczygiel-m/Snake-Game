import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    Timer timer;
    Random random = new Random();
    final static int SCREEN_HEIGHT = 1200;
    final static int SCREEN_WIDTH = 100;
    final static int UNIT_SIZE = 25;
    final static int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    final static int DELAY = 125;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    boolean isFirstGame = true;
    char direction = 'R';
    boolean running = false;
    BufferedImage backgroundImage;


    GamePanel () {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.ORANGE);
        this.setFocusable(true);
        this.setVisible(true);
        this.addKeyListener(new MyKeyAddapter());

        try {
            backgroundImage = ImageIO.read(new File("src\\bricks-ideal-as-a-background-image.jpg"));
        } catch (IOException exception) {
            System.out.println("Nie udalo sie wczytac obrazu backgroundImage");
        }
    }

    public void startGame() {
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH /UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT /UNIT_SIZE) * UNIT_SIZE;
    }

    public void checkCollisions() {
        //check if head hit body parts
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        if (x[0] < 0) running = false;
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) running = false;
        if (y[0] < 0) running = false;
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) running = false;

        if (!running) {
            timer.stop();
        }
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.black);
                    //g.drawRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.setColor(new Color(25, 132, 10));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    public void restart() {
        isFirstGame = false;
        running = true;
        applesEaten = 0;
        bodyParts = 5;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        direction = 'R';
        timer.start();
        getGraphics().dispose();
        repaint();
        draw(getGraphics());
    }

    public void gameOver(Graphics g) {

        if (isFirstGame) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            g.drawString("Welcome to 'Snake'", 100, 100);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            g.drawString("Game Over!", 100, 100);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            g.drawString("Your score: " + applesEaten, 100, 200);
        }
        g.drawString("Press 'enter' to play again", 100, 300);
        g.drawString("Press 'escape' to leave", 100, 400);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAddapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_ENTER:
                    if (!running) {
                        restart();
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (!running) System.exit(0);
            }
        }
    }

}
