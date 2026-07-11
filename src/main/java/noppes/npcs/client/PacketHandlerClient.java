package noppes.npcs.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipeList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.player.GuiBook;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.events.ItemInteractEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerClient implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(!packet.channel.equals("CNPCs Client"))
			return;
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(packet.data))));
			client(dis,(EntityPlayer) player,EnumPacketType.values()[dis.readInt()]);
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void client(DataInputStream dis, EntityPlayer player, EnumPacketType type) throws IOException{		
		if(type == EnumPacketType.CHATBUBBLE){
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			EntityNPCInterface npc = (EntityNPCInterface) entity;
			if(npc.messages == null)
				npc.messages = new RenderChatMessages();
			String text = NoppesStringUtils.formatText(dis.readUTF(), player, npc);
			npc.messages.addMessage(text, npc);
			player.addChatMessage(npc.getCommandSenderName() + ": " + text);
		}
		if(type == EnumPacketType.Bank){
			NoppesUtil.bankData(dis);
		}
		else if(type == EnumPacketType.Chat){
			String message = "";
			try{
				while(true){
					String str = dis.readUTF();
					message += StatCollector.translateToLocal(str);
				}
			}catch(EOFException e){
				
			}
			player.addChatMessage(message);
		}
		else if(type == EnumPacketType.Message){
			String description = StatCollector.translateToLocal(dis.readUTF());
			String message = dis.readUTF();
			Achievement ach = new QuestAchievement(message, description);
			Minecraft.getMinecraft().guiAchievement.queueTakenAchievement(ach);
			Minecraft.getMinecraft().guiAchievement.achievementGetLocalText = ach.getDescription();
		}
		else if(type == EnumPacketType.SyncRecipes){
			NBTTagList list = CompressedStreamTools.read(dis).getTagList("recipes");
			HashMap<Integer,RecipeCarpentry> recipes = new HashMap<Integer,RecipeCarpentry>();
	        if(list == null)
	        	return;
            for(int i = 0; i < list.tagCount(); i++)
            {
            	RecipeCarpentry recipe = new RecipeCarpentry();
            	recipe.readNBT((NBTTagCompound)list.tagAt(i));
            	recipes.put(recipe.id,recipe);
            }
            RecipeController.reloadGlobalRecipes(recipes);
	        
		}
		else if(type == EnumPacketType.Dialog){
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			NoppesUtil.openDialog(dis,(EntityNPCInterface) entity,player);
		}
		else if(type == EnumPacketType.QuestCompletion){
			NoppesUtil.guiQuestCompletion(player,CompressedStreamTools.read(dis));
		}	
		else if(type == EnumPacketType.EditingNpc){
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			NoppesUtil.setLastNpc((EntityNPCInterface) entity);
		}
		else if(type == EnumPacketType.PlayMusic){
			MusicController.Instance.playMusic("customnpcs:Failboat103 - Excalibuuur");
		}			
		else if(type == EnumPacketType.PlaySound){
			MusicController.Instance.playSound(dis.readUTF(),dis.readFloat(),dis.readFloat(),dis.readFloat());
		}			
		else if(type == EnumPacketType.UpdateNpc){
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			entity.readFromNBT(compound);
		}
		else if(type == EnumPacketType.SaveRole){
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(compound.getInteger("EntityId"));
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return; 
			((EntityNPCInterface)entity).advanced.setRole(compound.getInteger("Role"));
			((EntityNPCInterface)entity).roleInterface.readEntityFromNBT(compound);
			NoppesUtil.setLastNpc((EntityNPCInterface) entity);
		}
		else if(type == EnumPacketType.Gui){
			EnumGuiType gui = EnumGuiType.values()[dis.readInt()];
			CustomNpcs.proxy.openGui(NoppesUtil.getLastNpc(), gui, dis.readInt(), dis.readInt(), dis.readInt());
		}
		else if(type == EnumPacketType.Particle){
			NoppesUtil.spawnParticle(dis);
		}
		else if(type == EnumPacketType.Delete){
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			((EntityNPCInterface)entity).delete();
		}
		else if(type == EnumPacketType.ScrollList){
			NoppesUtil.setScrollList(dis);
		}
		else if(type == EnumPacketType.ScrollData){
			NoppesUtil.setScrollData(dis);
		}
		else if(type == EnumPacketType.ScrollSelected){
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !(gui instanceof IScrollData))
				return;
			String selected = dis.readUTF();

			((IScrollData)gui).setSelected(selected);
		}
		else if(type == EnumPacketType.RedstoneBlockSave){
			NoppesUtil.saveRedstoneBlock(player,dis);
		}
		else if(type == EnumPacketType.WaypointSave){
			NoppesUtil.saveWayPointBlock(player,dis);
		}
		else if(type == EnumPacketType.GuiData){
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null)
				return;
			
			if(gui instanceof GuiNPCInterface2 && ((GuiNPCInterface2)gui).hasSubGui()){
				gui = (GuiScreen) ((GuiNPCInterface2)gui).getSubGui();
			}
			if(gui instanceof IGuiData)
				((IGuiData)gui).setGuiData(CompressedStreamTools.read(dis));
		}
		else if(type == EnumPacketType.GuiError){
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !(gui instanceof IGuiError))
				return;
			
			int i = dis.readInt();
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			
			((IGuiError)gui).setError(i, compound);
		}
		else if(type == EnumPacketType.GuiClose){
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null)
				return;
			
			int i = dis.readInt();
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			if(gui instanceof IGuiClose)
				((IGuiClose)gui).setClose(i, compound);
			
			Minecraft mc = Minecraft.getMinecraft();
	        mc.displayGuiScreen(null);
	        mc.setIngameFocus();
		}
		else if(type == EnumPacketType.MerchantAdd){
            MerchantRecipeList merchantrecipelist = MerchantRecipeList.readRecipiesFromStream(dis);
            ItemInteractEvent.Merchant.setRecipes(merchantrecipelist);
		}
		else if(type == EnumPacketType.OpenBook){
			int x = dis.readInt(), y = dis.readInt(), z = dis.readInt();
			
			NoppesUtil.openGUI(player, new GuiBook(player, ItemStack.loadItemStackFromNBT(CompressedStreamTools.read(dis)), x, y, z));
		}
	}



}
