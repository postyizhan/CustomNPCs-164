package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;

public class HitboxData {

    private float widthScale = 1.0f;
    private float heightScale = 1.0f;
    private boolean hitboxEnabled = false;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("HitboxEnabled", hitboxEnabled);
        if (hitboxEnabled) {
            compound.setFloat("HitboxWidthScale", widthScale);
            compound.setFloat("HitboxHeightScale", heightScale);
        }
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        hitboxEnabled = compound.getBoolean("HitboxEnabled");
        if (hitboxEnabled) {
            widthScale = compound.getFloat("HitboxWidthScale");
            heightScale = compound.getFloat("HitboxHeightScale");
        }
    }

    public float getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(float widthScale) {
        this.widthScale = Math.max(0.1f, Math.min(10.0f, widthScale));
    }

    public float getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(float heightScale) {
        this.heightScale = Math.max(0.1f, Math.min(10.0f, heightScale));
    }

    public boolean isHitboxEnabled() {
        return hitboxEnabled;
    }

    public void setHitboxEnabled(boolean hitboxEnabled) {
        this.hitboxEnabled = hitboxEnabled;
    }
}
