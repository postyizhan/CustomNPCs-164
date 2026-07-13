# Mount 角色配置界面回移文档

## 特性来源
完善 CustomNPCs 1.6.4 的 Mount 角色功能，添加配置界面

## 改动文件清单
- `src/main/java/noppes/npcs/client/gui/roles/GuiNpcRoleMount.java` - 新增，Mount 角色配置界面（112 行）
- `src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcAdvanced.java` - 修改，添加 Mount 配置入口
- `src/main/resources/assets/customnpcs/lang/zh_CN.lang` - 修改，添加语言键

## 功能说明

### GuiNpcRoleMount（配置界面）
提供 Mount 角色的参数配置：
- **跳跃强度**：滑块控件，范围 0.0-2.0，步长 0.1（ID 0）
- **允许疾跑**：Yes/No 按钮，控制是否允许骑乘时疾跑加速（ID 1）
- **完成按钮**：保存配置并关闭界面（ID 66）

### 界面布局
- 继承 `GuiNPCInterface`
- 实现 `IGuiData` 和 `ISliderListener` 接口
- 背景：`menubg.png`
- 尺寸：256x216

### 数据保存
- 通过 `NoppesUtil.sendData()` 发送 `EnumPacketType.MainmenuAdvancedSave`
- 服务端自动同步到 `DataAdvanced` 的 NBT 存储
- 关闭界面时自动保存

## 前置条件
1. 创造模式
2. NPC 法杖（生成 NPC）
3. NPC 扳手（打开编辑界面）
4. 将 NPC 角色设置为 Mount

## 测试步骤

### Agent 已验证项
- [x] 编译通过（`./gradlew build` - BUILD SUCCESSFUL）
- [x] Java 7 语法检查（无 Lambda/Stream）
- [x] GUI 控件完整性（滑块 + 按钮）
- [x] 数据保存逻辑正确（onGuiClosed 调用 save）
- [x] 滑块步长正确（0.1 步长）

### 需人工游戏内验证项

#### 1. 打开配置界面
1. 启动客户端（`./gradlew runClient`）
2. 切换到创造模式（`/gamemode 1`）
3. 用 NPC 法杖生成一个 NPC
4. 用 NPC 扳手右键打开编辑界面
5. 切换到"高级"页面
6. 将角色设置为"坐骑"（Mount）
7. 点击"角色"按钮
8. **预期结果**：打开 Mount 配置界面，显示跳跃强度滑块和允许疾跑按钮

#### 2. 配置跳跃强度
1. 在 Mount 配置界面中
2. 拖动跳跃强度滑块
3. **预期结果**：
   - 滑块显示值从 0.0 到 2.0
   - 步长为 0.1（例如：1.0 → 1.1 → 1.2）
   - 标签实时更新为"跳跃强度: X.X"
4. 设置跳跃强度为 1.5
5. 点击"完成"按钮关闭界面
6. 重新打开 Mount 配置界面
7. **预期结果**：跳跃强度保持为 1.5

#### 3. 配置允许疾跑
1. 在 Mount 配置界面中
2. 点击"允许疾跑"按钮切换为"否"
3. 点击"完成"按钮
4. 重新打开 Mount 配置界面
5. **预期结果**：允许疾跑显示为"否"

#### 4. 骑乘功能验证
1. 配置 Mount 角色的跳跃强度为 2.0
2. 配置允许疾跑为"是"
3. 切换到生存模式（`/gamemode 0`）
4. 右键 NPC 骑乘
5. 按空格键跳跃
6. **预期结果**：跳跃高度明显高于默认（约 2 格高）
7. 按住 Ctrl 键疾跑
8. **预期结果**：移动速度加快

#### 5. 禁用疾跑验证
1. 配置允许疾跑为"否"
2. 骑乘 NPC
3. 按住 Ctrl 键
4. **预期结果**：无法疾跑，速度不变

## 回归检查点
- [ ] 不影响其他 NPC 功能
- [ ] 不影响其他角色的配置界面
- [ ] Mount 角色的其他功能正常（骑乘/下马）
- [ ] 配置保存到 NBT 正确
- [ ] 重新加载世界后配置保留

## 技术细节

### 滑块实现
```java
// 初始化滑块（范围 0.0-2.0，通过 sliderValue 0.0-1.0 映射）
addSlider(new GuiNpcSlider(this, 0, guiLeft + 53, guiTop + 36, null,
    getJumpStrengthText(jumpStrength), jumpStrength / 2.0F));

// 滑块值更新（步长 0.1）
int step = Math.round(slider.sliderValue * 20.0F);  // 0-20
float jumpStrength = step / 10.0F;                  // 0.0-2.0
slider.sliderValue = step / 20.0F;                  // 回写归一化值
```

### 数据保存
```java
@Override
public void save() {
    NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, 
        npc.advanced.writeToNBT(new NBTTagCompound()));
}
```

### RoleMount 字段
- `jumpStrength` (float) - 跳跃强度，范围 0.0-2.0，默认 1.0
- `allowSprint` (boolean) - 允许疾跑，默认 true

### NBT 标签
- `MountJumpStrength` (float) - 跳跃强度
- `MountAllowSprint` (boolean) - 允许疾跑

## 已知限制
- 界面仅支持跳跃强度和疾跑配置
- 未来可扩展：移动速度倍率、最大乘客数量等

## 相关提交
- `55d6dba` - feat(role): 添加 Mount 角色配置界面
- `b1ce72e` - Merge feat/mount-gui

---

**测试状态**：需人工游戏内验证  
**优先级**：中（完善现有功能）
