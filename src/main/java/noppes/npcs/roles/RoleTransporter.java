package noppes.npcs.roles;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;

public class RoleTransporter extends RoleInterface{
	
	public int transportId = -1;
	public String name;
	
	public RoleTransporter(EntityNPCInterface npc) {
		super(npc);
	}
	

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("TransporterId", transportId);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		transportId = nbttagcompound.getInteger("TransporterId");
		TransportLocation loc = getLocation();
		if(loc != null)
			name = loc.name;
		
		if(loc == null){
			transportId = -1;
			name = "";
		}
	}
	private int ticks = 10;
	@Override
	public boolean aiShouldExecute() {
		ticks--;
		if(ticks > 0)
			return false;
		ticks = 10;
		
		if(!hasTransport())
			return false;
		
		TransportLocation loc = getLocation();
		if(loc.type != 0)
			return false;

		List<EntityPlayer> inRange = npc.worldObj.getEntitiesWithinAABB(
				EntityPlayer.class,npc.boundingBox.expand(6D,
						6D, 6D));
		for(EntityPlayer player :inRange){
			if(!npc.canEntityBeSeen(player))
				continue;
			PlayerTransportData data = PlayerDataController.instance.getTransportData(player);
			if(!data.transports.contains(transportId)){
				PlayerDataController.instance.addTransport(transportId,player);
				NoppesUtilServer.sendData(player, EnumPacketType.Chat, "transporter.unlock1", " " + loc.name + " ", "transporter.unlock2");
			}
		}
		return false;
		
	}
	@Override
	public void aiStartExecuting() {
		
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if(hasTransport()){
			TransportLocation loc = getLocation();
			if(loc.type == 2){
				PlayerTransportData data = PlayerDataController.instance.getTransportData(player);
				if(!data.transports.contains(transportId)){
					PlayerDataController.instance.addTransport(transportId,player);
					NoppesUtilServer.sendData(player, EnumPacketType.Chat, "transporter.unlock1", " " + loc.name + " ", "transporter.unlock2");
				}
			}
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTransporter, npc);
		}
		return false;
	}
	public TransportLocation getLocation(){
		if(npc.worldObj.isRemote)
			return null;
		return TransportController.getInstance().getTransport(transportId);
	}
	public boolean hasTransport(){
		TransportLocation loc = getLocation();
		return loc != null && loc.isNpc(npc);
	}

	public void setTransport(TransportLocation location) {
		transportId = location.id;
		name = location.name;
	}

}
