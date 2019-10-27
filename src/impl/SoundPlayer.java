package impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	
	private String filename;
	private List<String> theDayPossibleNames;
	private List<String> peaceSignPossibleNames;
	private List<String> youSayRunPossibleNames;
	
	public SoundPlayer(String musicName) {
		this.initializeNamesPossibilities();
		if (theDayPossibleNames.contains(musicName))
			this.filename = "the_day.wav";
		else if (peaceSignPossibleNames.contains(musicName))
			this.filename = "peace_sign.wav";
		else if (youSayRunPossibleNames.contains(musicName))
			this.filename = "you_say_run.wav";
		else
			this.filename = "the_day.wav";			// Choix par défaut
	}
	
	
	public void playSoundInBackground() {
		// Création d'un nouveau thread dédié à la lecture du fichier audio
		new Thread(() -> playSound()).start();
	}
	
	private void playSound() {	
		String path = "src/resources/audios/"+this.filename;
		File f = new File(path);
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			// Lit l'audio jusqu'à une interruption volontaire
			// Si la fin du fichier est atteinte, la lecture reprendra du début
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeNamesPossibilities() {
		theDayPossibleNames = new ArrayList<>();
		peaceSignPossibleNames = new ArrayList<>();
		youSayRunPossibleNames = new ArrayList<>();
		
		theDayPossibleNames.add("default");
		theDayPossibleNames.add("1");
		theDayPossibleNames.add("the day");
		theDayPossibleNames.add("the_day");
		
		peaceSignPossibleNames.add("2");
		peaceSignPossibleNames.add("peace sign");
		peaceSignPossibleNames.add("peace_sign");
		
		youSayRunPossibleNames.add("3");
		youSayRunPossibleNames.add("you say run");
		youSayRunPossibleNames.add("you_say_run");
	}
}
