package noppes.npcs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.renderer.RenderNPCInterface;

import org.lwjgl.opengl.GL11;

public class EntityEnderFX extends EntityPortalFX{

    private float portalParticleScale;
    private int particleNumber;
    private RenderNPCInterface npcRenderer;
    private EntityNPCInterface npc;
    private static final ResourceLocation field_110737_b = new ResourceLocation("textures/particle/particles.png");
	public EntityEnderFX(EntityNPCInterface npc, double par2, double par4,
			double par6, double par8, double par10, double par12) {
		super(npc.worldObj, par2, par4, par6, par8, par10, par12);
		
		npcRenderer = (RenderNPCInterface) RenderManager.instance.getEntityRenderObject(npc);
		this.npc = npc;
		particleNumber = npc.worldObj.rand.nextInt(2);
        portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleRed = particleGreen = particleBlue = 1;
	}

	@Override
    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.draw();
        float scale = ((float)particleAge + par2) / (float)particleMaxAge;
        scale = 1.0F - scale;
        scale *= scale;
        scale = 1.0F - scale;
        particleScale = portalParticleScale * scale;
        
    	Minecraft mc = Minecraft.getMinecraft();
    	
    	ClientProxy.bindTexture(npcRenderer.getEntityTexture(npc));
    	
        float f = 0.875f;
        float f1 = f + 0.125f;
        float f2 = 0.75f - (particleNumber * 0.25f);
        float f3 = f2 + 0.25f;
        float f4 = 0.1F * particleScale;
        float f5 = (float)((prevPosX + (posX - prevPosX) * (double)par2) - interpPosX);
        float f6 = (float)((prevPosY + (posY - prevPosY) * (double)par2) - interpPosY);
        float f7 = (float)((prevPosZ + (posZ - prevPosZ) * (double)par2) - interpPosZ);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(this.getBrightnessForRender(par2));
        par1Tessellator.setColorOpaque_F(1, 1, 1);
        par1Tessellator.addVertexWithUV(f5 - par3 * f4 - par6 * f4, f6 - par4 * f4, f7 - par5 * f4 - par7 * f4, f1, f3);
        par1Tessellator.addVertexWithUV((f5 - par3 * f4) + par6 * f4, f6 + par4 * f4, (f7 - par5 * f4) + par7 * f4, f1, f2);
        par1Tessellator.addVertexWithUV(f5 + par3 * f4 + par6 * f4, f6 + par4 * f4, f7 + par5 * f4 + par7 * f4, f, f2);
        par1Tessellator.addVertexWithUV((f5 + par3 * f4) - par6 * f4, f6 - par4 * f4, (f7 + par5 * f4) - par7 * f4, f, f3);
        
        tessellator.draw();
    	ClientProxy.bindTexture(field_110737_b);
        tessellator.startDrawingQuads();
        
    }
    
    public int getFXLayer(){
    	return 0;
    }
}
