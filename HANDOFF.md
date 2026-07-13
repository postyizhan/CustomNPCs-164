# 工作交接文档 - CustomNPCs 1.6.4 回移项目

> 创建时间：2026-07-13 13:55  
> 当前上下文：510k/1000k (51%)  
> 状态：需要子代理接替修复 GUI 布局问题

## 🚨 紧急问题清单

### 1. Display GUI 布局混乱（高优先级）
**问题描述**：
- 右列控件位置错误，覆盖层路径输入框位置异常
- "宽度缩放"/"覆盖层射线"等标签显示在错误位置
- 右列控件的 Y 坐标计算有误

**根本原因**：
- `GuiNpcDisplay.initGui()` 的右列布局使用了错误的 Y 起始坐标
- 覆盖层路径输入框使用了两行布局（yRight+15）但标签仍在 yRight+5

**修复方案**：
1. 读取 `src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcDisplay.java`
2. 重新计算右列控件的 Y 坐标：
   ```java
   int yRight = guiTop + 4;  // 起始Y与左列一致
   
   // 碰撞箱缩放（3行）
   addLabel(..., guiLeft + 200, yRight + 5);
   addButton(..., guiLeft + 290, yRight, ...);
   yRight += 23;
   addLabel(..., guiLeft + 200, yRight + 5);
   addTextField(..., guiLeft + 290, yRight, ...);
   yRight += 23;
   addLabel(..., guiLeft + 200, yRight + 5);
   addTextField(..., guiLeft + 290, yRight, ...);
   yRight += 23;
   
   // 色调系统（2行）
   addLabel(..., guiLeft + 200, yRight + 5);
   addButton(..., guiLeft + 290, yRight, ...);
   yRight += 23;
   addLabel(..., guiLeft + 200, yRight + 5);
   addButton(..., guiLeft + 270, yRight, ...);  // 启用按钮
   addTextField(..., guiLeft + 307, yRight, ...); // 颜色输入框
   addButton(..., guiLeft + 349, yRight, ...);   // X按钮
   yRight += 23;
   
   // 皮肤覆盖层（2行）
   addLabel(..., guiLeft + 200, yRight + 5);
   addButton(..., guiLeft + 290, yRight, ...);
   yRight += 23;
   addLabel(..., guiLeft + 200, yRight + 5);
   addTextField(..., guiLeft + 200, yRight + 15, 145, 20, ...);  // 路径输入框在标签下方
   ```

### 2. RoleMount 不能操纵（高优先级）
**问题描述**：
- 玩家可以骑乘 NPC，但无法用 WASD 控制移动
- 原版 `mountEntity()` 机制需要额外的移动输入监听

**根本原因**：
- `RoleMount` 只实现了骑乘交互，未实现移动控制逻辑
- 需要在 `EntityNPCInterface.onLivingUpdate()` 监听骑乘玩家的输入

**修复方案**：
1. 在 `EntityNPCInterface.onLivingUpdate()` 添加：
   ```java
   // 在方法末尾添加
   if (this.riddenByEntity instanceof EntityPlayer && this.roleInterface instanceof RoleMount) {
       EntityPlayer rider = (EntityPlayer) this.riddenByEntity;
       
       // 应用玩家的移动输入到 NPC
       float strafe = rider.moveStrafing;
       float forward = rider.moveForward;
       
       if (forward != 0.0F || strafe != 0.0F) {
           float speed = 0.35F;  // 移动速度
           float yaw = rider.rotationYaw;
           
           // 计算移动方向
           this.motionX += (double)(MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI) * forward * speed);
           this.motionZ += (double)(MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI) * forward * speed);
       }
       
       // 跳跃控制（可选，需要 RoleMount.getJumpStrength()）
       // if (rider.movementInput.jump && this.onGround) {
       //     this.motionY = 0.42D * ((RoleMount)this.roleInterface).getJumpStrength();
       // }
   }
   ```

### 3. RoleMount 不能编辑（中优先级）
**问题描述**：
- 选择 Mount 角色后，高级页面的"编辑"按钮被禁用
- 无法配置跳跃强度/允许疾跑等参数

**根本原因**：
- `GuiNpcAdvanced` 的按钮3禁用逻辑包含了 Mount：
  ```java
  getButton(3).enabled = npc.advanced.role != EnumRoleType.None 
      && npc.advanced.role != EnumRoleType.Postman 
      && npc.advanced.role != EnumRoleType.Mount;  // ← 这里错误禁用了
  ```

**修复方案**：
1. 移除 Mount 的禁用逻辑：
   ```java
   getButton(3).enabled = npc.advanced.role != EnumRoleType.None 
       && npc.advanced.role != EnumRoleType.Postman;
   ```

2. 创建 `GuiNpcRoleMount` 配置界面（可选）：
   ```java
   // 在 src/main/java/noppes/npcs/client/gui/roles/GuiNpcRoleMount.java
   public class GuiNpcRoleMount extends GuiNPCInterface implements IGuiData {
       private RoleMount role;
       
       public void initGui() {
           super.initGui();
           // 跳跃强度滑块
           // 允许疾跑复选框
       }
   }
   ```

3. 在 `GuiNpcAdvanced.actionPerformed()` 添加 Mount 分支：
   ```java
   switch(npc.advanced.role){
       // ... 现有代码 ...
       case Mount:
           NoppesUtil.openGUI(player, new GuiNpcRoleMount(npc));
           break;
   }
   ```

## 📁 相关文件清单

### 需要修改的文件
1. `src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcDisplay.java` - 修复右列布局
2. `src/main/java/noppes/npcs/EntityNPCInterface.java` - 添加骑乘移动控制
3. `src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcAdvanced.java` - 移除 Mount 禁用逻辑

### 可选创建的文件
1. `src/main/java/noppes/npcs/client/gui/roles/GuiNpcRoleMount.java` - Mount 配置界面

## 🔧 测试验证步骤

### GUI 布局验证
1. `./gradlew runClient`
2. 创造模式 → NPC 法杖生成 NPC
3. 扳手打开编辑 → Display 页
4. 检查右列控件位置：
   - 碰撞箱缩放：3 行（启用+宽度+高度）
   - 色调系统：2 行（启用+受伤色调+调色盘按钮）
   - 皮肤覆盖层：2 行（启用+路径输入框）
5. 所有标签应在左侧，输入框/按钮应对齐

### 骑乘控制验证
1. 高级页面 → 角色选择 Mount
2. 生存模式右键 NPC 骑乘
3. 按 **W** 键前进 → NPC 应向玩家视角方向移动
4. 按 **A/D** 键 → NPC 应左右移动
5. 按 **空格** → （如果实现了跳跃控制）NPC 应跳跃

### Mount 编辑验证
1. 高级页面 → 角色选择 Mount
2. "编辑"按钮应**可点击**（enabled=true）
3. 点击后应打开 Mount 配置界面（如果已实现）或提示未实现

## 🎯 Git 提交建议

修复完成后，建议分 3 个提交：

```bash
git add src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcDisplay.java
git commit -m "fix(gui): 修复Display右列布局，重新计算Y坐标"

git add src/main/java/noppes/npcs/EntityNPCInterface.java
git commit -m "feat(role): Mount角色添加骑乘移动控制逻辑"

git add src/main/java/noppes/npcs/client/gui/mainmenu/GuiNpcAdvanced.java
git commit -m "fix(gui): 移除Mount角色的编辑按钮禁用逻辑"
```

## 📊 项目状态快照

### 已完成的 M 级任务（6/6）
- ✅ #15 全键值汉化
- ✅ #8 碰撞箱缩放
- ✅ #10 色调系统
- ✅ #9 皮肤覆盖层
- ✅ #7 坐骑角色
- ✅ #5 战术AI

### 最近提交（最新5条）
```
efddbff Merge feat/role-mount-gui
8e48e65 feat(role): Mount角色添加到GUI选择列表
76db6cc Merge feat/color-picker-tint
442b337 feat(gui): 色调系统添加调色盘按钮
39e5fb2 Merge fix/display-gui-layout
```

### 编译状态
- ✅ BUILD SUCCESSFUL (最后验证时间：13:52)
- Java 版本：Java 7
- Gradle 版本：9.5.0

## 🤖 子代理任务分配

建议创建 1 个子代理完成所有修复：

```python
Agent(
    prompt="""
    修复 CustomNPCs-164 项目的 3 个问题：
    
    1. GuiNpcDisplay 右列布局混乱 - 重新计算 yRight 坐标
    2. RoleMount 骑乘后无法移动 - 在 EntityNPCInterface.onLivingUpdate() 添加移动控制
    3. Mount 角色无法编辑 - 移除 GuiNpcAdvanced 的 Mount 禁用逻辑
    
    读取交接文档 HANDOFF.md 了解详细方案，修复后编译验证。
    """,
    description="修复Display布局和Mount骑乘控制"
)
```

## 📝 注意事项

1. **Y 坐标计算**：右列起始 Y 必须与左列一致（`guiTop + 4`），每行间隔 23 像素
2. **移动控制**：需要使用 `MathHelper.sin/cos` 计算方向向量，避免使用 Java 8 API
3. **编译目标**：锁定 Java 7，不要使用 Lambda/Stream 等 Java 8 特性
4. **测试环境**：`./gradlew runClient` 启动客户端，切换创造模式测试

## 🔗 相关文档

- 项目指南：`CLAUDE.md`
- TODO 清单：`TODO.md`
- 测试文档：`docs/porting/`
- 技术决策：`TODO.md` 底部的 ADR 章节

---

**下一步行动**：派出子代理执行修复，完成后合并到 main 分支。
