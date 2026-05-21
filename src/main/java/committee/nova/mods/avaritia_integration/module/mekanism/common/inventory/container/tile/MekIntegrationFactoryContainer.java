package committee.nova.mods.avaritia_integration.module.mekanism.common.inventory.container.tile;

import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationContainerTypes;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityChemicalToItemMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.fml.ModList;

public class MekIntegrationFactoryContainer extends MekanismTileContainer<TileEntityMIFactory<?>> {

    public MekIntegrationFactoryContainer(int id, Inventory inv, TileEntityMIFactory<?> tile) {
        super(MekIntegrationContainerTypes.FACTORY, id, inv, tile);
    }

    @Override
    protected int getInventoryYOffset() {
        if (tile.hasSecondaryResourceBar()) {
            return 95;
        }
        if (tile instanceof TileEntityChemicalToItemMIFactory<?>) {
            return 98;
        }
        return 85;
    }

    @Override
    protected int getInventoryXOffset() {
        if (ModList.get().isLoaded("evolvedmekanism")) {
            if (tile.tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()) {
                int index = tile.tier.ordinal() - 4;
                return (22 * (index + 2)) - (3 * index);
            }
        }
        return tile.tier == FactoryTier.ULTIMATE ? 26 : 8;
    }
}
