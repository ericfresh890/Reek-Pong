import javax.swing.*;

public class GameWindow extends JFrame
{
    public GameWindow(GamePanel gamePanel)
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(gamePanel);
        this.pack();
        this.setVisible(true);
    }
}
