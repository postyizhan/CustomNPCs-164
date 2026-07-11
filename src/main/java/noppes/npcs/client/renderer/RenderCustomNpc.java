package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import noppes.npcs.EntityCustomNpc;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.model.ModelMPM;
import noppes.npcs.client.model.ModelNPCMale;

public class RenderCustomNpc extends RenderNPCHumanMale{


	public RenderCustomNpc() {
		super(new ModelMPM(0), new ModelMPM(0.5f), new ModelMPM(1));
		// TODO Auto-generated constructor stub
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		renderNPC((EntityCustomNpc) entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderNPC((EntityCustomNpc) entity, d, d1, d2, f, f1);
	}

	private void renderNPC(EntityCustomNpc npc, double d, double d1,
			double d2, float f, float f1) {
		if(npc.renderEntity == null)
			super.doRenderLiving(npc, d, d1, d2, f, f1);
		else{

			Render render = RenderManager.instance.getEntityRenderObject(npc.renderEntity);
		}
	} 
}