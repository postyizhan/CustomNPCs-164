# Display GUI 布局修复测试说明

## 特性来源与问题背景

Display 页面此前将碰撞箱缩放、受伤色调和皮肤覆盖层控件作为“右列”添加到原页面，但 `GuiNPCInterface2` 的内容区宽度只有 420，而原有名称、纹理、披风等控件已经延伸到 `guiLeft + 363`。新增区域从 `guiLeft + 200` 开始，导致标签、文本框和按钮发生真实的矩形交叠。

由于通用 GUI 按“标签、文本框、按钮”的顺序绘制，截图中多数新增标签会被原有文本框和按钮遮住；部分重叠按钮还存在点击命中冲突。该问题不是 GUI Scale 或背景纹理导致的。

本修复按 1.6.4 GUI 习惯重新组织界面，没有直接回移 CustomNPC-Plus 的嵌套设置窗口实现。Display 主页面保留原有控件，碰撞箱、色调和皮肤覆盖层设置移入同一 Display GUI 的高级设置页。

## 改动文件

| 文件 | 改动 |
|---|---|
| `src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcDisplay.java` | 将新增设置移入高级页；保留唯一控件 ID；修复碰撞箱高度字段事件映射；使用一级 SubGui 打开颜色选择器；统一六位 RGB 显示 |
| `docs/porting/display-gui-layout-fix.md` | 记录修复范围、测试步骤和验证边界 |

本修复不修改网络消息、NBT 数据结构、渲染逻辑、通用 GUI 基类或 `CustomNPC-Plus/` 参考目录。

## 前置条件

1. 启动 Minecraft 1.6.4 开发客户端并进入世界。
2. 准备一个可编辑的 CustomNPC。
3. 使用 NPC Wand 打开 NPC 编辑器。
4. 进入 `Display` 页面。
5. 推荐先使用截图复现环境：窗口约 922×532、GUI Scale 2。

## 测试步骤与预期结果

### 1. Display 主页面布局

1. 打开 `Display` 页面。
2. 检查 Name、Title、Model、Size、Texture、Cape、Overlay、Living Animation、Tint、Visible。
3. 棐查 Living Animation 同一行右侧的 `Settings` / `设置` 按钮。

预期结果：

- 所有标签完整可见。
- 文本框和按钮之间没有重叠。
- 点击任意控件只触发对应操作。
- 页面不再显示漂浮的 `1.00`、`Disabled` 或 `Select` 控件。

### 2. 高级设置页导航

1. 点击 `Settings` / `设置`。
2. 检查 Hitbox Scale、Width Scale、Height Scale、Tint System、Hurt Tint、Skin Overlay、Overlay Path。
3. 点击右上角 `Done` / `完成`。

预期结果：

- 高级设置以单列显示，所有控件位于 GUI 背景范围内。
- 标签、字段和按钮没有重叠。
- `Done` 返回原 Display 主页面，不关闭整个 NPC 编辑器。

### 3. 标题与碰撞箱字段 ID 回归

1. 在主页面修改 NPC Title。
2. 进入高级设置页并启用 Hitbox Scale。
3. 将 Width Scale 和 Height Scale 分别修改为不同值，例如 `1.50` 和 `2.25`。
4. 输入后直接点击 `Done`，保存并重新打开 NPC。

预期结果：

- Title 保持输入的标题。
- Width Scale 为 `1.50`。
- Height Scale 为 `2.25`。
- 修改 Height Scale 不会覆盖 Title。

### 4. 碰撞箱范围处理

1. 在 Width Scale 输入小于 `0.1` 的值。
2. 移开焦点。
3. 在 Height Scale 输入大于 `10.0` 的值。
4. 移开焦点。
5. 输入非数字内容并移开焦点。
6. 分别输入 `NaN`、`Infinity` 和 `-Infinity` 并移开焦点。

预期结果：

- 宽度回显为数据层限制后的 `0.10`。
- 高度回显为数据层限制后的 `10.00`。
- 非数字及非有限浮点值恢复为当前有效值，不会写入 NPC 数据。
- GUI 不崩溃。

### 5. 受伤色调与颜色选择器

1. 启用 Tint System。
2. 启用 Hurt Tint。
3. 在颜色字段输入六位十六进制颜色。
4. 点击颜色编辑按钮，选择带前导零的颜色，例如接近 `0000ff`。
5. 在颜色选择器中点击 `Done`。

预期结果：

- 颜色选择器作为 Display 的子界面打开。
- 完成选择后返回高级设置页，不关闭 NPC 编辑器。
- 颜色字段始终显示六位十六进制值，例如 `0000ff`，不会缩短为 `ff`。
- 颜色字段文字颜色同步更新。

### 6. 皮肤覆盖层

1. 启用 Skin Overlay。
2. 在 Overlay Path 输入有效资源路径。
3. 直接点击 `Done`，保存并重新打开 NPC。

预期结果：

- 启用时路径字段可编辑，禁用时不可编辑。
- 保存后路径和启用状态保持。
- 有效覆盖层贴图正常渲染。

## 回归检查点

- Name 显示模式切换正常。
- Model 编辑和 Size 输入正常。
- Texture 类型切换、玩家皮肤、URL 和纹理选择正常。
- Cape 与原 Glow Overlay 选择正常。
- Living Animation、基础 Skin Tint、Visible 正常。
- 高级设置各开关调用 `initGui()` 后仍停留在高级页。
- 顶部菜单切换、保存和重新打开 NPC 正常。
- 英文与简体中文标签均不会伸入控件区域。
- GUI Scale 1、2、3/Auto 下页面均未越出屏幕或背景。

## Agent 已验证项

- Java 7 兼容语法静态检查。
- 控件 ID 与 `actionPerformed()` / `unFocused()` 映射核对。
- 两个页面的控件矩形静态检查。
- `git diff --check`。
- `./gradlew build` 编译。

## 人工游戏内验证结果

维护者已于 2026-07-18 确认本次 Display GUI 修复可用，主页面与高级设置页可以正常操作。

以下功能仍应在相关资源或配置变化时作为回归检查点：

- 其他 GUI Scale 与中英文环境的实际视觉效果。
- Hitbox 实际碰撞范围。
- Hurt Tint 实际受伤闪烁颜色。
- Skin Overlay 实际贴图渲染。

**测试状态**：已通过本次游戏内验收。
