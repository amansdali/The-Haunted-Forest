import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * The GameObject class represents a GameObject accepted by the Map class
 * 
 * @see Map
 * @author ICS3U6
 * @author Amanda Li
 * @version May 2022
 */
abstract class GameObject{
    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle box;
//------------------------------------------------------------------------------    
    GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.box = new Rectangle(x, y, width, height);
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
    public Rectangle getBox(){
        return this.box;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setWidth(int width){
        this.width = width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    public void setBox(){
        this.box.setLocation(this.x, this.y); 
    }
//------------------------------------------------------------------------------    
    abstract void draw(Graphics g);
    abstract void drawHitbox(Graphics g);
}