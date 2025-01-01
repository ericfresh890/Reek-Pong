import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PauseButton extends Rectangle
{
    BufferedImage[] buttons;
    int rowIndex, index = 0;

    public PauseButton(int x, int y, int width, int height, int rowIndex)
    {
        super(x, y, width, height);
        this.rowIndex = rowIndex;


        loadImage();
    }

    public void loadImage()
    {
        buttons = new BufferedImage[3];

        BufferedImage temp = null;
        try {
            temp =ImageIO.read(getClass().getResourceAsStream("res/play.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for(int i = 0; i < buttons.length; i++)
            buttons[i] = temp.getSubimage(0, i * 56, 168, 56);
    }

    public void draw(Graphics g)
    {

        g.drawImage(buttons[rowIndex].getSubimage(index * 56, 0, 56, 56), x, y, width, height, null);
    }
}
