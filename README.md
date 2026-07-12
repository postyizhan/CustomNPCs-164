# CustomNPCs-164

**由 postyizhan 维护的 CustomNPCs Minecraft 1.6.4 延续版本。**

CustomNPCs 是 [Noppes](https://www.kodevelopment.nl/) 的原创作品。本 1.6.4 分支经 Noppes 于 2026-06-11 亲自授权继续维护,旨在为社区提供一个可信、开源的延续版本,让大家无需依赖非法反编译来更新和维护 1.6.4 版本。

## 目标

- 在 GitHub 上保持完整源码开放,任何人都无构建限制
- 在 Modrinth 发布构建产物
- 修复既有 bug、加入体验改进
- 酌情从更新版本的 CustomNPCs 及 CustomNPCs+ 回移特性

## 授权 / License

本项目采用 **PolyForm Noncommercial 1.0.0** 协议,详见 [LICENSE](LICENSE)。

> ⚠️ 本模组**免费且非商业**。你**不得**出售它,也**不得**将其收录进任何付费整合包、付费服务器包或其他付费资源。任何再分发都必须保持免费,并保留原作者 Noppes 的署名。
>
> This mod is **free and non-commercial**. You may NOT sell it, or include it in any paid modpack, paid server package, or other paid resource. Redistribution must keep it free and credit Noppes.

## 运行要求

- Minecraft 1.6.4 + Forge 9.11.1.960
- **Java 7 或更高运行时**:构建产物为 Java 7 字节码。这是硬上限——1.6.4 Forge 的 FML 内置 ASM 4.1 最高只能解析 Java 7 字节码,更高版本的 class 文件会被 FML 当作损坏文件而忽略整个模组。
- 用 **Java 8 或更高**运行 1.6.4 Forge 时,mods 目录还需放入 [LegacyJavaFixer](https://github.com/MinecraftForge/LegacyJavaFixer)(修复老 Forge 在新 JVM 上的模组排序崩溃,属 Forge 自身问题,与本模组无关)。

## 构建

原作者未提供构建环境,本仓库的 Forge Gradle 构建环境由 postyizhan 自行搭建(ForgeGradle 7 + MC 1.6.4 / Forge 9.11.1.960,Java 8 工具链)。

```bash
./gradlew build
```

产物位于 `build/libs/`。

## 致谢

- **Noppes** — CustomNPCs 原作者
- **postyizhan** — 1.6.4 延续版本维护者
