# 规格：文本框 Ctrl 快捷键（批次 2）

## 特性语义

现状：`GuiNpcTextField` 继承原版 `GuiTextField`，原版已支持 Ctrl+A/C/X（全选/复制/剪切），但 **Ctrl+V（粘贴）在数字框会粘入非法字符导致后续输入卡死**。

目标：在 `textboxKeyTyped` 中拦截 `GuiScreen.isCtrlKeyDown()` 的情况：
1. **Ctrl+V 粘贴过滤**：`numbersOnly` 字段的数字框粘贴时只保留数字和负号；普通文本框不拦截。
2. **其他 Ctrl 快捷键透传**：Ctrl+A/C/X 等直接调 `super.textboxKeyTyped` 让原版处理，绕过 `charAllowed` 门控。

## 改动文件（单文件）

`src/main/java/noppes/npcs/client/gui/util/GuiNpcTextField.java`

## 实现规格（Java 7 语法）

在 `textboxKeyTyped(char c, int i)` 方法（当前第 39-44 行）的 `charAllowed` 检查**之前**插入 Ctrl 分支：

```java
@Override
public boolean textboxKeyTyped(char c, int i)
{
    // Allow Ctrl shortcuts to bypass char filter
    if (GuiScreen.isCtrlKeyDown()) {
        // Ctrl+V (keyCode 47) on numbersOnly fields: filter clipboard
        if (i == 47 && numbersOnly) {
            String clipboard = GuiScreen.getClipboardString();
            if (clipboard != null) {
                StringBuilder filtered = new StringBuilder();
                for (int idx = 0; idx < clipboard.length(); idx++) {
                    char ch = clipboard.charAt(idx);
                    if (Character.isDigit(ch) || (ch == '-' && filtered.length() == 0)) {
                        filtered.append(ch);
                    }
                }
                if (filtered.length() > 0) {
                    writeText(filtered.toString());
                }
            }
            return true;
        }
        // Other Ctrl shortcuts (A/C/X etc.): delegate to super
        return super.textboxKeyTyped(c, i);
    }
    if(!charAllowed(c,i))
        return false;
    return super.textboxKeyTyped(c, i);
}
```

### 关键点

1. **keyCode 47 = V 键**（1.6.4 原版键码，已实测）。
2. **只过滤 `numbersOnly` 字段**（1.6.4 只有 `numbersOnly`，无 CNPC+ 的 `integersOnly/doublesOnly/floatsOnly`）。
3. **负号只允许在开头**：`ch == '-' && filtered.length() == 0`。
4. **Java 7 语法约束**：
   - 用 `for (int idx ...)` + `charAt(idx)` 而非增强 for（`char ch : clipboard.toCharArray()` 虽然 Java 7 可用，但与仓库既有风格对齐，数组遍历用索引）。
   - `StringBuilder` 可用（Java 5+）。
5. **不夹带 CNPC+ 的无关改动**：
   - 不引入 `integersOnly/doublesOnly/floatsOnly/fileNameSafe` 字段。
   - 不引入 `TextSplitter`/`FileNameHelper`/`canEdit` 字段。
   - 不引入 hover tooltip（`hoverableText`/`drawHover`）。
   - 字段命名保持 `numbersOnly`（不改名为 `integersOnly`）。

## 兼容性检查

- **原版 Ctrl+A/C/X 透传给 super**，行为不变。
- **Ctrl+V 在普通文本框透传**，粘贴不受限。
- **Ctrl+V 在数字框过滤后调 `writeText`**，相当于手敲过滤后的内容，触发 `charAllowed` 与 `unFocused` 边界检查链路正常。
- **非 Ctrl 按键路径一字节不变**。

## 测试步骤（写入 docs/porting/textfield-ctrl-shortcuts.md）

前置：同批次 1（`./gradlew runClient` → 创造 → NPC 法杖生成 NPC → 扳手打开编辑）。

1. **Stats 页数字框 Ctrl+V 过滤**：
   - 复制文本 `abc-123def456`。
   - 聚焦"Max Health"数字框（默认 20），Ctrl+A 全选 → Ctrl+V → 应仅粘入 `-123456`（字母过滤、负号在首位保留）。
   - 失焦（点别处）→ 数字校验通过（证明过滤值合法）。
2. **Ctrl+A/C/X 回归**：
   - 同数字框：输入 `999` → Ctrl+A → Ctrl+C → 切到另一框 → Ctrl+V → 应粘入 `999`。
   - Ctrl+X（剪切）→ 框应清空。
3. **普通文本框不受影响**：
   - Display 页"Name"文本框：复制 `NPC-测试-123` → Ctrl+V → 应完整粘入（包含符号与中文）。
4. **回归：非 Ctrl 输入**：
   - 数字框手敲 `abc` → 应无反应（`charAllowed` 拦截，旧行为）。
   - 数字框手敲 `123` → 正常显示。

## codex 编码约束

1. 仅改 `src/main/java/noppes/npcs/client/gui/util/GuiNpcTextField.java` 一个文件。
2. 只在 `textboxKeyTyped` 方法插入上述 Ctrl 分支，其余方法与字段一字节不动。
3. 不碰 `CustomNPC-Plus/` 和 `accesstransformer.cfg`。
4. 完成后 `./gradlew build` 验证。
