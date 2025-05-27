import java.awt.Graphics;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.Color;

/**
 * The Girl class represents a game object accepted by the Game class,
 * playable character that the user can control
 * 
 * @see Game
 * @author Amanda Li
 * @version May 2022
 */
public class Girl extends GameObject{
    private int xPosition, yPosition;
    private int health, score;
    private int direction;
    private final int RIGHT = 0;
    private final int LEFT = 1;
    private final int IDLE_RIGHT = 2;
    private final int IDLE_LEFT = 3;
    private final int JUMP_RIGHT = 4;
    private final int JUMP_LEFT = 5;
    private final int FALL_RIGHT = 6;
    private final int FALL_LEFT = 7;
    private final int FALLEN_RIGHT = 8;
    private final int FALLEN_LEFT = 9;
    private int Vy, Vx;
    private BufferedImage[][] frames;
    private BufferedImage[] progressBarFrames;
    private int progressBarFrame;
    private BufferedImage spriteSheet;
    private int row,col;
    private Rectangle[] hitBoxes;
    private boolean falling, fallen;
    private int bottomLevel;
//------------------------------------------------------------------------------    
    Girl(int x, int y, int rows, int columns, int width, int height){
        super(x, y, width, height);
        frames = new BufferedImage[rows][columns];
        try {
            row = RIGHT;
            spriteSheet =  ImageIO.read(new File("sprites/girl1.png"));
            for (int col=0; col<columns; col++){
                frames[row][col] = spriteSheet.getSubimage(width*col, 0, width, height);
            }
            row = LEFT;
            spriteSheet =  ImageIO.read(new File("sprites/girl2.png"));
            for (int col=0; col<columns; col++){
                frames[row][col] = spriteSheet.getSubimage(width*col, 0, width, height);
            }
            row = IDLE_RIGHT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/girl4.png"));
            }
            row = IDLE_LEFT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/girl6.png"));
            }
            row = JUMP_RIGHT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/girl3.png"));
            }
            row = JUMP_LEFT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/girl5.png"));
            }
            row = FALL_RIGHT;
            spriteSheet =  ImageIO.read(new File("sprites/girlFall.png"));
            for (int col=0; col<columns; col++){
                frames[row][col] = spriteSheet.getSubimage(width*col + 23*col, 0, width+10, height);
            }
            row = FALL_LEFT;
            spriteSheet =  ImageIO.read(new File("sprites/girlFall2.png"));
            for (int col=0; col<columns; col++){
                frames[row][col] = spriteSheet.getSubimage(width*col+23*col, 0, width+18, height);
            }
            row = FALLEN_RIGHT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/fallenRight.png"));
            }
            row = FALLEN_LEFT;
            for (int col=0; col<columns; col++){
                frames[row][col] = ImageIO.read(new File("sprites/fallenLeft.png"));
            }
            
            progressBarFrames = new BufferedImage[5];
            for(int i=88;i<88+progressBarFrames.length;i++){
                progressBarFrames[i-88]=ImageIO.read(new File("gui/UI-"+i+".png"));
            }
        } catch (IOException ex){System.out.println("error");}
        this.Vy = 0;
        this.Vx = 0;
        this.xPosition=x;
        this.yPosition=y;
        this.row = IDLE_RIGHT;
        this.col = 0;
        this.direction = RIGHT;
        this.health = Const.MAX_HEALTH;
        this.score = 0;
        this.hitBoxes = new Rectangle[2];
        this.falling = false;
        this.fallen = false;
        this.bottomLevel=Const.GROUND;
    }
//------------------------------------------------------------------------------  
    public int getXPosition(){
        return this.xPosition;
    }
    public int getHealth(){
        return this.health;
    }
    public int getScore(){
        return this.score;
    }
    public void setHealth(int health){
        this.health = health;
    }
    public void setScore(int score){
        this.score = score;
    }
    public void setVx(int Vx){
        this.Vx = Vx;
    }
    public boolean isFalling(){
        return (this.fallen || this.falling);
    }
    public String getDirection(){
        String direction = "";
        if(this.direction == RIGHT){
            direction = "right";
        }else if(this.direction == LEFT){
            direction = "left";
        }
        return direction;
    }
//------------------------------------------------------------------------------
    public void jump(int Vy){
        this.Vy = Vy;
    }    
//------------------------------------------------------------------------------
    public void draw(Graphics g){
        g.drawImage(this.frames[this.row][this.col], this.xPosition, this.yPosition, null); 
        if (this.health>0){
            g.setColor(Const.PINK);
            g.fillRect(this.xPosition+5, this.yPosition-9, (this.getWidth()/Const.MAX_HEALTH)*this.health, 5);
        }
        if(this.score<Const.MAX_SCORE/5){progressBarFrame = 0;}
        else if(this.score<(Const.MAX_SCORE/5)*2){progressBarFrame = 1;}
        else if(this.score<(Const.MAX_SCORE/5)*3){progressBarFrame = 2;}
        else if(this.score<(Const.MAX_SCORE/5)*4){progressBarFrame = 3;}
        else {progressBarFrame = 4;}
        g.drawImage(this.progressBarFrames[progressBarFrame], 20, 30, null); 
    }
    public void drawHitbox(Graphics g){
        g.setColor(Color.white);
        if (this.direction==RIGHT){
            g.drawRect(this.xPosition+20, this.yPosition+10, this.getWidth()-30, this.getHeight()-40);
            g.drawRect(this.xPosition+30, this.yPosition+50, this.getWidth()-50, this.getHeight()-50);
        }else if (this.direction==LEFT){
            g.drawRect(this.xPosition+10, this.yPosition+10, this.getWidth()-30, this.getHeight()-40);
            g.drawRect(this.xPosition+20, this.yPosition+50, this.getWidth()-50, this.getHeight()-50);
        }
    }
//------------------------------------------------------------------------------
    /**
     * If health is above zero, decreases health
     * 
     */
    public void loseHP(){
        if (this.health>0){
            this.health = this.health - 1;
        }
    }
    public void accellerate(){
        this.Vy += Const.GRAVITY;
    }
    public void moveLeft(){
        this.direction = LEFT;
        this.Vx = 0-(Const.X_STEP+Const.GAME_LAYER);
        this.bottomLevel=Const.GROUND;
    }
    public void moveRight(){
        this.direction=RIGHT;
        this.Vx =(Const.X_STEP+Const.GAME_LAYER);
        this.bottomLevel=Const.GROUND;
        
    }
    /**
     * Moves the y position, based on the ground and platforms on the map. If the girl is on the ground or a 
     * platform, the girl does not go further down
     * 
     * @param map that the girl interacts with
     */
    public void moveY(Map map){
        this.setY(this.getY() + this.Vy);
        Platform platform;
        for (int row=0; row<map.getGameObjects().length; row++){
            for (int col=0; col<map.getGameObjects()[row].length; col++){
                if(map.getGameObjects()[row][col] instanceof Platform){
                    platform = (Platform)map.getGameObjects()[row][col];
                    if (!(platform==null)){
                        if(this.landsOn(platform)){
                            if (this.getY()+this.getHeight()>platform.getY()){
                                this.setY((platform.getY()) - this.getHeight());
                                this.Vy = 0;
                                this.bottomLevel = platform.getY();
                            }
                        }
                    }
                }
            }
        }
        if (this.getY() + this.getHeight() >= Const.GROUND){
            this.setY(Const.GROUND - this.getHeight());
            this.Vy = 0;
            this.bottomLevel = Const.GROUND;
        }
        this.setBox(); 
        this.yPosition=this.getY();
    }
    /**
     * Moves the x of the girl, and runs the walking animation
     * 
     */
    public void moveX(){
        this.setX(this.getX()+this.Vx);
        this.col = (this.col + 1)%frames[row].length;
        this.setBox(); 
    }
    /**
     * Moves the x of the girl back one step
     * 
     */
    public void moveXBack(){
        this.setX(this.getX()-this.Vx);
        this.setBox(); 
    }
    /**
     * Returns a boolean representing whether the girl is on the lowest level possible
     * 
     * @return true if the bottom of the girl is on the bottom level, and false if not
     */
    public boolean isOnLevel(){
        return (this.getY() + this.getHeight() == this.bottomLevel);
    }
    /**
     * Updates the animation of the girl, taking into consideration whether or not the girl is currently moving
     * 
     * @param moving a boolean value on whether the girl is currently moving
     */
    public void updateAnimation(boolean moving){
        if (this.falling){
            if (this.direction==RIGHT){
                this.row = FALL_RIGHT;
            }else if (this.direction==LEFT){
                this.row = FALL_LEFT;
            }
        }else if(this.fallen){
            if (this.direction==RIGHT){
                this.row = FALLEN_RIGHT;
            }else if (this.direction==LEFT){
                this.row = FALLEN_LEFT;
            }
        }else if(!this.isOnLevel()){
            if (this.direction==RIGHT){
                this.row = JUMP_RIGHT;
            }else if (this.direction==LEFT){
                this.row = JUMP_LEFT;
            }
        }else if (!moving){
            if (this.direction==RIGHT){
                this.row = IDLE_RIGHT;
            }else if (this.direction==LEFT){
                this.row = IDLE_LEFT;
            }
        }else{
            if (this.direction==RIGHT){
                this.row = RIGHT;
            }else if (this.direction==LEFT){
                this.row = LEFT;
            }
        }
    }
    /**
     * If the girl has not already fallen, sets the animation of the girl to the falling or fallen animation
     * 
     */
    public void fall(){
        if (!this.falling && !this.fallen){
            this.col = 0;
            this.falling = true;
        }else if (this.falling){
            if (this.col+1<frames[this.row].length){
                this.col=this.col + 1;
            }else if(this.col+1==frames[this.row].length){
                this.col = 0;
                this.falling = false;
                this.fallen = true;
                if (this.direction==RIGHT){
                    this.row = FALLEN_RIGHT;
                }else if (this.direction==LEFT){
                    this.row = FALLEN_LEFT;
                }
            }
        }else{
            if (this.col+1<frames[this.row].length){
                this.col=this.col + 1;
            }
        }
    }
    /**
     * Returns whether or not the girl has fallen
     * 
     * @return true if fallen, and false if not
     */
    public boolean isFallen(){
        return this.fallen && this.col+1==frames[this.row].length;
    }
    /**
     * Returns whether or not the girl has collided with another game object
     * 
     * @param other game object to check for collision
     * @return true if collision occurs, false if not
     */
    public boolean collides(GameObject other){
        if (this.direction==RIGHT){
            this.hitBoxes[0] = new Rectangle(this.getX()+20, this.getY()+10, this.getWidth()-30, this.getHeight()-40);
            this.hitBoxes[1] = new Rectangle(this.getX()+30, this.getY()+50, this.getWidth()-50, this.getHeight()-50);
        }else if (this.direction==LEFT){
            this.hitBoxes[0] = new Rectangle(this.getX()+10, this.getY()+10, this.getWidth()-30, this.getHeight()-40);
            this.hitBoxes[1] = new Rectangle(this.getX()+20, this.getY()+50, this.getWidth()-50, this.getHeight()-50);
        }
        return this.hitBoxes[0].intersects(other.getBox())||this.hitBoxes[1].intersects(other.getBox());
    }
    /**
     * Returns a boolean value on whether the girl is on a platform
     * 
     * @param other game object to check whether the girl is standing on it
     * @return true if the bottom of the girl is located on the top of the object, and false if not
     */
    public boolean landsOn(GameObject other){
        if (this.direction==RIGHT){
            this.hitBoxes[1] = new Rectangle(this.getX()+30, this.getY()+50, this.getWidth()-50, this.getHeight()-50);
        }else if (this.direction==LEFT){
            this.hitBoxes[1] = new Rectangle(this.getX()+20, this.getY()+50, this.getWidth()-50, this.getHeight()-50);
        }
        return this.hitBoxes[1].intersects(other.getBox());
    }
    /**
     * Resets the properties of the girl
     * 
     * @param x coordinate to set the girl to
     * @param y coordinate to set the girl to
     * @param width of the girl
     * @param height of the girl
     */
    public void reset(int x, int y,int width, int height){
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.Vy = 0;
        this.Vx = 0;
        this.xPosition=x;
        this.yPosition=y;
        this.row = IDLE_RIGHT;
        this.col = 0;
        this.direction = RIGHT;
        this.health = Const.MAX_HEALTH;
        this.score = 0;
        this.hitBoxes = new Rectangle[2];
        this.falling = false;
        this.fallen = false;
        this.bottomLevel=Const.GROUND;
    }
}