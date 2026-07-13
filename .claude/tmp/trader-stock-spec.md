# 规格：#2 Trader 库存系统 —— 阶段一（全局库存 + 补货计时器）

## 范围（M，服务端+客户端+网络+存档）

给商人(Trader)每个出售槽加库存上限，售出扣减，库存耗尽则不可购买，按补货计时器定期恢复。**阶段一**：全局库存（所有玩家共享）+ 补货 + 管理端配置界面 + 保存包。**本阶段剥离**：按玩家库存(perPlayer)的 GUI 开关（数据结构保留但 GUI 不暴露）、客户端剩余量显示（阶段二）、CNPC+ 的 Market/coins/currencyCost/viewer 同步/recordHistory。

## 已确认事实

- 1.6.4 `RoleTrader`：`inventoryCurrency`/`inventorySold` 各 18 槽（`NpcMiscInventory`）。一个交易 = 同下标 currency↔sold 配对。NBT 存 `TraderCurrency`/`TraderSold`。
- **购买逻辑在 `ContainerNPCTrader.slotClick`（第 78-100 行）**：sold 槽下标 `i`（容器 slot），对应 role 数组下标 **`i-1`**（currency 单槽占容器 slot 0）。流程：`canGivePlayer`→`canBuy`（**扣货币有副作用**）→`givePlayer`。**库存检查必须在 canBuy 之前**（避免扣了钱没货）；`consumeStock` 在 givePlayer 之后。
- 1.6.4 **无客户端→服务端 role 保存通道**（`SaveRole` 只服务端→客户端）。库存配置（非槽位数据）必须**新增 1 个客户端→服务端包**。
- `EnumPacketType` 末尾当前是 `...OpenBook, CHATBUBBLE;` —— 新枚举**追加到 CHATBUBBLE 之后**（ordinal 兼容）。
- `VersionCompatibility.ModRev = 12`。TraderStock 用 `hasKey("Stock")` 守卫 + 默认 `enableStock=false`，老 NPC 无缝加载，**不 bump ModRev**。
- `GuiNpcTraderSetup.save()`（第 77 行）发 `MainmenuAdvancedSave`（存的是 advanced，不是 role）。
- `RoleTrader.interact`（第 37-41 行）打开 GUI 前是惰性补货检查的插入点。

## 新建文件

### `constants/EnumStockReset.java`
直接移植 CNPC+ 版（`NONE/MCDAILY/MCWEEKLY/MCCUSTOM/RLDAILY/RLWEEKLY/RLCUSTOM` + `getDefaultInterval/isRealTime/isMinecraftTime/getDisplayName/getDisplayNames`）。`StatCollector` 1.6.4 有。零改动。

### `roles/TraderStock.java`
移植 CNPC+ `controllers/data/TraderStock.java`，1.6.4 NBT API 适配（`getTagList(name)` 无第二参数、`NBTTagList.tagAt(i)`）。字段：
```java
boolean enableStock=false, perPlayer=false;
EnumStockReset resetType=EnumStockReset.NONE;
long customResetTime=0, lastResetTime=0;
int[] maxStock=new int[18];      // 初始全 -1=无限
int[] currentStock=new int[18];  // -1=未初始化(=maxStock)
HashMap<String,int[]> playerStock; // 按玩家已购量(阶段二用,阶段一保留结构)
```
方法：`getAvailableStock(slot,playerName)`、`hasStock(slot,playerName,amount)`、`consumeStock(slot,playerName,amount)`、`shouldReset(now)`、`resetStock(now)`、`validateStock()`、`readFromNBT/writeToNBT`（键 `EnableStock/PerPlayer/ResetType(ordinal)/CustomResetTime/MaxStock/CurrentStock/LastResetTime/PlayerStock`）。构造时 `maxStock`/`currentStock` 填 -1。

### `client/gui/roles/SubGuiNpcTraderStock.java`
库存配置子界面（`SubGuiInterface`）：
- 总开关 enableStock（GuiNpcButton no/yes 循环）
- 补货模式 resetType（GuiNpcButton 循环 `EnumStockReset.getDisplayNames()`）
- 自定义间隔 customResetTime（数字文本框，仅 MCCUSTOM/RLCUSTOM 时显示）
- 每槽 maxStock：18 个数字文本框（或简化：一个"默认上限"文本框一键填充所有槽 + 说明）。**MVP 简化**：18 槽逐个文本框排 3 列 × 6 行，-1 显示为空/"∞"。
- "立即补货"按钮（本地 resetStock 后随保存包一起发）
- Done 按钮
- 关闭时通过保存包把 stock NBT 发服务端。

## 修改文件

### `roles/RoleTrader.java`
- 加字段 `public TraderStock stock;`，构造器 `stock = new TraderStock();`
- `writeEntityToNBT`：`nbttagcompound.setCompoundTag("Stock", stock.writeToNBT(new NBTTagCompound()));`
- `readEntityFromNBT`：`if(nbttagcompound.hasKey("Stock")) stock.readFromNBT(nbttagcompound.getCompoundTag("Stock"));`
- 加转发方法 `hasStock(slot,playerName,amt)`/`consumeStock(slot,playerName,amt)`/`getAvailableStock(slot,playerName)`
- `interact`：打开 GUI 前惰性补货——`long now = stock.resetType.isRealTime() ? System.currentTimeMillis() : npc.worldObj.getTotalWorldTime(); if(stock.enableStock && stock.shouldReset(now)) stock.resetStock(now);`

### `containers/ContainerNPCTrader.java`
`slotClick` 第 92-98 行，在 `canBuy` **之前**插库存检查，`givePlayer` **之后**扣库存：
```java
if(!canGivePlayer(item, entityplayer)) return null;
if(role.stock.enableStock && !role.stock.hasStock(i-1, entityplayer.getCommandSenderName(), 1)) return null;  // 新增
if(!canBuy(role.inventoryCurrency.items.get(i-1),entityplayer)) return null;
ItemStack soldItem = item.copy();
givePlayer(soldItem, entityplayer);
if(role.stock.enableStock) role.stock.consumeStock(i-1, entityplayer.getCommandSenderName(), 1);  // 新增
return soldItem;
```
（1.6.4 玩家名用 `getCommandSenderName()`，编码时确认方法名——可能是 `getEntityName()`/`username`；核对其他 1.6.4 代码取玩家名的写法。）

### `client/gui/roles/GuiNpcTraderSetup.java`
加一个"库存选项"按钮 → `setSubGui(new SubGuiNpcTraderStock(role))`。

### `constants/EnumPacketType.java`
`CHATBUBBLE` 之后追加 `TraderStockSave("",true)`（客户端→服务端存库存配置，需 NPC 权限）。

### `PacketHandlerServer.java`
加 `type == EnumPacketType.TraderStockSave` 分支：读 stock NBT → `((RoleTrader)npc.roleInterface).stock.readFromNBT(compound)` → 标记 NPC 数据脏/保存。参考现有 role 相关分支的 NPC 获取与保存写法。

## 已再次确认（编码直接用，无需再查）

1. **玩家名**：用 `player.username`（1.6.4 EntityPlayer 的 public 字段，见 RoleTrader 邻近代码 `player.username`）。
2. **PacketHandlerServer 保存样板**（仿 `SaveNpc` 分支）：`npc` 在 needsNpc=true 分支自动可用；`NBTTagCompound compound = CompressedStreamTools.read(dis);` 读包；`((RoleTrader)npc.roleInterface).stock.readFromNBT(compound);` 然后 `npc.reset();` 并广播 `NoppesUtilServer.sendDataToAll(npc, EnumPacketType.UpdateNpc, npc.copy());`。分支写成 `else if(type == EnumPacketType.TraderStockSave){ ... }`，放在 if/else 链末尾。发送端 stock 保存包用 `NoppesUtil.sendData(EnumPacketType.TraderStockSave, stock.writeToNBT(new NBTTagCompound()))`。
3. **NBTTagList 遍历**：`for(int i=0;i<list.tagCount();i++){ NBTTagCompound c=(NBTTagCompound)list.tagAt(i); }`；`getTagList("X")` 单参数。

## codex 编码约束

1. 新建 3 文件（EnumStockReset/TraderStock/SubGuiNpcTraderStock）+ 改 5 文件（RoleTrader/ContainerNPCTrader/GuiNpcTraderSetup/EnumPacketType/PacketHandlerServer）+ lang（en_US 加 `stock.*` 键）。
2. **编码前确认**：无——三个关键点已在上面『已再次确认』给出，直接用。
3. Java 7 语法，保持反编译风格。
4. TraderStock 用 hasKey 守卫，不 bump ModRev。
5. 新枚举**只追加到 CHATBUBBLE 后**，不插中间。
6. 不碰 CustomNPC-Plus/、accesstransformer.cfg。不做 perPlayer GUI 开关、不做客户端剩余量显示、不碰 Market/coins。
7. 完成 `./gradlew build` 验证，输出 git diff --stat + 三个确认项到 lastmsg。
