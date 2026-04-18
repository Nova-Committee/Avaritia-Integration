package committee.nova.mods.avaritia_integration.module.enderio;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import net.minecraft.world.item.Rarity;

public enum AICapacitorDef {
    BLAZE_CUBE("blaze_cube_capacitor", 5.0f, ModRarities.EPIC),
    CRYSTAL_MATRIX("crystal_matrix_capacitor", 7.0f, ModRarities.EPIC),
    NEUTRONIUM("neutron_capacitor", 8.0f, ModRarities.LEGEND),
    INFINITY("infinity_capacitor", 10.0f, ModRarities.COSMIC);

    public final String id;
    public final float baseLevel;
    public final Rarity rarity;

    AICapacitorDef(String id, float baseLevel, Rarity rarity) {
        this.id = id;
        this.baseLevel = baseLevel;
        this.rarity = rarity;
    }
}
