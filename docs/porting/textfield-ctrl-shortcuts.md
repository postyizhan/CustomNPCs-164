# 测试指引：文本框 Ctrl 快捷键

> 分支 `feat/textfield-ctrl-shortcuts` · 回移自 CustomNPC-Plus (1.7.10)

## 特性说明

编辑 NPC 时所有文本输入框（名称、数值等）此前已支持原版的 Ctrl+A（全选）、Ctrl+C（复制）、Ctrl+X（剪切），但 **Ctrl+V（粘贴）在数字框会粘入非法字符**，导致后续输入卡死或保存时崩溃。

本特性在 `GuiNpcTextField` 拦截 Ctrl 键：
- **Ctrl+V 在数字框过滤**：只保留数字和首位负号，其余字符（字母、符号）自动过滤。
- **Ctrl+V 在普通文本框不受限**：名称等文本框正常粘贴，支持中文、符号。
- **其他 Ctrl 快捷键透传**：Ctrl+A/C/X 等维持原版行为不变。

## 改动文件

| 文件 | 改动 |
|---|---|
| `client/gui/util/GuiNpcTextField.java` | `textboxKeyTyped` 方法在 `charAllowed` 检查前插入 Ctrl 分支：`isCtrlKeyDown()` 时，keyCode 47（V键）且 `numbersOnly` 字段触发剪贴板过滤；其他 Ctrl 键透传给 `super` |

## Agent 已验证项（无需人工重复）

- [x] `./gradlew build` 编译通过
- [x] 交叉审查通过（恰好 1 文件、Java 7 语法、负号仅首位、非 Ctrl 路径不变）

## 需人工游戏内验证项

前置：同批次 1（`./gradlew runClient` → 创造 → NPC 法杖生成 NPC → 扳手打开编辑）。

### 1. 数字框 Ctrl+V 过滤（新功能）

编辑界面顶部进 **Stats 页**，第一个数字框"Max Health"（默认 20）：

1. 在系统剪贴板复制文本 `abc-123def456`（包含字母、符号、数字）。
2. 点击"Max Health"框聚焦 → **Ctrl+A**（全选）→ **Ctrl+V**。
3. 预期：框内显示 `-123456`（字母 `abc/def` 被过滤，负号 `-` 保留在首位，数字 `123456` 保留）。
4. 点击别处（失焦）→ 数字边界检查通过，框内保持 `-123456` 或被裁剪到 `min` 边界（证明过滤值合法）。

### 2. 数字框 Ctrl+A/C/X 回归（旧功能不变）

同"Max Health"框：

1. 手动输入 `999` → **Ctrl+A**（全选）→ **Ctrl+C**（复制）。
2. 切换到下一个数字框"Max Melee Strength" → **Ctrl+V** → 预期：粘入 `999`（纯数字，不被过滤）。
3. 回到"Max Health"框 → **Ctrl+A** → **Ctrl+X**（剪切）→ 预期：框清空，剪贴板有 `999`。

### 3. 普通文本框不受影响

编辑界面进 **Display 页**，"Name"文本框（默认"CustomNpc"）：

1. 在系统剪贴板复制文本 `NPC-测试-123!@#`（包含中文、符号、数字）。
2. 聚焦"Name"框 → **Ctrl+A** → **Ctrl+V**。
3. 预期：完整粘入 `NPC-测试-123!@#`（无任何字符被过滤）。
4. Esc 关闭编辑界面 → 扳手重新打开 → Name 保持刚才的值（证明保存链路正常）。

### 4. 回归：非 Ctrl 输入仍受字符限制

Stats 页"Max Health"框：

1. 聚焦后手动按键盘输入字母 `abc` → 预期：无任何字符显示（`charAllowed` 拦截）。
2. 手动输入 `123` → 预期：正常显示 `123`。
3. 手动输入 `-` → 预期：仅在空框时允许（负号首位限制）。

### 5. 边界情况：空剪贴板 / 全字母剪贴板

1. 复制纯字母 `abcdef` → 聚焦数字框 → Ctrl+V → 预期：无反应（过滤后空字符串，`writeText` 被跳过）。
2. 复制空字符串 → Ctrl+V → 预期：无反应。

### 6. 整体回归：所有文本框正常输入

随意在 Stats / Display / AI / Advanced 页的文本框与数字框测试手动输入、删除、选中、左右移动光标，行为应与改动前一致。

## 结果反馈

任一步骤不符合预期，请记录：哪一步、哪个框、复制内容、实际现象。客户端 log 在 `runs/main/client/logs/latest.log`。
