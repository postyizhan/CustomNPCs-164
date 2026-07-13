# 规格：#1 颜色选择器 + 接入 Faction 颜色编辑

## 范围（S-M，纯客户端，零 NBT/网络/AT）

移植 CNPC+ 的通用取色子界面 `SubGuiColorSelector` 到 1.6.4，并接入已有的 **Faction 颜色编辑**（`GuiNPCManageFactions` 的颜色输入框旁加一个"取色"按钮，点开可视化色板选色）。

**不做**：对话视觉（DialogColorData/DialogImage）——1.6.4 Dialog 无对应字段，牵动 NBT+网络+渲染，超出 S-M；CNPC+ 其余消费方（Marks/Animation/Magic/Tags/LinkedItem）都是 1.6.4 没有的独有系统。

## 改动文件

| 文件 | 改动 |
|---|---|
| `src/main/resources/assets/customnpcs/textures/gui/color.png` | 新增：从 CNPC+ 拷贝同名贴图（120x120 显示、内部 480x480 色板） |
| `src/main/java/noppes/npcs/client/gui/SubGuiColorSelector.java` | 新增：取色子界面 |
| `src/main/java/noppes/npcs/client/gui/global/GuiNPCManageFactions.java` | 接入：颜色框旁加"取色"按钮 → 打开 SubGuiColorSelector → 关闭时回写 faction.color 与文本框 |

## SubGuiColorSelector 实现（适配 1.6.4）

以 CNPC+ 版为蓝本（`CustomNPC-Plus/.../SubGuiColorSelector.java`），做以下 1.6.4 适配：

1. **构造器**：`public SubGuiColorSelector(int color)`，设 `xSize=182; ySize=222; this.color=color; setBackground("menubg.png");`（同 CNPC+）。
2. **文本框构造器改 8 参**（1.6.4 无 6 参重载）：
   ```java
   this.addTextField(textfield = new GuiNpcTextField(0, this, fontRenderer, guiLeft + 53, guiTop + 20, 70, 20, getColor()));
   textfield.setTextColor(color);
   ```
3. **Done 按钮**：id 66，`addButton(new GuiNpcButton(66, guiLeft + 112, guiTop + 175, 60, 20, "gui.done"));`。
4. **getColor()**：把 color 转 6 位十六进制字符串（补前导 0），同 CNPC+。
5. **keyTyped**：文本变化时尝试 `Integer.parseInt(text,16)` 更新 color 与文字颜色，失败则回退旧文本，同 CNPC+。
6. **drawScreen**：`super.drawScreen(...)` 后绑定 `customnpcs:textures/gui/color.png`，`drawTexturedModalRect(colorX, colorY, 0, 0, 120, 120)`，同 CNPC+。
7. **mouseClicked**：点击色板区域时读 `color.png` 像素（`bufferedimage.getRGB((i-guiLeft-30)*4, (j-guiTop-50)*4) & 0xFFFFFF`）设为选中色，同 CNPC+。用 try/finally 关流。
8. **unFocused(GuiNpcTextField)**：解析文本框十六进制回写 color，同 CNPC+。
9. **colorX = guiLeft+30; colorY = guiTop+50;** 在 initGui 设置。
10. **回调父界面（已确认机制）**：1.6.4 有 `noppes.npcs.client.gui.util.ISubGuiListener { void subGuiClosed(SubGuiInterface subgui); }`，且 `SubGuiInterface.close()` 会自动调用 `parent.subGuiClosed(this)`（若 parent 实现该接口）。因此 SubGuiColorSelector **无需自定义回调**，只需暴露 public `color` 字段（已有）。父界面 `GuiNPCManageFactions` 追加 `implements ISubGuiListener`，实现 `subGuiClosed(SubGuiInterface subgui)`：若 `subgui instanceof SubGuiColorSelector`，取 `faction.color = ((SubGuiColorSelector)subgui).color`，然后 `initGui()` 重建界面刷新颜色框显示（现有 initGui 第 69-75 行已根据 faction.color 生成 6 位 hex 文本框并 setTextColor，重建即自动刷新）。

### Java 7 约束
- 不用 diamond 需确认（1.6.4 是 `-source 1.7`，可用 diamond）；实际本仓库反编译代码不用 diamond，保持风格用显式类型。
- try/finally 关流（不用 try-with-resources 也可，但 1.7 支持；保持与 CNPC+ 一致用 try/finally）。

## GuiNPCManageFactions 接入

在颜色文本框（id 1，第 72-75 行）附近加一个取色按钮，例如 id 20，位置紧贴颜色框右侧或下方（注意不与现有按钮 0/1/2/3/4 冲突，也不超出界面）。按钮 actionPerformed 分支：
```java
if(guibutton.id == 20){
    setSubGui(new SubGuiColorSelector(faction.color));
}
```
回读逻辑用已确认的 `ISubGuiListener.subGuiClosed` 机制：`GuiNPCManageFactions implements ISubGuiListener`，在 `subGuiClosed(SubGuiInterface subgui)` 里 `if(subgui instanceof SubGuiColorSelector){ faction.color = ((SubGuiColorSelector)subgui).color; initGui(); }`。若 `faction.id == -1`（未选中阵营）则不显示取色按钮（放在第 62 行 `if(faction.id==-1) return;` 之后）。

## codex 编码约束

1. 改 2 个 java 文件（贴图 color.png 已由 Claude 用 shell 拷贝到位，无需 codex 处理二进制）。
2. `SubGuiColorSelector` 严格适配 1.6.4 的 8 参 `GuiNpcTextField` 构造器。
3. **先探明** 1.6.4 有无 `ISubGuiListener`/`subGuiClosed` 回调机制，据此选回读方案，并在 lastmsg 说明选了哪种。
4. 不碰 CustomNPC-Plus/、accesstransformer.cfg、其他文件。
5. 完成 `./gradlew build` 验证。
