package noppes.npcs.client.controllers;

import java.util.TreeSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import noppes.npcs.client.AssetsBrowser;

public class MusicController {
	public TreeSet<String> sounds = new TreeSet<String>();
	public TreeSet<String> music = new TreeSet<String>();
	
	public static MusicController Instance;
    public String playing = "";

	private SoundPool musicPool;
	
	public MusicController(){
		Instance = this;
        this.musicPool = new SoundPool(Minecraft.getMinecraft().getResourceManager(),"music", false);
		loadSounds();
	}

	private void loadSounds() {
		System.out.println("loading sounds");
		checkFolder(true,Minecraft.getMinecraft().sndManager.soundPoolSounds, "/customnpcs/sound/", "customnpcs:");

		checkFolder(false,musicPool, "/customnpcs/music/", "customnpcs:");
	}
	private void checkFolder(boolean isSounds, SoundPool pool, String folder, String name){
		AssetsBrowser browser = new AssetsBrowser(folder,new String[]{"wav", "ogg"});
		for(String file : browser.files){
			String dir = name + file;
			pool.addSound(dir);
			addSound(isSounds, dir);
		}
		for(String map : browser.folders){
			checkFolder(isSounds, pool, folder + map + "/",name + map + "/");
		}
	}

	private void addSound(boolean isSounds, String sound) {

		sound = sound.substring(0, sound.indexOf("."));
        sound = sound.replaceAll("/", ".");
		if(isSounds){
            while (Character.isDigit(sound.charAt(sound.length() - 1)))
            {
            	sound = sound.substring(0, sound.length() - 1);
            }
            sounds.add(sound);
		}
		else{
			music.add(sound);
		}
	}

	public void stopMusic(){
		SoundManager manager = Minecraft.getMinecraft().sndManager;
		if(manager == null)
			return;
        if (manager.sndSystem.playing("streaming"))
        	manager.sndSystem.stop("streaming");
        if (manager.sndSystem.playing("BgMusic"))
        	manager.sndSystem.stop("BgMusic");
		playing = "";
	}
	
	public void playStreaming(String music, float x, float y, float z){
		Minecraft mc = Minecraft.getMinecraft();
    	if(mc.gameSettings.soundVolume == 0 || music == null)
    		return;

        SoundPoolEntry soundpoolentry = musicPool.getRandomSoundFromSoundPool(music);
		
		if(soundpoolentry == null)
			return;
		playing = music;
		SoundManager manager = mc.sndManager;
		manager.stopAllSounds();
        manager.sndSystem.newStreamingSource(true, "streaming", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, x, y, z, 2, 64.0F);
        manager.sndSystem.setVolume("streaming", 0.5F * mc.gameSettings.soundVolume);
        manager.sndSystem.play("streaming");
	}

	public void playMusic(String music) {
		Minecraft mc = Minecraft.getMinecraft();
    	if(mc.gameSettings.soundVolume == 0 || music == null)
    		return;

        SoundPoolEntry soundpoolentry = musicPool.getRandomSoundFromSoundPool(music);
		
		if(soundpoolentry == null)
			return;
		playing = music;
		SoundManager manager = mc.sndManager;
		manager.stopAllSounds();

        manager.sndSystem.backgroundMusic("streaming", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false);
        manager.sndSystem.setVolume("streaming", 0.5F * mc.gameSettings.soundVolume);
        manager.sndSystem.play("streaming");
	}
	public boolean isStreaming() {
		SoundManager manager = Minecraft.getMinecraft().sndManager;
    	if(manager.sndSystem != null)
    		return manager.sndSystem.playing("streaming");
		return false;
	}

	public boolean isPlaying(String sound) {
    	if(isStreaming() && sound.equals(playing))
        	return true;
		return false;
	}
	private int soundID = 0;
	public void playSound(String music, float x, float y, float z) {
		Minecraft mc = Minecraft.getMinecraft();
    	if(mc.gameSettings.soundVolume == 0 || music == null)
    		return;

        SoundPoolEntry soundpoolentry = musicPool.getRandomSoundFromSoundPool(music);
		
		if(soundpoolentry == null)
			return;
		String sound = "csound_" + soundID++;
		SoundManager manager = mc.sndManager;
        manager.sndSystem.newSource(true, sound, soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false, x, y, z, 2, 64.0F);
        manager.sndSystem.setVolume(sound, 0.5F * mc.gameSettings.soundVolume);
        manager.sndSystem.play(sound);
	}
}
