import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The Game class runs the main game, and allows the user to control a Girl object on a map
 * 
 * @see Girl
 * @see Map
 * @see Button
 * @see Audio
 * @see Background
 * @author Amanda Li
 * @version May 2022
 */
public class Game{
    private JFrame gameWindow;
    GamePanel gamePanel;   
    MyKeyListener keyListener; 
    MyMouseListener mouseListener; 
    MyMouseMotionListener mouseMotionListener; 
    boolean leftIsHeld, rightIsHeld, spaceIsHeld;
    int mouseX,mouseY;
    
    Girl girl;
    Map map;
    int mapNum;
    Background[] backgrounds;
    int xPosition,xPosition2;
    
    Button startButton, settingsButton, exitButton, leaveSettingsButton, choiceButton, hitboxesButton,
        level1Button, level2Button, level3Button;
    BufferedImage settingsPanel, soundText, levelsText, leaveSettingsText, gameLostText, gameWonText, hitboxesText, 
        nextLevelText, startGameText, tryAgainText, level1Text, level2Text, level3Text, pauseText, fallenText,
        movementInstructionsText, collectText,jumpText,ghostText,hfText,credits1Text,credits2Text,thanksText,
        title1,title2,story1,story2,story3,story4,story5,story6,story7;
    Audio music, jumpSound, collectSound, loseSound;
    boolean running, inStart, paused, inGame, inGameEnd, inSettings, closing;
    long startTime, currentTime, elapsedTime;
    boolean gameWon;
    boolean musicOn, showHitboxes;
//------------------------------------------------------------------------------
    Game(){
        gameWindow = new JFrame("Game Window");   
        gameWindow.setSize(Const.WIDTH,Const.HEIGHT);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);        
        
        gamePanel = new GamePanel();  
        gameWindow.add(gamePanel); 
        
        keyListener = new MyKeyListener();
        mouseListener = new MyMouseListener();
        mouseMotionListener = new MyMouseMotionListener();
        gamePanel.addKeyListener(keyListener);
        gamePanel.addMouseListener(mouseListener);
        gamePanel.addMouseMotionListener(mouseMotionListener);
        
        startButton= new Button ("gui/UI-49.png", "gui/UI-74.png", Const.WIDTH/2-40, Const.HEIGHT/2-20);
        settingsButton= new Button ("gui/UI-109.png", "gui/UI-110.png", Const.WIDTH-200, 20);
        exitButton= new Button ("gui/UI-18.png", "gui/UI-19.png", Const.WIDTH-110, 20);
        leaveSettingsButton= new Button ("gui/UI-0.png", "gui/UI-1.png", 250, 400);
        choiceButton= new Button ("gui/UI-26.png", "gui/UI-27.png", 200, 60);
        hitboxesButton= new Button ("gui/UI-26.png", "gui/UI-27.png", 200, 130);
        level1Button= new Button ("gui/UI-7.png", "gui/UI-8.png", 200, 200);
        level2Button= new Button ("gui/UI-7.png", "gui/UI-8.png", 350, 200);
        level3Button= new Button ("gui/UI-7.png", "gui/UI-8.png", 500, 200);
        level1Button.setImageNum(2);
        level2Button.setImageNum(1);
        level3Button.setImageNum(1);
        
        try {                
            settingsPanel = ImageIO.read(new File("gui/settingsPanel.png"));
            soundText = ImageIO.read(new File("gui/soundText.png"));
            levelsText = ImageIO.read(new File("gui/levelsText.png"));
            leaveSettingsText = ImageIO.read(new File("gui/leaveSettingsText.png"));
            gameLostText = ImageIO.read(new File("gui/gameLostText.png"));
            gameWonText = ImageIO.read(new File("gui/gameWonText.png"));
            nextLevelText = ImageIO.read(new File("gui/nextLevelText.png"));
            startGameText = ImageIO.read(new File("gui/startGameText.png"));
            tryAgainText = ImageIO.read(new File("gui/tryAgainText.png"));
            level1Text = ImageIO.read(new File("gui/level1Text.png"));
            level2Text = ImageIO.read(new File("gui/level2Text.png"));
            level3Text = ImageIO.read(new File("gui/level3Text.png"));
            pauseText = ImageIO.read(new File("gui/pauseText.png"));
            fallenText = ImageIO.read(new File("gui/fallenText.png"));
            hitboxesText = ImageIO.read(new File("gui/hitboxesText.png"));
            movementInstructionsText = ImageIO.read(new File("gui/movementInstructionsText.png"));
            collectText = ImageIO.read(new File("gui/collectText.png"));
            jumpText = ImageIO.read(new File("gui/jumpText.png"));
            ghostText = ImageIO.read(new File("gui/ghostText.png"));
            hfText = ImageIO.read(new File("gui/hfText.png"));
            credits1Text = ImageIO.read(new File("gui/credits1Text.png"));
            credits2Text = ImageIO.read(new File("gui/credits2Text.png"));
            thanksText = ImageIO.read(new File("gui/thanksText.png"));
            title1 = ImageIO.read(new File("gui/title1.png"));
            title2 = ImageIO.read(new File("gui/title2.png"));
            story1 = ImageIO.read(new File("gui/story1.png"));
            story2 = ImageIO.read(new File("gui/story2.png"));
            story3 = ImageIO.read(new File("gui/story3.png"));
            story4 = ImageIO.read(new File("gui/story4.png"));
            story5 = ImageIO.read(new File("gui/story5.png"));
            story6 = ImageIO.read(new File("gui/story6.png"));
            story7 = ImageIO.read(new File("gui/story7.png"));
        } catch (IOException ex){}
        backgrounds = new Background[5];
        for (int i=0;i<backgrounds.length;i++){
            String filename = "backgrounds/Layer"+i;
            backgrounds[i] = new Background(filename+".png", filename+".png", Const.X_STEP+i);
        }
        girl = new Girl(Const.WIDTH/2-73, Const.GROUND-80, 10, 20, 73,80);  
        musicOn = true;
        music=new Audio("sound/music.wav");
        jumpSound=new Audio("sound/ha.wav");
        collectSound=new Audio("sound/kling.wav");
        loseSound=new Audio("sound/bleh.wav");
        showHitboxes = true;
        mapNum = 1;
        gameWindow.setVisible(true); 
        resetGame();
    }
//------------------------------------------------------------------------------  
    /**
     * Starts and runs the program.
     * 
     */
    public void run(){
        startTime = System.currentTimeMillis();
        running = true;
        inStart = true;
        paused = false;
        inGame = false;
        inGameEnd = false;
        inSettings = false;
        closing = false;
        music.start();
        music.loop();
        hitboxesButton.setImageNum(2);
        showHitboxes = !(hitboxesButton.getImageNum()==2);
        while (running) {
            try  {Thread.sleep(Const.FRAME_PERIOD);} catch(Exception e){}
            gameWindow.repaint();
            if (inStart){
                runStart();
            }else if (paused){
                pauseGame();
            }else if(inGame){
                runGame();
            }else if(inGameEnd){
                endGame();
            }else if(inSettings){
                runSettings();
            }else if(closing){
                closeGame();
            }
        }
    }  
//------------------------------------------------------------------------------
    /**
     * Runs the start screen at the beginning of the game.
     * 
     */
    public void runStart(){
        inStart = true;
        paused = false;
        inGame = false;
        inGameEnd = false;
        inSettings = false;
        startButton.setMouseIsOver(startButton.mouseIsOver(mouseX,mouseY));
        if(startButton.getMouseIsOver()){
            startButton.setImageNum(2);
        }else{
            startButton.setImageNum(1);
        }
        for (Background background: backgrounds){background.scrollLeft();}
        xPosition-=(Const.X_STEP+Const.GAME_LAYER);
    }
//------------------------------------------------------------------------------
    /**
     * Pauses the game.
     * 
     */
    public void pauseGame(){
        inStart = false;
        paused = true;
        inGame = false;
        inGameEnd = false;
        inSettings = false;
        startButton.setMouseIsOver(startButton.mouseIsOver(mouseX,mouseY));
        if(startButton.getMouseIsOver()){startButton.setImageNum(2);}
        else{startButton.setImageNum(1);}
    }
//------------------------------------------------------------------------------
    /**
     * Runs the main game
     * 
     */
    public void runGame(){
        inStart = false;
        paused = false;
        inGame = true;
        inGameEnd = false;
        inSettings = false;
        settingsButton.setMouseIsOver(settingsButton.mouseIsOver(mouseX,mouseY));
        if(settingsButton.getMouseIsOver()){settingsButton.setImageNum(2);}
        else{settingsButton.setImageNum(1);}
        exitButton.setMouseIsOver(exitButton.mouseIsOver(mouseX,mouseY));
        if(exitButton.getMouseIsOver()){exitButton.setImageNum(2);}
        else{exitButton.setImageNum(1);}
        checkCollision();
        if(leftIsHeld&&!girl.isFalling()&&xPosition+(Const.X_STEP+Const.GAME_LAYER)<Const.LEFT_BORDER){
            for (Background background: backgrounds){background.scrollRight();}
            girl.moveLeft();
            girl.moveX();
            xPosition+=(Const.X_STEP+Const.GAME_LAYER);
            xPosition2+=(Const.X_STEP+Const.GAME_LAYER_2);
            for (int row=0; row<map.getGameObjects().length; row++){
                for (int col=0; col<map.getGameObjects()[row].length; col++){
                    if(map.getGameObjects()[row][col]!=null){
                        if(map.getGameObjects()[row][col] instanceof Platform){
                            if(girl.collides(map.getGameObjects()[row][col])){
                                girl.moveXBack();
                                for (Background background: backgrounds){background.scrollLeft();}
                                xPosition-=(Const.X_STEP+Const.GAME_LAYER);
                                xPosition2-=(Const.X_STEP+Const.GAME_LAYER_2);
                            }
                        }
                    }
                }
            }
        }else if (rightIsHeld&&!girl.isFalling()&&xPosition-(Const.X_STEP+Const.GAME_LAYER)>-Const.RIGHT_BORDER){
            for (Background background: backgrounds){background.scrollLeft();}
            girl.moveRight();
            girl.moveX();
            xPosition-=(Const.X_STEP+Const.GAME_LAYER);
            xPosition2-=(Const.X_STEP+Const.GAME_LAYER_2);
            for (int row=0; row<map.getGameObjects().length; row++){
                for (int col=0; col<map.getGameObjects()[row].length; col++){
                    if(map.getGameObjects()[row][col]!=null){
                        if(map.getGameObjects()[row][col] instanceof Platform){
                            if(girl.collides(map.getGameObjects()[row][col])){
                                girl.moveXBack();
                                for (Background background: backgrounds){background.scrollRight();}
                                xPosition+=(Const.X_STEP+Const.GAME_LAYER);
                                xPosition2+=(Const.X_STEP+Const.GAME_LAYER_2);
                            }
                        }
                    }
                }
            }
        }else{
            girl.setVx(0);
        }
        map.updateMap(this);
        updateGirl();
    }
    /**
     * Updates the properties and animations of the girl
     * 
     */
    private void updateGirl(){ 
        girl.accellerate();
        girl.moveY(map);
        if (girl.getHealth()==0){
            loseSound.start();
            girl.fall();
            if(girl.isFallen()){
                if(spaceIsHeld){
                    gameWon = false;
                    endGame();
                }
            }
        }
        if (girl.getScore()==Const.MAX_SCORE){
            gameWon = true;
            if (mapNum==Const.NUM_OF_MAPS){
                mapNum = 1;
            }else{
                mapNum = this.mapNum+1;
            }
            endGame();
        }
        girl.updateAnimation(leftIsHeld||rightIsHeld);
    }
    /**
     * Checks for collision between the girl and the ghosts and points on the map
     * 
     */
    private void checkCollision(){
        for (int row=0;row<map.getGameObjects().length;row++){
            for(int col=0;col<map.getGameObjects()[row].length;col++){
                if (map.getGameObjects()[row][col] != null ){
                    if(map.getGameObjects()[row][col] instanceof Ghost){
                        if (girl.collides(map.getGameObjects()[row][col])){
                            girl.loseHP();
                        }
                    }
                    if(map.getGameObjects()[row][col] instanceof Point){
                        if (girl.collides(map.getGameObjects()[row][col])){
                            collectSound.stop();          
                            collectSound.flush();            
                            collectSound.setFramePosition(0);  
                            collectSound.start();
                            girl.setScore(girl.getScore()+1);
                            map.removePoint(row,col);
                        }
                    }
                }
            }
        }
    }
    
//------------------------------------------------------------------------------
    /**
     * Ends the round.
     * 
     */
    public void endGame(){
        inStart = false;
        paused = false;
        inGame = false;
        inGameEnd = true;
        inSettings = false;
        startButton.setMouseIsOver(startButton.mouseIsOver(mouseX,mouseY));
        if(startButton.getMouseIsOver()){startButton.setImageNum(2);}
        else{startButton.setImageNum(1);}
    }
//------------------------------------------------------------------------------
    /**
     * Runs the settings.
     * 
     */
    public void runSettings(){
        inStart = false;
        paused = false;
        inGame = false;
        inGameEnd = false;
        inSettings = true;
        exitButton.setMouseIsOver(exitButton.mouseIsOver(mouseX,mouseY));
        if(exitButton.getMouseIsOver()){exitButton.setImageNum(2);}
        else{exitButton.setImageNum(1);}
        leaveSettingsButton.setMouseIsOver(leaveSettingsButton.mouseIsOver(mouseX,mouseY));
        if(leaveSettingsButton.getMouseIsOver()){leaveSettingsButton.setImageNum(1);}
        else{leaveSettingsButton.setImageNum(2);}
        if (mapNum==1){
            level1Button.setImageNum(2);
            level2Button.setImageNum(1);
            level3Button.setImageNum(1);
        }else if (mapNum==2){
            level1Button.setImageNum(1);
            level2Button.setImageNum(2);
            level3Button.setImageNum(1);
        }else if (mapNum==3){
            level1Button.setImageNum(1);
            level2Button.setImageNum(1);
            level3Button.setImageNum(2);
        }
    }
//------------------------------------------------------------------------------
    /**
     * Closes the game, runs the credits, and ends the program.
     * 
     */
    public void closeGame(){
        inStart = false;
        paused = false;
        inGame = false;
        inGameEnd = false;
        inSettings = false;
        closing = true;
        for (Background background: backgrounds){background.scrollLeft();}
        xPosition-=(Const.X_STEP+Const.GAME_LAYER);
        elapsedTime = (System.currentTimeMillis() - startTime)/1000;
        if (elapsedTime>Const.CREDITS_LENGTH){
            gameWindow.dispose();
            running = false;
            music.stop();
        }
    }
//------------------------------------------------------------------------------  
    /**
     * Resets the game to start a new round.
     * 
     */
    private void resetGame(){
        girl.reset(Const.WIDTH/2-73, Const.GROUND-80, 73,80);  
        map = new Map("maps/map"+mapNum+".txt");
        xPosition=0;
        xPosition2=0;
        mouseX=0;
        mouseY=0;
        gameWon = false;
        loseSound.stop();         
        loseSound.flush();   
        loseSound.setFramePosition(0);
    }
//------------------------------------------------------------------------------  
    public class MyKeyListener implements KeyListener{   
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT){
                leftIsHeld = true;
            } 
            if (key == KeyEvent.VK_RIGHT){
                rightIsHeld = true;
            }
            if (key == KeyEvent.VK_SPACE){
                spaceIsHeld = true;
            }
            if (key == KeyEvent.VK_UP && girl.isOnLevel() && !girl.isFalling()){
                jumpSound.stop();         
                jumpSound.flush();   
                jumpSound.setFramePosition(0);
                jumpSound.start();
                girl.jump(Const.JUMP_SPEED);
            }
        }
        public void keyReleased(KeyEvent e){ 
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT){
                leftIsHeld = false;
            }
            if (key == KeyEvent.VK_RIGHT){
                rightIsHeld = false;
            }
            if (key == KeyEvent.VK_SPACE){
                spaceIsHeld = false;
            }
        }   
        public void keyTyped(KeyEvent e){
            char keyChar = e.getKeyChar();
            if(keyChar == 'p' && inGame){
                pauseGame();
            }
        }    
    }
//------------------------------------------------------------------------------ 
    public class MyMouseListener implements MouseListener{
        public void mouseClicked(MouseEvent e){
            mouseX=e.getX();
            mouseY=e.getY();
            if (startButton.mouseIsOver(mouseX, mouseY)){
                if(paused){
                    runGame();
                }else if (inGameEnd||inStart){
                    startTime = System.currentTimeMillis();
                    resetGame();
                    runGame();
                }
            }
            if (settingsButton.mouseIsOver(mouseX, mouseY)&&inGame){
                runSettings();
            }
            if (exitButton.mouseIsOver(mouseX, mouseY)&&inGame){
                resetGame();
                startTime = System.currentTimeMillis();
                closeGame();
            }
            if (leaveSettingsButton.mouseIsOver(mouseX, mouseY)&&inSettings){
                runGame();
            }
            if (choiceButton.mouseIsOver(mouseX, mouseY)&&inSettings){
                if (choiceButton.getImageNum()==1){choiceButton.setImageNum(2);}
                else{choiceButton.setImageNum(1);}
                musicOn = !(choiceButton.getImageNum()==2);
                if (musicOn){
                    music.start();
                    music.loop();
                }else{
                    music.stop();
                }
            }
            if (hitboxesButton.mouseIsOver(mouseX, mouseY)&&inSettings){
                if (hitboxesButton.getImageNum()==1){hitboxesButton.setImageNum(2);}
                else{hitboxesButton.setImageNum(1);}
                showHitboxes = !(hitboxesButton.getImageNum()==2);
            }
            if (level1Button.mouseIsOver(mouseX, mouseY)&&inSettings){
                mapNum = 1;
                resetGame();
            }
            if (level2Button.mouseIsOver(mouseX, mouseY)&&inSettings){
                mapNum = 2;
                resetGame();
            }
            if (level3Button.mouseIsOver(mouseX, mouseY)&&inSettings){
                mapNum = 3;
                resetGame();
            }
        }
        public void mousePressed(MouseEvent e){   
        }
        public void mouseReleased(MouseEvent e){
        }
        public void mouseEntered(MouseEvent e){
        }
        public void mouseExited(MouseEvent e){
        }
    }
//------------------------------------------------------------------------------  
    public class MyMouseMotionListener implements MouseMotionListener{
        public void mouseMoved(MouseEvent e){
            mouseX=e.getX();
            mouseY=e.getY();
        }
        public void mouseDragged(MouseEvent e){
            mouseX=e.getX();
            mouseY=e.getY();
        }         
    }
//------------------------------------------------------------------------------
    public class GamePanel extends JPanel{
        GamePanel(){
            setFocusable(true);
            requestFocusInWindow();
        }
        @Override
        public void paintComponent(Graphics g){ 
            super.paintComponent(g);
            if (inStart){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                backgrounds[4].draw(g);
                startButton.draw(g);
                g.drawImage(startGameText, 90, 30, null);
                g.drawImage(title1, xPosition+800, 0, null);
                g.drawImage(title2, xPosition+1800, 0, null);
                g.drawImage(story1, xPosition+2400, 0, null);
                g.drawImage(story2, xPosition+3000, 90, null);
                g.drawImage(story3, xPosition+3600, 50, null);
                g.drawImage(story4, xPosition+4200, 0, null);
                g.drawImage(story5, xPosition+4800, 70, null);
                g.drawImage(story6, xPosition+5400, 40, null);
                g.drawImage(story7, xPosition+6200, 0, null);
            }else if (paused){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                backgrounds[4].draw(g);
                startButton.draw(g);
                g.drawImage(startGameText, 90, 30, null);
            }else if(inGame){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                if(mapNum==1){
                    g.drawImage(movementInstructionsText, xPosition+400, 360, null);
                    g.drawImage(jumpText, xPosition+1500, 310, null);
                    g.drawImage(collectText, xPosition+920, 320, null);
                    g.drawImage(ghostText, xPosition+1800, 310, null);
                    g.drawImage(hfText, xPosition+2200, 350, null);
                }
                girl.draw(g);
                map.draw(g); 
                if(showHitboxes){
                    girl.drawHitbox(g);
                    map.drawHitboxes(g);
                }
                backgrounds[4].draw(g);
                if(mapNum==1){
                    g.drawImage(level1Text, xPosition+20, 20, null);
                }else if(mapNum==2){
                    g.drawImage(level2Text, xPosition+20, 20, null);
                }else if(mapNum==3){
                    g.drawImage(level3Text, xPosition+20, 20, null);
                }
                settingsButton.draw(g);
                exitButton.draw(g);
                g.drawImage(pauseText, xPosition2+120, 65, null);
                if(girl.getHealth()==0){g.drawImage(fallenText, 0, 15, null);}
            }else if(inGameEnd){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                backgrounds[4].draw(g);
                if(gameWon){
                    g.drawImage(gameWonText, 50, -20, null);
                    g.drawImage(nextLevelText, 90, 30, null);
                }else{
                    g.drawImage(gameLostText, 50, -20, null);
                    g.drawImage(tryAgainText, 90, 30, null);
                }
                startButton.draw(g);
            }else if(inSettings){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                backgrounds[4].draw(g);
                g.drawImage(settingsPanel, 60, 20, null);
                leaveSettingsButton.draw(g);
                choiceButton.draw(g);
                hitboxesButton.draw(g);
                level1Button.draw(g);
                level2Button.draw(g);
                level3Button.draw(g);
                g.drawImage(hitboxesText, 180,60,null);
                g.drawImage(soundText, 250, 10, null);
                g.drawImage(levelsText, 230, 190, null);
                g.drawImage(leaveSettingsText, 140, 270, null);
            }else if(closing){
                backgrounds[0].draw(g);
                backgrounds[1].draw(g);
                backgrounds[2].draw(g);
                backgrounds[3].draw(g);
                backgrounds[4].draw(g);
                g.drawImage(thanksText, xPosition+800, 20, null);
                g.drawImage(credits1Text, xPosition+2400, 20, null);
                g.drawImage(credits2Text, xPosition+3000, 20, null);
            }
        }    
    }    
//------------------------------------------------------------------------------
    public static void main(String[] args){
        Game game = new Game();
        game.run();
    }
}