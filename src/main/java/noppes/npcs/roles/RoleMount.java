package noppes.npcs.roles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;

public class RoleMount extends RoleInterface {

    private float jumpStrength = 1.0F;
    private boolean allowSprint = true;

    public RoleMount(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("MountJumpStrength", jumpStrength);
        compound.setBoolean("MountAllowSprint", allowSprint);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        jumpStrength = compound.getFloat("MountJumpStrength");
        if (jumpStrength == 0.0F) {
            jumpStrength = 1.0F;
        }
        allowSprint = !compound.hasKey("MountAllowSprint") || compound.getBoolean("MountAllowSprint");
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (player == null || npc.worldObj.isRemote) {
            return false;
        }
        if (npc.riddenByEntity != null && npc.riddenByEntity != player) {
            return false;
        }
        if (player.ridingEntity == npc) {
            player.mountEntity(null);
        } else {
            player.mountEntity(npc);
        }
        return true;
    }

    public float getJumpStrength() {
        return jumpStrength;
    }

    public void setJumpStrength(float jumpStrength) {
        this.jumpStrength = Math.max(0.0F, Math.min(2.0F, jumpStrength));
    }

    public boolean isAllowSprint() {
        return allowSprint;
    }

    public void setAllowSprint(boolean allowSprint) {
        this.allowSprint = allowSprint;
    }
}
