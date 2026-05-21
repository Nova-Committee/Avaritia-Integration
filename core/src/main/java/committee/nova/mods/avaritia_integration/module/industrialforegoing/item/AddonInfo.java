package committee.nova.mods.avaritia_integration.module.industrialforegoing.item;

import com.buuz135.industrial.item.addon.ProcessingAddonItem;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import com.hrznstudio.titanium.api.augment.AugmentTypes;
import com.hrznstudio.titanium.api.augment.IAugmentType;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class AddonInfo {
    private final int tier;
    private final String materialName;
    private final Supplier<Item> gear;
    private final Supplier<Fluid> fluid;
    private final int amount;
    private final int processingTime;
    private AddonInfo(int tier, Supplier<Item> gear, Supplier<Fluid> fluid, int amount, int processingTime, String materialName){
        this.tier = tier;
        this.materialName = materialName;
        this.gear = gear;
        this.fluid = fluid;
        this.amount = amount;
        this.processingTime = processingTime;
    }

    public static AddonInfo create(int tier, Supplier<Item> gear, Supplier<Fluid> fluid, int amount, int processingTime, String materialName){
        return new AddonInfo(tier,gear,fluid,amount,processingTime,materialName);
    }

    public void registry(HashMap<String, Supplier<? extends AddonItem>> map, DeferredRegister<Item> register){
        String speed = getId(AugmentTypes.SPEED);
        String efficiency = getId(AugmentTypes.EFFICIENCY);
        String processing = getId(ProcessingAddonItem.PROCESSING);
        map.put(speed,register.register(speed,() -> new ModSpeedAddonItem(tier, Component.translatable(getDescription())){
            @Override
            public void registerRecipe(RecipeOutput consumer) {
                dissolutionChamberRecipe(this.getDefaultInstance(),List.of(
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.GLASS_PANES),
                        tagValue(Tags.Items.GLASS_PANES),
                        itemValue(gear.get().getDefaultInstance()),
                        itemValue(gear.get().getDefaultInstance()),
                        itemValue(Items.SUGAR.getDefaultInstance()),
                        itemValue(Items.SUGAR.getDefaultInstance())
                ), new FluidStack(fluid.get(),amount),processingTime,consumer);
            }
        }));
        map.put(efficiency,register.register(efficiency,() -> new ModEfficiencyAddonItem(tier, Component.translatable(getDescription())){
            @Override
            public void registerRecipe(RecipeOutput consumer) {
                dissolutionChamberRecipe(this.getDefaultInstance(),List.of(
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.GLASS_PANES),
                        tagValue(Tags.Items.GLASS_PANES),
                        itemValue(gear.get().getDefaultInstance()),
                        itemValue(gear.get().getDefaultInstance()),
                        tagValue(Tags.Items.RODS_BLAZE),
                        tagValue(Tags.Items.RODS_BLAZE)
                ), new FluidStack(fluid.get(),amount),processingTime,consumer);
            }
        }));
        map.put(processing,register.register(processing,() -> new ModProcessingAddonItem(tier, Component.translatable(getDescription())){
            @Override
            public void registerRecipe(RecipeOutput consumer) {
                dissolutionChamberRecipe(this.getDefaultInstance(),List.of(
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.DUSTS_REDSTONE),
                        tagValue(Tags.Items.GLASS_PANES),
                        tagValue(Tags.Items.GLASS_PANES),
                        itemValue(gear.get().getDefaultInstance()),
                        itemValue(gear.get().getDefaultInstance()),
                        itemValue(Items.CRAFTING_TABLE.getDefaultInstance()),
                        itemValue(Items.FURNACE.getDefaultInstance())
                ), new FluidStack(fluid.get(),amount),processingTime,consumer);
            }
        }));
    }

    public String getId(IAugmentType type){
        return type.getType().toLowerCase() + "_addon_" + materialName.toLowerCase();
    }

    public String getDescription(){
        return "addon." + AvaritiaIntegration.MOD_ID + "." + materialName.toLowerCase();
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
