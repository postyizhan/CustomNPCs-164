package noppes.npcs;

import java.util.HashMap;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DataInventory implements IInventory{
	public HashMap<Integer,ItemStack> items = new HashMap<Integer,ItemStack>();
	public HashMap<Integer,Integer> dropchance = new HashMap<Integer,Integer>();
	public HashMap<Integer, ItemStack> weapons = new HashMap<Integer, ItemStack>();
	public HashMap<Integer, ItemStack> armor = new HashMap<Integer, ItemStack>();
		
	public int minExp = 0;
	public int maxExp = 0;
	
	private EntityNPCInterface npc;
	
	public DataInventory(EntityNPCInterface npc){
		this.npc = npc;
	}
	public NBTTagCompound writeEntityToNBT(NBTTagCompound nbttagcompound){
		nbttagcompound.setInteger("MinExp", minExp);
		nbttagcompound.setInteger("MaxExp", maxExp);
		nbttagcompound.setTag("NpcInv", NBTTags.nbtItemStackList(items));
		nbttagcompound.setTag("Armor", NBTTags.nbtItemStackList(getArmor()));
		nbttagcompound.setTag("Weapons", NBTTags.nbtItemStackList(getWeapons()));
		nbttagcompound.setTag("DropChance", NBTTags.nbtIntegerIntegerMap(dropchance));
		
		return nbttagcompound;
	}
	public void readEntityFromNBT(NBTTagCompound nbttagcompound){
		minExp = nbttagcompound.getInteger("MinExp");
		maxExp = nbttagcompound.getInteger("MaxExp");
		items = NBTTags.getItemStackList(nbttagcompound.getTagList("NpcInv"));
		setArmor(NBTTags.getItemStackList(nbttagcompound.getTagList("Armor")));
		setWeapons(NBTTags.getItemStackList(nbttagcompound.getTagList("Weapons")));
		dropchance = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("DropChance"));
		npc.updateTasks();
	}
	private HashMap<Integer, ItemStack> getWeapons() {
		return weapons;
	}
	private void setWeapons(HashMap<Integer, ItemStack> list) {
		weapons = list;
	}
	private HashMap<Integer, ItemStack> getArmor() {
		return armor;
	}
	private void setArmor(HashMap<Integer, ItemStack> list) {
		armor = list;
	}
	public ItemStack getWeapon(){
		return getWeapons().get(0);
	}
	public ItemStack getProjectile(){
		return getWeapons().get(1);
	}
	public ItemStack getOffHand(){
		return getWeapons().get(2);
	}

	public void dropStuff(boolean dropXP) {
		if(npc.worldObj.isRemote)
			return;
		for (int i : items.keySet()) {
			ItemStack item = items.get(i);
			if(item == null)
				continue;
			int dchance = 100;
			if(dropchance.containsKey(i))
				dchance = dropchance.get(i);
			int chance = npc.worldObj.rand.nextInt(100) + dchance;
			if(chance >= 100)
				npc.dropPlayerItemWithRandomChoice(item.copy(), true);
		}
		
		if (dropXP) {
			int var1 = minExp;
			if (maxExp - minExp > 0)
				var1 += npc.worldObj.rand.nextInt(maxExp - minExp);
	
	        while (var1 > 0)
	        {
	            int var2 = EntityXPOrb.getXPSplit(var1);
	            var1 -= var2;
	            npc.worldObj.spawnEntityInWorld(new EntityXPOrb(npc.worldObj, npc.posX, npc.posY, npc.posZ, var2));
	        }
		}
	}
	
	public ItemStack armorItemInSlot(int i) {
		return getArmor().get(i);
	}
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 15;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		if(i < 4)
			return armorItemInSlot(i);
		else if(i < 7)
			return getWeapons().get(i-4);
		else
			return items.get(i-7);
	}
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		int i =0;
        HashMap<Integer,ItemStack> var3;

        if (par1 >= 7)
        {
        	var3 = items;
            par1 -= 7;
        }
        else if (par1 >= 4)
        {
        	var3 = getWeapons();
            par1 -= 4;
            i = 1;
        }
        else{
        	var3 = getArmor();
            i = 2;
        }
        
        ItemStack var4 = null;
        if (var3.get(par1) != null)
        {

            if (var3.get(par1).stackSize <= par2)
            {
                var4 = var3.get(par1);
                var3.put(par1,null);
            }
            else
            {
                var4 = var3.get(par1).splitStack(par2);

                if (var3.get(par1).stackSize == 0)
                {
                    var3.put(par1,null);
                }
            }
        }
        if(i == 1)
        	setWeapons(var3);
        if(i == 2)
        	setArmor(var3);
        return var4;
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		int i = 0;
        HashMap<Integer,ItemStack> var2;;

        if (par1 >= 7)
        {
        	var2 = items;
            par1 -= 7;
        }
        else if (par1 >= 4)
        {
        	var2 = getWeapons();
            par1 -= 4;
            i = 1;
        }
        else{
        	var2 = getArmor();
            i = 2;
        }

        if (var2.get(par1) != null)
        {
            ItemStack var3 = var2.get(par1);
            var2.put(par1,null);
            if(i == 1)
            	setWeapons(var2);
            if(i == 2)
            	setArmor(var2);
            return var3;
        }
        else
        {
            return null;
        }
	}
	@Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
		int i = 0;
        HashMap<Integer,ItemStack> var3;

        if (par1 >= 7)
        {
        	var3 = items;
            par1 -= 7;
        }
        else if (par1 >= 4)
        {
        	var3 = getWeapons();
            par1 -= 4;
            i = 1;
        }
        else{
        	var3 = getArmor();
            i = 2;
        }

        var3.put(par1,par2ItemStack);
        if(i == 1)
        	setWeapons(var3);
        if(i == 2)
        	setArmor(var3);
    }
	@Override
	public String getInvName() {
		return "NPC Inventory";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public void onInventoryChanged() {
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}
	@Override
	public void openChest() {
	}
	@Override
	public void closeChest() {
	}
	@Override
	public boolean isInvNameLocalized() {
		return true;
	}
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
}
