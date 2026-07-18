# #21 视觉资源盘点（M0 初始切片）

## 范围与可复现来源

本清单只覆盖双方 `assets/customnpcs/textures/` 下的 `items/`、`blocks/`、`models/`、`gui/`。当前树为本仓库工作树；候选源为仓库内只读参考 clone `CustomNPC-Plus/`，锁定完整 commit：

```text
3448957d046dae5c5b12b0b5f4287bf592e29de7
```

运行：

```bash
python tools/visual_assets_inventory.py
python tools/visual_assets_inventory.py --check
```

脚本默认校验参考 clone 的 `HEAD` 必须等于上述 commit，并拒绝该参考仓库中的 tracked 修改/删除及 untracked 文件，避免工作树内容冒充锁定来源；随后生成/核对 [visual-assets-inventory.csv](visual-assets-inventory.csv)。CSV 按路径稳定排序，逐文件记录相对路径、类别、文件类型、双方存在性与 SHA-256；对实际具有 PNG signature 的文件验证首 chunk 的 IHDR 长度、类型、CRC 和非零宽高，再记录宽、高和 PNG color type，损坏的 IHDR 会使生成一致失败。`.png.mcmeta` 作为独立非 PNG 文件记录并参与 hash/差异统计，不从扩展名臆测 PNG；其他扩展名或无扩展名文件同样可被盘点。

该 CSV 是候选证据，不是复制白名单。脚本的 `identical`/`different` 仅表示字节 hash，PNG 元数据也只是格式事实；它们**不构成**许可、消费方、视觉兼容或 1.6.4 运行时兼容结论。

## 当前计数

本次生成共 572 行（计入 `.png.mcmeta` 等非 PNG 文件）：

| 类别 | 并集 | identical | different | current-only | upstream-only |
|---|---:|---:|---:|---:|---:|
| items | 358 | 84 | 250 | 0 | 24 |
| blocks | 28 | 23 | 0 | 1 | 4 |
| models | 83 | 4 | 1 | 6 | 72 |
| gui | 103 | 26 | 5 | 3 | 69 |

按实际 PNG signature 统计，PNG 单边数量为：items 当前/上游 334/347，blocks 24/27，models 11/76，gui 34/100。表格的文件项并集与单边 PNG 数量不可直接比较：它还纳入非 PNG 文件及双方路径并集；上游 items 另有 11 个 `.png.mcmeta`，models 另有 1 个，当前树没有对应 sidecar。blocks 的上游独有 PNG 为 `campfire.png`、`candle.png`、`lantern.png`、`npcScripted.png`，当前独有 PNG 为 `npcCarpentryBench.png`。

路径身份按大小写精确字符串处理，与 Linux 语义一致；双方先分别枚举为“精确相对路径 → 实际文件”的映射，再做并集和读取。真实发现的 `models/Candle.png`（当前树）与 `models/candle.png`（上游树）是 **case-only collision**，CSV 保留两行并显式标记：前者为 `current-only`，后者为 `upstream-only`，不能在 Windows 上折叠成两个虚假的 `different`。

## 分类词汇与形成方法

后续人工审查必须为候选赋予以下处置分类；初始 CSV 不自动填写这些结论：

- `identical/no-op`：双方内容相同，无需变更。
- `visual-compatible`：已有 1.6.4 消费方可在不改行为、UV/布局或数据语义的前提下使用。
- `model-bound`：必须与 Java 模型、renderer、inventory 形态一起评估/交付。
- `layout-bound`：必须与 GUI Java 消费者、`xSize/ySize`、UV、控件与 Container 槽位一起评估。
- `missing-system/excluded`：依赖缺失或明确禁做的系统，或明确超出 #21。
- `orphan`：静态搜索和人工追踪均找不到可达的 1.6.4 消费方。

下一阶段白名单从 CSV 而不是目录复制形成：先按双方存在性和 hash 筛出候选；再逐文件做大小写精确路径检查；在 Java 注册/renderer/GUI 与动态路径构造中追踪所有消费者；核对 PNG 尺寸、color type、UV、模型/Container 耦合和 `.png.mcmeta`；最后由人工填写“源路径 → 目标路径 → 全部消费者 → 分类 → 理由 → 许可/署名状态”。只有 `visual-compatible` 且许可门禁已关闭的既有静态物品资源，才可进入 M1 的小批白名单。启发式搜索结果不得提升为最终结论。

## 代表性消费者与耦合点

以下仅用于说明审查入口，不声称已经覆盖全部动态消费者：

- items：`CustomItems` 通过 `setTextureName("customnpcs:…")` 注册大量既有物品；还需人工验证物品栏、手持/掉落、NPC 装备与交易、subtype 和 glint。
- blocks：`CustomNpcResourceListener` 动态处理 `textures/blocks/<name>.png`；上游独有 campfire/candle/lantern 不能因同目录存在就判定可用，必须绑定具体注册对象与 renderer。
- models：`BlockBarrelRenderer` 消费 `textures/models/Barrel.png`，`BlockLampRenderer` 消费 `Lamp.png`，`BlockMailboxRenderer` 消费 `mailbox1.png`/`mailbox2.png`。上游大量 banner/furniture/lamp 材质属于 `model-bound` 候选。
- gui：`GuiNpcCarpentryBench` 当前消费 `textures/gui/anvil.png`，所以新版 anvil 背景不能直接覆盖；`GuiNPCInterface`/`GuiContainerNPCInterface2` 可动态拼接 GUI 路径；`GuiCustomScroll` 消费共享 `misc.png`，菜单按钮也有共享 atlas 消费方。必须建立 PNG—全部 Java 消费者—UV/槽位矩阵。

## 明确排除项

本切片不复制或修改任何产品资源。后续 #21 仍明确排除：

- `blocks/npcScripted.png` 与脚本 IDE/自定义 GUI 等缺失或禁做系统资源；
- ability、auction、magic、Profile、Party、Companion/inventory shell 等未移植系统的 GUI；
- 动画 `.png.mcmeta` 行为（sidecar 保留在清单中仅为差异证据）；
- 1.8+ JSON blockstate/model、死资源、无入口 GUI、1.7.10-only metadata；
- 实体皮肤/overlay（属于 #24），Short Lamp 新注册对象（#22），任意 RGB/额外木种数据扩展（#23）；
- 与 #20、自然生成、动画、HUD、对话视觉等独立功能绑定的资源提前落地。

## 许可事实与未决门禁

可确认的仓库内事实：

1. 当前项目 `LICENSE` 为 PolyForm Noncommercial 1.0.0，并记载 Noppes 授权与 noncommercial 条件。
2. 锁定上游根目录 `LICENSE` 的 “Use CustomNPC+ Code or Assets” 条款明确覆盖 assets，要求适当署名 KAMKEEL、标明改动，并包含 copyleft/open-source 要求；其 External Clause 又要求遵守个别部分的替代许可。
3. 上游树中除根 `LICENSE` 外，还发现 `docs/legal/LICENSE`、`docs/legal/ADDITIONAL_LICENSE_INFO`、`docs/legal/ASSEMBLY_EXCEPTION` 及 `docs/legal/jquery.md`、`jqueryUI.md`、`jszip.md`、`pako.md` 等生成文档/依赖相关法律文件。这些文件已列入事实范围，但尚未发现适用于本次视觉素材、可逐素材证明作者、原始来源或替代许可的清单。文件位于该仓库以及根许可声称覆盖它，**不等于**已证明每个第三方素材可按本项目发布方式再分发。

因此许可 M0 **尚未完成**。批量复制前仍须由维护者/人工完成：逐候选确认原创或第三方来源与授权链；判断上游 copyleft 条款与本项目 PolyForm Noncommercial 的组合是否兼容；确定发布 jar、仓库与发布页中的 KAMKEEL/Noppes attribution、改动声明、上游链接及可能的逐素材 NOTICE 文本；必要时取得权利人明确许可。自动脚本和本文件不提供法律意见，也不把“未发现额外许可文件”包装成许可通过。

## 验证边界

### Agent 已验证

- 默认生成与 `--check` 可在锁定且 clean 的上游 commit 上稳定复现 CSV；重复生成字节一致。
- 持久标准库 unittest 使用临时目录，覆盖 exact-path case-only 双树、identical/different/current-only/upstream-only、tracked/untracked dirty 上游拒绝、PNG IHDR 长度/类型/CRC/非零尺寸校验、`.png.mcmeta` 和普通非 PNG 文件。
- `python -m py_compile tools/visual_assets_inventory.py tools/test_visual_assets_inventory.py` 通过。
- `git diff --check` 通过。
- 未运行 Gradle：本切片仅改文档与标准库盘点工具，未改 Java 产品代码或资源，Gradle 构建不会增加针对这些输出的有效验证。

### 需人工验证/关闭

- 全量消费者（尤其动态路径）、可达性、GUI UV/槽位、模型绑定与大小写在打包环境中的最终结论。
- 每个候选的分类与白名单批准；当前 CSV 的 hash 状态不是分类替代品。
- 第三方素材来源、许可兼容性与最终 attribution/NOTICE 方案。
- 后续实际采用资源时的游戏内物品/方块/GUI、专用服务器、旧存档和 F3+T 验证。
