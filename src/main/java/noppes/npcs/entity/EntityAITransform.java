package noppes.npcs.entity;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;

public class EntityAITransform extends EntityAIBase {
    private EntityNPCInterface npc;
    private int transformCooldown;
    private float healthThreshold;

    public EntityAITransform(EntityNPCInterface npc, float healthThreshold) {
        this.npc = npc;
        this.healthThreshold = healthThreshold;
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (transformCooldown > 0) {
            transformCooldown--;
            return false;
        }
        float healthPercent = npc.getHealth() / npc.getMaxHealth();
        return healthPercent <= healthThreshold;
    }

    @Override
    public void startExecuting() {
        // 基础版本：只触发变身冷却，实际变身逻辑需要配合外部系统
        transformCooldown = 600;
        // TODO: 实际变身逻辑（更换模型/贴图/数据）需要额外系统支持
    }
}
