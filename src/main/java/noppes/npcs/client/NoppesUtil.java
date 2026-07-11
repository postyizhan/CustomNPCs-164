package noppes.npcs.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityEnderFX;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.player.GuiDialogInteract;
import noppes.npcs.client.gui.player.GuiQuestCompletion;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.Quest;
import noppes.npcs.entity.EntityNpcEnderchibi;
import cpw.mods.fml.common.network.PacketDispatcher;

public class NoppesUtil {

	public static void requestOpenGUI(EnumGuiType gui) {
		requestOpenGUI(gui, 0, 0, 0);
	}

	public static void requestOpenGUI(EnumGuiType gui, int i, int j, int k) {
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(EnumPacketType.Gui.ordinal());
			out.writeInt(gui.ordinal());
			out.writeInt(i);
			out.writeInt(j);
			out.writeInt(k);
			out.close();
	        PacketDispatcher.sendPacketToServer(new Packet250CustomPayload("CNPCs Server",bytes.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataOutputStream getDataOutputStream(ByteArrayOutputStream stream) throws IOException{
        return new DataOutputStream(new GZIPOutputStream(stream));
	}

	public static void spawnParticle(DataInputStream dis) {
		try {
			double posX = dis.readDouble();
			double posY = dis.readDouble();
			double posZ = dis.readDouble();
			float height = dis.readFloat();
			float width = dis.readFloat();
			float yOffset = dis.readFloat();
			
			String particle = dis.readUTF();
			
			World worldObj = Minecraft.getMinecraft().theWorld;

			Random rand = worldObj.rand;
			if(particle.equals("heal")){
		        for (int k = 0; k < 6; k++)
		        {
		        	worldObj.spawnParticle("instantSpell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0, 0, 0);
		        	worldObj.spawnParticle("spell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0, 0, 0);
		        }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static EntityNPCInterface lastNpc;
	public static EntityNPCInterface getLastNpc() {
		return lastNpc;
	}
	public static void setLastNpc(EntityNPCInterface npc) {
		lastNpc = npc;
	}

	public static void openGUI(EntityPlayer player, Object guiscreen) {
		CustomNpcs.proxy.openGui(player, guiscreen);
	}

	public static void setScrollList(DataInputStream dis) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui == null || !(gui instanceof IScrollData))
			return;
		Vector<String> data = new Vector<String>();
		String line;
		
		try {
			while((line = dis.readUTF()) != null){
				data.add(line);
			}
		} catch (Exception e) {
			
		}
		
		((IScrollData)gui).setData(data,null);
	}
	public static void setScrollData(DataInputStream dis) {
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui == null)
			return;
		Vector<String> list = new Vector<String>();
		HashMap<String,Integer> data = new HashMap<String,Integer>();
		
		try {
			while(true){
				int id = dis.readInt();
				String name = dis.readUTF();
				data.put(name, id);
				list.add(name);
			}
		} catch (Exception e) {
		}
		if(gui instanceof GuiNPCInterface2 && ((GuiNPCInterface2)gui).hasSubGui()){
			gui = (GuiScreen) ((GuiNPCInterface2)gui).getSubGui();
		}
		if(gui instanceof GuiContainerNPCInterface2 && ((GuiContainerNPCInterface2)gui).hasSubGui()){
			gui = (GuiScreen) ((GuiContainerNPCInterface2)gui).getSubGui();
		}
		if(gui instanceof IScrollData)
			((IScrollData)gui).setData(list, data);
		
	}

	public static void guiQuestCompletion(EntityPlayer player, NBTTagCompound read) {
		Quest quest = new Quest();
		quest.readNBT(read);
		if (!quest.completeText.equals(""))
			NoppesUtil.openGUI(player, new GuiQuestCompletion(quest));
		else
			NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, quest.id);
	}
	
	public static void openDialog(DataInputStream dis, EntityNPCInterface npc, EntityPlayer player) throws IOException {

		if(DialogController.instance == null)
			DialogController.instance = new DialogController();
		NBTTagCompound compound = CompressedStreamTools.read(dis);
		Dialog dialog = new Dialog();
		dialog.readNBT(compound);
		CustomNpcs.proxy.openGui(player, new GuiDialogInteract(npc, dialog));
	}

	public static void sendData(EnumPacketType enu, Object... obs) {
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(enu.ordinal());
			for(Object ob : obs){
				if(ob == null)
					continue;
				if(ob instanceof Map){
					Map<String,Integer> map = (Map<String, Integer>) ob;
					for(String key : map.keySet()){
						int value = map.get(key);
						out.writeInt(value);
						out.writeUTF(key);
					}
				}
				else if(ob instanceof Enum)
					out.writeInt(((Enum<?>) ob).ordinal());
				else if(ob instanceof Double)
					out.writeDouble((Double) ob);
				else if(ob instanceof Float)
					out.writeFloat((Float) ob);
				else if(ob instanceof Integer)
					out.writeInt((Integer) ob);
				else if(ob instanceof String)
					out.writeUTF((String) ob);
				else if(ob instanceof NBTTagCompound)
					CompressedStreamTools.write((NBTTagCompound) ob, out);
				else if(ob instanceof MerchantRecipeList)
					((MerchantRecipeList)ob).writeRecipiesToStream(out);
				
			}
			out.close();
	        PacketDispatcher.sendPacketToServer(new Packet250CustomPayload("CNPCs Server",bytes.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void bankData(DataInputStream dis) throws IOException {
		BankController controller = BankController.getInstance();
		controller.loadBanks(dis);
		GuiScreen gui = Minecraft.getMinecraft().currentScreen;
		if(gui != null)
			gui.initGui();
	}
	public static void spawnEnderchibi(EntityNpcEnderchibi chibi) {
		Random rand = chibi.worldObj.rand;
		EntityEnderFX fx = new EntityEnderFX(chibi, chibi.posX + (rand.nextDouble() - 0.5D) * chibi.width, (chibi.posY + rand.nextDouble() * chibi.height), chibi.posZ + (rand.nextDouble() - 0.5D) * chibi.width, (rand.nextDouble() - 0.5D) * 2D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2D);
		Minecraft.getMinecraft().effectRenderer.addEffect(fx);
	}
	public static void saveRedstoneBlock(EntityPlayer player, DataInputStream dis) throws IOException {
		NBTTagCompound compound = CompressedStreamTools.read(dis);
		int x = compound.getInteger("x");
		int y = compound.getInteger("y");
		int z = compound.getInteger("z");
		
		TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
		tile.readFromNBT(compound);
		
		CustomNpcs.proxy.openGui(x, y, z, EnumGuiType.RedstoneBlock, player);
	}
	public static void saveWayPointBlock(EntityPlayer player, DataInputStream dis) throws IOException {
		NBTTagCompound compound = CompressedStreamTools.read(dis);
		int x = compound.getInteger("x");
		int y = compound.getInteger("y");
		int z = compound.getInteger("z");
		
		TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
		tile.readFromNBT(compound);
		
		CustomNpcs.proxy.openGui(x, y, z, EnumGuiType.Waypoint, player);
	}
}
