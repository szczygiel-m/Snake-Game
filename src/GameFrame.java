import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame() {
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        gamePanel.startGame();
        this.setVisible(true);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(100, 100);
        this.setResizable(false);
        this.pack();
    }
}
