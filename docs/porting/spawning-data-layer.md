# 自然生成数据层回移文档

## 特性来源
回移自 CustomNPC-Plus (1.7.10) 的自然生成系统（阶段 1：数据层 + 存储）

## 改动文件清单
- `src/main/java/noppes/npcs/controllers/data/SpawnData.java` - 新增，生成配置数据结构（304 行）
- `src/main/java/noppes/npcs/controllers/SpawnController.java` - 新增，全局配置管理器（105 行）

## 功能说明

### SpawnData（生成配置数据）
包含完整的 NPC 自然生成配置：
- **标识**：id（配置ID）、name（配置名称）
- **权重**：itemWeight（1-100，控制生成概率）
- **位置**：biomes（生物群系列表）、dimensions（维度ID集合）
- **实体**：spawnCompounds（实体槽位，支持多个 NBT）
- **生成类型**：animalSpawning/monsterSpawning/liquidSpawning/airSpawning（四种生成模式）
- **高度限制**：spawnHeightMin/spawnHeightMax（Y 坐标范围）
- **数量控制**：maxAlive（最大存活数量）、cooldownTicks（冷却时间）、attemptsPerCycle（每周期尝试次数）、playerMinDistance（玩家最小距离）
- **消失模式**：despawnMode（0=强制自然消失、1=保留模板、2=强制持久）

### SpawnController（全局管理器）
提供配置的增删改查和持久化：
- **单例模式**：`SpawnController.Instance` 全局访问
- **CRUD 方法**：addSpawn/removeSpawn/getSpawn
- **文件存储**：自动保存到 `world/customnpcs/spawns.dat`
- **数据安全**：三文件轮换（spawns.dat / _old / _new）+ synchronized 锁

### NBT 序列化
标签命名与 CustomNPC-Plus 保持一致：
- SpawnId/SpawnName/SpawnWeight - 基本信息
- SpawnBiomes/SpawnDimensions - 列表（NBTTagList）
- SpawnCompound[数字] - 实体槽位（动态键）
- AnimalSpawning/MonsterSpawning/LiquidSpawning/CaveSpawning - 生成类型
- HeightMin/HeightMax/MaxAlive/CooldownTicks/AttemptsPerCycle/PlayerMinDistance/DespawnMode - 控制参数

## 前置条件
无（数据层独立，无 UI 和游戏内交互）

## 测试步骤

### Agent 已验证项
- [x] 编译通过（`./gradlew build` - BUILD SUCCESSFUL）
- [x] Java 7 语法检查（无 Lambda/Stream/Optional）
- [x] NBT 序列化对称性（readNBT/writeNBT 字段完整）
- [x] 数值范围校验（weight 1-100，maxAlive/cooldownTicks ≥0，attemptsPerCycle ≥1）
- [x] null 安全（所有 setter 方法有 null 检查）
- [x] 线程安全（SpawnController 使用 synchronized 锁）

### 需人工游戏内验证项（后续阶段）
当前阶段为纯数据层，无游戏内可见功能。以下验证将在后续阶段完成：

#### 阶段 2（GUI 层）完成后
1. 启动客户端（`./gradlew runClient`）
2. 使用命令打开配置界面（待实现）
3. 创建生成配置，填写参数
4. 保存后检查 `world/customnpcs/spawns.dat` 是否生成
5. 重新加载世界，验证配置是否保留

#### 阶段 4（生成逻辑）完成后
1. 配置一个 NPC 在"平原"生物群系生成
2. 传送到平原生物群系
3. 等待观察是否有 NPC 自然生成
4. 验证生成数量是否符合 maxAlive 限制
5. 验证生成位置是否符合高度限制

## 回归检查点
- [ ] 不影响现有 NPC 功能（已有 NPC 正常加载和保存）
- [ ] 旧存档加载正常（spawns.dat 不存在时正常初始化）
- [ ] 编译无警告（除 Gradle 的 native-access 警告）
- [ ] 文件操作异常处理完善（loadSpawns 有 try-catch + 回退逻辑）

## 已知限制与技术细节

### 1. WeightedRandom.Item 兼容性
- **问题**：1.6.4 中 `WeightedRandom.Item` 不存在或 API 不同
- **解决**：不继承该类，直接在 SpawnData 中定义 `itemWeight` 字段
- **影响**：后续阶段 4（生成逻辑）需要手动实现权重随机选择，不能使用 `WeightedRandom.getRandomItem()`

### 2. dimensions 默认值
- **CustomNPC-Plus**：默认包含所有维度
- **当前实现**：默认为空集合
- **决策**：将在阶段 2（GUI）实现时决定默认行为（可能在界面添加"全选"按钮）

### 3. 文件存储位置
- **路径**：`<world-save>/customnpcs/spawns.dat`
- **格式**：压缩 NBT（CompressedStreamTools）
- **结构**：
  ```
  root {
    lastID: int
    Data: list<SpawnData compound>
  }
  ```

### 4. 线程安全
- 所有 SpawnController 的公开方法使用 `synchronized(lock)` 保护
- 文件操作在同一个锁内完成，避免并发写入冲突

## 剥离的部分
以下 CustomNPC-Plus 的特性未包含在此阶段：
- **脚本事件**：`EnumScriptType.CNPC_NATURAL_SPAWN`（1.6.4 无脚本引擎）
- **权限系统**：`CustomNpcsPermissions.GLOBAL_NATURALSPAWN`（1.6.4 无权限系统）
- **API 接口**：`INaturalSpawn` / `INaturalSpawnsHandler`（1.6.4 无脚本 API）

## 下一步
- **阶段 2**：实现 GUI 层（GuiNpcNaturalSpawns + 3 个子界面）
- **阶段 3**：实现网络同步（4 个网络包）
- **阶段 4**：实现生成逻辑（NPCSpawning.java + 事件 Hook）

---

**提交**：`d609234` feat(spawning): 添加自然生成数据层  
**合并**：`a07c25a` Merge feat/spawning-data-layer  
**阶段**：1/4（数据层完成）
