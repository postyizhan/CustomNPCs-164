# 规格：#3 Cloner 文件夹分类（方案 A，客户端本地）

## 范围（S-M，纯客户端，零网络/零 ModRev 风险）

把 1.6.4 克隆库现有的"固定 9 个数字 Tab"升级为"任意个命名文件夹"。clone 存储保持客户端本地（`clonednpcs.dat`），只把 `ClonedTab`(int) 语义扩展为 `ClonedFolder`(String)。**不做**：tag 系统、子文件夹、服务端共享库、目录结构存储（方案 B）。

## 向后兼容策略（关键）

读旧 clone 时：`if(!nbt.hasKey("ClonedFolder"))` → 若有 `ClonedTab` 则归入 `"Tab " + tab` 文件夹名，否则归入常量 `"Default"`。写回统一带 `ClonedFolder`。这样老存档 clone 自动进命名文件夹，用户无感，无需迁移代码。

## 改动文件

| 文件 | 新建/改 | 职责 |
|---|---|---|
| `client/controllers/CloneController.java` | 改 | `addClone` 签名 `int tab`→`String folder`；写/读 `ClonedFolder`；新增 `getFolders()`（扫描所有 clone 的 ClonedFolder 去重 + 常量 Default，排序返回）、`getClones(folder)`（按 folder 过滤）、`renameFolder(old,new)`/`deleteFolder(folder)`（遍历改写/移除 list 里该 folder 的 clone），`getFolderOf(nbt)`（兼容读取，封装上面的兼容逻辑） |
| `client/gui/GuiNpcMobSpawner.java` | 改 | 左侧 9 个固定 Tab SideButton → 动态文件夹列表（用一个 `GuiCustomScroll` 作左导航，list = getFolders()）；`activeTab`(int)→`activeFolder`(String，默认 "Default"）；`showClones()` 按 activeFolder 过滤；加"新建文件夹/重命名/删除文件夹"按钮 |
| `client/gui/GuiNpcMobSpawnerAdd.java` | 改 | Tab 数字按钮（1-9）→ 文件夹选择：一个循环按钮在 getFolders() 间循环 + 一个"新建文件夹"按钮（打开 SubGuiCloneFolderName）；`addClone(toClone, name, folder)` |
| `controllers/data/CloneFolder.java` | 新建 | 移植 CNPC+ 版（`name`+`createdDate`+`isValidName` 静态校验）。放 `noppes.npcs.controllers.data` 包（1.6.4 有此包吗？——若无则放 `noppes.npcs.controllers`；编码前确认） |
| `client/gui/SubGuiCloneFolderName.java` | 新建 | 新建/重命名文件夹输入弹窗：`SubGuiInterface` + 一个文本框 + Done/Cancel，用 `CloneFolder.isValidName()` 校验，非法则不关闭。通过 `ISubGuiListener.subGuiClosed` 回传新名字给父界面 |
| `assets/customnpcs/lang/*.lang` | 改 | 加 `cloner.newfolder`、`cloner.renamefolder`、`cloner.deletefolder`、`cloner.default` 等键（en_US 必加，zh_CN 若存在也加） |

**不改**：`EnumPacketType`、`PacketHandlerServer`、`ItemNpcCloner`、`SpawnMob`/`MobSpawner` 包（保持客户端本地）。

## 关键实现细节

### CloneController（客户端本地，`CustomNpcs.Dir/clonednpcs.dat`）
- **兼容读取封装**：
  ```java
  public static String getFolderOf(NBTTagCompound nbt){
      if(nbt.hasKey("ClonedFolder"))
          return nbt.getString("ClonedFolder");
      if(nbt.hasKey("ClonedTab"))
          return "Tab " + nbt.getInteger("ClonedTab");
      return "Default";
  }
  ```
- `getFolders()`：遍历所有 clone 收集 `getFolderOf` 去重 → TreeSet 排序 → 保证 "Default" 恒在列表（即使空）→ 返回 ArrayList。
- `addClone(Entity, String name, String folder)`：写 `ClonedFolder`=folder（不再写 ClonedTab）。
- `renameFolder(old, neu)`：遍历 clones，`getFolderOf(nbt).equals(old)` 的改写 `ClonedFolder=neu`，saveClones。
- `deleteFolder(folder)`：遍历移除该 folder 的 clone（或移到 Default——**MVP 选移到 Default 更安全，避免误删 clone**），saveClones。规格定：**deleteFolder 把该文件夹的 clone 移到 Default，不删 clone 本身**。

### GuiNpcMobSpawner 左栏改造
- 移除 9 个固定 `GuiMenuSideButton`（id 21-29）。
- 新增左侧文件夹 `GuiCustomScroll folderScroll`（id 1），放在原 SideButton 区域（`guiLeft - 73` 左侧，宽 ~70，高与主列表相当）。`setList(CloneController.getFolders())`，`setSelected(activeFolder)`。
- `customScrollClicked` 里若是 folderScroll → `activeFolder = folderScroll.getSelected(); showClones();`（**注意**：GuiNpcMobSpawner 现在没实现 scroll 监听接口，需加 `implements GuiCustomScrollActionListener` 或用现有回调机制——编码前确认 GuiCustomScroll 的点击回调方式，参考 GuiNPCManageFactions 的 `customScrollClicked`）。
- 顶部/右侧加按钮："新建文件夹"（打开 SubGuiCloneFolderName 空名字模式）、"重命名"（打开预填 activeFolder）、"删除文件夹"（对 activeFolder 执行 deleteFolder，Default 不可删/不可重命名）。
- `showClones()` 用 `CloneController.getClones(activeFolder)` 或在遍历里 `getFolderOf(comp).equals(activeFolder)` 过滤。

### GuiNpcMobSpawnerAdd
- 移除 1-9 Tab 数字按钮。
- 加文件夹循环按钮：`new GuiNpcButton(2, ..., foldersArray, 0)`（foldersArray = getFolders() 转 String[]），默认选 "Default"。右键反向循环（已支持）。
- 加"新建文件夹"按钮：打开 SubGuiCloneFolderName，回传后加入选择。
- save：`CloneController.addClone(toClone, name, 选中的folder名)`。

### SubGuiCloneFolderName
- 构造器 `SubGuiCloneFolderName(String initialName)`（新建传""，重命名传旧名）。
- 文本框 + Done(id 66)/Cancel。Done 时 `CloneFolder.isValidName(text)` 校验，通过则存 `public String folderName = text` 并 close()（触发父界面 subGuiClosed 回读）；不通过则标红/不关闭。
- 文本框可复用 `setFileNameSafe` 若存在（1.6.4 无此方法，用普通文本框 + isValidName 校验即可）。

## 已确认事实（编码直接用，无需再查）

1. **`noppes.npcs.controllers.data` 包不存在** → `CloneFolder` 放 `noppes.npcs.controllers` 包。
2. **scroll 点击回调**：`GuiCustomScroll` 的 parent 若 `implements GuiCustomScrollActionListener`（`noppes.npcs.client.gui.util.GuiCustomScrollActionListener`）则回调 `customScrollClicked(int i,int j,int k, GuiCustomScroll scroll)`。GuiNpcMobSpawner 需加此 implements，用 `scroll` 参数区分是文件夹导航还是主 clone 列表（按 id 或引用相等判断）。
3. **lang 路径** `src/main/resources/assets/customnpcs/lang/*.lang`，主文件 `en_US.lang`。至少改 en_US；其他语言可选。

## codex 编码约束

1. 改 3 个 java + 新建 2 个 java + 改 lang 文件。
2. Java 7 语法。保持仓库反编译代码风格。
4. 不碰 CustomNPC-Plus/、accesstransformer.cfg、EnumPacketType、网络层。
5. clone 存储保持客户端本地单文件，不改成目录结构。
6. deleteFolder 把 clone 移到 Default，不删 clone。
7. 完成 `./gradlew build` 验证，输出 git diff --stat 与三个确认项到 lastmsg。
