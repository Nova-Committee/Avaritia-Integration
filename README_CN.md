<p align="center">
    <img width="690" src="web/logo.png" alt="title">
</p>
<hr>
<p align="center">
    <a href="https://www.curseforge.com/minecraft/mc-mods/avaritia-integration">
        <img src="https://img.shields.io/badge/Available%20for-MC%201.21.1-c70039" alt="Supported Versions">
    </a>
    <a href="https://www.curseforge.com/minecraft/mc-mods/avaritia-integration">
        <img src="https://cf.way2muchnoise.eu/1347675.svg" alt="CurseForge Download">
    </a>
    <img src="https://img.shields.io/badge/license-MIT%2FARR-green" alt="License">
</p>

<p align="center">
    <a href="README.md">English</a> |
    <a href="README_CN.md">简体中文</a>
</p>

## **简介:**
* 本模组为 Re:Avaritia 与多个模组提供 Minecraft 1.21.1（NeoForge）联动。

## **✏️作者:**
- 程序: `cnlimiter` `IAFEnvoy` `Frostbite-time` `CreepingCreeper` `Oganesson897` `cu6` `lostmyself8` `Lounode` `Y-Xiao233`
- 美术: `MHanHanBing` `Neo-Tix`

## **🔒许可证:**
- 代码: [MIT](LICENSE)
- 贴图资源：**保留所有权利（All Rights Reserved，ARR）**。贴图所有者：**MHanHanBing**。未经作者事先书面许可，不得复制、再分发、修改或用于整合包。所有者保留追究未经授权使用行为法律责任的权利。

## **❗注意:**
* 将本模组的贴图资源用于整合包前，须取得贴图所有者的书面许可。

## **🎮联动**
* ✅: 完整支持<br>
* 🚧: 部分支持<br>
* 🔲: 计划支持<br>
* ❌: 暂不计划<br>

| 模组 | 内容 | 状态 | 备注 |
|-------------------------------------|:--------|:------:|-------|
| Applied Energistics 2 | 无尽 ME 组件与存储元件 | ✅ | |
| Botania | 阿斯加德蒲公英、索利安德之嗜、无尽魔力池、无尽小土豆、阿尔法火花 | ✅ | 需要 Botania 455-SNAPSHOT 或更高 |
| Create | 创世构件 / 化合物、水晶矩阵机壳、中子机械压力机、矩阵动力搅拌器、极限工作盆 / 烈焰人燃烧室 / 置物台 / 鼓风机 / 粉碎轮 | ✅ | |
| Ender IO | 无尽电容、无尽 / 中子研磨球 | ✅ | |
| Industrial Foregoing | 中子 / 烈焰立方 / 水晶矩阵 / 无尽插件，虚空物质，远古髓液 | ✅ | |
| Industrial Foregoing Extra Upgrades | 烈焰立方 / 水晶矩阵 / 中子 / 无尽能量插件 | ✅ | |
| Mekanism | 中子态素收集器、奇点压缩机、工厂、合金、电路、能量板 | ✅ | |
| Mekanism Generators | 无尽 / 中子太阳能发电机 | ✅ | |
| PneumaticCraft: Repressurized | 创造压缩铁锭 | ✅ | |
| Refined Storage | 无限存储元件 | ✅ | |
| Thermal Expansion | 创造升级 | ✅ | 仅数据包 |
| Tinkers' Construct | 无尽 / 中子 / 水晶矩阵 / 烈焰立方材料与词条 | ✅ | 含流体、方块和箭物品注册 |
| Mystical Agriculture | 烈焰立方、水晶矩阵和无尽作物 | 🚧 | 已有实现，运行未验 |
| Apotheosis | | 🔲 | |
| Blood Magic | | 🔲 | |
| GregTechCEu Modern | 材料 / 元素注册与部件统一 | 🚧 | 已有实现，运行未验 |
| NuclearCraft: Neoteric | | 🔲 | |
| SlashBlade | 宇宙终焉之刃与陨剑技能 / 实体 | 🚧 | 已有实现，运行未验 |
| Tetra | | 🔲 | |
| Thaumaturge | 极元始珍珠与终结要素 | 🚧 | 需另行安装 Thaumaturge 1.1.0 或更新版本 |

### Thaumaturge

在注魔矩阵中心放 1 颗全新元始珍珠，外圈放 7 颗。顺利完成后，中心变为极元始珍珠，外圈的 7 颗各增加 1 点损伤。原生配方也接受已磨损的珍珠、结节和微粒。混用不同损伤值时，矩阵可能在同一座踏台继续消耗上一步留下的剩余物。

在研究台用贪婪和异域合成 1 点终结研究点。首次发现终结时，Thaumaturge 还会额外奖励 2 点。注魔用的终结源质可通过魔豆杂交、蒸馏和装罐获得。「七阶」是设定文案。按基础要素在第一层计算，最长链为六层（地 → 生命 → 死亡 → 灵魂 → 贪婪 → 终结）。

已有 `config/avaritia/integration/load_rules.json` 的实例请在现有配置中加入下面这一项并重启。原图与离线转换命令见[贴图转换说明](integrations/thaumaturge/tools/README.md)。

```json
"avaritia_integration_thaumaturge": {
  "dependencies": [{"modid": "thaumaturge", "minVersion": "1.1.0"}],
  "enabled": true
}
```
