package essentials;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundPlayer {
	Random rand = new Random();
	public String[] fileName = new String[6];
	public Music[] TestMusic = new Music[6];
	public Sound sword;
	public Sound bow;
	public Sound death;
	public Sound eat;
	public Sound slurp;
	public Sound spear;
	public Sound punch;
	public void startLoadingSound(){
		if(TinySound.isInitialized()){
			int song = rand.nextInt(6);
			System.out.println(song);
			TestMusic[song] = TinySound.loadMusic(new File("res" + File.separatorChar + "music" + File.separatorChar + "song" + (song + 1) + ".ogg"), true);
			TestMusic[song].play(false);
			if(TestMusic[song].playing() == false){
				System.out.println("No song? Blasphemy!");
				TestMusic[song].unload();
				TestMusic[song] = TinySound.loadMusic(new File("res" + File.separatorChar + "music" + File.separatorChar + "song" + (song + 1) + ".ogg"), true);
				TestMusic[song].play(false);
			}
			delay(song);
		}
	}
	private void delay(final int song){
        long delay = getDelayTime(song);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
        	public void run(){
                addSong(rand.nextInt(6), song);
        	}
        }, delay);
	}
	private long getDelayTime(int song){
		long time = 0L;
		if(song == 0)time = getSongLength(3, 22);
		if(song == 1)time = getSongLength(1, 54);
		if(song == 2)time = getSongLength(2, 29);
		if(song == 3)time = getSongLength(3, 6);
		if(song == 4)time = getSongLength(3, 44);
		if(song == 5)time = getSongLength(3, 15);
		return time;
	}
	private void addSong(int song, int previous){
		if(TinySound.isInitialized()){
			TestMusic[previous].unload();
			while(song == previous){
				song = rand.nextInt(6);
				if(song != previous)
					break;
			}
			System.out.println(song);
			TestMusic[song] = TinySound.loadMusic(new File("res" + File.separatorChar + "music" + File.separatorChar + "song" + (song + 1) + ".ogg"), true);
			TestMusic[song].play(false);
			if(TestMusic[song].playing() == false){
				System.out.println("No song? Blasphemy!");
				TestMusic[song].unload();
				TestMusic[song] = TinySound.loadMusic(new File("res" + File.separatorChar + "music" + File.separatorChar + "song" + (song + 1) + ".ogg"), true);
				TestMusic[song].play(false);
			}
			delay(song);
		}
	}
	private long getSongLength(int minutes, int seconds){
		long length = (long) ((minutes * 60 + seconds) * 1000);
		return length;
	}
}
