package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;

public class CloneFolder {
    public String name;
    public long createdDate;

    public CloneFolder() {
        this.name = "";
        this.createdDate = System.currentTimeMillis();
    }

    public CloneFolder(String name) {
        this.name = name;
        this.createdDate = System.currentTimeMillis();
    }

    public void readNBT(NBTTagCompound compound) {
        name = compound.getString("Name");
        createdDate = compound.getLong("Created");
    }

    public NBTTagCompound writeNBT(NBTTagCompound compound) {
        compound.setString("Name", name);
        compound.setLong("Created", createdDate);
        return compound;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (name.length() > 32) {
            return false;
        }
        if (!name.equals(name.trim())) {
            return false;
        }
        if (name.equals(".") || name.equals("..")) {
            return false;
        }
        if (name.startsWith("___")) {
            return false;
        }
        // 移除纯数字限制：允许用户创建数字文件夹名（与旧 Tab N 共存）
        for (char c : name.toCharArray()) {
            if (c == '/' || c == '\\' || c == ':' || c == '*' || c == '?' || c == '"' || c == '<' || c == '>' || c == '|') {
                return false;
            }
            if (c < 32) {
                return false;
            }
        }
        return true;
    }
}
