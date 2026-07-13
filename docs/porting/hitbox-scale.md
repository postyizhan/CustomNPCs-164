# 碰撞箱缩放系统测试文档

## 功能概述
允许在 Display 界面独立配置 NPC 的碰撞箱宽度和高度缩放比例（0.1-10.0倍），服务端碰撞箱立即生效。

## 实现类
- `HitboxData` - 碰撞箱缩放数据（宽高比例+启用开关）
- `DataDisplay.hitboxData` - 集成到显示数据
- `EntityNPCInterface.updateHitbox()` - 应用缩放到 width/height
- `GuiNpcDisplay` - 配置界面

## 测试步骤

### 1. 基础碰撞箱缩放
1. 右键 NPC Staff → Display 标签
2. 找到"Hitbox Scale"选项，点击按钮切换为"Enabled"
3. 输入宽度缩放（Width Scale）：`2.0`
4. 输入高度缩放（Height Scale）：`0.5`
5. 关闭界面

**预期结果**：
- NPC 碰撞箱宽度变为原来的 2 倍
- NPC 碰撞箱高度变为原来的 0.5 倍
- 可以从侧面更远距离碰撞 NPC
- NPC 可以通过更低的空间

### 2. 极限值测试
1. 输入宽度 `0.1`，高度 `0.1` → 碰撞箱缩小到最小
2. 输入宽度 `10.0`，高度 `10.0` → 碰撞箱放大到最大
3. 尝试输入超出范围的值（如 `20.0`）→ 自动限制到 `10.0`

### 3. 禁用测试
1. 将"Hitbox Scale"切换为"Disabled"
2. 关闭界面

**预期结果**：
- 碰撞箱恢复原始大小
- 配置的缩放值仍保留（重新启用时恢复）

### 4. 持久化测试
1. 配置碰撞箱缩放并启用
2. `/npc save` 保存 NPC
3. 重启世界/服务器
4. 重新加载 NPC

**预期结果**：碰撞箱缩放配置保持不变

## 已知限制
- **当前版本**：只修改服务端碰撞箱（width/height）
- **视觉缩放**：模型/渲染器的视觉缩放需另外实现（走自有渲染器）

## 语言键
```properties
# en_US.lang
display.hitbox=Hitbox Scale
display.hitboxWidth=Width Scale
display.hitboxHeight=Height Scale

# zh_CN.lang
display.hitbox=碰撞箱缩放
display.hitboxWidth=宽度缩放
display.hitboxHeight=高度缩放
```

## 提交记录
- `092963f` - feat(display): 碰撞箱缩放系统(服务端)
