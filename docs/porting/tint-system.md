# 测试指引：色调系统

> 分支 `feat/tint-system` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

编辑 NPC Display 界面时可配置 **受伤闪烁颜色**（原版固定红色），渲染器在 `RenderNPCInterface.renderPlayerScale()` 应用色调到 GL 颜色系统。

本特性在 `DataDisplay` 新增 `TintData` 字段，支持：
- **受伤闪烁色调**（Hurt Tint）：NPC 受到伤害时闪烁的颜色，默认 `ff0000` 红色，可配置为绿色/蓝色/黄色等。
- **持久色调**（General Tint）：数据结构已完成（`generalTint` + `generalAlpha`），GUI 配置留待后续扩展。
- **启用开关**：可以临时禁用色调系统而不丢失配置值。

## 改动文件

| 文件 | 改动 |
|---|---|
| `controllers/data/TintData.java` | 新增：受伤色调（`hurtTint` 默认 0xff0000）+ 持久色调（`generalTint` + `generalAlpha`）+ 启用开关（`tintEnabled` / `hurtTintEnabled` / `generalTintEnabled`）+ NBT 读写 |
| `DataDisplay.java` | 新增字段 `public TintData tintData = new TintData()` + 在 `writeToNBT`/`readToNBT` 中调用 `tintData` 的 NBT 方法 |
| `client/renderer/RenderNPCInterface.java` | `renderPlayerScale()` 方法在计算 RGB 后插入色调逻辑：若 `tintData.isTintEnabled()` 且 NPC 受伤（`hurtTime > 0 || deathTime > 0`）且 `hurtTintEnabled` 则用 `hurtTint` 颜色替换 RGB，否则若 `generalTintEnabled` 则混合 `generalTint` |
| `client/gui/mainmenu/GuiNpcDisplay.java` | Display 界面底部新增两行控件：系统启用按钮（button 11）+ 受伤色调启用按钮（button 12）+ 颜色输入框（textfield 13，十六进制）+ 输入框 `setTextColor()` 实时显示颜色 |
| `assets/customnpcs/lang/en_US.lang` | 新增 `display.tintSystem` / `display.hurtTint` |
| `assets/customnpcs/lang/zh_CN.lang` | 新增对应中文翻译 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 色调应用在 `renderPlayerScale()` 的 `glColor3f()` 调用前（不影响模型缩放）
- [x] 十六进制颜色输入框的 `setTextColor()` 方法使输入框文本颜色与配置值一致
- [x] 受伤闪烁优先级高于持久色调（if-else 分支）

## 需人工游戏内验证项

前置：`./gradlew runClient` → 创造模式 → NPC 法杖生成 NPC → 扳手打开编辑界面。

### 1. 启用色调系统（Tint System）

编辑界面进 **Display 页**，滚动到底部：

1. 找到"Tint System"（色调系统）行，默认按钮显示"Disabled"（禁用）。
2. 点击按钮切换为"Enabled"（启用）。
3. 预期：下方"Hurt Tint"（受伤闪烁）行的输入框变为可编辑。

### 2. 配置受伤闪烁颜色（Hurt Tint）

"Hurt Tint"行，默认按钮"Enabled"，输入框显示 `ff0000`（红色）：

1. 聚焦输入框，清空后输入 `00ff00`（绿色）→ 失焦。
2. 预期：输入框**文本颜色变为绿色**（实时反馈）。
3. Esc 关闭编辑界面。
4. **切换到生存模式**，用剑攻击 NPC。
5. 预期：NPC 受到伤害时闪烁**绿色**（而非原版红色），闪烁持续时间与原版一致（约 0.5 秒）。

### 3. 测试其他颜色

扳手重新编辑 → Display 页，依次测试：

| 输入值 | 颜色 | 预期效果 |
|--------|------|----------|
| `0000ff` | 蓝色 | 受伤闪烁蓝色 |
| `ffff00` | 黄色 | 受伤闪烁黄色 |
| `ff00ff` | 洋红 | 受伤闪烁洋红 |
| `ffffff` | 白色 | 受伤闪烁白色（高亮） |
| `000000` | 黑色 | 受伤闪烁黑色（暗影） |

每次修改后关闭界面 → 生存模式攻击 NPC 验证。

### 4. 禁用受伤闪烁（Hurt Tint Disabled）

编辑界面 Display 页：

1. 确保"Tint System"为 Enabled。
2. 点击"Hurt Tint"按钮切换为"Disabled"（禁用）。
3. 预期：颜色输入框**仍可编辑**（不变灰），但值保留。
4. Esc 关闭 → 生存模式攻击 NPC。
5. 预期：NPC 受伤时**不再闪烁任何颜色**（保持原始皮肤颜色，无红色无配置色）。

### 5. 禁用整个系统（Tint System Disabled）

编辑界面 Display 页：

1. 确保 Hurt Tint 配置为非默认颜色（如 `00ff00` 绿色）。
2. 点击"Tint System"按钮切换为"Disabled"。
3. 预期：下方"Hurt Tint"行的输入框变灰不可编辑，但**值仍显示**。
4. Esc 关闭 → 生存模式攻击 NPC。
5. 预期：NPC 受伤时闪烁**原版红色**（系统禁用，回退到原版行为）。
6. 重新编辑 → 切换"Tint System"为"Enabled" → 关闭。
7. 预期：受伤闪烁立即恢复为配置的绿色（之前的 `00ff00` 配置被保留）。

### 6. 颜色输入框错误处理

编辑界面 Display 页，"Hurt Tint"输入框：

1. 输入非法值 `gggggg`（字母超出十六进制范围）→ 失焦。
2. 预期：输入框自动重置为默认值 `ff0000`，文本颜色变为红色。
3. 输入不足 6 位 `ff` → 失焦。
4. 预期：前补零为 `0000ff`（蓝色），或按实际 `NumberFormatException` 处理逻辑重置。

### 7. 持久化测试

编辑界面配置色调后：

1. "Tint System" Enabled → "Hurt Tint" Enabled + `0000ff`（蓝色）。
2. `/npc save` 保存 NPC 到磁盘。
3. 退出世界 → 重新进入 → 扳手编辑 NPC。
4. 预期：Display 页底部"Tint System" / "Hurt Tint"状态保持，颜色输入框显示 `0000ff`。
5. 生存模式攻击 NPC → 预期：受伤闪烁蓝色。

### 8. 回归：未启用时无副作用

1. 创建新 NPC（默认 Tint System 为 Disabled）。
2. 生存模式攻击 → 预期：受伤闪烁原版红色（与原版 1.6.4 完全一致）。
3. 编辑 Display 页 → 预期："Tint System" 显示 Disabled，"Hurt Tint"输入框灰色显示 `ff0000`。

## 已知限制

- **当前版本**：只实现受伤闪烁颜色配置（`hurtTint` + `hurtTintEnabled`）。
- **未实现**：持久色调（General Tint）的 GUI 配置界面，数据结构 `generalTint` / `generalAlpha` / `generalTintEnabled` 已完成，留待后续扩展（需新增颜色输入框 + 透明度滑块）。

## 结果反馈

任一步骤不符合预期，请记录：哪一步、输入的颜色码、实际闪烁颜色、是否能复现。客户端 log 在 `runs/main/client/logs/latest.log`。
