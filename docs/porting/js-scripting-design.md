# 设计存档：JS 脚本支持 MVP（未排期）

> 状态：**已设计，未排期**。启动前请先读本档 + [TODO.md](../../TODO.md) ADR ①③。
> 调查基线：CustomNPC-Plus (1.7.10) 的 `scripted/`（~21k 行）+ `controllers/Script*`（脚本运行时）+ `client/gui/util/script/`（51k 行编辑器）。MVP 裁剪至约 **3400-4000 行**（全量的 5%）。

## 1. 目标与范围

**做**：NPC 自带脚本（挂在每个 NPC 上），13 个事件槽，JavaScript 语言，游戏内文本区编辑 + 世界存档 `scripts/` 目录文件加载与热重载，op-only 权限。

**不做**（明确裁剪）：
- 全局脚本（Player/Forge/GlobalNPC 三套，CNPC+ 的 `player_scripts.json` 等）
- Janino（Java 语言脚本，独立可剥离路径，`IScriptHandler.createJaninoScriptUnit()` 默认返回 null 即可无残留）
- 脚本编辑器 IDE（51k 行的补全/解析子系统）
- `eventScripts` 多标签任意脚本单元列表（CNPC+ 扩展）、脚本 >65535 字符分片、per-container 语言、ES6 flag
- `api` 接口层（`ICustomNpc`/`IPlayer` 等接口/实现对偶体系）
- CNPC+ 其余 ~140 个事件（block/item/player/forge/ability/party/auction/animation…）

## 2. 引擎层（与 CNPC+ 的关键偏离）

CNPC+ 硬 import `jdk.nashorn.api.scripting.NashornScriptEngineFactory` / `ScriptObjectMirror`（`controllers/ScriptController.java` L4-5、`ScriptContainer.java` L4）——JDK 8+ 才有，与本项目 Java 7 编译目标（TODO.md ADR ①）冲突。

**MVP 改用纯 JSR-223 标准 API**（ADR ③）：

```java
ScriptEngineManager manager = new ScriptEngineManager();
ScriptEngine engine = manager.getEngineByName("JavaScript");
// Java 7 运行时 → 内置 Rhino；Java 8 → 内置 Nashorn；均实现 Invocable
if (engine == null) { /* 引擎缺失：脚本静默跳过，GUI 显示提示 */ }
```

事件调用不用 `ScriptObjectMirror.call()`，改用标准 `Invocable`：

```java
engine.eval(fullScript);                       // 定义 function interact(event){...}
((Invocable) engine).invokeFunction(hookName, event);  // NoSuchMethodException = 该事件未定义，忽略
```

脚本作者写法与 CNPC 1.7.x 一致：每个事件对应一个同名 JS 函数（`function tick(event)`、`function interact(event)`…）。

## 3. 数据结构与持久化

**挂载点**：`EntityNPCInterface` 新增字段 `public DataScript script`（构造时创建，同 `DataDisplay`/`DataStats` 等既有 Data* 分区惯例）。

**DataScript**（裁剪自 CNPC+ `controllers/data/DataScript.java` 378 行 → ~180 行）：
- `HashMap<EnumScriptType, ScriptContainer> scripts` —— 按事件类型分槽（这正是 Noppes 初版遗留结构，即 MVP 想要的最小形态）
- `boolean enabled`、`String language = "JavaScript"`、`boolean hasInited`（首次任意事件触发时自动补发 INIT，CNPC+ `DataScript.callScript` L158-164 同款）
- NBT：`ScriptsContainers`（TagList，每项 `Type` + `Script` + `ScriptList`）+ `ScriptEnabled` + `ScriptLanguage`。存档结构新增字段天然被 `VersionCompatibility.CompatabilityFix` 兜底，**无需 bump ModRev**（纯新增 tag）。

**ScriptContainer**（裁剪自 CNPC+ 431 行 → ~180 行）：`script` 字符串 + `scriptList`（外部文件引用）+ `console`（捕获输出/异常回显 GUI）+ `run(type, event)`。

**ScriptController**（裁剪自 CNPC+ 401 行 → ~150 行）：单例；引擎发现（上节）；扫描世界存档 `scripts/js/` 目录递归加载 `.js` 文件到内存 map（对应 CNPC+ `loadCategories` L249-267）；文件 `lastModified` 时间戳驱动热重载（对应 `ScriptContainer.run` L225-228 的 `lastLoaded` 机制）。

## 4. 事件模型（13 事件 × 1.6.4 插桩点对照）

`EnumScriptType`（新建，~40 行）+ `EventHooks`（新建，只含 onNPC* 静态方法，~150 行，对应 CNPC+ `EventHooks.java` L276-423 的 NPC 段）。

| 事件 | 1.6.4 触发点（`src/main/java/noppes/npcs/EntityNPCInterface.java`） | 插桩成本 |
|---|---|---|
| `INIT` | 首次任意事件触发时 DataScript 内部自动补发 | 零（内部机制） |
| `TICK` | `onLivingUpdate()` L217 | 已有方法，插一行 |
| `INTERACT` | `interact(EntityPlayer)` L282，返回 boolean 可拦截 | 已有，完美契合 |
| `DAMAGED` | `attackEntityFrom(DamageSource,float)` L338 | 已有 |
| `KILLED` | `onDeath(DamageSource)` L951 | 已有 |
| `ATTACK` | `attackEntityAsMob(Entity)` L180 | 已有 |
| `TARGET` | `setAttackTarget(EntityLivingBase)` L393 | 已有 |
| `TARGET_LOST` | 同上 null 分支 | 已有 |
| `COLLIDE` | 实体碰撞处理处 | 需插桩（中） |
| `DIALOG` | 服务端对话选项处理（`PacketHandlerServer`/`NoppesUtilServer` 的 Dialog 分支） | 需插桩（中） |
| `DIALOG_CLOSE` | 对话关闭包处理 | 需插桩（中） |
| `KILLS` | `onDeath` 中判定击杀方为本 NPC | 需插桩（中） |
| `TIMER` | 无对应机制 | MVP 可略（低优先） |

分发链（以 interact 为例，对应 CNPC+ `EventHooks.onNPCInteract` L318-327，删去全局脚本与 EventBus）：

```java
public static boolean onNPCInteract(EntityNPCInterface npc, EntityPlayer player) {
    NpcEvent.InteractEvent event = new NpcEvent.InteractEvent(npc, player);
    return npc.script.callScript(EnumScriptType.INTERACT, event, "player", player);
    // 返回 event.isCanceled()，调用方据此拦截原逻辑
}
```

## 5. API 包装层（去接口层）

Nashorn/Rhino 均为**鸭子类型**调用——脚本 `npc.setName(...)` 只要求具体类有该 public 方法，不需要接口。故 MVP **不移植 `api` 接口层**，只做 5 个具体包装类（裁剪自 CNPC+ `scripted/`，去掉 `implements IXxx`，返回类型改具体类）：

| 类 | 裁剪自（行数） | MVP 目标 | 核心方法 |
|---|---|---|---|
| `ScriptEntity` | ScriptEntity.java (986) | ~200 行 | getPos/getX/Y/Z、setPosition、getMotion/setMotion、getTempData/getStoredData |
| `ScriptLivingBase` | (433) | ~150 行 | getHealth/setHealth、getAttackTarget |
| `ScriptNpc` | ScriptNpc.java (1779，合并 ScriptLiving) | ~400 行 | getName/setName、say、setMaxHealth、getFaction、giveItem/dropItem、kill、setDialog、executeCommand、setAttackTarget、setRotation、getHome |
| `ScriptPlayer` | ScriptPlayer.java (947) | ~250 行 | message、getName、giveItem/removeItem、hasPermission、getInventory、setPosition、getStoredData |
| `ScriptWorld` | ScriptWorld.java (1299) | ~300 行 | getBlock/setBlock、spawnEntity、getTime/isDay、playSound、broadcast、getEntities、getTempData/getStoredData |

事件对象 `NpcEvent`（13 个内部类，裁剪自 CNPC+ `scripted/event/NpcEvent.java` 494 行 → ~250 行，去 `implements INpcEvent.*`）。

引擎注入（对应 CNPC+ `ScriptContainer.run` L253-259）：`npc`、`world`、`event` + 常量表 put 进 bindings。

## 6. GUI 方案

**零新控件**——1.6.4 已自带多行文本框：`client/gui/util/GuiNpcTextArea.java` + `SubGuiNpcTextArea.java`（CNPC+ 里同名文件即初版遗留，证明该方案曾是官方形态）。

新增一个 `SubGuiNpcScript`（~300 行，参考 CNPC+ `client/gui/script/GuiScriptInterface.java` 但大幅精简）：
- 事件下拉（13 槽切换）+ 文本区 + 启用开关 + 保存按钮 + console 输出区（显示脚本异常）
- 备选极简模式：「选磁盘文件 + 选事件」——因为 `scripts/` 目录加载与热重载在控制器层已完备，编辑交给外部编辑器，GUI 只做绑定
- 入口挂在 NPC 编辑器 Advanced 页（`GuiNpcAdvanced` 加一行按钮）

## 7. 网络与权限

- 新增 1 个网络包（对照 CNPC+ `EventScriptPacket.java` 113 行）：GET（打开 GUI 时拉取脚本 NBT）/ SAVE（保存时分页回传，NBT 可能超单包上限）。**按 1.6.4 惯例追加到 `EnumPacketType` 末尾**（ordinal 序列化，工作流铁律），在 `PacketHandlerServer` if 链加分支。
- 权限：op-only —— `MinecraftServer.getConfigurationManager().isPlayerOpped(...)`（1.6.4 对应方法），不移植 CNPC+ 的 ConfigScript/Developer 体系；后续可加 `@ConfigProp` 开关 `ScriptingEnabled`（默认 true）与 `ScriptOpsOnly`（默认 true）。

## 8. 分阶段实施建议

| 阶段 | 内容 | 量级 | 可验证性 |
|---|---|---|---|
| S1 核心运行时 | EnumScriptType + ScriptContainer + DataScript + ScriptController + EventHooks + EntityNPCInterface 插桩（7 个已有触发点） | ~1000 行 | 编译通过；用 NBT 编辑器手工塞脚本可跑通 tick/interact |
| S2 包装 API | 5 个 ScriptXxx 类 + NpcEvent 13 事件类 | ~1800 行 | 编译通过；脚本能调 npc.say()/player.message() |
| S3 GUI + 网络 + 收尾 | SubGuiNpcScript + EventScriptPacket + 权限 + dialog/collide/kills 插桩 + 测试文档 | ~600 行 | 游戏内全流程：编辑→保存→触发→console 回显 |

每阶段一个特性分支、独立交叉审查；S1/S2 合并后对玩家不可见（无入口），S3 合并后特性整体可用。

## 9. 风险与开放问题

- **Rhino/Nashorn 语法差异**：`Java.type()`（Nashorn）vs `importClass()`（Rhino）等——文档中注明推荐玩家用 Java 8 运行时（Nashorn），Rhino 仅保底。
- **脚本执行在主线程**：死循环会卡服。MVP 不做沙箱/超时（CNPC+ 也没做），文档警示 + op-only 权限兜底。
- **console 回显长度**：NBT 存 console 字符串需截断（CNPC+ 做法：保留最近 N 行）。
