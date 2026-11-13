package committee.nova.mods.avaritia_integration.init.data.provider;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import committee.nova.mods.avaritia.init.data.provider.recipe.ModExtremeSmithingRecipeBuilder;
import committee.nova.mods.avaritia.init.data.provider.recipe.ModShapedRecipeBuilder;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationItems;
import committee.nova.mods.avaritia_integration.module.bloodmagic.registry.BloodMagicIntegrationItems;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationItems;
import committee.nova.mods.avaritia_integration.module.enderio.registry.EnderIOIntegrationItems;
import committee.nova.mods.avaritia_integration.module.industrialforegoing.registry.IndustrialForegoingIntegrationItems;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationItems;
import mods.flammpfeil.slashblade.init.SBItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

public class AIRecipes extends RecipeProvider implements IConditionBuilder {
    public AIRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        InventoryChangeTrigger.TriggerInstance lul = has(Items.AIR);

        //Slash Blade

        ModExtremeSmithingRecipeBuilder.smithing(
                        Ingredient.of(ModItems.upgrade_smithing_template.get()),
                        Ingredient.of(SBItems.slashblade),
                        CompoundIngredient.of(Ingredient.of(ModItems.infinity_sword.get()), Ingredient.of(ModItems.enhancement_core.get()), Ingredient.of(ModItems.eternal_singularity.get())),
                        RecipeCategory.TOOLS,
                        SlashBladeIntegrationItems.STREDGEUNIVERSE.get().asItem())
                        .unlockedBy("has_item", has(ModItems.upgrade_smithing_template.get()))
                        .save(consumer);

        //Ender IO

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EnderIOIntegrationItems.INFINITY_GRINDING_BALL.get())
                .pattern(" a ")
                .pattern("aaa")
                .pattern(" a ")
                .define('a', ModItems.infinity_ingot.get())
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EnderIOIntegrationItems.NEUTRON_GRINDING_BALL.get())
                .pattern(" a ")
                .pattern("aaa")
                .pattern(" a ")
                .define('a', ModItems.neutron_ingot.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

//        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EnderIOIntegrationItems.INFINITY_CAPACITOR.get())
//                .pattern("   aaa   ")
//                .pattern("  aasaa  ")
//                .pattern("  adrda  ")
//                .pattern("  arqra  ")
//                .pattern("  adrda  ")
//                .pattern("  a   a  ")
//                .pattern("  aaaaa  ")
//                .pattern("  e   e  ")
//                .pattern("  e   e  ")
//                .define('a', ModItems.infinity_ingot.get())
//                .define('s', ModItems.infinity_catalyst.get())
//                .define('d', ModItems.crystal_matrix_ingot.get())
//                .define('f', ModItems.endest_pearl.get())
//                .define('e', ModItems.neutron_ingot.get())
//                .define('q', EIOItems.OCTADIC_CAPACITOR)
//                .define('r', EIOItems.VIBRANT_CRYSTAL)
//                .showNotification(true)
//                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);

        //Botania

        ModShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaIntegrationBlocks.INFINITY_POTATO.get())
                .pattern("aaaaaaaaa")
                .pattern("aaaaaaaaa")
                .pattern("aaadadaaa")
                .pattern("aaaaaaaaa")
                .pattern("aadasadaa")
                .pattern("aaadddaaa")
                .pattern("aaaaaaaaa")
                .pattern("aaaaaaaaa")
                .pattern("aaaaaaaaa")
                .define('a', BotaniaBlocks.tinyPotato)
                .define('s', ModItems.infinity_catalyst.get())
                .define('d', ModItems.diamond_lattice.get())
                .showNotification(true)
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);


        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaIntegrationBlocks.ASGARD_DANDELION.get())
                .pattern("   rrr   ")
                .pattern("  rrrrr  ")
                .pattern("  rrsrr  ")
                .pattern("  rrrrr  ")
                .pattern("   rrr   ")
                .pattern(" aa d aa ")
                .pattern("aaaadaaaa")
                .pattern(" aa d aa ")
                .pattern("    d    ")
                .define('a', ModItems.neutron_nugget.get())
                .define('s', ModItems.infinity_catalyst.get())
                .define('r', ModItems.infinity_ingot.get())
                .define('d', ModItems.neutron_ingot.get())
                .showNotification(true)
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);

        ModExtremeSmithingRecipeBuilder.smithing(
                        Ingredient.of(ModItems.upgrade_smithing_template.get()),
                        Ingredient.of(BotaniaItems.spark),
                        CompoundIngredient.of(Ingredient.of(ModItems.endest_pearl.get()), Ingredient.of(ModItems.enhancement_core.get()), Ingredient.of(ModItems.infinity_catalyst.get())),
                        RecipeCategory.TOOLS,
                        BotaniaIntegrationItems.ALPHA_SPARK.get())
                .unlockedBy("has_item", has(ModItems.upgrade_smithing_template.get()))
                .save(consumer);


        //AE2

        ModShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .pattern("  rrrrr  ")
                .pattern(" vesssev ")
                .pattern("reafcfaer")
                .pattern("rsfxzxfsr")
                .pattern("rsczdzcsr")
                .pattern("rsfxzxfsr")
                .pattern("reafcfaer")
                .pattern(" vesssev ")
                .pattern("  rrrrr  ")
                .define('a', AEItems.ENDER_DUST)
                .define('s', AEItems.SKY_DUST)
                .define('d', AEBlocks.CONTROLLER)
                .define('f', AEItems.CELL_COMPONENT_256K)
                .define('z', AEItems.SPATIAL_128_CELL_COMPONENT)
                .define('x', AEItems.MATTER_BALL)
                .define('c', AEItems.SINGULARITY)
                .define('e', ModItems.infinity_catalyst.get())
                .define('r', ModItems.infinity_ingot.get())
                .define('v', ModItems.neutron_gear.get())
                .showNotification(true)
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get())
                .pattern("aq")
                .pattern("s ")
                .define('a', AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .define('q', AEItems.FLUID_CELL_HOUSING)
                .define('s', AEItems.ITEM_CELL_HOUSING)
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL_BIG.get())
                .pattern("aq")
                .define('a', AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get())
                .define('q', AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get())
                .unlockedBy("has_item", has(ModItems.neutron_ingot.get())).save(consumer);


        //Blood Magic

        ModShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BloodMagicIntegrationItems.BLOOD_ORB_OF_ARMOK.get())
                .pattern("         ")
                .pattern("   rrr   ")
                .pattern("  rqsqr  ")
                .pattern("  rsfsr  ")
                .pattern(" drxsxrd ")
                .pattern("dddrrrddd")
                .pattern(" ddddddd ")
                .pattern("  ddddd  ")
                .pattern("         ")
                .define('x', BloodMagicItems.STRONG_TAU_ITEM.get())
                .define('q', BloodMagicItems.ARCHMAGE_BLOOD_ORB.get())
                .define('f', BloodMagicItems.VENGEFUL_CRYSTAL.get())
                .define('s', ModItems.infinity_catalyst.get())
                .define('r', ModItems.infinity_ingot.get())
                .define('d', ModItems.neutron_ingot.get())
                .showNotification(true)
                .unlockedBy("has_item", has(ModItems.infinity_ingot.get())).save(consumer);


        //Industrial Foregoing
        IndustrialForegoingIntegrationItems.ADDONS.values().forEach(addon -> addon.get().registerRecipe(consumer));

        //Mystical Agradditions
        ModShapedRecipeBuilder.shaped(RecipeCategory.MISC, com.blakebr0.mysticalagradditions.init.ModItems.CREATIVE_ESSENCE.get())
                .pattern("  e e e  ")
                .pattern(" e  e  e ")
                .pattern(" e eee e ")
                .pattern(" eeiiiee ")
                .pattern(" eigggie ")
                .pattern("eeigcgiee")
                .pattern("eeigggiee")
                .pattern(" eeiiiee ")
                .pattern("   eee   ")
                .define('g', com.blakebr0.mysticalagradditions.init.ModItems.INSANIUM_GEMSTONE.get())
                .define('i', com.blakebr0.mysticalagradditions.init.ModItems.INSANIUM_INGOT.get())
                .define('e', com.blakebr0.mysticalagradditions.init.ModItems.INSANIUM_ESSENCE.get())
                .define('c', ModItems.infinity_catalyst.get())
                .showNotification(true)
                .unlockedBy("has_item",has(ModItems.infinity_catalyst.get())).save(consumer);
    }
}
