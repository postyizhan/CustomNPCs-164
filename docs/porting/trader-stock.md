# 测试指引：Trader 库存系统（阶段一：全局库存+补货计时器）

> 分支 `feat/trader-stock` · 回移自 CustomNPC-Plus (1.7.10) · 阶段一

## 特性说明

商人(Trader)角色现在支持**库存系统**：每个出售物品可配置库存上限，售出后扣减，库存耗尽则玩家无法购买，按配置的**补货计时器**定期自动恢复库存。

**阶段一范围**：全局库存（所有玩家共享同一库存池）+ 6种补货模式（永不/MC日/MC周/MC自定义/现实日/现实周/现实自定义）+ 管理端配置界面。**本阶段未含**：按玩家库存（数据结构已保留，界面未暴露）、客户端剩余量显示（玩家买东西时看不到剩余数量，阶段二加）。

## 改动文件

| 文件 | 改动 |
|---|---|
| `constants/EnumStockReset.java` | 新增：补货模式枚举（NONE/MCDAILY/MCWEEKLY/MCCUSTOM/RLDAILY/RLWEEKLY/RLCUSTOM） |
| `roles/TraderStock.java` | 新增：库存数据（enableStock/resetType/maxStock[18]/currentStock[18]）+ 逻辑（hasStock/consumeStock/shouldReset/resetStock） |
| `client/gui/roles/SubGuiNpcTraderStock.java` | 新增：库存配置子界面（总开关+补货模式+18槽上限+立即补货） |
| `roles/RoleTrader.java` | 加 `stock` 字段；NBT 读写用 hasKey 守卫；interact 打开GUI前惰性补货 |
| `containers/ContainerNPCTrader.java` | slotClick 购买逻辑：stock检查在canBuy前、consumeStock在givePlayer后 |
| `client/gui/roles/GuiNpcTraderSetup.java` | 加"Stock Options"按钮打开库存配置 |
| `constants/EnumPacketType.java` | 追加 `TraderStockSave`（客户端→服务端保存库存配置） |
| `PacketHandlerServer.java` | TraderStockSave 分支：读NBT→stock.readFromNBT→npc.reset+广播 |
| `assets/customnpcs/lang/en_US.lang` | 新增 `stock.*` 语言键 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 交叉审查通过（买卖安全顺序：stock检查→canBuy→givePlayer→consumeStock；NBT hasKey守卫、不bump ModRev；EnumPacketType追加末尾；包处理仿SaveNpc模式reset+广播）

## 需人工游戏内验证项

前置：`./gradlew runClient` 单人世界创造模式。物品栏取 **NPC 法杖** 和 **NPC 扳手**。生成一个 NPC（法杖右键），扳手打开编辑。

### 1. 配置商人角色 + 库存

1. NPC 编辑界面 → **Role** 页 → 角色下拉选 **Trader**。
2. **Trader Inventory Setup** 按钮 → 配置交易：在 **Currency** 栏放钻石（或任意物品作货币），在对应 **Sold** 栏放面包（或任意卖的物品）。可配多个交易槽。Esc 关闭。
3. 同 Role 页应看到新增的 **Stock Options** 按钮 → 点击打开库存配置界面：
   - **Enable Stock** 开关 → 选 **Yes**
   - **Reset Type** → 选 **MC Daily**（MC 时间每24000 tick=1天补货）
   - **Max Stock** 18 个槽对应 18 个交易槽：找到刚才配的面包对应槽（第几个Sold槽就是第几个Max Stock框），输入 `3`（库存上限3个）
   - **Done** 保存
4. **Esc** 退出编辑，保存 NPC。

### 2. 购买与库存扣减（核心功能）

1. 右键商人 NPC → 打开交易界面。
2. 背包准备足够的钻石（货币）。点交易槽（面包）→ 第1次购买成功（获得面包，扣钻石，**库存剩2**）。
3. 再买 → 第2次成功（**库存剩1**）。
4. 再买 → 第3次成功（**库存剩0**）。
5. **再次尝试购买** → 应**无反应**（库存耗尽，点击不给货也不扣钱）。关闭交易界面。

### 3. 补货计时器（MC Daily）

1. 用命令加速时间：`/time add 24000`（跳过1个MC日）。
2. 再次右键商人 → **打开交易界面时触发惰性补货检查**（`RoleTrader.interact` 里的 `shouldReset→resetStock`）。
3. 尝试购买面包 → 应**能买到**（库存已重置为3）。再买2次耗尽，第4次又买不到。

### 4. 立即补货按钮

1. 扳手打开编辑 → Role → Stock Options。
2. **Restock Now** 按钮 → 点击（立即调 `resetStock` 重置库存到 maxStock）→ Done 保存。
3. 右键商人 → 应能买到（库存已手动重置）。

### 5. 补货模式切换（Real-Time）

1. Stock Options → **Reset Type** 改为 **RL Custom**（现实时间自定义间隔）。
2. **Custom Reset Time** 输入 `60000`（60秒=1分钟，单位毫秒）。Done 保存。
3. 买空库存后，等待 **1分钟现实时间** → 再打开交易界面 → 应补货（用 System.currentTimeMillis 计时）。

### 6. 无限库存（-1）

1. Stock Options → 某槽 Max Stock 清空（或输入 `-1`）→ Done。
2. 该槽应**无限供应**（可无限购买）。

### 7. 禁用库存

1. Stock Options → **Enable Stock** 改 **No** → Done。
2. 所有交易槽应**无限供应**（库存系统关闭）。

### 8. 回归：普通交易不受影响

1. 禁用库存或设 maxStock=-1 时，交易应与改动前完全一致（无限供应、货币正常扣除）。
2. NPC 重新加载（退游戏重进/重启服务器）→ 库存配置应持久化（NBT 存 `Stock`）。

### 9. 多玩家共享库存（全局库存）

若在服务器/局域网：玩家 A 买空库存 → 玩家 B 也买不到（全局共享）。补货后所有玩家都能买。

## 已知阶段一限制（非bug，阶段二再做）

- **客户端看不到剩余量**：玩家打开交易界面时无法得知还剩几个（阶段二加客户端同步显示）。
- **按玩家库存未暴露**：`TraderStock.perPlayer` 字段存在但 GUI 未给开关（阶段二加）。
- **配置界面不实时刷新**：改 maxStock 不立即影响 currentStock，需手动"Restock Now"或等补货计时器。

## 结果反馈

任一步骤异常请记录：哪一步、交易槽配置、maxStock值、补货模式、实际现象。服务端 log 在 `runs/main/server/logs/latest.log`（若是单人则在 client）。
