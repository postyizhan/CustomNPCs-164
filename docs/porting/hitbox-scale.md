# 测试指引：碰撞箱缩放系统

> 分支 `feat/hitbox-scale` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

编辑 NPC Display 界面时可独立配置碰撞箱（hitbox）的宽度和高度缩放比例（0.1-10.0倍），**服务端碰撞箱立即生效**，玩家可以从不同距离与 NPC 碰撞、NPC 可以通过不同高度的空间。

本特性在 `DataDisplay` 新增 `HitboxData` 字段，在 `EntityNPCInterface.updateHitbox()` 应用缩放到 `width`/`height`：
- **独立于模型大小**：碰撞箱缩放与 Display 界面的"Size"（模型大小）独立计算，可以做超大碰撞箱+小模型或反之。
- **启用开关**：可以临时禁用碰撞箱缩放而不丢失配置值。
- **NBT 持久化**：缩放配置保存在 NPC NBT 数据中，重启世界/服务器后保持。

## 改动文件

| 文件 | 改动 |
|---|---|
| `controllers/data/HitboxData.java` | 新增：宽高缩放比例（`widthScale`/`heightScale` 默认1.0）+ 启用开关（`hitboxEnabled` 默认false）+ NBT 读写 + setter 自动限制范围 0.1-10.0 |
| `DataDisplay.java` | 新增字段 `public HitboxData hitboxData = new HitboxData()` + 在 `writeToNBT`/`readToNBT` 中调用 `hitboxData` 的 NBT 方法 |
| `EntityNPCInterface.java` | `updateHitbox()` 方法最后新增：`if (display.hitboxData.isHitboxEnabled())` 时应用宽高缩放到 `width`/`height` |
| `client/gui/mainmenu/GuiNpcDisplay.java` | Display 界面底部新增三行控件：启用按钮（button 10）+ 宽度输入框（textfield 10）+ 高度输入框（textfield 11）+ 按钮事件调用 `initGui()` 刷新输入框状态 |
| `assets/customnpcs/lang/en_US.lang` | 新增 `display.hitbox` / `display.hitboxWidth` / `display.hitboxHeight` |
| `assets/customnpcs/lang/zh_CN.lang` | 新增对应中文翻译 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 碰撞箱缩放应用在 `updateHitbox()` 最后（不影响姿态/动画的基础计算）
- [x] 输入框 `setEnabled()` 在启用时才可编辑
- [x] setter 方法限制范围 0.1-10.0（输入 20.0 会被裁剪到 10.0）

## 需人工游戏内验证项

前置：`./gradlew runClient` → 创造模式 → NPC 法杖生成 NPC → 扳手打开编辑界面。

### 1. 启用碰撞箱缩放（Hitbox Scale）

编辑界面进 **Display 页**，滚动到底部：

1. 找到"Hitbox Scale"（碰撞箱缩放）行，默认按钮显示"Disabled"（禁用）。
2. 点击按钮切换为"Enabled"（启用）。
3. 预期：下方两行输入框变为可编辑（背景由灰变白）。

### 2. 配置宽度缩放（Width Scale）

"Width Scale"（宽度缩放）输入框，默认 `1.00`：

1. 聚焦输入框，输入 `2.0` → 失焦。
2. Esc 关闭编辑界面。
3. **切换到生存模式**，走到 NPC 侧面。
4. 预期：在 **距离 NPC 约 1.2 格**（原版 0.6格 × 2倍）时即可碰撞到 NPC（无法穿过），推动 NPC 时碰撞箱横向范围变宽。
5. 扳手重新打开编辑 → Display 页 → 预期：Width Scale 保持 `2.00`。

### 3. 配置高度缩放（Height Scale）

"Height Scale"（高度缩放）输入框，默认 `1.00`：

1. 输入 `0.5` → 失焦 → Esc 关闭。
2. 生存模式，在 NPC 头顶放置方块，制造一个 **高度 0.9 格**（原版 1.8格 × 0.5倍）的空间。
3. 预期：NPC 可以通过该空间（碰撞箱高度缩小），玩家按 Shift（1.5格高）无法通过。

### 4. 极限值测试

编辑界面 Display 页：

1. Width Scale 输入 `0.1` → Height Scale 输入 `0.1` → 关闭。
2. 预期：NPC 碰撞箱极小（约 0.06 × 0.18 格），几乎贴身才能碰撞，可以通过 1 格高的空间。
3. 重新编辑，输入 `10.0` / `10.0` → 关闭。
4. 预期：碰撞箱极大（6 × 18 格），远距离即可碰撞 NPC，占据巨大空间。
5. 尝试输入 `20.0` → 失焦 → 预期：自动裁剪为 `10.00`（上限保护）。

### 5. 禁用测试

编辑界面 Display 页：

1. 确保 Width/Height 配置为非 1.0 值（如 2.0 / 0.5）。
2. 点击"Hitbox Scale"按钮切换为"Disabled"。
3. 预期：输入框变灰不可编辑，但**值仍显示**（不清空）。
4. Esc 关闭 → 生存模式测试碰撞。
5. 预期：碰撞箱恢复原版大小（0.6 × 1.8 格），缩放失效。
6. 重新编辑 → 切换为"Enabled" → 关闭。
7. 预期：缩放立即重新生效（之前的 2.0 / 0.5 配置被保留）。

### 6. 与模型大小独立

编辑界面 Display 页顶部：

1. "Size"（模型大小）改为 `10`（2倍大）。
2. "Hitbox Scale" → Width `2.0` / Height `0.5`。
3. Esc 关闭 → 生存模式观察。
4. 预期：
   - **视觉模型**：2倍大（Size 生效）
   - **碰撞箱宽度**：0.6 ÷ 5 × 10（Size） × 2.0（Width Scale）= **2.4 格**
   - **碰撞箱高度**：1.8 ÷ 5 × 10（Size） × 0.5（Height Scale）= **1.8 格**
   - 即：碰撞箱缩放与 Size 是**叠乘**关系。

### 7. 持久化测试

编辑界面配置碰撞箱缩放后：

1. `/npc save` 保存 NPC 到磁盘。
2. 退出世界 → 重新进入 → 扳手编辑 NPC。
3. 预期：Hitbox Scale 启用状态、Width/Height 值均保持。
4. 生存模式测试碰撞 → 预期：缩放效果持续生效。

### 8. 回归：未启用时无副作用

1. 创建新 NPC（默认 Hitbox Scale 为 Disabled）。
2. 生存模式测试碰撞 → 预期：与原版 1.6.4 完全一致（0.6 × 1.8 格）。
3. 编辑 Display 页 → 预期："Hitbox Scale" 显示 Disabled，输入框灰色显示 `1.00`。

## 已知限制

- **当前版本**：只修改服务端碰撞箱（`width` / `height` 字段），玩家与 NPC、NPC 与方块的物理碰撞立即生效。
- **未实现**：视觉渲染缩放（模型本身的大小）需走自有渲染器另做，与本特性无关（Display 界面的"Size"字段已控制视觉缩放）。

## 结果反馈

任一步骤不符合预期，请记录：哪一步、输入的缩放值、实际碰撞距离/高度、是否能复现。客户端 log 在 `runs/main/client/logs/latest.log`。
