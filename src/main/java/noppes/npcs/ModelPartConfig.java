package noppes.npcs;

import net.minecraft.nbt.NBTTagCompound;

public class ModelPartConfig {
	public float scaleX = 1, scaleY = 1, scaleZ = 1;
	public float transX = 0, transY = 0, transZ = 0;
	

	public NBTTagCompound writeToNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setFloat("ScaleX", scaleX);
		compound.setFloat("ScaleY", scaleY);
		compound.setFloat("ScaleZ", scaleZ);
		
		compound.setFloat("TransX", transX);
		compound.setFloat("TransY", transY);
		compound.setFloat("TransZ", transZ);
		return compound;
	}
	
	public void readFromNBT(NBTTagCompound compound){
		scaleX = compound.getFloat("ScaleX");
		scaleY = compound.getFloat("ScaleY");
		scaleZ = compound.getFloat("ScaleZ");

		transX = compound.getFloat("TransX");
		transY = compound.getFloat("TransY");
		transZ = compound.getFloat("TransZ");
	}
	
	public String toString(){
		return "ScaleX: " + scaleX + " - ScaleY: " + scaleY + " - ScaleZ: " + scaleZ;
	}

	public void setScale(float x, float y, float z) {
		scaleX = x;
		scaleY = y;
		scaleZ = z;
	}
	public void setScale(float x, float y) {
		scaleZ = scaleX = x;
		scaleY = y;
	}

}
