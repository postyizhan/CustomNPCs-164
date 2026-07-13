# 战术AI测试文档

## 功能概述
新增三个战术 AI 行为类，可通过 `EntityNPCInterface.tasks` 添加到 NPC AI 系统。

## 实现类
- `EntityAIFollow` - 跟随目标（保持距离）
- `EntityAILeapAtTargetNpc` - 扑击目标（跳跃攻击）
- `EntityAITransform` - 生命值触发变身

## 测试步骤

### 1. EntityAIFollow（跟随AI）
**功能**：NPC 保持与目标的距离，既不太近也不太远。

**添加方式**（需代码）：
```java
EntityNPCInterface npc = ...;
npc.tasks.addTask(2, new EntityAIFollow(npc, 1.0D, 3.0F, 16.0F));
// 速度1.0，最小距离3格，最大距离16格
```

**测试**：
1. 设置 NPC 攻击性
2. 攻击 NPC 使其仇恨
3. 观察 NPC 行为

**预期结果**：
- NPC 与玩家距离 > 16格 → 不跟随
- 距离 3-16格 → 跟随玩家保持距离
- 距离 < 3格 → 停止移动（等待玩家远离）

### 2. EntityAILeapAtTargetNpc（扑击AI）
**功能**：NPC 在地面时向目标跳跃扑击，带冷却时间。

**添加方式**：
```java
npc.tasks.addTask(3, new EntityAILeapAtTargetNpc(npc, 0.4F));
// 跳跃高度 0.4（motionY）
```

**测试**：
1. 设置 NPC 攻击性
2. 保持距离 2-4格
3. NPC 在地面时触发

**预期结果**：
- NPC 距离 2-4格 且在地面 → 跳跃扑击
- 扑击后 40 tick（2秒）冷却
- 跳跃轨迹朝向目标

### 3. EntityAITransform（变身AI）
**功能**：NPC 生命值低于阈值时触发变身（基础框架）。

**添加方式**：
```java
npc.tasks.addTask(1, new EntityAITransform(npc, 0.3F));
// 生命值 ≤ 30% 触发
```

**测试**：
1. 攻击 NPC 至生命值 30% 以下
2. AI 触发变身冷却（600 tick）

**预期结果**：
- 生命值阈值触发
- 进入 10 分钟冷却
- **注意**：实际变身逻辑（模型/贴图/数据切换）需额外实现

## 已知限制
- **当前版本**：AI 类已完成，需手动通过代码添加到 `EntityNPCInterface.tasks`
- **未实现**：
  - AI 配置 GUI
  - EntityAITransform 的实际变身逻辑（需配合外部系统）

## 扩展方向
1. 创建 AI 配置 GUI（类似 RoleInterface 配置界面）
2. 在 `DataAI` 添加战术 AI 配置字段
3. 在 `EntityNPCInterface` 构造函数根据配置动态添加 AI

## AI 优先级建议
```java
// 示例：战斗型 NPC AI 配置
npc.tasks.addTask(1, new EntityAITransform(npc, 0.2F));      // 最高优先级
npc.tasks.addTask(2, new EntityAILeapAtTargetNpc(npc, 0.4F)); 
npc.tasks.addTask(3, new EntityAIFollow(npc, 1.0D, 3.0F, 16.0F));
npc.tasks.addTask(4, new EntityAIAttackOnCollide(...));      // 原版攻击AI
```

## 提交记录
- `59d73f5` - feat(ai): 战术AI三个行为类(基础版)
