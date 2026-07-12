# 测试指引：颜色选择器 + Faction 取色

> 分支 `feat/color-selector-dialog-visual` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

新增一个通用的可视化取色子界面 `SubGuiColorSelector`：一张色板贴图 + 十六进制输入框，点击色板任意位置即取该点颜色，也可直接手输 6 位十六进制。首个接入点是**阵营（Faction）颜色编辑**——此前只能在文本框手敲十六进制，现在颜色框旁多了一个 `...` 按钮，点开可视化选色。

> 范围说明：本批只接入 Faction 颜色。CNPC+ 的对话视觉（DialogColorData/DialogImage）因 1.6.4 Dialog 无对应字段、牵动 NBT+网络+渲染，未纳入本特性（见 [TODO.md](../../TODO.md)）。

## 改动文件

| 文件 | 改动 |
|---|---|
| `src/main/resources/assets/customnpcs/textures/gui/color.png` | 新增：色板贴图（从 CNPC+ 拷贝） |
| `client/gui/SubGuiColorSelector.java` | 新增：取色子界面（点击色板读像素取色 / 手输十六进制 / Done 关闭回传 `color`） |
| `client/gui/global/GuiNPCManageFactions.java` | 接入：颜色框旁加 `...` 取色按钮（id 20），`implements ISubGuiListener` 在 `subGuiClosed` 回读 `faction.color` |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 交叉审查通过（8 参文本框构造器、`ISubGuiListener` 回读机制、取色按钮 id 20 不冲突、纹理绑定 `mc.renderEngine.bindTexture` 符合 1.6.4 惯例、try/finally 关流）

## 需人工游戏内验证项

前置：`./gradlew runClient` 进入单人世界，创造模式，NPC 法杖右键 NPC 打开编辑界面。

### 1. 打开阵营管理界面

编辑界面找到**阵营管理**入口（NPC 编辑主菜单 → Advanced/管理阵营，或通过管理 GUI 打开 `GuiNPCManageFactions`）。左侧列表选中一个已有阵营（如未有则先 Add 一个）。

### 2. 取色按钮出现

选中阵营后，"Color/颜色"文本框右侧应出现一个 `...` 按钮。**未选中任何阵营时**（faction.id == -1），该按钮不应出现。

### 3. 色板取色（新功能）

1. 点 `...` 按钮 → 弹出取色子界面：上方十六进制输入框（文字颜色随当前色），下方 120x120 彩色色板。
2. 在色板不同区域点击 → 输入框的十六进制值与文字颜色应随点击位置实时变化。
3. 点 **Done** 关闭 → 回到阵营界面，颜色文本框应显示刚选的颜色值（6 位十六进制），且文字颜色同步更新。

### 4. 手输十六进制（回归 + 子界面内）

1. 取色子界面内：在输入框手敲 `FF0000` → 文字应变红；敲非法字符（如 `GG`）→ 应被拒绝或回退。
2. 阵营界面颜色文本框直接手输（不经子界面）：敲 `00FF00` 失焦 → `faction.color` 应更新为绿色（原有行为，回归）。

### 5. 保存持久化

选好颜色后，切换到别的阵营再切回来（或关闭重开管理界面）→ 颜色应保持（证明 `faction.color` 经 `save()` → `FactionSave` 包正常持久化）。

### 6. 回归：阵营其它编辑不受影响

阵营名称改名、Points 编辑、hidden/attacked 开关、hostiles 敌对列表勾选 —— 全部应与改动前一致。

## 结果反馈

任一步骤异常请记录：哪一步、阵营名、操作、实际现象。客户端 log 在 `runs/main/client/logs/latest.log`。
