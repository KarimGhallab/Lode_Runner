package main;

import contracts.EngineContract;
import data.AnsiColor;
import impl.SoundPlayer;
import impl_bug.EngineImpl;
import services.Engine;

public class MainContractsBug {

	public static void main(String[] args) {
		System.out.print(AnsiColor.INTEL_BLUE + "[INFORMATION] Game's initialisation..." + AnsiColor.RESET);
		System.out.flush();
		
		String wantedMusic = parseArgs(args);
		startMusic(wantedMusic);

		// L'ENGINE
		Engine engineDecorated = new EngineImpl();
		Engine engineContract = new EngineContract(engineDecorated);
		engineContract.initWithContract(30, 23, 3, 2);
		
		System.out.println(AnsiColor.INTEL_BLUE + "Done" + AnsiColor.RESET);
	}
	
	private static String parseArgs(String[] args) {
		for(int i=0; i<args.length; i++) {
			String[] splitted = args[i].split("=");
			if (splitted.length == 2 && splitted[0].toLowerCase().equals("music"))
				return splitted[1]; 
		}
		return "default";
	}
	
	private static void startMusic(String wantedMusic) {
		new SoundPlayer(wantedMusic).playSoundInBackground();
	}
}