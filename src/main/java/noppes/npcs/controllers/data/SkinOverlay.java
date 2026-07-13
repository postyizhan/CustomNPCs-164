package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;

public class SkinOverlay {
    public String texture = "";
    public int color = 0xFFFFFF;
    public float alpha = 1.0F;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("OverlayTexture", texture);
        compound.setInteger("OverlayColor", color);
        compound.setFloat("OverlayAlpha", alpha);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        texture = compound.getString("OverlayTexture");
        color = compound.getInteger("OverlayColor");
        alpha = compound.getFloat("OverlayAlpha");
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.max(0.0F, Math.min(1.0F, alpha));
    }

    public boolean isEmpty() {
        return texture == null || texture.isEmpty();
    }
}
