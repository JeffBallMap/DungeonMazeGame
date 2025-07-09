package main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		
		soundURL[0]=getClass().getResource("/sounds/Musicski.wav");
		soundURL[1]=getClass().getResource("/sounds/Gems.wav");
		soundURL[2]=getClass().getResource("/sounds/TimeBonus.wav");
		soundURL[3]=getClass().getResource("/sounds/Walkin.wav");
		soundURL[4]=getClass().getResource("/sounds/Fin.wav");
		
	}
	
	public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Sound error: " + e);
        }
    }
	
	public void play() {
		
		clip.start();
		
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}
