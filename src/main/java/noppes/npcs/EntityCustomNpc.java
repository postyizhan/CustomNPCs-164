package noppes.npcs;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EntityCustomNpc extends EntityNPCInterface{
	public Entity renderEntity;
	public String renderEntityName = "";

	public ModelPartConfig arms = new ModelPartConfig();
	public ModelPartConfig body = new ModelPartConfig();
	public ModelPartConfig legs = new ModelPartConfig();
	public ModelPartConfig head = new ModelPartConfig();

	public ModelPartData legParts = new ModelPartData();
	private HashMap<String,ModelPartData> parts = new HashMap<String,ModelPartData>();
	public byte breasts = 0;
	public int animationStart;
	
	public EntityCustomNpc(World world) {
		super(world);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setRenderEntityName(compound.getString("RenderEntityName"));
		arms.readFromNBT(compound.getCompoundTag("ArmsConfig"));
		body.readFromNBT(compound.getCompoundTag("BodyConfig"));
		legs.readFromNBT(compound.getCompoundTag("LegsConfig"));
		head.readFromNBT(compound.getCompoundTag("HeadConfig"));

		legParts.readFromNBT(compound.getCompoundTag("LegParts"));

		HashMap<String,ModelPartData> parts = new HashMap<String,ModelPartData>();
		NBTTagList list = compound.getTagList("Parts");
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound item = (NBTTagCompound) list.tagAt(i);
			ModelPartData part = new ModelPartData();
			part.readFromNBT(item);
			parts.put(item.getString("PartName"), part);
		}
		this.parts = parts;
		breasts = compound.getByte("Breasts");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("RenderEntityName", renderEntityName);
		compound.setCompoundTag("ArmsConfig", arms.writeToNBT());
		compound.setCompoundTag("BodyConfig", body.writeToNBT());
		compound.setCompoundTag("LegsConfig", legs.writeToNBT());
		compound.setCompoundTag("HeadConfig", head.writeToNBT());

		compound.setCompoundTag("LegParts", legParts.writeToNBT());
		NBTTagList list = new NBTTagList();
		for(String name : parts.keySet()){
			NBTTagCompound item = parts.get(name).writeToNBT();
			item.setString("PartName", name);
			list.appendTag(item);
		}
		compound.setTag("Parts", list);
		compound.setByte("Breasts", breasts);
	}

	private void setRenderEntityName(String name) {
		renderEntity = null;
		try {
			Class<?> cls = Class.forName(name);
            if (EntityLivingBase.class.isAssignableFrom(cls)) {
            	renderEntity = (Entity) cls.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldObj});
            }
            
		} catch (Exception e) {
			
		}
	}

	public ModelPartData getPartData(String part) {
		return parts.get(part);
	}	
	public float getBodyY(){
		if(legParts.type == 3)
			return (0.9f - body.scaleY) * 0.75f + getLegsY();
		if(legParts.type == 3)
			return (0.5f - body.scaleY) * 0.75f + getLegsY();
		return (1 - body.scaleY) * 0.75f + getLegsY();
	}

	public float getLegsY() {
		if(legParts.type == 3)
			return (0.87f - legs.scaleY) * 1f;
		return (1 - legs.scaleY) * 0.75f;
	}
}
