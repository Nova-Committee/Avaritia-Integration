package committee.nova.mods.avaritia_integration.integrations.ifeu.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;
import committee.nova.mods.avaritia_integration.integrations.industrialforegoing.registry.IndustrialForegoingIntegrationFluids;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.ifeu.api.item.EnergyAddonItem;
import net.yxiao233.ifeu.common.registry.IFEUItems;

import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.buuz135.industrial.utils.IndustrialTags;

import java.util.List;
import java.util.Optional;

public class IFEUIntegrationItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);
    public static final DeferredHolder<Item, EnergyAddonItem> ENERGY_ADDON_BLAZE_CUBE = registerLegacy("blaze_cube", 8);
    public static final DeferredHolder<Item, EnergyAddonItem> ENERGY_ADDON_CRYSTAL_MATRIX = registerLegacy(
            "crystal_matrix", 16);
    public static final DeferredHolder<Item, EnergyAddonItem> ENERGY_ADDON_NEUTRON = registerLegacy("neutron", 32);
    public static final DeferredHolder<Item, EnergyAddonItem> ENERGY_ADDON_INFINITY = ITEMS.register(
            "energy_addon_infinity",
            () -> new EnergyAddonItem(64, IFEUItems.TAB_ADDONS) {

                @Override
                public void registerRecipe(RecipeOutput recipeOutput) {
                    var recipe = new DissolutionChamberRecipe(List.of(
                            Ingredient.of(IFEUItems.ENERGY_ADDON_6.get()),
                            tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                            tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                            tagValue(IndustrialTags.Items.PLASTIC),
                            tagValue(IndustrialTags.Items.PLASTIC),
                            Ingredient.of(AIItems.INFINITY_GEAR.get()),
                            Ingredient.of(AIItems.INFINITY_GEAR.get()),
                            Ingredient.of(AIItems.INFINITY_GEAR.get())),
                            new FluidStack(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getSourceFluid(), 2000),
                            400, Optional.of(getDefaultInstance()), Optional.empty());
                    DissolutionChamberRecipe.createRecipe(recipeOutput,
                            BuiltInRegistries.ITEM.getKey(this).getPath(), recipe);
                }

                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
                                            TooltipFlag flag) {
                    tooltip.add(Component.translatable("tooltip.avaritia_integration.energy_addon_cap"));
                }
            });

    private static DeferredHolder<Item, EnergyAddonItem> registerLegacy(String name, int tier) {
        return ITEMS.register("energy_addon_" + name, () -> new EnergyAddonItem(tier, IFEUItems.TAB_ADDONS) {

            @Override
            public void registerRecipe(RecipeOutput output) {}

            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
                                        TooltipFlag flag) {
                super.appendHoverText(stack, context, tooltip, flag);
                tooltip.add(Component.translatable("tooltip.avaritia_integration.energy_addon_legacy"));
            }
        });
    }

    private static Ingredient tagValue(TagKey<Item> tag) {
        return Ingredient.of(tag);
    }
}
