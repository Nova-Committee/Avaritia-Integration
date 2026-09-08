package committee.nova.mods.avaritia_integration.integrations.mekanism.client.recipe_viewer;

import committee.nova.mods.avaritia_integration.integrations.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.recipe.MekIntegrationRecipeType;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.util.MekIntegrationUtils;

import net.minecraft.world.level.ItemLike;

import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.tier.FactoryTier;

public final class MekIntegrationRecipeViewerTypes {

    private MekIntegrationRecipeViewerTypes() {}

    public static final RVRecipeTypeWrapper<?, ChemicalStackToItemStackRecipe, ?> COLLECTING = new RVRecipeTypeWrapper<>(
            MekIntegrationRecipeType.COLLECTING, ChemicalStackToItemStackRecipe.class, 20, 12, 132, 62,
            MekIntegrationBlocks.NEUTRON_COLLECTOR, factories(MekIntegrationFactoryType.NEUTRON_COLLECTING));

    public static final RVRecipeTypeWrapper<?, ItemStackToItemStackRecipe, ?> COMPRESSING = new RVRecipeTypeWrapper<>(
            MekIntegrationRecipeType.MEK_COMPRESSING, ItemStackToItemStackRecipe.class, -28, -16, 144, 54,
            MekIntegrationBlocks.SINGULARITY_COMPRESSOR,
            factories(MekIntegrationFactoryType.SINGULARITY_COMPRESSING));

    private static ItemLike[] factories(MekIntegrationFactoryType type) {
        FactoryTier[] tiers = MekIntegrationUtils.getFactoryTier();
        ItemLike[] items = new ItemLike[tiers.length];
        for (int i = 0; i < tiers.length; i++) {
            items[i] = MekIntegrationBlocks.getMekIntegrationFactory(tiers[i], type);
        }
        return items;
    }
}
