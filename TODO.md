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

### #21 CustomNPC-Plus 视觉资源与既有渲染回移（P1，分阶段）

> 规划范围：物品贴图、方块贴图/Java 模型与现有 GUI。参考源是仓库内只读的 `CustomNPC-Plus/`（1.7.10 分支），不是笼统的“1.8-1.12 全资源包”。回移时保持 Minecraft 1.6.4 的 `assets/customnpcs/` 结构与 Java 渲染管线，**不引入 1.8+ JSON blockstate/model 格式**。

#### 目标与原则

- 以启动实施时锁定的 CustomNPC-Plus 完整提交哈希为候选源；逐文件记录来源路径、目标路径、现有消费方和采用/转换/排除结论。
- 只回移 1.6.4 已有物品、方块和可达 GUI 实际会消费的资源；PNG 存在不等于对应功能已经移植。
- 物品贴图、方块模型和 GUI 分开交付。每批保持可审查、可回滚，不整目录复制，不顺带新增玩法。
- 方块模型按 1.6.4 的 `ModelRenderer.addBox`、TESR、`ISimpleBlockRenderingHandler`/`IItemRenderer` 习语重写；不得照搬 1.7.10 `ModelBox`、对象注册和 TileEntity 网络 API。
- GUI 以“贴图—Java 消费者—Container 槽位/UV”作为一个验收单元；不能只换背景而不核对 `xSize/ySize`、控件和点击区域。
- 批量复制前先核验 CustomNPC-Plus 资源许可、第三方素材来源和署名要求，并确定本项目发布包中的 attribution/NOTICE 处理方式。

#### 当前盘点基线（规划时快照）

- `textures/items`：当前 334 项，CustomNPC-Plus 358 项；24 项仅上游存在，且 334 个同名文件中约 250 个内容不同。主要工作是已有物品的分批视觉替换，不是一次新增全部物品。
- `textures/blocks`：当前 24 项，上游 27 项；同名方块 PNG 基本没有视觉更新，上游独有的 `campfire.png`、`candle.png`、`lantern.png` 必须和具体 renderer/模型一起评估；`npcScripted.png` 排除。
- `textures/models`：当前 11 项，上游 77 项；新增材质主要服务于 banner、barrel、chair/couch、lamp/lantern、campfire/candle、mailbox、pedestal、anvil/carpentry 等现代 Java 模型，不能只复制 PNG。
- `textures/gui`：当前 34 项，上游 100 项；31 个同名顶层 PNG 中大部分完全相同。不同的 `anvil.png`、`npcinv.png`、`slot.png`、`icons.png`、`logo.png` 都与布局或消费方变化耦合，不属于无条件覆盖项。

#### 里程碑

- [ ] **M0：来源、许可和消费方清单冻结（S）**
  - [x] 新建 `docs/porting/visual-assets-inventory.md`，锁定上游完整 commit，并用可复现 CSV 记录双方存在性、hash、PNG 尺寸/色彩类型及差异状态。
  - [ ] 将资源人工分类为 `identical/no-op`、`visual-compatible`、`model-bound`、`layout-bound`、`missing-system/excluded`、`orphan`（已定义分类与白名单方法，尚未逐候选定案）。
  - [ ] 显式识别同名但内容变化的资源、路径大小写、动画 `.png.mcmeta`、无消费方资源及禁做系统资源（hash/sidecar/明确排除项已盘点；完整消费者与大小写人工审查未完成）。
  - [ ] 完成许可兼容、第三方素材来源和署名方案（已记录上游许可事实与未决门禁，尚未关闭）；M0 未完成前不得开始批量复制。

- [ ] **M1：既有物品静态贴图（S-M，按类别分批）**
  - 只处理已由 1.6.4 注册并实际可获得/显示的物品，不为使用上游 PNG 而新增物品、工具系统或网络数据。
  - 建议按工具/枪弹、近战武器、盾牌、材料/货币、乐器/杂项拆分提交；每批保存旧/新 checksum、尺寸和 alpha 边界清单。
  - 不纳入 `.png.mcmeta` 动画行为；若基础 PNG 可静态显示，可单独评估静态版本。
  - 每批验证物品栏、快捷栏、地面掉落、第一/第三人称、NPC 装备/交易槽和附魔 glint。

- [ ] **M2：既有方块现代模型试点（M）**
  - 优先选择已有注册、TileEntity 和 renderer 的单个方块家族（建议 tall lamp 或 barrel），验证 `ModelBox` → `ModelRenderer.addBox`、UV、子模型层级、多 pass 和物品栏 renderer 的完整回移链。
  - 同一切片必须包含模型类、renderer、所需贴图、inventory 形态和测试文档；不得把“世界里能渲染”视为完成。
  - 首个试点不夹带任意 RGB、paintbrush、六木种、legacy/modern 配置开关或 registry 重命名。

- [ ] **M3：既有方块模型按家族扩展（M-L，多批）**
  - 木制家具：chair、stool、table、couch wood。
  - 染色家具：couch wool、banner/wall banner、tall lamp；首阶段保留 1.6.4 现有 16 色和 NBT 语义。
  - 发光家具：campfire、candle、现有 lamp/lantern 外观；逐项验证 floor/wall/ceiling、lit/unlit、lightmap 与 GL 状态恢复。
  - 工作台/展示家具：carpentry/anvil、mailbox、pedestal、weapon rack、book/sign 等，逐家族确认实际收益后排期。
  - 每个家族检查世界模型、背包/手持模型、metadata、旋转、连接形态、碰撞/选择框、掉落与旧存档。

- [ ] **M4：GUI 资源—消费者矩阵与精选视觉更新（S-M）**
  - 先为候选 PNG 列出全部 Java 消费者、绘制 UV、`xSize/ySize`、共享 atlas 区域和 Container 槽位坐标；无矩阵的 PNG 不进入提交。
  - 纯视觉批次每次只处理 1-3 个单消费者、非共享 atlas 的兼容资源；允许盘点结论为“当前 CustomNPC-Plus 无需直接覆盖的 GUI 贴图”。
  - `slot.png`、`misc.png`、菜单按钮等共享 atlas 必须覆盖全部消费者的人工回归，不能只测一个页面。
  - 小型控件可另立切片（如 Cloner 文件夹图标、明确有消费需求的双向按钮/提示按钮），但不得带入 fullscreen/panning 或 CNPC+ 的 Java 8 callback 基类。

- [ ] **M5：功能绑定 GUI 专项（随对应特性推进，M-L）**
  - 铁砧/木工台资源拆分并入 #20：新版 `anvil.png` 不得直接覆盖当前由木工台消费的旧背景。
  - NPC inventory 新布局必须等待容量、分页、loot mode、Container 和网络规格单独批准。
  - Trader GUI 只能基于本仓库已回移的库存数据模型继续演进，不以 CustomNPC-Plus HEAD 反向覆盖本地实现。
  - 自然生成、动画、HUD、对话视觉的 GUI 分别跟随对应路线图特性，不在本 Epic 中提前铺设死资源。

- [ ] **M6：收口与发布门禁（S）**
  - 清单中每个候选均有采用/转换/排除结论，无“待确认”项；生产 jar 中资源路径与清单一致且大小写正确。
  - 每阶段 `./gradlew build` 通过，并有独立的 `docs/porting/<slug>.md`、交叉审查结果和明确的人工游戏内验证清单。
  - 专用服务端启动安全；客户端类不泄漏到公共侧；旧世界已有物品和方块无需重放置即可正常显示、保存和交互。
  - 许可、署名及发布说明核验完成后，才可勾选整个 #21。

#### 独立后续特性（不混入视觉资源批次）

- [ ] **#22 Short Lamp 新方块**：这是新增注册对象，不是纯模型换肤。需单独设计数字 block ID、Block/TileEntity、1.6.4 模型与 renderer、物品栏渲染、语言键、旧世界 ID 冲突检查和测试文档。
- [ ] **#23 家具任意 RGB 与额外木种**：任意 RGB、paintbrush、`TileVariant` 数据迁移以及 acacia/dark oak 语义属于数据/交互扩展；启动前必须冻结 NBT 迁移方案，并判断是否 bump `VersionCompatibility.ModRev`。1.6.4 默认木材仍限制为 oak/spruce/birch/jungle。
- [ ] **#24 实体皮肤素材扩充**：实体/皮肤/overlay 不纳入 #21；与现有“皮肤覆盖层扩展”协调后另行建立素材来源、模型兼容和人工验证矩阵。

#### 明确非目标

- 不升级 Minecraft、Forge、Java、assets namespace，也不引入 1.8+ JSON block/item model 系统。
- 不复制 `CustomNPC-Plus/` 整个资源目录，不修改该参考仓库；无 1.6.4 消费方的资源默认排除。
- 不在视觉任务中改变历史 registry 名、数字 ID、NBT 含义或 lit/unlit 映射；需要这些变化时停止并升级为独立特性。
- 不回移动画 `.mcmeta` 能力、scripted block，或 ability、auction、magic、Profile、Party、脚本 IDE、自定义 GUI、Companion/inventory shell 等缺失/禁做系统的资源。
- 不为“资源完整率”引入死资源、无入口 GUI 或不可获得的 1.7.10-only metadata。

#### 完成定义与人工验证矩阵

- **自动/静态**：构建通过；新增资源均有消费方；jar 路径与大小写核验；禁止残留 `IIcon`、`S35PacketUpdateTileEntity`、`Item.getItemFromBlock`、`ModelBox`、`java.util.function` 等未适配的 1.7.10/Java 8 API；服务端公共类无 client import。
- **物品人工验证**：所有采用项覆盖 GUI、掉落、第一/第三人称、NPC 装备/交易、附魔效果和实际 subtype；F3+T 后无 missing texture。
- **方块人工验证**：覆盖 inventory/world、四向或实际旋转、全部 metadata/颜色、连接状态、lit/unlit、区块重载、退出重进、旧存档、pick block/掉落和多个 TESR 同屏；无 UV 缺面或 GL/lightmap 污染。
- **GUI 人工验证**：覆盖所有可达页面及子页、槽位点击/拖拽/shift-click、滚动/分页/hover/disabled、中文/英文、多个 GUI Scale 与窗口尺寸；视觉槽位和鼠标命中区域完全一致。
- **多人/回归**：专用服务器加入、重连和旧世界验证通过；只改变批准的视觉，不改变原有物品使用、方块交互、保存/取消与网络行为。

## 技术决策记录（ADR）

### ① 编译目标锁定 Java 7（硬上限，勿再尝试升 8）

1.6.4 FML 内置 **ASM 4.1**，只能解析 class 文件版本 ≤51（Java 7）。Java 8 字节码（v52）会让 FML 启动时的注解扫描抛 `IllegalArgumentException` 并**把整个 mod 当损坏文件丢弃**——已实测（dev 与生产环境皆然），见提交 `42d3610`（升 8）→ `b6abb6e`（回退 7）。绕过手段（魔改玩家实例的 ASM 库/重发行 Forge/类加载 hack）代价与风险远超收益。Java 7 语法（diamond/try-with-resources/multi-catch/switch-on-String）对回移已够用。玩家运行时用 Java 8 没有问题（配 legacyjavafixer）。

### ② 不自写提供 Mixin 的前置 mod

- **不可行**：1.6.4 Forge 运行时 = `launchwrapper 1.8` + `asm-all 4.1`（从 Forge 9.11.1 universal 的 version.json 实测核验）；SpongePowered Mixin 至少需要 ASM 5+（0.8.x 需 6+）与 launchwrapper 1.11+，且社区不存在任何 1.6.4 端口。自写 = 移植 Mixin 库 + 遮蔽新版 ASM + 类加载顺序 hack + 永久维护。
- **不必要**：CNPC+ 靠 mixin 实现的特性（受伤染色、碰撞箱视觉缩放、动画贴图）hook 的是原版渲染内部——而 1.6.4 里 NPC 渲染器 `RenderCustomNpc` 是**我们自己的代码**，直接改即可。
- **兜底方案**：真遇到必须修改原版类的场景，用 1.6.4 原生的经典 coremod 机制（`cpw.mods.fml.relauncher.IFMLLoadingPlugin` + `IClassTransformer`，ASM 4.1 API），内嵌在本 mod jar 里（CodeChickenCore 模式），不需要独立前置 mod。

### ③ 脚本引擎用纯 JSR-223，不硬依赖 Nashorn

CNPC+ 硬 import `jdk.nashorn.api.scripting.*`（JDK 8+ 才有），与 ADR ① 的 Java 7 编译目标冲突。MVP 改用纯 `javax.script` 标准 API：`ScriptEngineManager.getEngineByName("JavaScript")` + `Invocable.invokeFunction()`——Java 7 运行时自动落内置 Rhino、Java 8 自动落 Nashorn，两引擎都实现 `Invocable`；引擎缺失时脚本静默跳过并在 GUI 提示。回移的是行为语义而非实现细节（工作流铁律）。
