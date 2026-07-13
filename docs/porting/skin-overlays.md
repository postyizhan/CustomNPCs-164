# 皮肤覆盖层系统测试文档

## 功能概述
允许在 Display 界面配置单层皮肤覆盖贴图，支持颜色着色和透明度混合，渲染器叠加绘制在主皮肤之上。

## 实现类
- `SkinOverlay` - 单层覆盖数据（贴图路径+颜色+透明度）
- `DataSkinOverlays` - 覆盖层管理（当前单层）
- `DataDisplay.skinOverlays` - 集成到显示数据
- `RenderNPCInterface.renderModel()` - 渲染覆盖层
- `GuiNpcDisplay` - 配置界面

## 测试步骤

### 1. 基础覆盖层
1. 准备测试贴图（与 NPC 主皮肤同尺寸）
2. 右键 NPC Staff → Display 标签
3. 找到"Skin Overlay"，点击切换为"Enabled"
4. 输入"Overlay Texture"路径，例如：
   - `customnpcs:textures/entity/humanmale/overlay_test.png`
   - 或使用原版贴图路径测试
5. 关闭界面

**预期结果**：
- 覆盖层贴图叠加绘制在主皮肤之上
- 覆盖层略微放大（1.002倍）避免 Z-fighting
- 透明部分显示底层皮肤

### 2. 颜色和透明度（需代码扩展）
当前版本数据结构已完成，GUI 配置待扩展：
- `overlay.setColor(0xff0000)` - 红色着色
- `overlay.setAlpha(0.5f)` - 50% 透明度

### 3. 禁用测试
1. 将"Skin Overlay"切换为"Disabled"
2. 关闭界面

**预期结果**：
- 覆盖层不再渲染
- 配置的贴图路径仍保留

### 4. 空贴图路径
1. 清空"Overlay Texture"输入框
2. 关闭界面

**预期结果**：
- 即使启用，也不渲染覆盖层（isEmpty 检测）

## 已知限制
- **当前版本**：单层覆盖（多层管理结构已完成，扩展为 HashMap 即可）
- **GUI 简化**：只配置贴图路径，颜色/透明度/缩放/偏移需手动代码设置

## 语言键
```properties
# en_US.lang
display.skinOverlay=Skin Overlay
display.overlayTexture=Overlay Texture

# zh_CN.lang
display.skinOverlay=皮肤覆盖层
display.overlayTexture=覆盖层贴图
```

## 提交记录
- `91809ea` - feat(display): 皮肤覆盖层系统(单层简化版)
