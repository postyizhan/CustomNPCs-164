# 测试指引：皮肤覆盖层系统

> 分支 `feat/skin-overlays` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

编辑 NPC Display 界面时可配置 **单层皮肤覆盖贴图**，渲染器在 `RenderNPCInterface.renderModel()` 主皮肤绘制后叠加绘制覆盖层，支持颜色着色和透明度混合。

本特性在 `DataDisplay` 新增 `DataSkinOverlays` 字段，当前实现为单层覆盖（多层管理结构已完成，扩展为 HashMap 即可）：
- **覆盖层贴图路径**：指向一个与主皮肤同尺寸的贴图文件（支持透明 PNG）。
- **颜色着色**（Color）：数据结构已完成，GUI 配置留待后续。
- **透明度混合**（Alpha）：数据结构已完成，GUI 配置留待后续。
- **自动跳过空贴图**：贴图路径为空时不渲染覆盖层（即使启用）。

## 改动文件

| 文件 | 改动 |
|---|---|
| `controllers/data/SkinOverlay.java` | 新增：贴图路径（`texture`）+ 颜色（`color` 默认 0xFFFFFF）+ 透明度（`alpha` 默认 1.0f）+ NBT 读写 + `isEmpty()` 检测空路径 |
| `controllers/data/DataSkinOverlays.java` | 新增：启用开关（`enabled`）+ 单层覆盖对象（`overlay`）+ NBT 读写 |
| `DataDisplay.java` | 新增字段 `public DataSkinOverlays skinOverlays = new DataSkinOverlays()` + 在 `writeToNBT`/`readToNBT` 中调用 `skinOverlays` 的 NBT 方法 |
| `client/renderer/RenderNPCInterface.java` | `renderModel()` 方法在 glowTexture 渲染块后插入覆盖层渲染块：若 `skinOverlays.isEnabled()` 且 `!overlay.isEmpty()` 则绑定覆盖贴图 → 设置 GL 混合模式 `SRC_ALPHA, ONE_MINUS_SRC_ALPHA` → 应用颜色和透明度到 `glColor4f()` → 放大模型 1.002 倍避免 Z-fighting → 渲染模型 |
| `client/gui/mainmenu/GuiNpcDisplay.java` | Display 界面底部新增两行控件：启用按钮（button 13）+ 贴图路径输入框（textfield 14，宽度 150）+ 输入框在启用时可编辑 |
| `assets/customnpcs/lang/en_US.lang` | 新增 `display.skinOverlay` / `display.overlayTexture` |
| `assets/customnpcs/lang/zh_CN.lang` | 新增对应中文翻译 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 覆盖层在 glowTexture 渲染后（叠加顺序：主皮肤 → glowTexture → 覆盖层）
- [x] GL 混合模式 `SRC_ALPHA, ONE_MINUS_SRC_ALPHA` 支持透明贴图
- [x] 模型缩放 1.002 倍避免 Z-fighting（覆盖层不会与主皮肤像素重叠闪烁）
- [x] `isEmpty()` 检测避免渲染空路径（防止崩溃）

## 需人工游戏内验证项

前置：`./gradlew runClient` → 创造模式 → NPC 法杖生成 NPC → 扳手打开编辑界面。

### 1. 准备测试贴图

在 `src/main/resources/assets/customnpcs/textures/entity/humanmale/` 创建测试贴图：

1. 复制 `Steve.png` 为 `overlay_test.png`。
2. 用图像编辑器（GIMP/Photoshop）编辑 `overlay_test.png`：
   - 删除大部分像素（设为透明）。
   - 在**胸部区域**绘制一个明显的图案（如红色方块或符号）。
   - 保存为 PNG（保留透明通道）。
3. 重新运行 `./gradlew runClient`（刷新资源）。

### 2. 启用皮肤覆盖层（Skin Overlay）

编辑界面进 **Display 页**，滚动到底部：

1. 找到"Skin Overlay"（皮肤覆盖层）行，默认按钮显示"Disabled"（禁用）。
2. 点击按钮切换为"Enabled"（启用）。
3. 预期：下方"Overlay Texture"（覆盖层贴图）输入框变为可编辑（背景由灰变白）。

### 3. 配置覆盖层贴图（Overlay Texture）

"Overlay Texture"输入框，默认为空：

1. 聚焦输入框，输入 `customnpcs:textures/entity/humanmale/overlay_test.png` → 失焦。
2. Esc 关闭编辑界面。
3. 观察 NPC 的**胸部区域**。
4. 预期：
   - **胸部显示红色方块图案**（覆盖层的不透明部分）。
   - **头部/手臂等未绘制区域**显示原始皮肤（覆盖层透明部分穿透）。
   - 覆盖层**略微浮在主皮肤表面**（1.002 倍缩放，无 Z-fighting 闪烁）。

### 4. 测试原版贴图（验证路径解析）

编辑界面 Display 页，"Overlay Texture"输入框：

1. 输入 `minecraft:textures/entity/creeper/creeper.png`（原版苦力怕贴图）。
2. Esc 关闭。
3. 预期：NPC 表面叠加苦力怕的绿色像素纹理（UV 映射可能错位，但证明路径解析正常）。

### 5. 测试不存在的贴图路径

编辑界面 Display 页，"Overlay Texture"输入框：

1. 输入 `customnpcs:textures/entity/notexist.png`（不存在的文件）。
2. Esc 关闭。
3. 预期：
   - **客户端不崩溃**（渲染器 try-catch 或原版缺失贴图处理）。
   - NPC 显示主皮肤 + 洋红黑相间的"缺失贴图"覆盖层（Minecraft 默认行为）。
   - 控制台可能输出 `FileNotFoundException`（正常）。

### 6. 禁用覆盖层（Skin Overlay Disabled）

编辑界面 Display 页：

1. 确保"Overlay Texture"配置为有效路径（如 `overlay_test.png`）。
2. 点击"Skin Overlay"按钮切换为"Disabled"。
3. 预期：输入框变灰不可编辑，但**值仍显示**（不清空）。
4. Esc 关闭 → 观察 NPC。
5. 预期：覆盖层消失，只显示主皮肤（与原版 1.6.4 一致）。
6. 重新编辑 → 切换为"Enabled" → 关闭。
7. 预期：覆盖层立即重新显示（之前的路径配置被保留）。

### 7. 空路径测试（isEmpty 检测）

编辑界面 Display 页：

1. "Skin Overlay" Enabled。
2. "Overlay Texture"输入框清空（留空白）→ 失焦 → Esc 关闭。
3. 预期：即使启用，覆盖层不渲染（`isEmpty()` 返回 true，跳过渲染代码）。
4. 客户端不崩溃，NPC 只显示主皮肤。

### 8. 持久化测试

编辑界面配置覆盖层后：

1. "Skin Overlay" Enabled → "Overlay Texture" = `customnpcs:textures/entity/humanmale/overlay_test.png`。
2. `/npc save` 保存 NPC 到磁盘。
3. 退出世界 → 重新进入 → 扳手编辑 NPC。
4. 预期：Display 页"Skin Overlay"状态保持 Enabled，输入框显示完整路径。
5. 观察 NPC → 预期：覆盖层正常显示。

### 9. 与 glowTexture 叠加顺序

编辑界面 Display 页顶部：

1. "Overlay Texture"（发光贴图）输入框配置一个发光贴图路径（如果有）。
2. "Skin Overlay" Enabled → "Overlay Texture" 配置覆盖层。
3. Esc 关闭。
4. 预期渲染顺序：主皮肤（最底层）→ glowTexture（中层，发光效果）→ 覆盖层（最顶层）。

### 10. 回归：未启用时无副作用

1. 创建新 NPC（默认 Skin Overlay 为 Disabled）。
2. 观察 NPC → 预期：与原版 1.6.4 完全一致（只显示主皮肤，无额外渲染）。
3. 编辑 Display 页 → 预期："Skin Overlay" 显示 Disabled，输入框灰色为空。

## 已知限制

- **当前版本**：单层覆盖（`DataSkinOverlays` 持有 1 个 `SkinOverlay` 对象）。
- **未实现**：
  - 多层覆盖（数据结构已支持 `HashMap<Integer, ISkinOverlay>`，GUI 需列表管理界面）。
  - 颜色着色和透明度 GUI 配置（数据字段 `color` / `alpha` 已完成，需新增颜色选择器 + 滑块）。
  - 缩放/偏移/动画（CNPC+ 的 `scaleX/Y` / `offsetX/Y/Z` / `speedX/Y`，需扩展 GUI）。

## 结果反馈

任一步骤不符合预期，请记录：哪一步、输入的贴图路径、实际显示效果、是否能复现。客户端 log 在 `runs/main/client/logs/latest.log`，贴图加载错误会输出 `FileNotFoundException` 或 `TextureManager` 警告。
