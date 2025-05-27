import java.awt.Graphics;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;

/**
 * The Point class represents a game object accepted by the Map class,
 * 
 * @see Map
 * @author Amanda Li
 * @version May 2022
 */
public class Point extends GameObject{
    private int xPosition, yPosition;
    private BufferedImage picture;    
//------------------------------------------------------------------------------    
    Point(int row, int col, String picName){
        super((col * Const.GRIDSIZE), (row * Const.GRIDSIZE), 32, 32);      
        try {                
            picture = ImageIO.read(new File(picName));
        } catch (IOException ex){}
        this.xPosition = col * Const.GRIDSIZE;
        this.yPosition = row * Const.GRIDSIZE;
    }
//------------------------------------------------------------------------------    
    public void setXPosition(int gamePosition){
        this.xPosition = gamePosition + this.getX();
    }
//------------------------------------------------------------------------------    
    public void draw(Graphics g){
        g.drawImage(this.picture, this.xPosition, this.yPosition, null);  
    }
    public void drawHitbox(Graphics g){
        g.setColor(Color.white);
        g.drawRect(this.xPosition, this.yPosition, this.getWidth(), this.getHeight());
    }
}