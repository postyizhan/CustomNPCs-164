package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;

public class TintData {
    private boolean tintEnabled = false;
    private boolean hurtTintEnabled = true;
    private boolean generalTintEnabled = false;
    private int hurtTint = 0xff0000;
    private int generalTint = 0x000000;
    private int generalAlpha = 40;

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("TintEnabled", tintEnabled);
        if (tintEnabled) {
            compound.setBoolean("HurtTintEnabled", hurtTintEnabled);
            compound.setBoolean("GeneralTintEnabled", generalTintEnabled);
            compound.setInteger("HurtTint", hurtTint);
            compound.setInteger("GeneralTint", generalTint);
            compound.setInteger("GeneralAlpha", generalAlpha);
        }
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound) {
        tintEnabled = compound.getBoolean("TintEnabled");
        if (tintEnabled) {
            hurtTintEnabled = compound.getBoolean("HurtTintEnabled");
            generalTintEnabled = compound.getBoolean("GeneralTintEnabled");
            hurtTint = compound.getInteger("HurtTint");
            generalTint = compound.getInteger("GeneralTint");
            generalAlpha = compound.getInteger("GeneralAlpha");
        }
    }

    public boolean isHurtTintEnabled() {
        return hurtTintEnabled;
    }

    public void setHurtTintEnabled(boolean hurtTintEnabled) {
        this.hurtTintEnabled = hurtTintEnabled;
    }

    public int getHurtTint() {
        return hurtTint;
    }

    public void setHurtTint(int hurtTint) {
        this.hurtTint = hurtTint;
    }

    public int getGeneralTint() {
        return generalTint;
    }

    public void setGeneralTint(int generalTint) {
        this.generalTint = generalTint;
    }

    public boolean isTintEnabled() {
        return tintEnabled;
    }

    public void setTintEnabled(boolean tintEnabled) {
        this.tintEnabled = tintEnabled;
    }

    public boolean isGeneralTintEnabled() {
        return generalTintEnabled;
    }

    public void setGeneralTintEnabled(boolean generalTintEnabled) {
        this.generalTintEnabled = generalTintEnabled;
    }

    public int getGeneralAlpha() {
        return generalAlpha;
    }

    public void setGeneralAlpha(int generalAlpha) {
        this.generalAlpha = Math.max(0, Math.min(255, generalAlpha));
    }

    public boolean shouldDisableVanillaColor(boolean isHurt) {
        if (isHurt) {
            return (isTintEnabled() && (!isHurtTintEnabled() && !isGeneralTintEnabled()));
        } else {
            return !(isTintEnabled() && isGeneralTintEnabled());
        }
    }
}
