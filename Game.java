public class Game implements Runnable
{
    protected static  int WIDTH = 1170;
    protected static int HEIGHT = WIDTH * 5 / 9;
    private Thread gameThread;
    private final int FPS = 400;
    private final int UPS = 200;
    GamePanel gamePanel;

    Game()
    {
        gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        startLoop();
    }

    private void startLoop()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    {
        double TPF = 1000000000/FPS;
        double TPU = 1000000000/UPS;

        long now = System.nanoTime();

        double deltaF = 0;
        double deltaU = 0;

        while(true)
        {
            long current = System.nanoTime();

            deltaF += (current - now)/TPF;
            deltaU += (current - now)/TPU;
            now = current;

            if(deltaF >= 1)
            {
                gamePanel.repaint();
                deltaF--;
            }
            if(deltaU >= 1)
            {
                gamePanel.update();
                deltaU--;
            }
        }
    }
}