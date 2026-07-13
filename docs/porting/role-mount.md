# 坐骑角色测试文档

## 功能概述
新增 RoleMount 角色类型，允许玩家右键 NPC 骑乘/下马，支持跳跃强度和疾跑配置。

## 实现类
- `RoleMount` - 坐骑角色类
  - `interact(EntityPlayer)` - 右键切换骑乘状态
  - `jumpStrength` - 跳跃强度（0-2倍）
  - `allowSprint` - 是否允许疾跑

## 测试步骤

### 1. 设置坐骑角色
1. 右键 NPC Staff → Role 标签
2. 选择角色类型为"Mount"（需通过代码或命令设置）
3. 配置跳跃强度和疾跑选项（当前需代码配置）

### 2. 骑乘测试
1. 右键 NPC
2. 玩家应骑在 NPC 上
3. 使用 WASD 移动
4. 空格跳跃
5. 再次右键 NPC 下马

**预期结果**：
- 右键切换骑乘状态
- 玩家可控制 NPC 移动方向
- NPC 利用原版寻路系统移动
- 下马后 NPC 停止移动

### 3. 多人测试
1. 玩家 A 骑乘 NPC
2. 玩家 B 尝试右键同一 NPC

**预期结果**：
- 玩家 B 无法骑乘（已被占用）
- 只有当前骑乘者可以下马

### 4. 持久化测试
1. 设置坐骑角色配置
2. `/npc save` 保存
3. 重启世界
4. 重新加载 NPC

**预期结果**：
- 跳跃强度和疾跑配置保持不变
- 仍可正常骑乘

## 已知限制
- **当前版本**：基础骑乘交互（利用原版 `mountEntity` 机制）
- **未实现**：
  - 跳跃控制（jumpStrength 数据已存在，需监听玩家输入）
  - 飞行坐骑
  - 坐骑 GUI 配置界面
  - 骑乘偏移/姿态调整

## 扩展方向
1. 在 `EntityNPCInterface.onUpdate()` 监听 `riddenByEntity` 玩家输入
2. 检测跳跃键按下，应用 `jumpStrength` 到 `motionY`
3. 创建 `GuiNpcRoleMount` 配置界面

## NBT 数据
```nbt
{
  MountJumpStrength: 1.0f,  // 0.0-2.0
  MountAllowSprint: true
}
```

## 提交记录
- `1520a17` - feat(role): 坐骑角色RoleMount(简化版)
