# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目背景

本仓库是 **CustomNPCs**(Minecraft 1.6.4 版本)的延续维护版本,原作者为 Noppes,已于 2026-06-11 亲自授权 postyizhan 继续维护。目标是保持完整源码开源、在 Modrinth 发布构建产物、修复既有 bug,并酌情从更新版本的 CustomNPCs / CustomNPC+ 回移特性。

模组采用 **PolyForm Noncommercial 1.0.0** 协议:免费且非商业,禁止出售或收录进付费整合包/服务器包,再分发必须保留 Noppes 的署名(见 `LICENSE` 和 `mcmod.info` 的 `authorList`)。

`CustomNPC-Plus/` 是一个独立 clone 进来的参考仓库(KAMKEEL 维护的 1.7.10 分支,`.gitignore` 中已排除,不会被提交),仅用于查阅、比对、回移特性,不属于本项目的构建产物,不要修改其内容。

## 构建与运行

- 构建工具链:ForgeGradle 7(`net.minecraftforge.gradle` 插件)+ Minecraft 1.6.4 / Forge 9.11.1.960,源码按 `-source 1.7 -target 1.7` 编译。**Java 7 是硬上限**:1.6.4 FML 内置 ASM 4.1 只能解析 class 版本 ≤51,Java 8 字节码(52)会让 FML 注解扫描抛 IllegalArgumentException 并丢弃整个 mod(dev 与生产环境皆然,已实测)。可用语法:diamond、try-with-resources、multi-catch、switch-on-String;**不可用**:lambda、方法引用、接口默认方法;API 也只用 Java 7 及以下(保住 Java 7 运行时玩家)。
- 构建:
  ```bash
  ./gradlew build
  ```
  产物位于 `build/libs/`,jar 文件名版本号形如 `1.6.4-<git短提交id>`(见 `build.gradle` 中 `gitHash` 逻辑,每次构建时动态取当前 `HEAD` 的短哈希,因此**构建前需保证工作区处于预期的 git 提交状态**)。
- 运行客户端 / 服务端(通过 ForgeGradle 的 run 任务,工作目录分别为 `runs/main/client/`、`runs/main/server/`):
  ```bash
  ./gradlew runClient
  ./gradlew runServer
  ```
- **dev 运行环境准备(首次必做)**:
  1. `./gradlew downloadGameAssets` — 下载 1.6.4 legacy 布局游戏资产(语言文件/音效/字体)到 `runs/main/client/assets/`。运行入口 slime-launcher 不传 `--assetsDir`,1.6.4 客户端默认读工作目录下的 `assets/`。缺少则游戏无法切换语言(只有英文)且没有任何声音。
  2. 用 Java 8+ 运行时跑 run 任务,必须在 `runs/main/client/mods/`(server 同理)放入 `legacyjavafixer-1.0.jar`,否则 FML 在新 JVM 上因模组排序问题启动崩溃(1.6.4 Forge 自身缺陷,与本模组无关)。
- 本项目**没有测试套件**(无 `src/test`,无测试框架依赖)。验证改动主要靠 `./gradlew build` 编译通过,以及必要时手动通过 `runClient`/`runServer` 进行游戏内验证。
- Mappings 使用 `snapshot 20130918-1.6.3`,并通过 `useDefaultAccessTransformer()` 加载 `src/main/resources/META-INF/accesstransformer.cfg`,用于放开原版 Minecraft 中被 CustomNPCs 直接访问的私有/受保护字段。**新增对原版私有/受保护成员的直接访问时,必须同步在该 AT 文件中补充对应条目,否则编译会因访问权限报错**。
- `mcmod.info` 中的 `version` 字段通过 Gradle `processResources` 任务在构建期用 `project.version` 就地展开,不要手动写死版本号。

## 代码架构

### 顶层结构

- `src/main/java/noppes/npcs/` — 模组主代码,包名固定为 `noppes.npcs`(与原作者 Noppes 保持一致,不要重命名)。
- `src/main/resources/assets/customnpcs/` — 贴图、音效、语言文件等资源,资源域名固定为 `customnpcs`。
- `CustomNPC-Plus/` — 仅供参考的外部仓库,不参与构建。

### 客户端/服务端分离(SidedProxy)

模组通过 `CustomNpcs.java` 中的 `@SidedProxy(clientSide = "noppes.npcs.client.ClientProxy", serverSide = "noppes.npcs.CommonProxy")` 分离客户端与服务端逻辑:
- `CommonProxy` 只处理服务端安全的逻辑(GUI Container 分发、pickup handler 注册)。
- `noppes.npcs.client.ClientProxy` 继承并覆盖 `CommonProxy`,补充渲染、贴图下载、粒子等客户端专属实现。
- 涉及渲染、`Minecraft.getMinecraft()` 等客户端 API 的代码只能出现在 `noppes.npcs.client` 包及 proxy 覆盖方法中,不能直接写进公共(服务端也会加载的)类里,否则专用服务端会因缺少客户端类而崩溃。

### 网络通信(自定义 Packet250CustomPayload 通道)

三个通道对应三个 Handler,均以 `EnumPacketType` 枚举区分具体消息类型,消息体用 `DataOutputStream`/`DataInputStream` 手写编码后 GZIP 压缩传输(1.6.4 时代 Forge 网络层的典型写法,没有现代的自动序列化):
- `"CNPCs Server"` → `PacketHandlerServer`(客户端→服务端,如保存 NPC、GUI 操作、任务/对话/银行等数据变更)
- `"CNPCs Client"` → `noppes.npcs.client.PacketHandlerClient`(服务端→客户端,如同步显示数据、播放粒子)
- `"CNPCs Player"` → `PacketHandlerPlayer`(双向共享的数据同步)

新增一种网络消息时,通常需要同步:在 `EnumPacketType` 加枚举值、在对应 Handler 的 `switch`/`if` 分发逻辑加处理分支、在发送端手写对应的字节写入顺序,三处必须严格对应,顺序错一位就会读出错乱数据。

### NPC 实体的数据分区(Data*.java)

`EntityNPCInterface`(NPC 实体的核心接口/基类,`EntityCustomNpc` 是其默认实现)不是把所有 NPC 属性堆在一个类里,而是按关注点拆成独立的数据容器,在构造时创建并挂到实体上:

- `DataDisplay` — 外观(模型、贴图、颜色、名字显示等)
- `DataStats` — 战斗/生命等数值属性
- `DataAI` — AI 行为参数(索敌范围、寻路、受伤反应等)
- `DataAdvanced` — 角色(role)/职业(job)、对话行、音效等高级设定
- `DataInventory` — NPC 自身的物品栏

每个 `Data*` 类持有对所属 `npc` 的引用,并自带 `readToNBT`/`writeToNBT` 完成存档读写。改动 NPC 某一类属性时,先确认它属于哪个 `Data*` 分区,避免把逻辑塞进不相关的类。

### Role / Job 体系

`noppes.npcs.roles` 包实现 NPC 的"角色"(`RoleInterface` 及其实现,如 `RoleTrader`、`RoleBank`、`RoleFollower`、`RoleTransporter`、`RoleInnkeeper`、`RolePostman`)与"职业"(`JobInterface` 及其实现,如 `JobGuard`、`JobHealer`、`JobBard`、`JobBoss`、`JobSpawner`、`JobItemGiver`、`JobConversation`),两者是正交的:一个 NPC 可以同时具有一个 role 和一个 job,分别挂在 `DataAdvanced.role` / `DataAdvanced.job`(`EnumRoleType`/`EnumJobType`),在 `EntityNPCInterface` 中按当前枚举值分发到对应的 `roleInterface`/`jobInterface` 实例。

### AI 任务

`noppes.npcs.ai` 包是基于原版 `EntityAITasks` 体系的自定义 AI 任务集合(`EntityAI*` 系列,如索敌 `EntityAIClosestTarget`、避怪 `EntityAIAvoidTarget`、巡逻 `EntityAIWander`、职业绑定 `EntityAIJob`、角色绑定 `EntityAIRole` 等),通过 Access Transformer 暴露的 `executingTaskEntries` 字段与原版 AI 调度器交互。

### GUI / Container 分发

所有自定义 GUI 通过 `EnumGuiType` 枚举统一编号,`CommonProxy.getServerGuiElement` / `getContainer` 按枚举值 `switch` 出对应的 `noppes.npcs.containers.Container*` 实现(服务端侧的 `Container`),客户端侧对应的 `GuiScreen` 在 `noppes.npcs.client.gui` 包中(通过 `ClientProxy` 覆盖打开)。新增一个 GUI 需要同时新增 `EnumGuiType` 枚举值、`Container*` 实现,以及客户端 `Gui*` 实现。

### 配置系统

`noppes.npcs.config.ConfigLoader` 会扫描 `CustomNpcs` 类中标了 `@ConfigProp` 注解的 `static` 字段,自动生成/读取 `config/CustomNpcs.cfg`。新增全局可配置项时,在 `CustomNpcs.java` 里加一个带 `@ConfigProp` 的 `public static` 字段即可,不需要手写解析逻辑。

## 特性回移工作流(CustomNPC+ → 1.6.4)

从 `CustomNPC-Plus/` 参考仓库回移特性时遵循以下流程(2026-07 与维护者约定):

### 分工

- **Claude 主会话 = 编排**:冻结实现规格(改动文件清单 + 方法级代码规格 + 兼容性分析)、审查 codex 产出、编写测试文档、git 分支/提交/合并。
- **codex CLI(`gpt-5.6-sol`)= 主力编码 + 反向审查**。编码用 `codex exec -s workspace-write -C <仓库> -o <lastmsg文件> "<prompt>"`;审查用 `-s read-only` 并在 prompt 中指明 `git diff` 范围。编码 prompt 必须限定:仅改明示的文件清单、禁碰 `CustomNPC-Plus/` 与 `accesstransformer.cfg`(除非明示)、不夹带规格外特性、完成后自跑 `./gradlew build`。

### 流程(每特性)

独立分支 `feat/<slug>` → Claude 冻结规格 → codex 编码 → **交叉审查**(codex 写的派 Claude 审,Claude 写的派 codex 审)→ 修复循环 → `./gradlew build` 通过 → 测试文档 → 中文 Conventional Commits 提交 → 合入 main(审查通过即合,游戏内验证由维护者做,发现问题 fix-forward 不回滚)。

### 测试文档

每特性随代码提交 `docs/porting/<slug>.md`,必含:特性来源与改动文件、前置条件(如何生成 NPC/打开界面)、逐步操作 + 预期结果、回归检查点,以及"agent 已验证项"(编译/冒烟)与"**需人工游戏内验证项**"的明确划分——agent 无法交互测试游戏,交互验证一律交给维护者。

### 提交规范

中文 Conventional Commits:`feat(gui): ...`、`fix: ...`、`chore(build): ...`、`docs: ...`。正文说明改了什么、为什么,回移特性注明"回移自 CustomNPC-Plus (1.7.10)"及有意剥离/偏离的部分。

### 回移铁律

- 巨型系统**不回移**:脚本引擎/脚本编辑器/能力系统/拍卖行/魔法/Profile/Party/自定义 GUI 脚本/动画贴图与 MixinEntityTrackerEntry 修复——强依赖 1.7.10 的 Mixin/Janino/Nashorn 基础设施,1.6.4 不具备。
- CNPC+ 单个文件常夹带多个特性(hover tooltip、icon 渲染等),回移时**按规格剥离**,只取目标行为。
- 回移的是**行为语义**而非实现细节:实现按 1.6.4 代码库习语重写(例:单线程 GUI 不照搬 CNPC+ 的 `AtomicBoolean` 状态字段,改用方法参数传递)。
- 新增网络包只能追加到 `EnumPacketType` 末尾(ordinal 序列化);存档结构变更必须 bump `VersionCompatibility.ModRev` 并加迁移分支。
