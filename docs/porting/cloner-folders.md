# 测试指引：Cloner 文件夹分类

> 分支 `feat/cloner-folders` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

克隆库（用 NPC 克隆器保存/取用 NPC 模板）此前只有固定的"Tab 1 ~ Tab 9"九个无名分类。本特性把它升级为**任意个命名文件夹**：可新建、重命名、删除文件夹，把克隆归类到自定义命名的文件夹下。

**范围**（MVP）：单层命名文件夹、客户端本地存储（与原版一致，克隆库是每客户端本地的，不跨玩家共享）。**不含**：标签(tag)过滤、子文件夹、服务端共享库。

## 向后兼容

- 老存档里用旧"Tab N"保存的克隆，读取时自动归入名为 **"Tab N"** 的文件夹。
- 更早的、没有分类信息的克隆归入 **"Default"** 文件夹。
- 用户无感，无需手动迁移。

## 改动文件

| 文件 | 改动 |
|---|---|
| `client/controllers/CloneController.java` | `addClone` 用 `String folder`；写读 `ClonedFolder`；新增 `getFolderOf`（兼容旧 ClonedTab）/`getFolders`/`getClones(folder)`/`renameFolder`/`deleteFolder` |
| `client/gui/GuiNpcMobSpawner.java` | 左侧 9 个固定 Tab 按钮 → 动态文件夹滚动列表；新建/重命名/删除文件夹按钮；按 activeFolder 过滤克隆 |
| `client/gui/GuiNpcMobSpawnerAdd.java` | 保存对话框的 Tab 数字按钮 → 文件夹循环选择 + 新建文件夹按钮 |
| `controllers/CloneFolder.java` | 新增：文件夹名合法性校验 `isValidName`（≤32 字符、非纯数字、禁文件系统非法字符） |
| `client/gui/SubGuiCloneFolderName.java` | 新增：新建/重命名文件夹输入弹窗 |
| `assets/customnpcs/lang/en_US.lang` | 新增 `cloner.*` 语言键 |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 交叉审查通过（getFolderOf 兼容旧 ClonedTab/Default、deleteFolder 移到 Default 不删克隆、文件夹列表排序 Default 恒在、scroll 点击回调经 GuiCustomScrollActionListener、SubGui 经 ISubGuiListener 回传）

## 需人工游戏内验证项

前置：`./gradlew runClient` 进入单人世界，创造模式。物品栏（创造页签 Custom NPCs）取 **NPC 克隆器（Cloner）**。先生成几个 NPC（NPC 法杖），用克隆器右键 NPC 保存到克隆库。

### 1. 保存对话框的文件夹选择（新功能）

1. 克隆器右键一个 NPC → 弹出保存对话框：应看到名字输入框 + **文件夹循环按钮**（默认 "Default"）+ **New Folder 按钮**。
2. 点 New Folder → 弹出输入框 → 输入 `Boss`（合法名）→ Done → 回到保存对话框，文件夹按钮应显示 `Boss`。
3. 点 Save → 该 NPC 存入 `Boss` 文件夹。
4. 再保存另一个 NPC 到 Default（文件夹按钮切到 Default）。

### 2. 克隆库文件夹导航

1. 用克隆器右键地面（或空手打开克隆库主界面 `GuiNpcMobSpawner`）。
2. 左侧应是**文件夹列表**（滚动列表），含 `Default`、`Boss`（以及老存档遗留的 `Tab N`，如有）。
3. 点 `Boss` → 主列表只显示存入 Boss 的克隆；点 `Default` → 只显示 Default 的克隆。

### 3. 新建 / 重命名 / 删除文件夹

1. **New Folder** 按钮 → 输入 `Minions` → 新文件夹出现在左侧列表（选中态）。
2. 选中 `Boss`，**Rename Folder** → 改成 `Bosses` → 该文件夹及其内克隆归属应更新为 `Bosses`。
3. 选中 `Bosses`，**Delete Folder** → 该文件夹消失，**其中的克隆应移动到 Default 文件夹（不被删除）** → 切到 Default 确认克隆还在。
4. 选中 `Default` → Rename/Delete 按钮应为**禁用**（Default 不可改名/删除）。

### 4. 名称合法性校验

New Folder 时尝试非法名：纯数字 `123`、含斜杠 `a/b`、超长（>32 字符）、以 `___` 开头 → 输入框文字应变红且**不关闭**（拒绝非法名）。

### 5. 生成/移除克隆（回归）

1. 选中某文件夹里的克隆 → **MobSpawner/monsterPlacer** 按钮生成到世界 → 应正常生成对应 NPC。
2. 选中克隆 → **Remove** 按钮 → 克隆从库中删除。
3. 切到 **Entities** 顶部标签 → 原版实体列表应正常显示（文件夹导航仅在 Clones 模式出现）。

## 已知 MVP 限制（非 bug）

- **空文件夹不持久化**：新建一个文件夹但没往里存任何克隆，关闭克隆库再打开后该空文件夹会消失（文件夹列表是从克隆的归属反推的，没有独立的文件夹注册表）。往文件夹里存至少一个克隆后即稳定存在。
- 克隆库是**每客户端本地**的（存在 `.minecraft/customnpcs/clonednpcs.dat`），不跨玩家/不上传服务端——与原版行为一致。

## 结果反馈

任一步骤异常请记录：哪一步、文件夹名、克隆名、实际现象。客户端 log 在 `runs/main/client/logs/latest.log`。
