package noppes.npcs.client.events;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import noppes.npcs.CustomNpcs;


public class TextureLoadEvent{

	@ForgeSubscribe
	public void invoke(TextureStitchEvent.Post event) {
		File file = new File(CustomNpcs.Dir,"assets/customnpcs");
		if(!file.exists())
			file.mkdirs();
		
		File check = new File(file,"sound");
		if(!check.exists())
			check.mkdir();
		
		check = new File(file,"music");
		if(!check.exists())
			check.mkdir();
		
		check = new File(file,"textures");
		if(!check.exists())
			check.mkdir();
		
		

        SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
        FolderResourcePack pack = new FolderResourcePack(CustomNpcs.Dir);
        simplemanager.reloadResourcePack(pack);
	}

}
