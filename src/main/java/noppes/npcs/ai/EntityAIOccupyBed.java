package noppes.npcs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

public class EntityAIOccupyBed extends EntityAIBase
{
    private final EntityNPCInterface npc;

    /** For how long the NPCInterface should be Sleeping */
    private int maxSleepingTicks = 0;

    /** X Coordinate of a nearby bed */
    private int bedX = 0;

    /** Y Coordinate of a nearby bed */
    private int bedY = 0;

    /** Z Coordinate of a nearby bed */
    private int bedZ = 0;

    public EntityAIOccupyBed(EntityNPCInterface par1EntityNPCInterface)
    {
        this.npc = par1EntityNPCInterface;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return !this.npc.isSleeping() && this.getNearbyBedDistance() && !this.npc.worldObj.isDaytime();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.npc.worldObj.isDaytime() && this.isSittableBlock(this.npc.worldObj, this.bedX, this.bedY, this.bedZ);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.npc.getNavigator().tryMoveToXYZ((double)((float)this.bedX) + 0.5D, (double)(this.bedY + 1), (double)((float)this.bedZ) + 0.5D, 1.0D);
        this.npc.setSleeping(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.npc.setSleeping(false);
        this.occupyBed(this.npc.worldObj, this.bedX, this.bedY, this.bedZ, false);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.npc.getDistanceSq((double)this.bedX, (double)(this.bedY + 1), (double)this.bedZ) > 1.5D)
        {
            this.npc.setSleeping(false);
            this.npc.getNavigator().tryMoveToXYZ((double)((float)this.bedX), (double)(this.bedY + 1), (double)((float)this.bedZ), 1.0D);
        }
        else if (!this.npc.isSleeping())
        {
        	npc.prevRotationYaw = npc.rotationYaw = npc.prevRenderYawOffset = npc.renderYawOffset = this.getDirection(this.npc.worldObj, this.bedX, this.bedY, this.bedZ);
            this.npc.setSleeping(true);
            this.occupyBed(this.npc.worldObj, this.bedX, this.bedY, this.bedZ, true);
        }
    }

    /**
     * Searches for a block to sit on within a 8 block range, returns 0 if none found
     */
    protected boolean getNearbyBedDistance()
    {
        int var1 = (int)this.npc.posY;
        double var2 = 2.147483647E9D;

        for (int var4 = (int)this.npc.posX - 8; (double)var4 < this.npc.posX + 8.0D; ++var4)
        {
            for (int var5 = (int)this.npc.posZ - 8; (double)var5 < this.npc.posZ + 8.0D; ++var5)
            {
                if (this.isSittableBlock(this.npc.worldObj, var4, var1, var5) && this.npc.worldObj.isAirBlock(var4, var1 + 1, var5))
                {
                    double var6 = this.npc.getDistanceSq((double)var4, (double)var1, (double)var5);

                    if (var6 < var2)
                    {
                        this.bedX = var4;
                        this.bedY = var1;
                        this.bedZ = var5;
                        var2 = var6;
                    }
                }
            }
        }

        return var2 < 2.147483647E9D;
    }

    /**
     * Determines whether the NPCInterface wants to sit on the block at given coordinate
     */
    protected boolean isSittableBlock(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockId(par2, par3, par4);
        int var6 = par1World.getBlockMetadata(par2, par3, par4);

        if (var5 == Block.bed.blockID && !BlockBed.isBlockHeadOfBed(var6))
        {
        	return true;
        }

        return false;
    }
    
    protected int getDirection(World par1World, int par2, int par3, int par4) {
    	int orientation = -1;
        int var6 = par1World.getBlockMetadata(par2, par3, par4);
        int var7 = BlockBed.getDirection(var6);

        switch (var7) {
            case 0 : orientation = 180; break;
            case 1 : orientation = 270; break;
            case 2 : orientation = 0; break;
            case 3 : orientation = 90;
        }
        return orientation;
    }
    
    protected void occupyBed(World par1World, int par2, int par3, int par4, boolean par5)
    {
        int var5 = par1World.getBlockId(par2, par3, par4);

        if (var5 == Block.bed.blockID) {
        	BlockBed.setBedOccupied(par1World, par2, par3, par4, par5);
        }
    }
}
