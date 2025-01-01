import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

enum State
{
    MENU, PLAY, PAUSE, OPTION;
}

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
    private int player1, player2;
    Pad pad1, pad2;
    Ball ball;
    State state = State.MENU;
    MenuButton playButton, quitButton;
    PauseButton play, restart, home;

    private final int ballD, padW, padH;

    public GamePanel()
    {
        ballD = 28;
        padW = 20;
        padH = Game.HEIGHT/5;

        this.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);

        initButtons();
        newObjects();
    }

    public void initButtons()
    {
        playButton = new MenuButton( Game.WIDTH/2 - 280/2, 300, 280, 112, 0);
        quitButton = new MenuButton( Game.WIDTH/2 - 280/2, 450, 280, 112, 1);
        play = new PauseButton( Game.WIDTH/4 - 112/2, Game.HEIGHT/2 - 112/2, 112, 112, 0);
        restart = new PauseButton( Game.WIDTH/2 - 112/2, Game.HEIGHT/2 - 112/2, 112, 112, 1);
        home = new PauseButton( Game.WIDTH*3/4 - 112/2, Game.HEIGHT/2 - 112/2, 112, 112, 2);
    }

    public void newObjects()
    {
        pad1 = new Pad(0, Game.HEIGHT/2 -(padH/2), padW, padH, Color.red);
        pad2 = new Pad(Game.WIDTH - padW, Game.HEIGHT/2 -(padH/2), padW, padH, Color.blue);

        ball = new Ball((Game.WIDTH/2) - ballD/2, (Game.HEIGHT/2) - (ballD/2), ballD);
    }

    public void reset()
    {
        newObjects();
        player1 = 0;
        player2 = 0;
    }

    public void update()
    {
        if(state == State.PLAY)
        {
            pad1.move();
            pad2.move();
            ball.move();
            checkCollision();
        }
    }

    public void checkCollision()
    {
        if(ball.intersects(pad1))
        {
            ball.xVel *= -1;
            ball.xVel += ball.xSpeed;
        }
        if(ball.intersects(pad2))
        {
            ball.xVel *= -1;
            ball.xVel -= ball.xSpeed;
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(state == State.PLAY || state == State.PAUSE)
        {
            drawLayout(g);
            pad1.draw(g);
            pad2.draw(g);
            ball.draw(g);
            drawScore(g);
            if(state == State.PAUSE)
            {
                drawPauseOptions(g);
            }
        }
        if(state == State.MENU)
        {
            drawMenu(g);
        }
    }

    public void drawMenu(Graphics g)
    {
        g.drawImage(playButton.bgImg, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        g.setColor(new Color(0,0,0,100));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        g.setColor(Color.red);
        g.setFont(new Font("Verdana", Font.BOLD,14));
        g.drawString("Player 1 controls: 'W' and 'S'key                                    Player 2 controls: 'up' and 'down' arrow keys                                    Press 'Enter' to pause", 20, 600);

        g.setColor(Color.black);
        g.drawString("Eric...Fresh", 20, 630);
        playButton.draw(g);
        quitButton.draw(g);
    }

    public void drawPauseOptions(Graphics g)
    {
        play.draw(g);
        restart.draw(g);
        home.draw(g);
    }

    public void drawLayout(Graphics g)
    {
        Graphics2D gD = (Graphics2D)g;
        gD.setColor(new Color(51, 95, 81));  g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        gD.setColor(Color.white);
        gD.drawLine(0, Game.HEIGHT/2, Game.WIDTH, Game.HEIGHT/2);

        gD.setColor(Color.black);
        gD.setStroke(new BasicStroke(3));
        gD.drawLine(Game.WIDTH/2, 0, Game.WIDTH/2, Game.HEIGHT);

        gD.setStroke(new BasicStroke(1)); //reset
    }

    public void drawScore(Graphics g)
    {
        g.setColor(Color.black);
        g.setFont(new Font("Vermont", Font.ITALIC, 50));
        FontMetrics metrics = g.getFontMetrics();

        g.drawString(player1 + " : "+ player2, Game.WIDTH/2 - metrics.stringWidth(player1 + " : "+ player2)/2, 75);
    }

    public class Pad extends Rectangle
    {
        protected double ySpeed = 4;
        protected double yVel = 0;
        Color color;
        Pad(int x, int y, int width, int height, Color color)
        {
            super(x, y, width, height);
            this.color = color;
        }

        public void draw(Graphics g)
        {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }

        public void move()
        {
            y += yVel;
            if(y< 0) y = 0;
            if(y> Game.HEIGHT - height) y = Game.HEIGHT - height;
        }
    }

    public class Ball extends Rectangle2D.Double
    {
        double xSpeed = 0.3;
        double xVel = 1, yVel = 1;

        public Ball(double x, double y, double diameter)
        {
            super(x, y, diameter, diameter);

            newDirection();
        }

        public void newDirection()
        {
            if(new Random().nextInt(2) == 1) xVel *= -1;
            if(new Random().nextInt(2) == 1) yVel *= -1;}

        public void draw(Graphics g)
        {
            g.setColor(Color.white);
            g.fillOval((int)x, (int)y, (int)width, (int)height);
            g.setColor(Color.black);
            g.drawOval((int)x, (int)y, (int)width, (int)height);
        }

        public void move()
        {
            x += xVel;
            y += yVel;
            if(x < 0|| x > Game.WIDTH - ballD)
            {
                if(x > Game.WIDTH - ballD) player1++;
                if(x < 0) player2++;

                newObjects();
            }
            if(y < 0|| y > Game.HEIGHT - ballD) yVel*= -1;
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_W:
                        pad1.yVel = -pad1.ySpeed;
                break;
            case KeyEvent.VK_S:
                        pad1.yVel = pad1.ySpeed;
                break;
        }
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                        pad2.yVel = -pad2.ySpeed;
                break;
            case KeyEvent.VK_DOWN:
                        pad2.yVel = pad2.ySpeed;
                break;
            case KeyEvent.VK_ENTER:
                    if(state == State.PLAY)
                        state = State.PAUSE;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                pad1.yVel = 0;
                break;
        }
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                pad2.yVel = 0;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(state == State.MENU)
            if(playButton.contains(e.getPoint()))
                playButton.index = 2;
            if(quitButton.contains(e.getPoint()))
                quitButton.index = 2;

        if(state == State.PAUSE)
            if(play.contains(e.getPoint()))
                play.index = 2;
            if(restart.contains(e.getPoint()))
                restart.index = 2;
            if(home.contains(e.getPoint()))
                home.index = 2;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(state == State.MENU)
        {
            if(playButton.contains(e.getPoint()))
                state = State.PLAY;
            if(quitButton.contains(e.getPoint()))
                System.exit(0);

            playButton.index = 0;
            quitButton.index = 0;

        }
        if(state == State.PAUSE)
        {
            if(play.contains(e.getPoint()))
                state = State.PLAY;
            if(restart.contains(e.getPoint()))
            {
                reset();
                state = State.PLAY;
            }
            if(home.contains(e.getPoint()))
            {
                reset();
                state = State.MENU;
            }

            play.index = 0;
            restart.index = 0;
            home.index = 0;

        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if(state == State.MENU)
        {
            playButton.index = 0;
            quitButton.index = 0;

            if(playButton.contains(e.getPoint()))
                playButton.index = 1;
            if(quitButton.contains(e.getPoint()))
                quitButton.index = 1;
        }
        if(state == State.PAUSE)
        {
            play.index = 0;
            restart.index = 0;
            home.index = 0;

            if(play.contains(e.getPoint()))
                play.index = 1;
            if(restart.contains(e.getPoint()))
                restart.index = 1;
            if(home.contains(e.getPoint()))
                home.index = 1;
        }
    }
}