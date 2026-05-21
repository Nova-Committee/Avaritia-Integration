package committee.nova.mods.avaritia_integration.module.ifeu.rregistry;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.buuz135.industrial.utils.IndustrialTags;
import com.google.common.base.Supplier;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;
import committee.nova.mods.avaritia_integration.module.industrialforegoing.registry.IndustrialForegoingIntegrationFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.ifeu.api.item.EnergyAddonItem;
import net.yxiao233.ifeu.common.registry.IFEUItems;

import java.util.List;
import java.util.Optional;

public class IFEUIntegrationItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);
    public static DeferredHolder<Item,EnergyAddonItem> ENERGY_ADDON_BLAZE_CUBE = ITEMS.register("energy_addon_blaze_cube", () -> new EnergyAddonItem(7, IFEUItems.TAB_ADDONS){
        @Override
        public void registerRecipe(RecipeOutput recipeOutput) {
            dissolutionChamberRecipe(this.getDefaultInstance(), List.of(
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    itemValue(AIItems.BLAZE_CUBE_GEAR.get().getDefaultInstance()),
                    itemValue(AIItems.BLAZE_CUBE_GEAR.get().getDefaultInstance())
            ), new FluidStack(ModuleCore.ETHER.getSourceFluid(),1000),100,recipeOutput);
        }
    });
    public static DeferredHolder<Item,EnergyAddonItem> ENERGY_ADDON_CRYSTAL_MATRIX = ITEMS.register("energy_addon_crystal_matrix", () -> new EnergyAddonItem(8, IFEUItems.TAB_ADDONS){
        @Override
        public void registerRecipe(RecipeOutput recipeOutput) {
            dissolutionChamberRecipe(this.getDefaultInstance(), List.of(
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    itemValue(AIItems.CRYSTAL_MATRIX_GEAR.get().getDefaultInstance()),
                    itemValue(AIItems.CRYSTAL_MATRIX_GEAR.get().getDefaultInstance())
            ), new FluidStack(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getSourceFluid(),1000),200,recipeOutput);
        }
    });
    public static DeferredHolder<Item,EnergyAddonItem> ENERGY_ADDON_NEUTRON = ITEMS.register("energy_addon_neutron", () -> new EnergyAddonItem(9, IFEUItems.TAB_ADDONS){
        @Override
        public void registerRecipe(RecipeOutput recipeOutput) {
            dissolutionChamberRecipe(this.getDefaultInstance(), List.of(
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    itemValue(ModItems.neutron_gear.get().getDefaultInstance()),
                    itemValue(ModItems.neutron_gear.get().getDefaultInstance())
            ), new FluidStack(IndustrialForegoingIntegrationFluids.VOID_MATTER.getSourceFluid(),1000),300,recipeOutput);
        }
    });
    public static DeferredHolder<Item,EnergyAddonItem> ENERGY_ADDON_INFINITY = ITEMS.register("energy_addon_infinity", () -> new EnergyAddonItem(10, IFEUItems.TAB_ADDONS){
        @Override
        public void registerRecipe(RecipeOutput recipeOutput) {
            dissolutionChamberRecipe(this.getDefaultInstance(), List.of(
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    tagValue(IndustrialTags.Items.PLASTIC),
                    itemValue(AIItems.INFINITY_GEAR.get().getDefaultInstance()),
                    itemValue(AIItems.INFINITY_GEAR.get().getDefaultInstance())
            ), new FluidStack(IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getSourceFluid(),2000),400,recipeOutput);
        }
    });
    public static  DeferredHolder<Item, EnergyAddonItem> registerEnergyAddon(String prefix, DeferredHolder<Item,Item> input, Supplier<DeferredHolder<Fluid,Fluid>> fluid, int amount, int processingTime) {
        return ITEMS.register("energy_addon_" + prefix, () -> new EnergyAddonItem(7, ModuleCore.TAB_CORE){
            @Override
            public void registerRecipe(RecipeOutput recipeOutput) {
                dissolutionChamberRecipe(this.getDefaultInstance(), List.of(
                        tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                        tagValue(Tags.Items.STORAGE_BLOCKS_REDSTONE),
                        tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                        tagValue(Tags.Items.GLASS_PANES_COLORLESS),
                        tagValue(IndustrialTags.Items.PLASTIC),
                        tagValue(IndustrialTags.Items.PLASTIC),
                        itemValue(input.get().getDefaultInstance()),
                        itemValue(input.get().getDefaultInstance())
                ), new FluidStack(fluid.get(),amount),processingTime,recipeOutput);
            }
        });
    }

    private static void dissolutionChamberRecipe(ItemStack result, List<Ingredient> inputs, FluidStack inputFluid, int processingTime, RecipeOutput output){
        var recipe = new DissolutionChamberRecipe(inputs, inputFluid, processingTime, Optional.of(result), Optional.empty());
        DissolutionChamberRecipe.createRecipe(output, BuiltInRegistries.ITEM.getKey(result.getItem()).getPath(),recipe);
    }

    private static Ingredient tagValue(TagKey<Item> tagKey){
        return Ingredient.of(tagKey);
    }

    private static Ingredient itemValue(ItemStack stack){
        return Ingredient.of(stack);
    }
}
