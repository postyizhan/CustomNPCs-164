package noppes.npcs.client.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import noppes.npcs.EntityNPCInterface;

public class RenderNpcDragon extends RenderNPCInterface{

	public RenderNpcDragon(ModelBase model, float f) {
		super(model, f);
	}

    protected void renderPlayerScale(EntityNPCInterface npc, float f)
    {
    	GL11.glTranslatef(0, 0, 0.6f / 5 * npc.display.modelSize);
    	super.renderPlayerScale(npc, f);
    }
}
