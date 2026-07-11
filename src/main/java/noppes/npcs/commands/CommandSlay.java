package noppes.npcs.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import noppes.npcs.EntityNPCInterface;

public class CommandSlay extends CommandBase{
	
	private Map<String,Class<?>> map = new HashMap<String,Class<?>>();
	
	public CommandSlay(){
		HashMap<String,Class<?>> list = new HashMap<String,Class<?>>(EntityList.stringToClassMapping);		
		for(String name : list.keySet()){
			Class<?> cls = list.get(name);
			if(EntityNPCInterface.class.isAssignableFrom(cls))
				continue;
			if(!EntityLivingBase.class.isAssignableFrom(cls))
				continue;
			map.put(name.toLowerCase(), list.get(name));
		}

		map.remove("monster");
		map.remove("mob");
		
		map.put("all",EntityLivingBase.class);
		map.put("mobs",EntityMob.class);
		map.put("animals", EntityAnimal.class);
		map.put("items", EntityItem.class);
		map.put("xporbs", EntityXPOrb.class);
	}

	@Override
	public String getCommandName() {
		return "slay";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/slay all, /slay mobs, /slay animals, /slay <entityname>";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] vars) {
		if(!(icommandsender instanceof EntityPlayer)){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Can only be used by players"));
			return;
		}
		if(vars.length == 0){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("No entities given"));
			return;
		}
		EntityPlayer player = (EntityPlayer) icommandsender;
		ArrayList<Class<?>> toDelete = new ArrayList<Class<?>>();
		for(String delete : vars){
			Class<?> cls = map.get(delete.toLowerCase());
			if(cls != null)
				toDelete.add(cls);
			if(delete.equals("mobs")){
				toDelete.add(EntityGhast.class);
				toDelete.add(EntityDragon.class);
			}
		}
		int count = 0;
		
		AxisAlignedBB range = player.boundingBox.expand(120, 120, 120);
		List<Entity> list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, range);
		
		for(Entity entity : list){
			if(entity instanceof EntityPlayer)
				continue;
			if(entity instanceof EntityTameable && ((EntityTameable)entity).isTamed())
				continue;
			if(entity instanceof EntityNPCInterface)
				continue;
			if(delete(entity,toDelete))
				count++;
		}
		if(toDelete.contains(EntityXPOrb.class)){
			list = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, range);
			for(Entity entity : list){
				entity.isDead = true;
				count++;
			}
		}
		if(toDelete.contains(EntityItem.class)){
			list = player.worldObj.getEntitiesWithinAABB(EntityItem.class, range);
			for(Entity entity : list){
				entity.isDead = true;
				count++;
			}
		}
		
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(count + " entities deleted"));
	}
	
    private boolean delete(Entity entity, ArrayList<Class<?>> toDelete) {
		for(Class<?> delete : toDelete){
			if(delete == EntityAnimal.class && (entity instanceof EntityHorse)){
				continue;
			}
			if(delete.isAssignableFrom(entity.getClass())){
				entity.isDead = true;
				return true;
			}
		}
		return false;
	}

	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] vars)
    {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		return getListOfStringsFromIterableMatchingLastWord(vars, list);
    }

}
