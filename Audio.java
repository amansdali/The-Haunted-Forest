import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

/**
 * An audio file that can be played, stopped, looped, and adjusted in the Game class
 * 
 * @see Game
 * @author ICS3U6
 * @author Amanda Li
 * @version May 2022
 */
public class Audio {
    Clip audio;
    Audio (String audioName){
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(audioName));
            this.audio = AudioSystem.getClip();
            this.audio.open(audioStream);
        } 
        catch (IOException ex){
            System.out.println("File not found!");
        }
        catch (UnsupportedAudioFileException ex){
            System.out.println("Unsupported file!");
        }   
        catch (LineUnavailableException ex){
            System.out.println("Audio feed already in use!");
        }
    }
//------------------------------------------------------------------------------
    public void start(){
        this.audio.start();
    }
    public void stop(){
        this.audio.stop();
    }
    public void flush(){
        this.audio.flush();
    }
    public void setFramePosition(int frames){
        this.audio.setFramePosition(frames);
    }    
    public void addLineListener(LineListener listener){
        this.audio.addLineListener(listener);
    }
    public boolean isRunning(){
        return this.audio.isRunning();
    }
    public void loop(){
        this.audio.loop(Clip.LOOP_CONTINUOUSLY); 
    }
    public void loop(int count){
        this.audio.loop(count);
    }
}

