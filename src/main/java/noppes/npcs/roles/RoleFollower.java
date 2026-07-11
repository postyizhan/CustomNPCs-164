package noppes.npcs.roles;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;

public class RoleFollower extends RoleInterface{
	
	private String owner;
	public boolean isFollowing = true;
	public HashMap<Integer,Integer> rates;
	public NpcMiscInventory inventory;
	public String dialogHire = StatCollector.translateToLocal("follower.hireText") + " {days} " + StatCollector.translateToLocal("follower.days");
	public String dialogFarewell = StatCollector.translateToLocal("follower.farewellText") + " {player}";
	public int daysHired;
	public long hiredTime;
	
	public int updateTick = 0;
	
	public RoleFollower(EntityNPCInterface npc) {
		super(npc);
		inventory = new NpcMiscInventory(3);
		rates = new HashMap<Integer, Integer>();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("MercenaryDaysHired", daysHired);
		nbttagcompound.setLong("MercenaryHiredTime", hiredTime);
		nbttagcompound.setString("MercenaryDialogHired", dialogHire);
		nbttagcompound.setString("MercenaryDialogFarewell", dialogFarewell);
		if(owner != null && !owner.isEmpty())
			nbttagcompound.setString("MercenaryOwner", owner);
    	nbttagcompound.setTag("MercenaryDayRates", NBTTags.nbtIntegerIntegerMap(rates));
    	nbttagcompound.setCompoundTag("MercenaryInv", inventory.getToNBT());
    	nbttagcompound.setBoolean("MercenaryIsFollowing", isFollowing);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		owner = nbttagcompound.getString("MercenaryOwner");
		daysHired = nbttagcompound.getInteger("MercenaryDaysHired");
		hiredTime = nbttagcompound.getLong("MercenaryHiredTime");
		dialogHire = nbttagcompound.getString("MercenaryDialogHired");
		dialogFarewell = nbttagcompound.getString("MercenaryDialogFarewell");
		rates = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("MercenaryDayRates"));
		inventory.setFromNBT(nbttagcompound.getCompoundTag("MercenaryInv"));
		isFollowing = nbttagcompound.getBoolean("MercenaryIsFollowing");
	}

	@Override
	public boolean aiShouldExecute() {
        if(!hasOwner() || !isFollowing)
        	return false;
        else if(getDaysLeft() <= 0){
			EntityPlayer player = getOwner();
			if(player != null)
				player.addChatMessage(dialogFarewell.replaceAll("\\{player\\}", player.username));
			killed();
			return false;
		}
        else if (this.npc.getDistanceToEntity(getOwner()) < 10)
            return false;
        return true;
	}

	@Override
	public void aiUpdateTask(){
    	updateTick++;
    	if(updateTick < 10)
    		return;
        EntityPlayer theOwner = getOwner();
        this.npc.getLookHelper().setLookPositionWithEntity(theOwner, 10.0F, (float)this.npc.getVerticalFaceSpeed());
        if (!this.npc.getNavigator().tryMoveToEntityLiving(theOwner, 1.0D))
        {
            if (this.npc.getDistanceSqToEntity(theOwner) >= 144.0D)
            {
                int var1 = MathHelper.floor_double(theOwner.posX) - 2;
                int var2 = MathHelper.floor_double(theOwner.posZ) - 2;
                int var3 = MathHelper.floor_double(theOwner.boundingBox.minY);

                for (int var4 = 0; var4 <= 4; ++var4)
                {
                    for (int var5 = 0; var5 <= 4; ++var5)
                    {
                        if ((var4 < 1 || var5 < 1 || var4 > 3 || var5 > 3) && this.npc.worldObj.doesBlockHaveSolidTopSurface(var1 + var4, var3 - 1, var2 + var5) && !npc.worldObj.isBlockNormalCube(var1 + var4, var3, var2 + var5) && !npc.worldObj.isBlockNormalCube(var1 + var4, var3 + 1, var2 + var5))
                        {
                            this.npc.setLocationAndAngles((double)((float)(var1 + var4) + 0.5F), (double)var3, (double)((float)(var2 + var5) + 0.5F), this.npc.rotationYaw, this.npc.rotationPitch);
                            this.npc.getNavigator().clearPathEntity();
                            return;
                        }
                    }
                }
            }
        }
        
        updateTick = 0;
    }

	@Override
	public void aiStartExecuting() {
		updateTick = 10;
	}

	@Override
	public boolean aiContinueExecute() {
        EntityPlayer owner = getOwner();
        return owner!= null && !this.npc.getNavigator().noPath() && this.npc.getDistanceToEntity(owner) > 2 && isFollowing;
    }
	
	public boolean isFollowing(){
		if(!isFollowing)
			return false;
		
		return getOwner() != null;
	}
	
	public EntityPlayer getOwner(){
		if(owner == null || owner.isEmpty())
			return null;
		return npc.worldObj.getPlayerEntityByName(owner);
	}
	public boolean hasOwner(){
		if(daysHired <= 0)
			return false;
		return getOwner() != null;
	}
	@Override
	public void killed() {
		owner = null;
		daysHired = 0;
		hiredTime = 0;
	}
	public int getDaysLeft(){
		if( daysHired <= 0)
			return 0;
		int days = (int) ((npc.worldObj.getWorldTime()- hiredTime) / 24000);
		return daysHired - days;
	}
	public void addDays(int days) {
		daysHired += days + getDaysLeft();
		hiredTime = npc.worldObj.getWorldTime();
	}
	@Override
	public boolean interact(EntityPlayer player) {
		if(owner == null || owner.isEmpty()){
			npc.say(player, npc.advanced.getInteractLine());
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollowerHire, npc);
		}
		else if(player == getOwner()){
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollower, npc);
		}
		return false;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	public void setOwner(String username) {
		if(owner == null || !owner.equals(username))
			killed();
		owner = username;
	}
}
