import java.awt.Graphics;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/**
 * The Map class represents a collection of game objects accepted by the Game class,
 * collection of the Ghost, Platform, and Point objects in a level
 * 
 * @see Game
 * @see Ghost
 * @see Platform
 * @see Point
 * @see GameObject
 * @author ICS3U6
 * @author Amanda Li
 * @version May 2022
 */
public class Map {
    GameObject[][] gameObjects;
//------------------------------------------------------------------------------       
    Map(String mapName){
        int rows = 0;
        int columns = 0;
        try {                
            Scanner input = new Scanner(new File(mapName));
            while (input.hasNext()){
                String line = input.nextLine();
                rows ++;
                if (line.length()>columns){columns = line.length();}
            }
            input.close();
        } catch (IOException ex){}  
        this.gameObjects = new GameObject[rows][columns];
        try {                
            Scanner input = new Scanner(new File(mapName));
            for (int row=0; row<rows; row++){
                String line = input.nextLine();
                for (int col=0; col<line.length(); col++){
                    if (line.charAt(col) == Const.CIRCLE_LETTER){
                        Point newPoint = new Point(row, col, Const.CIRCLE_TILE_NAME);
                        this.addPoint(newPoint, row, col);
                    }
                    if (line.charAt(col) == Const.GHOST_LETTER){
                        Ghost newGhost = new Ghost(row, col, "sprites/ghost.png", 2, 7, 64, 64,Const.X_STEP/2);
                        this.addGhost(newGhost,row,col);
                    }
                    if (line.charAt(col) == Const.PLATFORM_LETTER){
                        Platform newPlatform = new Platform(row, col, "sprites/grassTile.png");
                        this.addPlatform(newPlatform,row,col);
                    }
                }
            }
            input.close();
        } catch (IOException ex){}
    }
//------------------------------------------------------------------------------       
    public GameObject[][] getGameObjects(){
        return this.gameObjects;
    }
//------------------------------------------------------------------------------   
    /**
     * Adds a point object to the list of game objects
     * 
     * @param point object to be added
     * @param row to add the object to
     * @param column to add the object to
     */
    public void addPoint(Point point, int row, int column){
        this.gameObjects[row][column] = point;
    }
    /**
     * Removes a point object from the list of game objects
     * 
     * @param row to remove the object from
     * @param column to remove the object from
     */
    public void removePoint(int row, int column){
        this.gameObjects[row][column] = null;
    }
    /**
     * Adds a ghost object to the list of game objects
     * 
     * @param ghost object to be added
     * @param row to add the object to
     * @param column to add the object to
     */
    public void addGhost(Ghost ghost, int row, int column){
        this.gameObjects[row][column] = ghost;
    }
    /**
     * Removes a ghost object from the list of game objects
     * 
     * @param row to remove the object from
     * @param column to remove the object from
     */
    public void removeGhost(int row, int column){
        this.gameObjects[row][column] = null;
    }
    /**
     * Adds a platform object to the list of game objects
     * 
     * @param platform object to be added
     * @param row to add the object to
     * @param column to add the object to
     */
    public void addPlatform(Platform platform, int row, int column){
        this.gameObjects[row][column] = platform;
    }
    /**
     * Removes a platform object from the list of game objects
     * 
     * @param row to remove the object from
     * @param column to remove the object from
     */
    public void removePlatform(int row, int column){
        this.gameObjects[row][column] = null;
    }
//------------------------------------------------------------------------------
    public void draw(Graphics g){
        for (int row=0; row<this.gameObjects.length; row++){
            for (int col=0; col<this.gameObjects[row].length; col++){
                if(gameObjects[row][col]!=null){
                    if (gameObjects[row][col] instanceof Point){
                        ((Point)gameObjects[row][col]).draw(g);
                    }
                    else if (gameObjects[row][col] instanceof Ghost){
                        ((Ghost)gameObjects[row][col]).draw(g);
                    }
                    else if (gameObjects[row][col] instanceof Platform){
                        ((Platform)gameObjects[row][col]).draw(g);
                    }
                }
            }
        }
    }
    public void drawHitboxes(Graphics g){
        for (int row=0; row<this.gameObjects.length; row++){
            for (int col=0; col<this.gameObjects[row].length; col++){
                if(gameObjects[row][col]!=null){
                    if (gameObjects[row][col] instanceof Point){
                        ((Point)gameObjects[row][col]).drawHitbox(g);
                    }
                    else if (gameObjects[row][col] instanceof Ghost){
                        ((Ghost)gameObjects[row][col]).drawHitbox(g);
                    }
                    else if (gameObjects[row][col] instanceof Platform){
                        ((Platform)gameObjects[row][col]).drawHitbox(g);
                    }
                }
            }
        }
    }
//------------------------------------------------------------------------------
    /**
     * Updates the positions of the objects on the map based on the position of the game
     * 
     * @param game the map corresponds to
     */
    public void updateMap(Game game){
        for (int row=0; row<this.gameObjects.length; row++){
            for (int col=0; col<this.gameObjects[row].length; col++){
                if(gameObjects[row][col]!=null){
                    if (gameObjects[row][col] instanceof Point){
                        ((Point)gameObjects[row][col]).setXPosition(game.xPosition);
                    }
                    if (gameObjects[row][col] instanceof Ghost){
                        updateGhost((Ghost)gameObjects[row][col],game);
                    }
                    if (gameObjects[row][col] instanceof Platform){
                        ((Platform)gameObjects[row][col]).setXPosition(game.xPosition);
                    }
                }
            }
        }
    }
    /**
     * Updates a ghost on the map; calls the blink method at random intervals and moves the ghost left or right
     * based on its movement pattern
     * 
     * @param ghost to update
     * @param game that the map corresponds to
     */
    private void updateGhost(Ghost ghost,Game game){
        ghost.setCurrentTime(System.currentTimeMillis());
        ghost.setElapsedTime((ghost.getCurrentTime() - ghost.getStartTime())/1000); 
        if (ghost.getElapsedTime() == ghost.getBlinkInterval()){
            ghost.setStartTime(ghost.getCurrentTime());
            ghost.setBlink(true);
            ghost.setBlinkInterval((int)(6*Math.random()));
        }
        ghost.blink();
        if (ghost.getDirection() == "left"){
            if (ghost.getX()>ghost.getStartX()){
                ghost.moveLeft(game);
            }else{
                ghost.setDirection("right");
                ghost.moveRight(game);
            }
        }else if (ghost.getDirection() == "right"){
            if (ghost.getX()<ghost.getStartX()+Const.GHOST_MOVEMENT_RANGE){
                ghost.moveRight(game);
            }else{
                ghost.setDirection("left");
                ghost.moveLeft(game);
            }
        }
    }
}