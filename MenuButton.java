import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MenuButton extends Rectangle
{
    BufferedImage bgImg;
    BufferedImage[] buttons;
    int rowIndex, index = 0;

    public MenuButton(int x, int y, int width, int height, int rowIndex)
    {
        super(x, y, width, height);
        this.rowIndex = rowIndex;


        loadImage();
    }

    public void loadImage()
    {
        buttons = new BufferedImage[2];
        bgImg = null;

        BufferedImage temp = null;
        try {
            temp =ImageIO.read(getClass().getResourceAsStream("res/button_atlas.png"));
            bgImg =ImageIO.read(getClass().getResourceAsStream("res/bgImg.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for(int i = 0; i < buttons.length; i++)
            buttons[i] = temp.getSubimage(0, i * 56 * 2, 420, 56);
    }

    public void draw(Graphics g)
    {

        g.drawImage(buttons[rowIndex].getSubimage(index * 140, 0, 140, 56), x, y, width, height, null);
    }
}
