package noppes.npcs;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cpw.mods.fml.common.IPlayerTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import noppes.npcs.client.AnalyticsTracking;

public class ServerTickHandler implements IPlayerTracker{
	private String serverName = null;

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if(serverName == null){
			String e = "local";
			MinecraftServer server = MinecraftServer.getServer();
			if(server.isDedicatedServer()){
				try {
					e = InetAddress.getByName(server.getServerHostname()).getCanonicalHostName();
				} catch (UnknownHostException e1) {
					e = MinecraftServer.getServer().getServerHostname();
				}
				if(server.getPort() != 25565)
					e += ":" + server.getPort();
			}
			if(e == null || e.startsWith("192.168") || e.contains("127.0.0.1") || e.startsWith("localhost"))
				e = "local";
			serverName = e;
		}
		AnalyticsTracking.sendData(player, "join", serverName);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
}
