# Avaritia-Integration 1.20.1 与 1.21.1 数值设计对比

> **历史对比快照**：下文表格的旧/新值对应调查时基线，不是修复和反向同步后的实时状态。2026-11-12后续已完成新版Create永久热源、匠魂能力及IFEU合并修复，并在独立1.20.1工作树同步六项数值。现状以[实施映射](numeric-sync-1.21.1-to-1.20.1.md)和[交付验收](../.dsh/evidence/sync-1201/final-report.md)为准。客户端未验边界见各报告。

日期：2026-11-12。旧版基线 `fd53d8a6c5cdd6b240ee35815f848e6e1b265b3d`（Forge1.20.1）；新版基线 `8e8edd1f040a61aa5bed423df72e663fc07c4a91`（NeoForge1.21.1）。本调查不调整任何玩法数值。这里的“当前值”取新版当前源码及发布资源，不直接照抄历史强化提交。倍率统一为新/旧；耗时减少对应理论速度倒数，不代表端到端吞吐实测。

## 结论与需用户判断的项

1. 两版确有成体系的主动强化，`ddd70d4`涉及Mekanism、EnderIO、Industrial Foregoing、IFEU及Botania火花/夹竹桃；但它不是当前所有数值差异的唯一来源。Mekanism随后两次调低机器耗能；无尽太阳能又增加BigInteger内部储能/注入。
2. 优先值得审视的不是AE2：AE2两类元件容量语义与待机耗能保持不变。反而是Create旧版十倍搅拌/瞬时粉碎实现未保留，燃烧器蛋糕时长缩短，以及Mekanism中子太阳能相对旧近上限实现大幅降低。
3. EnderIO中子研磨球Output1→5重复整份产物列表，Bonus5→2.5控制每轮概率；最终副产期望从min(1,5p)变为5×min(1,2.5p)，是旧版2.5～5倍，不能据Bonus字段减半判断最终副产降低。IFEU原四档8/16/32/64中16/32/64对基础容量≥3的机器在容量、单次输入、输出均等价，后续已合并获取并保留旧ID无损转换。
4. 匠魂材料数值一致，但旧TicHandler的无尽甲事件尚未整体迁移；这是行为缺失，不能因材料JSON相同就声称防护体验等价。
5. “无合理依据”需要区分：下表有明确提交意图的极高数值仍可供用户重新平衡；没有设计说明的Create差异只能列为移植实现差异/待决，不能擅自认定作者有意削弱或直接回退。
6. Blood Magic：用户已确认1.21.1不需要该模块。按需求不实施，不能继续列为上游缺失导致的技术阻塞。

## 检查范围与证据强度

检查Git跟踪的旧281个/新272个生产Java文件、资源与生成资源；对JSON路径、单复数recipe目录、字段重命名、NBT→组件和模块迁移做人工复核。自动清单只负责找候选，不是行为测试。[原始清单](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/inventory.json)、[Java差异候选](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/java-mekanism.txt)、[252条配方消耗/输出附表](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/recipe-ledger.csv)、[JSON差异候选](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/json-numeric-differences.json)、[配方归一化差异](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/normalized-recipe-diffs.txt)。附表含各条原始来源，可复核完整配方而非只看代表项；其中NBT字符串和默认count需按消费者解释。

本报告是**静态实现/历史调查PASS**，不是全模块运行验收。未逐机器测吞吐、太阳天气/遮挡、上游升级组合或整合包覆盖配置。J、FE、uI、AE等保持各API原单位，不跨模组混算。升级“tier”不自动当作最终倍率。

## 历史索引

|标记|提交及其作用|
|---|---|
|S|[ddd70d4](https://github.com/Nova-Committee/Avaritia-Integration/commit/ddd70d4cf120d53d2a319d603d63b7928c9f6cb0)：Scale infinity-tier integration energy, speed, and augment data；[完整patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-ddd70d4.patch)|
|M1|[67c12a4](https://github.com/Nova-Committee/Avaritia-Integration/commit/67c12a4ab0a3487087131268fde0eb7290031f02)：机器耗能与缓冲调至线缆可供给范围；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-67c12a4.patch)|
|M2|[f570b4c](https://github.com/Nova-Committee/Avaritia-Integration/commit/f570b4cdb8428c47ee0ce841f0504bf9cab64a5b)：BigInteger太阳能与进一步降低机器耗能；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-f570b4c.patch)|
|B1|[1ee07a0](https://github.com/Nova-Committee/Avaritia-Integration/commit/1ee07a0017132960b700256b3c6cb33425a9f6f6)：加强蒲公英；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-1ee07a0.patch)|
|B2|[8ba3177](https://github.com/Nova-Committee/Avaritia-Integration/commit/8ba3177590f86345fe951aaafcbefbca35565f8b)：魔力池、火花扫描与土豆范围；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-8ba3177.patch)|
|C|[a5339dc](https://github.com/Nova-Committee/Avaritia-Integration/commit/a5339dc2adb34167fb1cdac88f244f783729c23c)：Create6机器及剩余联动移植；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-a5339dc.patch)|
|I|[ae0ca53](https://github.com/Nova-Committee/Avaritia-Integration/commit/ae0ca533adadd16d1478ae8ed0a9c09084ad3144)：新版本线增加IFEU；旧1.20.1基线无该模块；[patch](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-ae0ca53.patch)|

## Mekanism机器、工厂和能量平板

**关键纠偏：旧UsageConfig虽声明50/100J/t，但真实BlockTypes绑定的是上游chemicalCrystallizer。** 所以有效旧值应为上游默认400J/t、160000J容量，而不是未消费的50/100。旧依赖精确为Mekanism10.4.15.75；[绑定:32](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/mekanism/common/registry/MekIntegrationBlockTypes.java#L32)、[上游耗能](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/old-UsageConfig.txt#L245)、[上游容量](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/old-StorageConfig.txt#L257)。源码注释里“旧80kJ”不符合这个实际依赖，未采用该注释作数值证据。

|项目|1.20.1有效默认值|1.21.1当前值|倍率/变化|依据与定性|
|---|---:|---:|---|---|
|中子收集器每工作tick耗能|400J|100000J|250×|S先改10000000，M1/M2再降低；主动调整|
|奇点压缩机每工作tick耗能|400J|800000J|2000×|S先改100000000，后续M1/M2降低；主动调整|
|中子收集器容量|160000J|20000000J|125×|当前为耗能×200；M1/M2|
|奇点压缩机容量|160000J|160000000J|1000×|当前为耗能×200；M1/M2|
|中子收集器/收集工厂基础周期|200tick|40tick|耗时0.2×，理想速度5×|S|
|奇点压缩机/压缩工厂基础周期|200tick|20tick|耗时0.1×，理想速度10×|S|
|无升级单次中子收集电量|80000J|4000000J|50×|上述耗能×周期，计算值|
|无升级单次压缩电量|80000J|16000000J|200×|计算值|
|工厂耗能|400J/活跃进程/t|100000或800000J/活跃进程/t|同上；还乘活跃进程数|不能把单机耗能直接当整厂耗能|
|工厂容量|80000J×进程数P|10000000J×P或80000000J×P|125×或1000×（相同P）|两版都取max(原机器容量/2,耗能)×P；新用long钳制|
|单台中子收集器气体槽|10000mB|1000000mB|100×|S|
|工厂气槽|每槽10000mB×P，共P槽|相同|1×（相同P）|**没有跟单机一起扩大100倍**，应单列供判断|
|压缩输入槽上限|65536物品|65536物品|1×|保留|
|中子平板容量|2147483647J|1000000000000000J|约465661.29×|S恢复专属高阶参数|
|中子平板充放参数|2147483647J/t|1000000000000J/t|约465.66×|S|
|无尽平板容量/充放参数|2147483647J(/t)|9223372036854775807J(/t)|4294967298×|S；有限long上限，不是数学无限|

当前[常量:8](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanism/common/config/MekIntegrationEnergy.java#L8)、[工厂容量:52](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanism/common/content/blocktype/MekIntegrationFactory.java#L52)、[工厂气槽:98](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanism/common/tile/factory/TileEntityChemicalToItemMIFactory.java#L98)、[旧平板:20](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/mekanism/common/registry/MekIntegrationItems.java#L20)。工厂高阶P由上游/附属枚举提供，新版显式支持Evolved额外层级；跨不同附属版本不伪造同P吞吐倍率。

### Mekanism太阳能：并非全都比旧版高

旧FloatingLong.MAX_VALUE实际是**18446744073709551615.9999**（无符号64位整数+四位小数；其divide(2)会舍入为9223372036854775808，即L+1），不能误写Double.MAX_VALUE。[旧类型初始化](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/FloatingLong.txt#L1813)。记该值为F，新long上限为L=9223372036854775807。

|项目|1.20.1|1.21.1当前实现|新/旧|
|---|---|---|---|
|中子普通太阳能配置峰值|FloatingLong.divide(2)实际为L+1，约9.223372×10^18J/t|10^10J/t|约1.0842×10^-9|
|中子高级太阳能配置峰值|同上，L+1|10^11J/t|约1.0842×10^-8|
|中子太阳能缓冲（普通/高级）|F|10^13J|约5.4210×10^-7|
|无尽太阳能配置峰值（普通/高级）|F|L|约0.5×，仅此API字段|
|无尽太阳能真实内部缓冲|F|BigInteger(BigDecimal.valueOf(Double.MAX_VALUE))+L，约1.797693×10^308J|约9.7453×10^288×|
|无尽太阳能内部注入|按天气日照系数计算的F峰值|上游触发内部生产插入时，改注入上述BigInteger量|不能用一个固定天气无关倍率表达|
|无尽太阳能对外I/O|FloatingLong范围|单次long接口至多L|内部巨大值不能当线缆单次传输率|

中子参数拆分来自S，有明确“split neutron vs infinity generation”意图；无尽BigInteger来自M2，不属于S原始设计。必须同时写配置字段与实际Mixin消费者。[新内部注入:25](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism-generators/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanismgenerator/mixin/MixinTileEntitySolarGenerator.java#L25)、[大整数容量:16](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanism/common/config/MekBigEnergy.java#L16)、[真实缓冲替换:40](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/mekanism-generators/src/main/java/committee/nova/mods/avaritia_integration/integrations/mekanismgenerator/mixin/MixinTileEntityGenerator.java#L40)。没有实测全天候输出；不承诺每个世界tick都有内部注入。

#### 主动设计与上游类型限制：分别判断

精确旧依赖Mekanism10.4.15.75的真实计算已用JShell执行：`FloatingLong.MAX_VALUE.divide(2)`输出**9223372036854775808**，即L+1。数学上的F/2为9223372036854775807.99995，但FloatingLong保留四位小数并舍入，不能把数学值冒充实际返回值。见[原始执行记录](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/solar-type-limits.txt)、[精确计算附表](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/solar-type-limits.json)。

|变化|类型约束造成的最低限度变化|现有实现及历史定性|
|---|---|---|
|中子峰值：旧L+1→新10^10/10^11|long无法精确表达L+1，但钳制到L只少**1J/t**，相对损失约1.0842×10^-19；仍保持原数量级|S父版本已经采用L；S再从L改为10^10/10^11，是**有明确拆档意图的主动数值降低**。约8～9个数量级的下降并非long类型必然结果|
|中子缓冲：旧F→新10^13|若只用单一long，旧F无法完整保留；钳制L约为旧值的一半|降至10^13远低于long上限，是S的主动容量选择；不能全归因于类型迁移|
|无尽配置峰值/对外long接口：旧F→L|单个long参数无法保留旧无符号范围及小数；至多L|约0.5×的**单字段/单次接口上限**有类型约束依据；不代表整台发电机总产能必然减半|
|无尽内部缓冲/注入：旧F→约1.797693×10^308|long接口不强制内部也只用long；内部可另用大整数|M2主动引入BigInteger缓冲及注入，属于实现与数值设计选择；对外单次long上限仍在|

直接证据：S父版[普通中子实现](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/pre-scale-NeutronSolarGeneratorBlockEntity.java)、[高级中子实现](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/pre-scale-NeutronAdvancedSolarGeneratorBlockEntity.java)均返回Long.MAX_VALUE；[S修改前后](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/history-ddd70d4.patch#L630)明确把它们换成专属常量。因此“上游改成long”与“为何选择10^10/10^11”是两个问题：前者限制表示范围，后者有历史主动调整证据。该结论不等于现值平衡合理，也不自动产生恢复旧值的任务，仍交由用户判断。

### Mekanism配方消耗

已对照同名实际JSON：27种奇点压缩仍各消耗1000原料；中子锭富集仍4份→1；浸注转换粉末10、富集物80；中子/无尽合金仍消耗80/40化学量。收集配方所需量：钚/钋1000、氘/氚/融合燃料10000、中子尘2000、中子粒5000、方块1，两版相同。`amount→count`、`infusion_conversion→chemical_conversion`属于API格式变化，不算加料/减料。[完整附表](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/recipe-ledger.csv)。新版新增4种粉碎尘配方均1→1，高阶工厂升级配方见附表；属于新增可达内容，不能给不存在的旧配方强算倍率。

## Ender IO

|项目|1.20.1|1.21.1|倍率/定性|
|---|---:|---:|---|
|炽骨/水晶/中子电容等级|5/7/8|无注册|实现缺失；已决定本轮不补|
|无尽电容等级|10|1000|100×；S明确主动强化|
|同一线性消费者|B×10|B×1000|100×|
|同一二次消费者|B×100|B×1000000|10000×；整机吞吐未测|
|中子研磨球主产物倍率|grinding=1|OutputMultiplier=5|5×|
|中子研磨球每轮副产概率系数|chance=5|BonusMultiplier=2.5|字段0.5×；结合Output1→5后最终副产期望提升2.5～5×|
|中子研磨球耐久预算|32767|4000000|约122.074×|
|中子研磨球耗能系数|0.1|0.05|0.5×|
|无尽研磨球主产物/副产物倍率|5/5|5/5|1×|
|无尽研磨球耐久预算/耗能系数|2147483647/0.01|相同|1×|

重要：S的直接父版中子球为Output5、Bonus1、耐久40000，和旧1.20.1并非相同基线；S只把Bonus1→2.5、耐久40000→4000000、耗能0.1→0.05。所以不能据S标题说中子球全部属性相对1.20.1都提升。旧`grinding`与`chance`由[序列化器](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/old-GrindingBallRecipe$Serializer.txt#L31)及[getters](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/old-GrindingBallRecipe.txt#L113)核实映射，没按字段名字猜。

无尽电容合金炉理论容量640亿uI超int上限，`Math.round(float)`结果饱和到2147483647；未当真实机器测量值。[专项报告及依赖版本](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/enderio-capacitor-investigation/report.md)。两种研磨球制作均5锭→1，未改材料成本；无尽电容新有合成配方，而旧数据生成器对应部分被注释，不能把旧有注册等同于旧可正常合成。

## Industrial Foregoing / IFEU

材质顺序统一为炽骨/水晶/中子/无尽。

|项目|1.20.1|1.21.1|新/旧；来源|
|---|---|---|---|
|IF插件tier|3/5/8/12|4/8/16/32|1.333/1.6/2/2.667×；S|
|速度插件写入Speed|4/6/9/13|5/9/17/33|1.25/1.5/1.889/2.538×；S|
|处理插件写入Processing|4/6/9/13|5/9/17/33|同上；S|
|效率插件写入耗能系数|约0.7/0.5/0.2/-0.2|0.25/0.10/0.05/0.01|约0.357/0.2/0.25/负值不适合倍率；S修正负系数并强化|
|每个插件配方液体|1000/1000/1000/2000mB|相同|1×|
|每个插件配方时长|100/200/300/400tick|相同|1×|
|每个插件配方输出|1|1（带组件）|S修复配方产物真实携带augment，不只是创造栏有效|
|钻取长老骨髓输出|50mB|50mB|1×，维度/深度-64～256、权重8数值不变|
|钻取虚空物质输出|20mB|20mB|1×，深度-32～64、权重8不变|
|IFEU能量插件tier|无此模块|8/16/32/64|新增，无旧版倍率；I引入、S从7/8/9/10改为现值|
|IFEU合成液体/时长|无|1000/1000/1000/2000mB，100/200/300/400tick|新增；无尽档用长老骨髓，非IF三类的虚空物质|

依据：[旧注册](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/industrialforegoing/registry/IndustrialForegoingIntegrationItems.java#L24)、[新注册](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/industrialforegoing/src/main/java/committee/nova/mods/avaritia_integration/integrations/industrialforegoing/registry/IndustrialForegoingIntegrationItems.java#L26)、[新效率公式](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/industrialforegoing/src/main/java/committee/nova/mods/avaritia_integration/integrations/industrialforegoing/item/ModEfficiencyAddonItem.java#L23)、[旧实际NBT](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/generated/resources/data/avaritia_integration/recipes/dissolution_chamber/efficiency_addon_blaze_cube.json#L29)。

**IFEU高档容量饱和值得用户审视。** 精确项目依赖CF8428353的实际消费者为`min(int(baseCapacity×10^((tier+2)/2)),2147483647)`，同时设容量/最大输入/最大输出。[消费者](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/EnergyAddonEntry.txt#L44)、[另一机器类型](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/EnumPropertyEnergyAddonEntry.txt#L51)。8/16/32/64对应未钳制系数10^5/10^9/10^17/10^33；32与64对任何正整数基础容量都会到int上限，16也在基础容量≥3时饱和。tier32→64不能解释为实际容量翻倍，本轮未跑真实机器组合。

## Botania魔力、范围与效果

|项目|1.20.1|1.21.1|新/旧；来源|
|---|---:|---:|---|
|无尽魔力池容量|2147483647|2147483647|1×|
|魔力池对物品每tick基础充放上限|1000×(风箱数+1)|2147483647|无风箱约2147483.647×；B2，当前不再乘风箱|
|Alpha火花传输基准|100000|2147483647|21474.83647×；S，并修复多连接乘法溢出|
|Alpha扫描范围|上游SparkHelper范围（Integration无自定义值）|32格、每20tick重扫|B2；旧上游常见12格须以精确依赖核实，不凭常见值算倍率|
|阿斯加德蒲公英每目标发送上限|32000|10000000|312.5×；B1，不属于S|
|蒲公英内部填充/容量|2147483647|相同|1×；发送范围8格不变|
|夹竹桃每次成功伤害产魔|32|1000|31.25×；S。该常量按一次hurt成功发放，并非按伤害点乘算|
|夹竹桃普通击杀奖励|1500|50000|33.333×；S|
|夹竹桃鸡击杀奖励|5000|250000|50×；S|
|夹竹桃单次伤害|4|40|10×；S|
|夹竹桃容量|32767|10000000|约305.185×；S|
|夹竹桃搜索半径/周期|15格/20tick|相同|1×|
|无尽土豆效果持续|3600tick（3分钟）|12000tick（10分钟）|3.333×；B2|
|土豆效果amplifier|1（等级II）|4（等级V）|增加3级；不能按等级数字当效果倍率|
|土豆名义radius|10格|32格|3.2×；B2|
|土豆真实距离判定|距离平方<10，且AABB高度±2|距离平方≤1024，AABB各向扩大32|旧半径受sqrt(10)≈3.162及高度限制；修正并扩大，不能仅写3.2×|
|土豆补饥饿/饱和参数|20/30|20/30|1×|

依据：[旧魔力池充放](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/botania/entity/InfinityManaPoolBlockEntity.java#L330)、[新充放](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/botania/src/main/java/committee/nova/mods/avaritia_integration/integrations/botania/entity/InfinityManaPoolBlockEntity.java#L364)、[夹竹桃常量及真实发放](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/botania/src/main/java/committee/nova/mods/avaritia_integration/integrations/botania/entity/SoarleanderBlockEntity.java#L85)、[土豆现值](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/botania/src/main/java/committee/nova/mods/avaritia_integration/integrations/botania/entity/InfinityTinyPotatoBlockEntity.java#L109)。魔力池配方扣魔力按上游配方执行；本轮未发现统一乘/除倍率，不能写新池可免费合成。蒲公英合成由旧9×9转为新版花药台配方，这是获取成本/流程变化而不是发电倍率，完整用料见附表。

## Create：保留数值与移植差异必须分开

|项目|1.20.1|1.21.1|差异/来源|
|---|---|---|---|
|压床/搅拌器/风扇/粉碎轮应力系数|16/16/8/24|相同|1×；注册值保留|
|盆物品输入/输出|各9槽，1024/槽|相同|1×|
|盆液体输入/输出|各2槽，16000mB/槽|相同|1×|
|置物台主槽堆叠上限|1024|1024|1×|
|压床每次盆处理最多循环|64|64|1×|
|极限盆配方最多物品输入项|64|81|1.265625×；C扩大配方表达上限，不是吞吐|
|极限盆物品输出/液体输入/液体输出上限|4/2/2|相同|1×|
|风扇处理设定|1tick计数|1tick计数|1×；非整条产线吞吐|
|搅拌处理速度参数|自定义tick中abs(rpm×10)|继承Create6上游tick|旧十倍速度参数未保留；C，未测同配方实际倍率|
|粉碎处理|每tick扣完剩余加工时间|继承Create6上游速度处理|旧瞬时机制未保留；C，无固定倍率|
|燃烧器累计热量/燃烧时间上限|1073741823tick|10000tick|约0.00000931323×；C|
|炽焰蛋糕燃烧时间|36000tick|5000tick|0.138889×；C|
|星辉蛋糕燃烧时间|36000tick|10000tick|0.277778×；C|
|炽骨/Blaze标签燃料|6000tick|5000tick（标签命中时）|0.833333×；C|
|Star标签燃料|6000tick|10000tick|1.666667×；C|
|物品本身的熔炉燃料值|炽焰蛋糕65536、星辉蛋糕2147483647|相同|1×；**与专用燃烧器消耗不同**|
|CCA液体兼容槽|4000mB|4000mB|1×|
|风扇实体推力上限|5，普通/潜行阻力参数512/4096|相同|1×|

依据：[旧十倍搅拌](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/create/content/matrix_mixer/MatrixMechanicalMixerBlockEntity.java#L175)、[新搅拌类完整入口](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/create/src/main/java/committee/nova/mods/avaritia_integration/integrations/create/content/matrix_mixer/MatrixMechanicalMixerBlockEntity.java#L18)、[旧燃料](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/create/content/extreme_burner/ExtremeBlazeBurnerBlockEntity.java#L317)、[新燃料](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/create/src/main/java/committee/nova/mods/avaritia_integration/integrations/create/content/extreme_burner/ExtremeBlazeBurnerBlockEntity.java#L167)。C标题证明是在移植中替换实现，没有找到单独平衡说明证明上述所有差异均为有意削弱，故列“移植差异待决”。旧CIKinetics的一大批上游式参数并不都被真实消费者读取，不能把删除未使用声明自动当削弱。

Create三条极限搅拌配方的数值未变：异彩化合物3黑曜石粉+3荧石粉+1玫瑰石英→1；创造化合物1暗影钢+1光辉石+1强化核心+64中子素尘→2；星燃料配方见附表，数量保持。盆内自定义Recipe使用count消费的真实逻辑需与上游Ingredient区分，本调查未额外改变处理。

## AE2与其余模块覆盖

|模块/项目|1.20.1|1.21.1|结论|
|---|---|---|---|
|AE2普通无尽元件待机耗能|8 AE参数|8|1×|
|AE2大整数无尽元件待机耗能|64 AE参数|64|1×|
|AE2普通元件容量/类型|声明-1/-1，消费者映射long上限；每key long计数|相同|容量模型保留；不是数学无限|
|AE2大整数元件|BigInteger计数，不限声明字节/类型；接口单次long|相同|1×模型，不存在固定有限“无限倍率”|
|AE2每种升级卡支持数|1|1|保留；新版增加数据拆解配方并恢复右键拆解路径，非容量强化|
|Thermal创造升级|RF_CREATIVE=1，MACHINE_ENERGY=0|无对应AugmentItem注册|实现缺失；不能写成“新值0”或无尽倍率；本轮仍未移植|
|IFEU|旧版无模块|四档能量插件|新增，详上|
|Mystical Agriculture种子tier|炽骨5、水晶6、无尽6|相同|1×；方块硬度5/抗爆10也保留|
|Mystical精华配方|固定阵列→单个结果|相同数值|1×；详附表|
|GregTech材料高炉温度|21800|21800|1×，四种材料同值；没有自定义EUt/时长新增倍率|
|PneumaticCraft|创造压缩铁为普通合成材料|相同|无自定义压力/气量/机器容量，不能给不存在的倍率|
|RefinedStorage|无尽存储零件为普通合成材料|相同|没有注册自定义磁盘容量，不能把名称解释为真实无限存储|
|SlashBlade剑耐久/攻速参数|10000/-2.4|相同|1×；五种附魔均100级保留|
|SlashBlade剑技|消耗10、7把幻影剑、每把10000伤害、间隔3tick|相同|1×；API迁移不作数值增强结论|
|匠魂四种材料head耐久|1350/1450/1900/9999|相同|1×|
|匠魂四种材料head挖速|4.5/14/6/511|相同|1×|
|匠魂四种材料head攻击|6/3/4.5/255|相同|1×|
|匠魂无尽甲板护甲（靴/腿/胸/头）|3/6/8/3，耐久9998，击退抗性1|相同|JSON保留，不代表事件防护等价|
|匠魂无尽修复/晶耀/炽热|每20tick修100；晶耀系数1+0.6×等级；炽热1+0.3×等级|相同|前轮a7ffe27恢复注册后数值保留|
|匠魂星驰|1+2×等级弹体，每弹50mB，速度3、散布0.1|相同|1×|
|匠魂旧TicHandler护甲事件|永恒计数>1夜视、>2飞行、>3多项免伤事件、>4加速等|无对应完整事件实现|移植缺失，不能以JSON相同掩盖；本次数值调查不补|
|Blood Magic|血球/网络1000000000 LP等|无模块，用户确认不需要|需求排除，无移植任务|
|共用熔融材料|温度3300/2800/8300/12300、黏度10000、密度2000|相同|1×；无尽流体密度-2000、温度12300也保留|

AE依据：[两种元件注册:24](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/ae2/src/main/java/committee/nova/mods/avaritia_integration/integrations/ae2/registry/AE2IntegrationItems.java#L24)、[普通元件实际容量:157](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/ae2/src/main/java/committee/nova/mods/avaritia_integration/integrations/ae2/me/AEUniversalCellInventory.java#L157)、[大整数long接口:196](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/integrations/ae2/src/main/java/committee/nova/mods/avaritia_integration/integrations/ae2/me/biginteger/AEBigIntegerCellInventory.java#L196)。匠魂17份材料定义/traits/统计/资源对象两版内容一致，见[旧完整材料](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/old-materials.json)、[新完整材料](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration/.dsh/evidence/numeric-comparison/new-materials.json)，包含全部handle/limb/grip等值；[旧护甲消费者:100](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/init/handler/TicHandler.java#L100)。Thermal[旧真实升级定义:21](/srv/projects/minecraft/3Nova/Avaritia/Avaritia-Integration-compare-1.20.1/src/main/java/committee/nova/mods/avaritia_integration/module/thermal_expansion/registry/ThermalExpansionIntegrationItems.java#L21)。

## 单独留给用户的平衡判断清单（本轮没有改）

- Create：是否恢复旧十倍搅拌/瞬时粉碎；蛋糕专用燃烧器36000→5000/10000是否接受。证据能证明差异，未证明作者平衡意图，需用户选择。
- Mekanism：是否接受中子太阳能从接近F/2降至10^10/10^11，以及无尽端内部跃迁到约Double.MAX_VALUE；两者并非线性进阶。即使有历史意图，也值得整包平衡审查。
- Mekanism：单机气槽100万，而工厂仍每槽10000×P，是否需统一设计。当前不是本轮误改，不能顺手补。
- IFEU：tier32/64均饱和int容量，是否保留材料进阶意义；EnderIO等级1000也有同类边界。任何调整要选真实消费者验证，不能只减小/增大显示数字。
- EnderIO中子研磨球：最终主产物与副产期望均增强；用户已批准同步5/2.5，未回退Bonus。保留Output与Bonus分字段解释，不能再把Bonus下降说成副产降低。
- 匠魂无尽甲事件：若仍要求旧体验，需要独立移植验收；本轮只记录数值与功能缺口。

本报告未把“高/低”自动判成bug，未改任何数值。模块启停已获批准，接续独立实现；不与平衡调整混为一个提交。
