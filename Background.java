import java.awt.Graphics;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Scrolling background that can scroll horizontally infinitely, run in the Game class
 * 
 * @see Game
 * @author Amanda Li
 * @version May 2022
 */
public class Background{
    private int x1, x2;
    private int y1, y2;
    private int width;
    private int step;
    private BufferedImage bckgPic1;
    private BufferedImage bckgPic2;
//------------------------------------------------------------------------------    
    Background(String picName1, String picName2, int step){
        this.x1 = 0;
        this.y1 = -220;        
        try {                
            this.bckgPic1 = ImageIO.read(new File(picName1));
            this.bckgPic2 = ImageIO.read(new File(picName2));
        } catch (IOException ex){}
        this.width = bckgPic1.getWidth();
        this.x2 = -this.width;
        this.y2 = -220;
        this.step = step;
    }
//------------------------------------------------------------------------------    
    public void draw(Graphics g){
        g.drawImage(this.bckgPic1, this.x1, this.y1, null);
        g.drawImage(this.bckgPic2, this.x2, this.y2, null);
    }
//------------------------------------------------------------------------------
    /**
     * Moves the x position of the image to the laft based on the step value
     * 
     */
    public void scrollLeft(){
        this.x1 -= this.step;
        this.x2 -= this.step;
        if (this.x1<=-this.width){
            this.x1 = 0;
            this.x2 = this.width;
        } else if (this.x2 <= -this.width){
            this.x2 = 0;
            this.x1 = this.width;
        }    
    }
    /**
     * Moves the y position to the right based on the step value.
     * 
     */
    public void scrollRight(){
        this.x1 += this.step;
        this.x2 += this.step;
        if (this.x1 >= this.width){
            this.x1 = -this.width;
            this.x2 = 0;
        } else if (this.x2 >= this.width){
            this.x2 = -this.width;
            this.x1 = 0;
        }    
    }
}