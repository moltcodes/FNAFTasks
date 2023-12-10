import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.Arrays;

public class Sound {
    public Clip clip;
    URL[] soundURL = new URL[30];

    public Sound(){
        soundURL[1] = getClass().getResource("Resources/Sounds/slideSound.wav");
        soundURL[2] = getClass().getResource("Resources/Sounds/FixBonnieBGSound.wav");
        soundURL[3] = getClass().getResource("Resources/Sounds/foxyRunsBGSound.wav");
        soundURL[4] = getClass().getResource("Resources/Sounds/whackAFreddyBGSound.wav");
        soundURL[5] = getClass().getResource("Resources/Sounds/hitSound.wav");
        soundURL[6] = getClass().getResource("Resources/Sounds/wrongClick.wav");
        soundURL[7] = getClass().getResource("Resources/Sounds/fixLightsBGSound.wav");
        soundURL[8] = getClass().getResource("Resources/Sounds/electricitySound.wav");
        soundURL[9] = getClass().getResource("Resources/Sounds/balloonPopBGSound.wav");
    }

    public void setFile(int i){
        try{
            //System.out.println(Arrays.toString(soundURL));
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e){
            System.out.println("Sound error!");
        }
    }

    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}
