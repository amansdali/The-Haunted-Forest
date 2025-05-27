import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * A button that can display an image, displays a different image depending on the status of the button, run in the 
 * Game class
 * 
 * @see Game
 * @author Amanda Li
 * @version May 2022
 */
public class Button{
    private boolean mouseIsOver;
    private int imageNum;
    private int x,y;
    private int width;
    private int height;
    private BufferedImage image1;
    private BufferedImage image2;
//------------------------------------------------------------------------------    
    Button(String picName1, String picName2, int x, int y){
        this.x = x;
        this.y = y;        
        try {                
            this.image1 = ImageIO.read(new File(picName1));
            this.image2 = ImageIO.read(new File(picName2));
        } catch (IOException ex){}
        this.width = image1.getWidth();
        this.height = image1.getHeight();
        this.mouseIsOver = false;
        this.imageNum = 1;
    }
//------------------------------------------------------------------------------    
    public void draw(Graphics g){
        if (this.imageNum==1){
            g.drawImage(this.getImage1(), this.getX(), this.getY(), null);
        }else{
            g.drawImage(this.getImage2(), this.getX(), this.getY(), null);
        }
    }
//------------------------------------------------------------------------------
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public BufferedImage getImage1(){
        return this.image1;
    }
    public BufferedImage getImage2(){
        return this.image2;
    }
    public void setMouseIsOver(boolean isOver){
        this.mouseIsOver = isOver;
    }
    public boolean getMouseIsOver(){
        return this.mouseIsOver;
    }
    public void setImageNum(int num){
        this.imageNum = num;
    }
    public int getImageNum(){
        return this.imageNum;
    }
//------------------------------------------------------------------------------
    /**
     * Takes the x and y values of the mouse pointer and returns a boolean representing whether or not the mouse is 
     * over this object.
     * 
     * @param x coordinate of the mouse
     * @param y coordinate of the mouse
     * @return true if the coordinates are within those of this object or false if not
     */
    public boolean mouseIsOver(int x, int y){
        boolean isOver = false;
        if (x>this.getX()&& x<this.getX()+this.getWidth()){
            if (y>this.getY()&&y<this.getY()+this.getHeight()){
                isOver = true;
            }
        }
        return isOver;
    }
    
}