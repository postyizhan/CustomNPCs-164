package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;

public class DataSkinOverlays {
    private boolean enabled = false;
    public SkinOverlay overlay = new SkinOverlay();

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("SkinOverlaysEnabled", enabled);
        if (enabled) {
            overlay.writeToNBT(compound);
        }
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        enabled = compound.getBoolean("SkinOverlaysEnabled");
        if (enabled) {
            overlay.readFromNBT(compound);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SkinOverlay getOverlay() {
        return overlay;
    }
}
