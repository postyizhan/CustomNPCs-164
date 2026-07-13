# 测试指引：坐骑角色

> 分支 `feat/role-mount` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

新增 `RoleMount` 角色类型，玩家可以**右键 NPC 骑乘/下马**，利用原版 `Entity.mountEntity()` 机制实现基础骑乘交互。

本特性在 `roles/` 包新增 `RoleMount` 类，实现 `RoleInterface` 接口：
- **右键切换骑乘状态**：玩家右键 NPC → 若未骑乘则骑上 → 若已骑乘则下马。
- **跳跃强度配置**（Jump Strength）：数据字段 `jumpStrength`（0-2 倍）已完成，实际跳跃控制需监听玩家输入（留待后续）。
- **允许疾跑配置**（Allow Sprint）：数据字段 `allowSprint` 已完成，实际疾跑控制需监听玩家输入（留待后续）。
- **NBT 持久化**：配置保存在 NPC NBT 数据中。

## 改动文件

| 文件 | 改动 |
|---|---|
| `roles/RoleMount.java` | 新增角色类：`interact(EntityPlayer)` 返回 boolean，调用原版 `player.mountEntity(npc)` 或 `player.mountEntity(null)` + `jumpStrength`（默认 1.0f，范围 0-2）+ `allowSprint`（默认 true）+ `writeEntityToNBT` / `readEntityFromNBT` 实现 NBT 持久化 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] `RoleMount` 正确实现 `RoleInterface` 的三个抽象方法（`interact` / `writeEntityToNBT` / `readEntityFromNBT`）
- [x] `interact()` 返回 boolean（true 表示交互成功，false 表示被拦截）
- [x] `jumpStrength` setter 自动限制范围 0.0-2.0
- [x] 骑乘状态检查（`npc.riddenByEntity` / `player.ridingEntity`）避免多人骑乘冲突

## 需人工游戏内验证项

前置：由于当前版本 **RoleMount 无 GUI 配置界面**，需通过命令或代码设置 NPC 角色为 Mount。

### 设置方法（二选一）

#### 方法 A：通过命令（推荐）

1. `./gradlew runClient` → 创造模式 → NPC 法杖生成 NPC。
2. 记住 NPC 的 UUID 或名称。
3. 在聊天框输入命令（假设存在）：
   ```
   /npc role mount [NPC名称]
   ```
   或编辑 NPC 的 NBT 数据（F3+I 复制实体 → NBT 编辑器）。

#### 方法 B：通过代码（临时测试）

在 `EntityNPCInterface` 的构造函数或某个初始化方法临时添加：
```java
if (this.display.name.equals("TestMount")) {
    this.role = new RoleMount(this);
}
```
保存 → `./gradlew runClient` → 创建名为"TestMount"的 NPC。

---

### 1. 基础骑乘/下马

设置 NPC 角色为 Mount 后：

1. **切换到生存模式**。
2. 走到 NPC 身边，准星对准 NPC（不要持有工具）。
3. **右键 NPC**。
4. 预期：
   - 玩家视角抬高（骑在 NPC 上）。
   - 玩家模型消失（被 NPC 遮挡或原版渲染逻辑）。
   - 玩家无法移动自己的位置（WASD 控制 NPC 移动，原版机制）。
5. **再次右键 NPC**。
6. 预期：玩家下马，恢复独立移动。

### 2. 骑乘状态下的移动

玩家骑乘 NPC 时：

1. 按 **W 键**前进。
2. 预期：NPC 向玩家视角方向移动（原版 `ridingEntity` 控制逻辑）。
3. 按 **A/D 键**左右移动。
4. 预期：NPC 转向并移动（原版寻路系统响应）。
5. 按 **空格键**。
6. 预期：当前版本**无跳跃反应**（`jumpStrength` 数据已存在，但未监听玩家输入，留待后续实现）。

### 3. 多人骑乘冲突

若在多人服务器或本地开两个客户端：

1. 玩家 A 骑乘 NPC。
2. 玩家 B 走到同一 NPC 旁，右键 NPC。
3. 预期：
   - 玩家 B 右键**无反应**（`interact()` 方法检测到 `npc.riddenByEntity != null && != player`，返回 false）。
   - 控制台无错误（不尝试强制骑乘）。
4. 玩家 A 下马 → 玩家 B 再次右键。
5. 预期：玩家 B 成功骑乘。

### 4. 骑乘状态持久化（NBT 测试）

1. 玩家骑乘 NPC。
2. **不要下马**，直接 `/npc save` 保存 NPC（骑乘状态不应保存到 NBT，只有角色类型和配置）。
3. 退出世界 → 重新进入。
4. 预期：NPC 角色仍为 Mount（可以再次骑乘），但玩家**未处于骑乘状态**（骑乘状态是运行时的，不持久化）。

### 5. 配置字段测试（需代码验证）

由于当前版本无 GUI，需代码临时修改 `RoleMount` 构造函数：
```java
public RoleMount(EntityNPCInterface npc) {
    super(npc);
    this.jumpStrength = 1.5f; // 临时测试值
    this.allowSprint = false;
}
```
保存 → 重新运行 → 编辑 NPC NBT 或 `/npc save` → 退出世界 → 重新进入 → 编辑 NPC（若有 GUI）或输出 NBT。

预期：`MountJumpStrength: 1.5f` / `MountAllowSprint: false` 保存到 NBT 并正确读回。

### 6. 下马后 NPC 行为

1. 玩家骑乘 NPC，按 WASD 移动一段距离。
2. 右键下马。
3. 预期：
   - NPC **停止移动**（无玩家控制输入）。
   - NPC 恢复 AI 行为（如果配置了巡逻/攻击 AI，开始执行）。
   - NPC 不会"记忆"骑乘期间的移动指令（原版机制）。

### 7. 回归：非 Mount 角色 NPC 不受影响

1. 创建新 NPC，角色设为 Follower / Trader / None（任何非 Mount 角色）。
2. 右键 NPC。
3. 预期：原版交互（打开对话/交易界面，或无反应），**不骑乘**（`RoleMount.interact()` 未被调用）。

## 已知限制

- **当前版本**：基础骑乘交互（利用原版 `mountEntity()` 机制），玩家可以骑乘/下马，但移动控制完全依赖原版逻辑。
- **未实现**：
  - **跳跃控制**：`jumpStrength` 数据字段已完成，但需在 `EntityNPCInterface.onUpdate()` 监听骑乘玩家的跳跃键输入（`movementInput.jump`），应用 `jumpStrength` 到 `motionY`。
  - **疾跑控制**：`allowSprint` 数据字段已完成，但需监听玩家疾跑键（`isSprinting()`），若 `!allowSprint` 则强制取消疾跑。
  - **飞行坐骑**：CNPC+ 的 `flyingMountEnabled` / `hoverMode` / `flyingAscendSpeed`（需额外的飞行逻辑，当前未实现）。
  - **骑乘偏移**：CNPC+ 的 `offsetX/Y/Z` 调整玩家在 NPC 上的坐标（需覆盖 `updateRiderPosition()`）。
  - **GUI 配置界面**：当前需手动代码或命令设置角色，需创建 `GuiNpcRoleMount` 配置界面（跳跃强度滑块 + 允许疾跑复选框）。

## 扩展方向

### 1. 实现跳跃控制

在 `EntityNPCInterface.onLivingUpdate()` 或 `onUpdate()` 添加：
```java
if (this.riddenByEntity instanceof EntityPlayer && this.role instanceof RoleMount) {
    EntityPlayer rider = (EntityPlayer) this.riddenByEntity;
    RoleMount mount = (RoleMount) this.role;
    if (rider.movementInput.jump && this.onGround) {
        this.motionY = 0.42D * mount.getJumpStrength(); // 原版跳跃 motionY = 0.42
    }
}
```

### 2. 实现疾跑控制

```java
if (!mount.isAllowSprint() && rider.isSprinting()) {
    rider.setSprinting(false);
}
```

### 3. 创建 GUI 配置界面

在 `client/gui/roles/GuiNpcRoleMount.java` 新增：
- 跳跃强度滑块（0.0 - 2.0，默认 1.0）
- 允许疾跑复选框（默认 true）
- "Save"按钮调用 `role.setJumpStrength()` / `role.setAllowSprint()`

## 结果反馈

任一步骤不符合预期，请记录：哪一步、NPC 角色设置方式、实际骑乘行为、是否能复现。客户端 log 在 `runs/main/client/logs/latest.log`，骑乘相关错误可能输出 `Entity.mountEntity()` 或 `riddenByEntity` 相关日志。
