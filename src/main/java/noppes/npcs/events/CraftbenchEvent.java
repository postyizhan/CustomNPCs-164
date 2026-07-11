package noppes.npcs.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;

public class CraftbenchEvent{
	@ForgeSubscribe
	public void invoke(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		int blockId = player.worldObj.getBlockId(event.x, event.y, event.z);
		if(blockId != Block.workbench.blockID || player.worldObj.isRemote)
			return;
		RecipeController controller = RecipeController.instance;
        NBTTagList list = new NBTTagList();
        for(RecipeCarpentry recipe : controller.globalRecipes.values()){
        	list.appendTag(recipe.writeNBT());
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("recipes", list);
        
        NoppesUtilServer.sendData(player, EnumPacketType.SyncRecipes, compound);
	}
}
