import java.awt.Graphics;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;

/**
 * The Ghost class represents a Ghost object accepted by the Map class.
 * 
 * @see Map
 * @author Amanda Li
 * @version May 2022
 */
public class Ghost extends GameObject{
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private int direction;
    long startTime, currentTime, elapsedTime;
    int blinkInterval;
    private boolean blink;
    private int Vy, speed;
    private BufferedImage[][] frames;
    private BufferedImage spriteSheet;
    private int row,col;
    private int xPosition, yPosition;
    private int startX;
    private Rectangle hitBox;
//------------------------------------------------------------------------------    
    Ghost(int rowNum, int colNum, String picName, int rows, int columns, int width, int height, int speed){
        super(colNum*Const.GRIDSIZE, rowNum*Const.GRIDSIZE, width, height);
        frames = new BufferedImage[rows][columns];
        try {
            spriteSheet =  ImageIO.read(new File(picName));
            for (int row=0; row<rows; row++){
                for (int col=0; col<columns; col++){
                    frames[row][col] = spriteSheet.getSubimage(width*col, height*row, width, height);
                }
            }
        } catch (IOException ex){System.out.println("error");}
        this.xPosition=colNum*Const.GRIDSIZE;
        this.yPosition=rowNum*Const.GRIDSIZE;
        this.startX=colNum*Const.GRIDSIZE;
        this.Vy = 0; 
        this.row = 0;
        this.col = 0;
        this.blink = false;
        this.blinkInterval = (int)(6*Math.random());
        this.speed=speed;
        this.direction=RIGHT;
        this.startTime=System.currentTimeMillis();
    }
//------------------------------------------------------------------------------  
    public int getStartX(){
        return this.startX;
    }
    public String getDirection(){
        String direction = "";
        if (this.direction == LEFT){
            direction = "left";
        }else if (this.direction == RIGHT){
            direction = "right";
        }
        return direction;
    }
    public long getStartTime(){
        return this.startTime;
    }
    public long getCurrentTime(){
        return this.currentTime;
    }
    public long getElapsedTime(){
        return this.elapsedTime;
    }
    public int getBlinkInterval(){
        return this.blinkInterval;
    }
    public void setStartTime(long time){
        this.startTime = time;
    }
    public void setCurrentTime(long time){
        this.currentTime = time;
    }
    public void setElapsedTime(long time){
        this.elapsedTime = time;
    }
    public void setBlinkInterval(int time){
        this.blinkInterval=time;
    }
    public void setDirection(String direction){
        if (direction == "left"){
            this.direction = LEFT;
        }else if (direction == "right"){
            this.direction = RIGHT;
        }
    }
    public void setBlink(boolean blink){
        this.blink=blink;
    }
//------------------------------------------------------------------------------
    public void draw(Graphics g){
        g.drawImage(this.frames[this.row][this.col], this.xPosition, this.yPosition, null); 
    }
    public void drawHitbox(Graphics g){
        g.setColor(Color.white);
        g.drawRect(this.xPosition+10, this.yPosition+10, this.getWidth()-20, this.getHeight()-20);
    }
//------------------------------------------------------------------------------
    /**
     * Runs the blink animation.
     * 
     */
    public void blink(){
        if(this.blink == true){
            if (this.col+1<frames[this.row].length){
                this.col = this.col + 1;
            }else{
                this.col = 0;
                this.blink = false;
            }
        }
    }
    /**
     * Moves the ghost left according to the movement of the game
     * 
     * @param game that the ghost is a part of
     */
    public void moveLeft(Game game){
        this.setX(this.getX()-this.speed);
        this.row=LEFT;
        this.xPosition = game.xPosition + this.getX();
        this.setBox(); 
    }
    /**
     * Moves the ghost right according to the movement of the game
     * 
     * @param game that the ghost is a part of
     */
    public void moveRight(Game game){
        this.setX(this.getX()+this.speed);
        this.row=RIGHT;
        this.xPosition = game.xPosition + this.getX();
        this.setBox(); 
    }
    @Override
    public Rectangle getBox(){
        return (new Rectangle(this.getX()+10, this.getY()+10, this.getWidth()-20, this.getHeight()-20));
    }
}