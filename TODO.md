# 特性回移路线图（CustomNPC+ 1.7.10 → CustomNpcs 1.6.4）

> 本文件是回移工作的持久化路线图，每完成一项就更新状态。工作流程见 [CLAUDE.md](CLAUDE.md) 的「特性回移工作流」章节。

## 已完成

| 特性 | 提交 | 测试文档 |
|---|---|---|
| 多值按钮右键反向循环 | `934b187` | [docs/porting/gui-button-right-click-cycle.md](docs/porting/gui-button-right-click-cycle.md) |
| 文本框 Ctrl 快捷键与数字框粘贴过滤 | `a8c07d2` | [docs/porting/textfield-ctrl-shortcuts.md](docs/porting/textfield-ctrl-shortcuts.md) |
| NPC 攻速强制 | 原版已有 | 无需回移：`DataStats.attackSpeed` + `EntityNPCInterface.attackEntityAsMob` 的 `hurtResistantTime` 强制 + `SubGuiNpcMeleeProperties` 输入框在 1.6.4 均已存在 |
| 颜色选择器 + 阵营取色 | `ed87138` | [docs/porting/color-selector-dialog-visual.md](docs/porting/color-selector-dialog-visual.md)（对话视觉未纳入，见下） |
| Cloner 克隆库命名文件夹分类 | `bd0c5a9` | [docs/porting/cloner-folders.md](docs/porting/cloner-folders.md) |
| Trader 库存系统（阶段一） | `fbdbb7f` | [docs/porting/trader-stock.md](docs/porting/trader-stock.md)（全局库存+补货，按玩家库存留阶段二；配置界面输入框已知问题见文档） |
| 全键值汉化 | `0311e25` | zh_CN.lang 完整汉化（942行），游戏内切换语言即可验证 |
| 碰撞箱缩放（服务端） | `092963f` | [docs/porting/hitbox-scale.md](docs/porting/hitbox-scale.md) |
| 色调系统 | `c191d65` | [docs/porting/tint-system.md](docs/porting/tint-system.md) |
| 皮肤覆盖层（单层） | `91809ea` | [docs/porting/skin-overlays.md](docs/porting/skin-overlays.md) |
| 坐骑角色 | `1520a17` | [docs/porting/role-mount.md](docs/porting/role-mount.md) |
| Mount 配置界面与骑乘移动控制 | `eb692bd` / `4ee1c87` | [docs/porting/mount-gui.md](docs/porting/mount-gui.md) / [docs/porting/mount-movement-fix.md](docs/porting/mount-movement-fix.md) |
| 战术AI | `59d73f5` | [docs/porting/tactical-ai.md](docs/porting/tactical-ai.md) |
| 自然生成数据层与持久化（阶段一） | `391a35c` / `64e1806` | [docs/porting/spawning-data-layer.md](docs/porting/spawning-data-layer.md) |
| Display GUI 主页面/高级设置页布局修复 | 本次提交 | [docs/porting/display-gui-layout-fix.md](docs/porting/display-gui-layout-fix.md) |

## 待办候选池（中小特性，按推荐顺序）

| # | 特性 | 说明 | 规模 |
|---|---|---|---|
| 6 | 自然生成后续阶段 | 在已完成数据层基础上实现 GUI、网络同步与运行时生成/消失机制 | M-L |
| 9 | 皮肤覆盖层扩展 | 在已完成单层覆盖基础上增加多层贴图、逐层着色与透明度 | M |
| 10 | 色调系统扩展 | 在已完成受伤色调基础上增加持久色调的 GUI 配置 | M |
| 11 | 动画系统 | 帧动画（`Animation`/`Frame`/`FramePart`），数据层自包含、渲染层接自有模型 | L |
| 12 | 飞行 NPC 寻路 | `EntityNPCFlying` + 3D 寻路器（`ai/pathfinder/` 约 1800 行，自包含） | L |
| 13 | HUD 任务追踪 | 任务追踪/指南针 HUD（不含能力热键栏） | M-L |
| 14 | 战斗策略系统 | `EnumCombatPolicy`（Flip/Brute/Stubborn/Tactical）+ 阵营被动保护 | L |

## 已评估暂缓

- **对话视觉（DialogColorData/DialogImage）**：颜色选择器（原候选 #1）已完成并接入 Faction；但对话文字颜色/立绘图片部分 1.6.4 Dialog 无对应字段，牵动 NBT + 网络包 + 渲染，超出小特性范围，留待后续单列。
- **全屏/可平移 GUI（原候选 #4）**：调查后发现 CNPC+ 的 `isPannableGUI`/全屏不是独立通用能力——`GuiNPCInterface` 因它从 273 行膨胀到 850 行（89 处 pan 引用，侵入式改写全部鼠标事件），且唯一消费方是脚本编辑器、节点图 `GuiDiagram`、Cloner 文件夹浏览器 `GuiDirectory`（`GuiNpcMobSpawnerFullscreen extends GuiDirectory`），这些 1.6.4 都不存在。为无消费方的基类做侵入式改造风险大收益低，跳过。

## 大型攻坚（已设计未排期）

### JS 脚本支持 MVP

给 NPC 增加 JavaScript 脚本能力（init/tick/interact/damaged/killed/attack/target 等 13 个事件），约 3400-4000 行（CNPC+ 全量脚本体系 >70k 行的 5%）。**结论：完全可行，不需要任何 coremod/Mixin**——1.6.4 的核心事件在 `EntityNPCInterface` 已有天然触发方法，多行文本框控件（`GuiNpcTextArea`/`SubGuiNpcTextArea`）也现成可用。

完整设计见 [docs/porting/js-scripting-design.md](docs/porting/js-scripting-design.md)。排期待定，启动前先完成候选池中若干中型特性磨合工作流。

## 明确不做

强依赖 1.7.10 时代基础设施（Mixin/Janino/Nashorn 硬绑定），或体量与收益完全不成比例：

- **脚本编辑器 IDE**（`client/gui/util/script/`，51k 行的补全/解析/高亮子系统）——脚本编辑用游戏内文本区 + 外部编辑器 + `scripts/` 目录热加载替代
- **能力系统**（Ability，含能量弹幕/结界/连招，依赖脚本引擎与魔法系统）
- **拍卖行与经济系统**（依赖货币/Profile/Party/Vault）
- **魔法系统**（与能力系统深度耦合）
- **Profile 多角色存档**（玩家数据层重构，风险大）
- **Party 组队系统**（依赖任务共享重构）
- **自定义 GUI 脚本**（依赖脚本引擎全量 API）
- **动画贴图**（mixin 实现 `MixinItemRenderer`/`MixinItemStack`，1.6.4 无法照搬）
- **MixinEntityTrackerEntry 隐身修复**（修的是 1.7.10 特有的 netty 竞态，1.6.4 无此 bug）

## 新增待办（用户需求）

### L 级（Low Priority，低优先级）

- [ ] **#20** 铁砧功能回移：回移 1.7.10+ 铁砧合成 GUI，支持物品重命名、附魔合并（需 `GuiRepair` 类 + `ContainerRepair` + 铁砧方块交互逻辑）
- [ ] **#21** 高版本贴图回移：回移 1.8-1.12 的所有新增/更新贴图（方块/物品/实体/GUI），更新 `assets/` 资源包结构到最新格式

## 技术决策记录（ADR）

### ① 编译目标锁定 Java 7（硬上限，勿再尝试升 8）

1.6.4 FML 内置 **ASM 4.1**，只能解析 class 文件版本 ≤51（Java 7）。Java 8 字节码（v52）会让 FML 启动时的注解扫描抛 `IllegalArgumentException` 并**把整个 mod 当损坏文件丢弃**——已实测（dev 与生产环境皆然），见提交 `42d3610`（升 8）→ `b6abb6e`（回退 7）。绕过手段（魔改玩家实例的 ASM 库/重发行 Forge/类加载 hack）代价与风险远超收益。Java 7 语法（diamond/try-with-resources/multi-catch/switch-on-String）对回移已够用。玩家运行时用 Java 8 没有问题（配 legacyjavafixer）。

### ② 不自写提供 Mixin 的前置 mod

- **不可行**：1.6.4 Forge 运行时 = `launchwrapper 1.8` + `asm-all 4.1`（从 Forge 9.11.1 universal 的 version.json 实测核验）；SpongePowered Mixin 至少需要 ASM 5+（0.8.x 需 6+）与 launchwrapper 1.11+，且社区不存在任何 1.6.4 端口。自写 = 移植 Mixin 库 + 遮蔽新版 ASM + 类加载顺序 hack + 永久维护。
- **不必要**：CNPC+ 靠 mixin 实现的特性（受伤染色、碰撞箱视觉缩放、动画贴图）hook 的是原版渲染内部——而 1.6.4 里 NPC 渲染器 `RenderCustomNpc` 是**我们自己的代码**，直接改即可。
- **兜底方案**：真遇到必须修改原版类的场景，用 1.6.4 原生的经典 coremod 机制（`cpw.mods.fml.relauncher.IFMLLoadingPlugin` + `IClassTransformer`，ASM 4.1 API），内嵌在本 mod jar 里（CodeChickenCore 模式），不需要独立前置 mod。

### ③ 脚本引擎用纯 JSR-223，不硬依赖 Nashorn

CNPC+ 硬 import `jdk.nashorn.api.scripting.*`（JDK 8+ 才有），与 ADR ① 的 Java 7 编译目标冲突。MVP 改用纯 `javax.script` 标准 API：`ScriptEngineManager.getEngineByName("JavaScript")` + `Invocable.invokeFunction()`——Java 7 运行时自动落内置 Rhino、Java 8 自动落 Nashorn，两引擎都实现 `Invocable`；引擎缺失时脚本静默跳过并在 GUI 提示。回移的是行为语义而非实现细节（工作流铁律）。
