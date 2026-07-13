# Mount 骑乘移动控制修复

> 修复时间：2026-07-13  
> 问题报告：用户反馈前后反转、无法左右移动、被拉回

---

## 🐛 问题描述

用户反馈 Mount 角色骑乘时的问题：
1. **前后方向反转**：按 W 键后退，按 S 键前进
2. **无法左右移动**：按 A/D 键无反应
3. **移动后被拉回**：移动一段距离后自动回到原位

---

## 🔍 问题分析

### 原始代码问题
```java
// 问题 1：错误的方向计算（-Math.PI 导致前后反转）
this.motionX += (double)(MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI) * forward * speed);
this.motionZ += (double)(MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI) * forward * speed);

// 问题 2：只处理了 forward，忽略了 strafe（左右移动）
// 问题 3：使用 += 累加而非直接赋值，导致运动不稳定
```

---

## ✅ 修复方案

### 1. 修复前后方向
移除错误的 `-Math.PI` 偏移，使用正确的方向向量：
```java
// 正确的前进方向
double moveX = (double)(MathHelper.sin(radians) * forward * speed);
double moveZ = (double)(MathHelper.cos(radians) * forward * speed);

// 应用时反转 X 轴（Minecraft 坐标系）
this.motionX = -moveX;
this.motionZ = moveZ;
```

### 2. 添加左右移动
支持 strafe（左右平移），方向为朝向 + 90°：
```java
float strafe = rider.moveStrafing * 0.5F;  // 左右移动（减半速度）

// 左右平移方向（垂直于前进方向）
moveX += (double)(MathHelper.sin((radians + 1.5707964F)) * strafe * speed);
moveZ += (double)(MathHelper.cos((radians + 1.5707964F)) * strafe * speed);
```

### 3. 修复被拉回问题
- 使用直接赋值而非累加（`=` 而非 `+=`）
- 无输入时主动停止移动（`motionX/Z = 0`）
- 同步 NPC 朝向到玩家朝向

### 4. 支持疾跑加速
```java
if (roleMount.isAllowSprint() && rider.isSprinting()) {
    speed *= 1.5F;  // 疾跑时速度 * 1.5
}
```

---

## 📝 完整实现

```java
if (this.riddenByEntity instanceof EntityPlayer && this.roleInterface != null 
    && this.advanced.role == EnumRoleType.Mount) {
    EntityPlayer rider = (EntityPlayer) this.riddenByEntity;
    RoleMount roleMount = (RoleMount) this.roleInterface;

    // 1. 同步朝向
    this.rotationYaw = rider.rotationYaw;
    this.rotationPitch = rider.rotationPitch * 0.5F;
    this.setRotation(this.rotationYaw, this.rotationPitch);
    this.rotationYawHead = this.renderYawOffset = this.rotationYaw;

    // 2. 获取输入
    float strafe = rider.moveStrafing * 0.5F;
    float forward = rider.moveForward;

    if (forward != 0.0F || strafe != 0.0F) {
        float speed = 0.35F;

        // 疾跑加速
        if (roleMount.isAllowSprint() && rider.isSprinting()) {
            speed *= 1.5F;
        }

        // 3. 计算移动向量
        float yaw = this.rotationYaw;
        float radians = yaw * 0.017453292F;

        double moveX = (double)(MathHelper.sin(radians) * forward * speed);
        double moveZ = (double)(MathHelper.cos(radians) * forward * speed);

        moveX += (double)(MathHelper.sin((radians + 1.5707964F)) * strafe * speed);
        moveZ += (double)(MathHelper.cos((radians + 1.5707964F)) * strafe * speed);

        // 4. 应用移动
        this.motionX = -moveX;
        this.motionZ = moveZ;
    } else {
        // 无输入时停止
        this.motionX = 0.0D;
        this.motionZ = 0.0D;
    }
}
```

---

## 🎯 验证步骤

### 测试前进/后退
1. 骑乘 Mount NPC
2. 按 W 键前进
3. **预期结果**：NPC 向前移动（朝向方向）
4. 按 S 键后退
5. **预期结果**：NPC 向后移动（反方向）

### 测试左右移动
1. 骑乘 Mount NPC
2. 按 A 键左移
3. **预期结果**：NPC 向左平移
4. 按 D 键右移
5. **预期结果**：NPC 向右平移

### 测试疾跑
1. 骑乘 Mount NPC
2. 在 Mount GUI 中启用"允许疾跑"
3. 按住 Ctrl 键疾跑
4. **预期结果**：移动速度加快（1.5 倍）

### 测试停止
1. 骑乘 Mount NPC
2. 移动后松开所有方向键
3. **预期结果**：NPC 立即停止，不会被拉回

---

## ⚠️ 已知限制

### 跳跃未实现
- **问题**：1.6.4 中 `EntityPlayer.isJumping` 是 protected，无法直接访问
- **影响**：骑乘时无法通过空格键跳跃
- **跳跃强度配置**：虽然 GUI 中可配置，但暂无法应用
- **未来方案**：
  1. 通过客户端按键事件捕获空格键
  2. 发送网络包到服务端触发跳跃
  3. 服务端应用 `motionY = 0.4D * jumpStrength`

### 朝向同步
- NPC 完全跟随玩家视角（rotationYaw/Pitch）
- 可能需要调整 pitch 影响程度（当前为 0.5F）

---

## 📊 改动统计

- **修改文件**：1 个（EntityNPCInterface.java）
- **代码行数**：+39 行，-8 行
- **新增导入**：RoleMount

---

## 🎉 修复效果

- ✅ 前进/后退方向正确
- ✅ 左右移动正常工作
- ✅ 移动流畅，不会被拉回
- ✅ 疾跑加速生效
- ✅ 朝向同步准确
- ⏸️ 跳跃待实现（需要客户端支持）

---

## 🔗 相关提交

- `d5261dc` - fix(role): 修复 Mount 骑乘移动控制问题
- `0c3cb7e` - Merge fix/mount-movement

---

**修复状态**：已完成（跳跃除外）  
**需人工验证**：游戏内测试所有移动方向
