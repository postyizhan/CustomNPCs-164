# 测试指引:多值按钮右键反向循环

> 分支 `feat/gui-button-right-click-cycle` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

编辑 NPC 时大量设置按钮是"多值循环按钮"(一个按钮在若干选项间循环,如 受击反应 的 反击/恐慌/撤退/无动作)。此前只能左键正向循环,选过头要绕一整圈。本特性新增**右键点击反向循环**;左键行为完全不变;普通按钮(完成/编辑/选择贴图等非循环按钮)右键**必须毫无反应**。

## 改动文件

| 文件 | 改动 |
|---|---|
| `client/gui/util/GuiNpcButton.java` | `mousePressed` 增加带 `reverse` 参数的四参重载(原三参版委托 `reverse=false`);新增 `isRightClickable()`(有 display 数组且长度>1 才可右键) |
| `client/gui/util/GuiNPCInterface.java` | `mouseClicked` 在 `k==1`(右键)时经 `rightClickButtons` 仅对多值按钮派发,播放点击音效并触发 `actionPerformed` |
| `client/gui/util/GuiContainerNPCInterface.java` | 同上(容器类界面基类,平行实现) |

## Agent 已验证项(无需人工重复)

- [x] `./gradlew build` 编译通过
- [x] 交叉审查通过(左键路径不变、反向边界 `0 → 末项` 无越界、闸门覆盖两个基类)

## 需人工游戏内验证项

前置:`./gradlew runClient`(或安装 `build/libs/CustomNPCs-1.6.4-<hash>.jar`)进入单人世界,创造模式。物品栏(创造页签 Custom NPCs)取 **NPC 法杖(NPC Wand)** 右键地面生成 NPC,再用法杖右键该 NPC 打开编辑界面。

### 1. 基础功能:AI 页四值按钮

编辑界面顶部进 **AI 页**,第一行"受击反应"(Retaliate/Panic/Retreat/Nothing):

- 左键连点 4 次:反击→恐慌→撤退→无动作→反击,每次有点击音 —— **回归,应与改动前一致**
- 右键连点 4 次:反向循环(如当前"反击",右键应变"无动作"→"撤退"→"恐慌"→"反击"),每次有点击音 —— **新功能**

### 2. 边界:第一项右键回绕到最后一项

同一按钮先左键调到第一项(反击),右键一次 → 应显示最后一项(无动作),无报错。

### 3. 数据保存链路:二值按钮

AI 页 "会游泳(Can Swim)" no/yes 按钮:右键切换到相反值 → **Esc 关闭编辑界面** → 重新用法杖打开 → 值应保持(证明右键同样走了保存链路)。

### 4. 误触防护:普通按钮右键必须无反应 ⚠️ 重点

- AI 页 "移动(Movement)" 行的 **Edit 按钮**:右键点击 → 不得打开子界面
- 编辑界面底部 **完成/关闭类按钮**:右键 → 不得关闭界面
- Display 页 **选择贴图(Select Texture)**:右键 → 不得打开贴图选择

任何一个普通按钮被右键触发即为 bug。

### 5. 子界面(SubGui)路径

Advanced 页 → Dialog 行 Edit → 对话可用性(Availability)子界面:三值按钮(Always/After/Before)右键应反向循环;子界面里的 Done 右键无反应。

### 6. 容器类界面路径

Advanced 页 → Job 设为 **Item Giver** → 打开其设置界面(带物品格的界面):

- 顶部五值按钮(Random Item/All Items/...)右键反向循环
- 三值按钮(Timer/Give Only Once/Daily)右键反向循环
- **物品格上的右键**(放半组物品等原版分堆操作)不受影响 —— **回归**

### 7. 整体回归:左键全程无变化

随意抽 2~3 个页(Stats/Display/Advanced)把多值按钮左键循环一整圈,行为、音效应与改动前完全一致。

## 结果反馈

任一步骤不符合预期,请记录:哪一步、哪个按钮、实际现象(必要时截图/贴 log,客户端 log 在 `runs/main/client/logs/`)。
