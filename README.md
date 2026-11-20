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

## **Introduction:**
* This mod adds compatibility between Re:Avaritia and many other mods on Minecraft 1.21.1 (NeoForge).

## **✏️Authors:**
- Programmer: `cnlimiter` `IAFEnvoy` `Frostbite-time` `CreepingCreeper` `Oganesson897` `cu6` `lostmyself8` `Lounode` `Y-Xiao233`
- Artist: `MHanHanBing` `Neo-Tix`

## **🔒License:**
- Code: [MIT](LICENSE)
- Texture resources: **All Rights Reserved (ARR)**. Texture owner: **MHanHanBing**. Without prior written permission from the author, these resources may not be copied, redistributed, modified, or used in modpacks. The owner reserves the right to pursue legal liability for unauthorized use.

## **❗Attention:**
* Including this mod's texture resources in a modpack requires prior written permission from the texture owner.

## **🎮Capability**
* ✅: Fully Supported<br>
* 🚧: Partly Supported<br>
* 🔲: Planned Support<br>
* ❌: No Support Planned<br>

| Mod | Content | Status | Notes |
|-------------------------------------|:--------|:------:|-------|
| Applied Energistics 2 | Infinity ME component and storage cells | ✅ | |
| Botania | Asgard Dandelion, Soarleander, Infinity Mana Pool, Infinity Potato, Alpha Spark | ✅ | Botania 455-SNAPSHOT or newer |
| Create | Creative Mechanism / Compound, Crystal Matrix Casing, Neutron Mechanical Press, Matrix Mechanical Mixer, Extreme Basin / Blaze Burner / Depot / Encased Fan / Crushing Wheels | ✅ | |
| Ender IO | Infinity Capacitor, Infinity / Neutron grinding balls | ✅ | |
| Industrial Foregoing | Neutron / Blaze Cube / Crystal Matrix / Infinity addons, Void Matter, Elderly Medulla | ✅ | |
| Industrial Foregoing Extra Upgrades | Blaze Cube / Crystal Matrix / Neutron / Infinity energy addons | ✅ | |
| Mekanism | Neutron Collector, Singularity Compressor, factories, alloys, circuits, energy tablets | ✅ | |
| Mekanism Generators | Infinity / Neutron solar generators | ✅ | |
| PneumaticCraft: Repressurized | Creative Compressed Iron | ✅ | |
| Refined Storage | Infinity Storage Part | ✅ | |
| Thermal Expansion | Creative augments | ✅ | Data-only |
| Tinkers' Construct | Infinity / Neutronium / Crystal Matrix / Blaze Cube materials and modifiers | ✅ | Includes fluid, block and arrow item registrations |
| Mystical Agriculture | Blaze Cube, Crystal Matrix and Infinity crops | 🚧 | Implemented; runtime behavior not yet verified |
| Apotheosis | | 🔲 | |
| Blood Magic | | 🔲 | |
| GregTechCEu Modern | Material / element registration and part unification | 🚧 | Implemented; runtime behavior not yet verified |
| NuclearCraft: Neoteric | | 🔲 | |
| SlashBlade | Stredge Universe blade and Meteorite Sword slash art / entity | 🚧 | Implemented; runtime behavior not yet verified |
| Tetra | | 🔲 | |
| Thaumaturge | Extremely Primordial Pearl and Terminus | 🚧 | Requires the separately installed Thaumaturge 1.1.0 or newer |

### Thaumaturge

Place one fresh Primordial Pearl on the central pedestal and seven on the surrounding pedestals. After a successful infusion, the central pearl becomes an Extremely Primordial Pearl and each surrounding fresh pearl gains one point of damage. The native ingredient also accepts worn pearls, nodules, and motes. Mixing damage values can let the matrix reuse an earlier remainder on the same pedestal.

Combine Desiderium and Alienis at the Research Table to gain one Terminus research point. Thaumaturge also awards two bonus points when you discover the aspect for the first time. Terminus essentia for infusion comes from breeding mana beans, distilling them, and collecting the essentia in a jar. The seventh-order description is lore. Counting the six primal aspects as layer one, the longest component chain has six layers (Terra → Victus → Mortuus → Spiritus → Desiderium → Terminus).

If `config/avaritia/integration/load_rules.json` already exists, add the following entry to the existing configuration and restart. See the [texture conversion instructions](integrations/thaumaturge/tools/README.md) for the original artwork and the offline conversion command.

```json
"avaritia_integration_thaumaturge": {
  "dependencies": [{"modid": "thaumaturge", "minVersion": "1.1.0"}],
  "enabled": true
}
```
