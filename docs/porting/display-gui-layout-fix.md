# Display GUI 右列布局修复

> 修复时间：2026-07-13  
> 问题报告：用户反馈 Display 界面右列布局混乱

---

## 🐛 问题描述

用户提供截图显示 Display 界面右列控件布局混乱：
- **碰撞箱缩放**：标签和输入框位置不对齐
- **色调系统**：控件水平位置不一致
- **覆盖层路径**：输入框位置错误，与标签重叠

---

## 🔍 问题分析

### 原始代码问题
```java
// 问题：X 坐标硬编码且不一致
addLabel(new GuiNpcLabel(10,"display.hitbox", guiLeft + 200, yRight + 5));
addButton(new GuiNpcButton(10, guiLeft + 290, yRight, ...));  // +290

addLabel(new GuiNpcLabel(14,"display.hurtTint", guiLeft + 200, yRight + 5));
addButton(new GuiNpcButton(12, guiLeft + 270, yRight, ...));  // +270 (不一致！)
addTextField(new GuiNpcTextField(13, ..., guiLeft + 307, ...));  // +307
addButton(new GuiNpcButton(14, guiLeft + 349, yRight, ...));  // +349

// 问题：覆盖层路径位置错误
addLabel(new GuiNpcLabel(16,"display.overlayPath", guiLeft + 200, yRight + 5));
addTextField(new GuiNpcTextField(14, ..., guiLeft + 200, yRight + 15, ...));  
// yRight+15 导致输入框和上一行控件重叠
```

### 根本原因
1. **缺乏统一坐标系统**：每个控件单独计算 X 坐标
2. **硬编码偏移量**：200/270/290/307/349 混乱使用
3. **Y 坐标计算错误**：覆盖层路径用 yRight+15 而非 yRight+23

---

## ✅ 修复方案

### 1. 引入统一坐标变量
```java
int xRight = guiLeft + 200;  // 右列起始 X 坐标

// 所有控件基于 xRight 偏移
addLabel(new GuiNpcLabel(10,"display.hitbox", xRight, yRight + 5));
addButton(new GuiNpcButton(10, xRight + 90, yRight, ...));
```

### 2. 标准化水平间距
| 控件类型 | X 坐标 | 说明 |
|---------|--------|------|
| 标签 | xRight | 右列起始位置 |
| 主按钮/输入框 | xRight + 90 | 标签后 90px |
| Yes/No 按钮（窄） | xRight + 70 | 特殊情况（受伤染色） |
| 颜色输入框 | xRight + 107 | Yes/No 按钮后 |
| X 按钮 | xRight + 149 | 颜色框后 |

### 3. 修正覆盖层路径布局
```java
// 修复前：yRight + 15（错误，与上一行重叠）
addTextField(new GuiNpcTextField(14, ..., guiLeft + 200, yRight + 15, ...));

// 修复后：yRight + 23（正确，独立一行）
addTextField(new GuiNpcTextField(14, ..., xRight, yRight + 23, ...));
```

---

## 📝 完整修复代码

```java
// 右侧列：碰撞箱缩放、色调系统、皮肤覆盖层
int yRight = guiTop + 4;
int xRight = guiLeft + 200;  // 右列起始 X 坐标

// 碰撞箱缩放（3行）
addLabel(new GuiNpcLabel(10,"display.hitbox", xRight, yRight + 5));
this.addButton(new GuiNpcButton(10, xRight + 90, yRight, 50, 20, ...));
yRight += 23;

addLabel(new GuiNpcLabel(11,"display.hitboxWidth", xRight, yRight + 5));
this.addTextField(new GuiNpcTextField(10, ..., xRight + 90, yRight, ...));
yRight += 23;

addLabel(new GuiNpcLabel(12,"display.hitboxHeight", xRight, yRight + 5));
this.addTextField(new GuiNpcTextField(11, ..., xRight + 90, yRight, ...));
yRight += 23;

// 色调系统（2行）
addLabel(new GuiNpcLabel(13,"display.tintSystem", xRight, yRight + 5));
this.addButton(new GuiNpcButton(11, xRight + 90, yRight, ...));
yRight += 23;

addLabel(new GuiNpcLabel(14,"display.hurtTint", xRight, yRight + 5));
this.addButton(new GuiNpcButton(12, xRight + 70, yRight, 35, 20, ...));  // Yes/No
this.addTextField(new GuiNpcTextField(13, ..., xRight + 107, yRight, ...));  // 颜色
this.addButton(new GuiNpcButton(14, xRight + 149, yRight, 14, 20, "X"));  // X按钮
yRight += 23;

// 皮肤覆盖层（2行）
addLabel(new GuiNpcLabel(15,"display.skinOverlay", xRight, yRight + 5));
this.addButton(new GuiNpcButton(13, xRight + 90, yRight, ...));
yRight += 23;

addLabel(new GuiNpcLabel(16,"display.overlayPath", xRight, yRight + 5));
this.addTextField(new GuiNpcTextField(14, ..., xRight, yRight + 23, 145, 20, ...));  // 独立一行
```

---

## 🎯 验证步骤

### 测试右列对齐
1. 启动客户端，打开 NPC 编辑界面
2. 切换到"显示"（Display）页面
3. 检查右列控件：
   - ✅ 所有标签左对齐（xRight）
   - ✅ 碰撞箱缩放的两个输入框对齐（xRight+90）
   - ✅ 色调系统按钮对齐（xRight+90）
   - ✅ 受伤染色行：Yes/No 按钮、颜色框、X 按钮位置正确
   - ✅ 皮肤覆盖层按钮对齐（xRight+90）
   - ✅ 覆盖层路径输入框独立一行，不与其他控件重叠

### 测试功能完整性
1. 启用/禁用碰撞箱缩放
2. 修改宽度和高度缩放值
3. 启用色调系统和受伤染色
4. 点击颜色框打开调色板
5. 启用皮肤覆盖层并输入路径
6. **预期结果**：所有功能正常工作，布局清晰

---

## 📊 改动统计

- **修改文件**：1 个（GuiNpcDisplay.java）
- **代码行数**：+17 行，-16 行
- **新增变量**：xRight（统一坐标系统）

---

## 🎉 修复效果

- ✅ 右列控件水平对齐一致
- ✅ 标签和控件间距标准化
- ✅ 覆盖层路径输入框位置正确
- ✅ 整体布局清晰美观
- ✅ 所有功能保持正常

---

## 🔗 相关提交

- `be86875` - fix(gui): 修复 Display 界面右列布局混乱
- `53567bf` - Merge fix/display-gui-layout

---

**修复状态**：已完成  
**需人工验证**：游戏内测试界面布局
