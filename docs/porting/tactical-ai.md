# 测试指引：战术 AI

> 分支 `feat/tactical-ai` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

新增三个战术 AI 行为类，可通过 `EntityNPCInterface.tasks` 添加到 NPC AI 系统，扩展 NPC 的战斗与生存策略：
- **EntityAIFollow**：跟随目标保持特定距离（远了靠近，近了后退）。
- **EntityAILeapAtTargetNpc**：跳跃扑击目标（狼/蜘蛛式攻击）。
- **EntityAITransform**：生命值低于阈值时触发变身（基础框架，实际变身逻辑需额外实现）。

本特性在 `entity/` 包新增三个 AI 类，均继承原版 `EntityAIBase`，可与原版 AI（`EntityAIAttackOnCollide` / `EntityAIWatchClosest` 等）组合使用。

## 改动文件

| 文件 | 改动 |
|---|---|
| `entity/EntityAIFollow.java` | 新增 AI 类：构造函数接收 `speed`（移动速度倍率）+ `minDist`（最小保持距离）+ `maxDist`（最大跟随距离）→ `shouldExecute()` 检测目标距离在范围内 → `updateTask()` 调用 `Navigator.tryMoveToEntityLiving()` 跟随目标 + `LookHelper` 注视目标 → MutexBits 3（与其他移动 AI 互斥） |
| `entity/EntityAILeapAtTargetNpc.java` | 新增 AI 类：构造函数接收 `leapMotionY`（跳跃高度）→ `shouldExecute()` 检测 NPC 在地面 + 目标距离 4-16 格 + 冷却结束 → `startExecuting()` 计算水平速度并设置 `motionX/Y/Z` → 冷却 40 tick（2秒）→ MutexBits 5（与跳跃相关 AI 互斥） |
| `entity/EntityAITransform.java` | 新增 AI 类：构造函数接收 `healthThreshold`（生命值阈值，如 0.3 表示 30%）→ `shouldExecute()` 检测 `getHealth() / getMaxHealth() <= threshold` + 冷却结束 → `startExecuting()` 触发冷却 600 tick（30秒）→ MutexBits 0（不与任何 AI 互斥）→ TODO 注释：实际变身逻辑需配合外部系统 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 三个 AI 类均正确继承 `EntityAIBase` 并实现必要方法（`shouldExecute` / `continueExecuting` / `startExecuting` / `updateTask`）
- [x] MutexBits 设置合理（避免 AI 冲突）
- [x] 距离计算使用 `getDistanceSqToEntity()`（平方距离，避免开方性能开销）
- [x] 跳跃冷却机制（`leapCooldown--` 在 `shouldExecute()` 中递减）

## 需人工游戏内验证项

前置：由于当前版本 **AI 类无 GUI 配置界面**，需通过代码手动添加 AI 到 `EntityNPCInterface.tasks`。

### 添加 AI 的方法

在 `EntityNPCInterface` 的构造函数或 `setEntityStats()` 方法临时添加测试代码：

```java
// 在构造函数末尾或 initEntityAI() 方法中
if (this.display.name.equals("FollowTest")) {
    this.tasks.addTask(2, new EntityAIFollow(this, 1.0D, 3.0F, 16.0F));
}
if (this.display.name.equals("LeapTest")) {
    this.tasks.addTask(3, new EntityAILeapAtTargetNpc(this, 0.4F));
}
if (this.display.name.equals("TransformTest")) {
    this.tasks.addTask(1, new EntityAITransform(this, 0.3F));
}
```

保存 → `./gradlew runClient` → 创建对应名称的 NPC。

---

### 1. EntityAIFollow（跟随 AI）

创建名为"FollowTest"的 NPC（已添加 `EntityAIFollow(speed=1.0, min=3.0, max=16.0)`）：

1. 编辑 NPC → Stats 页设置攻击性（Aggressive）→ 关闭。
2. **切换到生存模式**，用剑攻击 NPC 使其仇恨（红眼）。
3. **快速跑离 NPC 超过 16 格**。
4. 预期：NPC **开始跟随**玩家（调用 Navigator 寻路）。
5. **停下不动，让 NPC 接近到 3 格以内**。
6. 预期：NPC **停止移动**（距离 < minDist，`shouldExecute()` 返回 false），保持 3 格距离。
7. **缓慢后退到 5 格左右**。
8. 预期：NPC **继续跟随**，始终保持在 3-16 格范围内（不会贴身也不会远离）。

### 2. EntityAILeapAtTargetNpc（扑击 AI）

创建名为"LeapTest"的 NPC（已添加 `EntityAILeapAtTargetNpc(leapMotionY=0.4)`）：

1. 编辑 NPC → Stats 页设置攻击性（Aggressive）+ 速度适中。
2. 生存模式，攻击 NPC 使其仇恨。
3. **保持距离 2-4 格**，在平地上（NPC 脚下为固体方块）。
4. 预期：
   - NPC **跳跃扑向**玩家（`motionY = 0.4`，水平速度朝向玩家）。
   - 跳跃轨迹呈抛物线，落地后继续普通移动。
5. **立即再次保持 2-4 格距离**。
6. 预期：NPC **不立即跳跃**（冷却 40 tick = 2 秒），等待 2 秒后再次扑击。
7. **距离拉开到 10 格**。
8. 预期：NPC **不扑击**（`shouldExecute()` 检测距离 > 16 格，返回 false），只会普通追逐。

### 3. EntityAITransform（变身 AI）

创建名为"TransformTest"的 NPC（已添加 `EntityAITransform(threshold=0.3)`）：

1. 编辑 NPC → Stats 页设置较高生命值（如 Max Health = 100）+ 攻击性。
2. 生存模式，用剑攻击 NPC。
3. **持续攻击直到生命值降至 30 以下**（30% 阈值）。
4. 预期：
   - AI 触发（`shouldExecute()` 检测到生命值 ≤ 30%）。
   - `startExecuting()` 被调用，冷却设为 600 tick（30 秒）。
   - **当前版本无视觉变化**（TODO 注释：实际变身逻辑未实现，需配合外部系统更换模型/贴图/数据）。
5. **继续攻击 NPC 至濒死**。
6. 预期：AI **不再重复触发**（冷却 30 秒内，`shouldExecute()` 返回 false）。

### 4. AI 优先级与互斥（MutexBits）

创建一个 NPC 同时添加多个 AI：
```java
this.tasks.addTask(1, new EntityAITransform(this, 0.3F));        // 优先级 1（最高）
this.tasks.addTask(2, new EntityAIFollow(this, 1.0D, 3.0F, 16.0F)); // 优先级 2
this.tasks.addTask(3, new EntityAILeapAtTargetNpc(this, 0.4F)); // 优先级 3
this.tasks.addTask(4, new EntityAIAttackOnCollide(...));         // 优先级 4
```

攻击 NPC 使其仇恨，观察行为：

1. 生命值 > 30% 时：
   - 预期：跟随 AI（优先级 2）+ 扑击 AI（优先级 3）+ 近战 AI（优先级 4）轮流执行。
   - 距离 3-16 格时跟随，2-4 格时尝试扑击（冷却允许），贴身时普通攻击。
2. 生命值 ≤ 30% 时：
   - 预期：变身 AI 触发（优先级 1 最高），但因 MutexBits 0（不互斥）不影响其他 AI，跟随/扑击/攻击仍正常执行。

### 5. 跳跃扑击的地形限制

创建"LeapTest"NPC，在以下地形测试：

1. **NPC 在空中**（踩空或跳跃中）：
   - 攻击 NPC 使其仇恨 → 保持 2-4 格。
   - 预期：NPC **不扑击**（`shouldExecute()` 检测 `!npc.onGround`，返回 false）。
2. **NPC 在水中**：
   - 预期：NPC **不扑击**（水中 `onGround = false`，AI 不触发）。
3. **NPC 在平地但周围有障碍**：
   - 预期：NPC 扑击，但轨迹可能撞到方块（原版物理，正常现象）。

### 6. 变身 AI 冷却重置（NBT 测试）

1. 触发"TransformTest"NPC 的变身 AI（生命值降至 30%）。
2. **不要让 NPC 恢复生命**，立即 `/npc save` 保存。
3. 退出世界 → 重新进入。
4. 预期：
   - NPC 生命值保持（NBT 持久化）。
   - **变身 AI 冷却未保存**（`transformCooldown` 是运行时字段，重启后重置为 0）。
   - 若生命值仍 ≤ 30%，AI **立即再次触发**。

### 7. 回归：未添加 AI 的 NPC 不受影响

1. 创建新 NPC，**不添加**三个战术 AI（`tasks` 列表只有原版 AI）。
2. 设置攻击性，生存模式攻击。
3. 预期：NPC 行为与原版 1.6.4 完全一致（只有原版追逐/攻击 AI，无跟随/扑击/变身行为）。

## 已知限制

- **当前版本**：AI 类已完成，但需手动代码添加到 `tasks`，无 GUI 配置界面。
- **未实现**：
  - **AI 配置 GUI**：需创建类似 `GuiNpcAI` 的界面，添加"战术 AI"选项卡，配置跟随距离/扑击高度/变身阈值。
  - **EntityAITransform 的实际变身逻辑**：当前只触发冷却，实际需调用外部系统（如切换 `display.texture` / `display.modelType` / 修改 `stats.maxHealth` 等）。
  - **DataAI 集成**：需在 `DataAI` 类添加字段（`followEnabled` / `leapEnabled` / `transformEnabled` + 对应参数），在 `EntityNPCInterface` 构造时根据配置动态添加 AI。

## 扩展方向

### 1. 集成到 DataAI

在 `DataAI.java` 添加：
```java
public boolean followEnabled = false;
public double followSpeed = 1.0D;
public float followMinDist = 3.0F;
public float followMaxDist = 16.0F;

public boolean leapEnabled = false;
public float leapHeight = 0.4F;

public boolean transformEnabled = false;
public float transformThreshold = 0.3F;
```

在 `EntityNPCInterface` 构造函数：
```java
if (this.ais.followEnabled) {
    this.tasks.addTask(2, new EntityAIFollow(this, ais.followSpeed, ais.followMinDist, ais.followMaxDist));
}
// 同理添加其他 AI
```

### 2. 创建 AI 配置 GUI

在 `client/gui/mainmenu/GuiNpcAI.java` 新增"战术 AI"按钮 → 打开子界面 `SubGuiNpcTacticalAI.java`：
- 跟随 AI：启用复选框 + 速度滑块 + 最小/最大距离输入框
- 扑击 AI：启用复选框 + 跳跃高度滑块
- 变身 AI：启用复选框 + 生命值阈值滑块（0-100%）

### 3. 实现 EntityAITransform 的变身逻辑

在 `startExecuting()` 添加：
```java
// 示例：切换为"变身形态"贴图
npc.display.texture = "customnpcs:textures/entity/humanmale/transformed.png";
npc.textureLocation = null; // 强制重新加载贴图

// 示例：提升生命上限
npc.stats.setMaxHealth(npc.stats.maxHealth * 1.5f);
npc.setHealth(npc.getMaxHealth()); // 满血复活

// 示例：切换模型
npc.display.modelType = EnumModelType.Dragon;
npc.updateHitbox(); // 重新计算碰撞箱

npc.updateClient = true; // 通知客户端同步
```

## 结果反馈

任一步骤不符合预期，请记录：哪一步、NPC 配置（生命值/距离/AI 优先级）、实际行为、是否能复现。客户端 log 在 `runs/main/client/logs/latest.log`，AI 相关错误可能输出 `EntityAITasks` 或 `PathNavigate` 日志。
