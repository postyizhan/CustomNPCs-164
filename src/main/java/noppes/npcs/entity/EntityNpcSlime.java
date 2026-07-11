package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

public class EntityNpcSlime extends EntityNPCInterface{
	public EntityNpcSlime(World world)
    {
        super(world);
		scaleX = 2f;
		scaleY = 2f;
		scaleZ = 2f;
		labelOffset = -1.4f;
		display.texture = "customnpcs:textures/entity/slime/Slime.png";
	}    
	@Override
	public void updateHitbox() {

		width = 0.8f;
		height = 0.8f;
		width = (width / 5f) * display.modelSize;
		height = (height / 5f) * display.modelSize;
	}
}
