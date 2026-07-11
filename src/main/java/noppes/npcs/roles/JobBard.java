package noppes.npcs.roles;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomItems;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.constants.EnumBardInstrument;

public class JobBard extends JobInterface{
	public int minRange = 2;
	public int maxRange = 64;
	
	public boolean isStreamer = true;

	public String song = "";
	public String song2 = "";
	public String song3 = "";
	public String song4 = "";
	
	private EnumBardInstrument instrument = EnumBardInstrument.Banjo;

	public JobBard(EntityNPCInterface npc) {
		super(npc);
		if(CustomItems.banjo != null){
			mainhand = new ItemStack(CustomItems.banjo);
			overrideMainHand = overrideOffHand = true;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("BardSong", song);
		nbttagcompound.setInteger("BardMinRange", minRange);
		nbttagcompound.setInteger("BardMaxRange", maxRange);
		nbttagcompound.setInteger("BardInstrument", instrument.ordinal());
		nbttagcompound.setBoolean("BardStreamer", isStreamer);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		song = nbttagcompound.getString("BardSong");
		minRange = nbttagcompound.getInteger("BardMinRange");
		maxRange = nbttagcompound.getInteger("BardMaxRange");
		setInstrument(nbttagcompound.getInteger("BardInstrument"));
		isStreamer = nbttagcompound.getBoolean("BardStreamer");
	}
    
	public void setInstrument(int i) {
		if(CustomItems.banjo == null)
			return;
		instrument = EnumBardInstrument.values()[i];
		overrideMainHand = overrideOffHand = instrument != EnumBardInstrument.None;
		switch(instrument){
		case None:
			this.mainhand = null;
			this.offhand = null;
			break;
		case Banjo:
			this.mainhand = new ItemStack(CustomItems.banjo);
			this.offhand = null;
			break;
		case Violin:
			this.mainhand = new ItemStack(CustomItems.violin);
			this.offhand = new ItemStack(CustomItems.violinbow);
			break;
		case Guitar:
			this.mainhand = new ItemStack(CustomItems.guitar);
			this.offhand = null;
			break;
		case Harp:
			this.mainhand = new ItemStack(CustomItems.harp);
			this.offhand = null;
			break;
		}
	}
	public EnumBardInstrument getInstrument(){
		return instrument;
	}
	private long ticks = 0;
	public void onLivingUpdate() {
		if(!npc.worldObj.isRemote)
			return;
		ticks++;
		if(ticks % 10 != 0 )
			return;
		if(song.isEmpty())
			return;
		boolean isStreaming = MusicController.Instance.isStreaming();
				
		if(!isStreaming || !song.equals(MusicController.Instance.playing)){
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox.expand(minRange, minRange/2, minRange));
			if(!list.contains(Minecraft.getMinecraft().thePlayer))
				return;
			if(isStreamer)
				MusicController.Instance.playStreaming(song, (float)npc.posX, (float)npc.posY, (float)npc.posZ);
			else
				MusicController.Instance.playMusic(song);
		}else if(isStreamer){
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox.expand(maxRange, maxRange/2, maxRange));
			if(!list.contains(Minecraft.getMinecraft().thePlayer))
				MusicController.Instance.stopMusic();
		}
		
	}

	@Override
	public void killed() {
		delete();
	}

	@Override
	public void delete() {
		if(npc.worldObj.isRemote){
			if(MusicController.Instance.isPlaying(song)){
				MusicController.Instance.stopMusic();
			}
		}
	}
}
