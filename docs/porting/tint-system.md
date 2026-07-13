# 色调系统测试文档

## 功能概述
允许在 Display 界面配置 NPC 的受伤闪烁颜色和持久色调，渲染器应用到 GL 颜色系统。

## 实现类
- `TintData` - 色调数据（受伤色调+持久色调+透明度）
- `DataDisplay.tintData` - 集成到显示数据
- `RenderNPCInterface.renderPlayerScale()` - 应用色调到 GL 颜色
- `GuiNpcDisplay` - 配置界面

## 测试步骤

### 1. 受伤闪烁颜色
1. 右键 NPC Staff → Display 标签
2. 找到"Tint System"，点击切换为"Enabled"
3. "Hurt Tint"保持"Enabled"
4. 输入受伤闪烁颜色（默认 `ff0000` 红色），尝试：
   - `00ff00` - 绿色闪烁
   - `0000ff` - 蓝色闪烁
   - `ffff00` - 黄色闪烁
5. 关闭界面，攻击 NPC

**预期结果**：
- NPC 受到伤害时闪烁配置的颜色（而非原版红色）
- 闪烁持续时间与原版一致

### 2. 禁用受伤闪烁
1. 将"Hurt Tint"切换为"Disabled"
2. 攻击 NPC

**预期结果**：
- NPC 受伤不再闪烁
- 保持原始皮肤颜色

### 3. 系统禁用测试
1. 将"Tint System"切换为"Disabled"
2. 攻击 NPC

**预期结果**：
- 恢复原版受伤闪烁（红色）
- 配置值仍保留

### 4. 颜色输入框
- 颜色选择框显示当前颜色
- 输入不合法颜色码自动重置为默认值

## 已知限制
- **当前版本**：只实现受伤闪烁颜色
- **持久色调**：generalTint 数据结构已完成，GUI 配置留待后续扩展

## 语言键
```properties
# en_US.lang
display.tintSystem=Tint System
display.hurtTint=Hurt Tint

# zh_CN.lang
display.tintSystem=色调系统
display.hurtTint=受伤闪烁
```

## 提交记录
- `c191d65` - feat(display): 色调系统-受伤闪烁与持久色调
