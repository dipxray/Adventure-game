package main;

import java.net.URL;
import javax.sound.sampled.*;

public class Sound {

    public Clip clip;
    URL soundURL[] = new URL[10];

    public Sound() {

        // Sea sound
        soundURL[0] = getClass().getResource("/res/sound/mixkit-sea-waves-with-birds-loop-1185.wav");

        // Pause sound
        soundURL[1] = getClass().getResource("/res/sound/mixkit-retro-arcade-game-over-470.wav");
        // Object Collect sound
        soundURL[2] = getClass().getResource("/res/sound/collect.wav");
        // Door open
        soundURL[3] = getClass().getResource("/res/sound/mixkit-creaky-door-open-195_1.wav");
        soundURL[4] = getClass().getResource("/res/sound/mixkit-samurai-sword-impact-2789.wav"); // sword

        // Background music
        soundURL[6] = getClass().getResource("/res/sound/niknet_art-retro-8bit-happy-videogame-music-246631.wav");
    }

    public void setFile(int i) {
        try {

            if (soundURL[i] == null) {
                System.out.println("FILE NOT FOUND!");
                return;
            }

            //  Close previous clip properly
            if (clip != null) {
                clip.stop();
                clip.close();
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            if (clip == null)
                return;

            clip.setFramePosition(0);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}