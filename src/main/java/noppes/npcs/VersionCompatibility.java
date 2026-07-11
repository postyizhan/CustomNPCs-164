package noppes.npcs;

import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.Lines;

public class VersionCompatibility {
	public static int ModRev = 12;

	public static void CheckNpcCompatibility(EntityNPCInterface npc, NBTTagCompound compound){
		if(npc.npcVersion == ModRev)
			return;
		CompatabilityFix(compound, npc.advanced.writeToNBT(new NBTTagCompound()));
		CompatabilityFix(compound, npc.ai.writeToNBT(new NBTTagCompound()));
		CompatabilityFix(compound, npc.stats.writeToNBT(new NBTTagCompound()));
		CompatabilityFix(compound, npc.display.writeToNBT(new NBTTagCompound()));	
		CompatabilityFix(compound, npc.inventory.writeEntityToNBT(new NBTTagCompound()));	
		if(npc.npcVersion < 5){
			String texture = compound.getString("Texture");
			texture = texture.replace("/mob/customnpcs/", "customnpcs:textures/entity/");
			texture = texture.replace("/mob/", "customnpcs:textures/entity/");
			compound.setString("Texture", texture);
		}
		if(npc.npcVersion < 6 && compound.getTag("NpcInteractLines") instanceof NBTTagList){
			List<String> interactLines = NBTTags.getStringList(compound.getTagList("NpcInteractLines"));
			Lines lines = new Lines();
			for(int i = 0; i < interactLines.size(); i++){
				Line line = new Line();
				line.text = (String) interactLines.toArray()[i];
				lines.lines.put(i, line);
			}
			compound.setCompoundTag("NpcInteractLines", lines.writeToNBT());

			List<String> worldLines = NBTTags.getStringList(compound.getTagList("NpcLines"));
			lines = new Lines();
			for(int i = 0; i < worldLines.size(); i++){
				Line line = new Line();
				line.text = (String) worldLines.toArray()[i];
				lines.lines.put(i, line);
			}
			compound.setCompoundTag("NpcLines", lines.writeToNBT());

			List<String> attackLines = NBTTags.getStringList(compound.getTagList("NpcAttackLines"));
			lines = new Lines();
			for(int i = 0; i < attackLines.size(); i++){
				Line line = new Line();
				line.text = (String) attackLines.toArray()[i];
				lines.lines.put(i, line);
			}
			compound.setCompoundTag("NpcAttackLines", lines.writeToNBT());

			List<String> killedLines = NBTTags.getStringList(compound.getTagList("NpcKilledLines"));
			lines = new Lines();
			for(int i = 0; i < killedLines.size(); i++){
				Line line = new Line();
				line.text = (String) killedLines.toArray()[i];
				lines.lines.put(i, line);
			}
			compound.setCompoundTag("NpcKilledLines", lines.writeToNBT());

		}
		npc.npcVersion = ModRev;
	}
	public static void CheckAvailabilityCompatibility(ICompatibilty compatibilty, NBTTagCompound compound){
		if(compatibilty.getVersion() == ModRev)
			return;
		CompatabilityFix(compound, compatibilty.writeToNBT(new NBTTagCompound()));
		
		compatibilty.setVersion(ModRev);
	}
	public static void CompatabilityFix(NBTTagCompound compound,
			NBTTagCompound check) {
		Collection<NBTBase> tags = check.getTags();
		for(NBTBase nbt : tags){
			if(!compound.hasKey(nbt.getName())){
				compound.setTag(nbt.getName(), nbt);
			}
			else if(nbt instanceof NBTTagCompound && compound.getTag(nbt.getName()) instanceof NBTTagCompound){
				CompatabilityFix(compound.getCompoundTag(nbt.getName()), (NBTTagCompound)nbt);
			}
		}
	}
}
