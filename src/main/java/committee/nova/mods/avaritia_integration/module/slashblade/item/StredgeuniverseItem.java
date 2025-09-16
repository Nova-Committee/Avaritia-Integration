package committee.nova.mods.avaritia_integration.module.slashblade.item;


import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationSlashArts;
import committee.nova.mods.avaritia_integration.util.SwordUtil;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StredgeuniverseItem extends ItemSlashBlade {
    private final ResourceLocation slashArts;

    public StredgeuniverseItem() {
        super(new ItemTierSlashBlade(0, Float.POSITIVE_INFINITY) {
            @Override
            public int getUses() {
                return 0;
            }

            @Override
            public float getSpeed() {
                return 0;
            }

            @Override
            public float getAttackDamageBonus() {
                return Float.POSITIVE_INFINITY;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public int getEnchantmentValue() {
                return 0;
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.EMPTY;
            }
        }, Integer.MAX_VALUE, -2.4f, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
        this.slashArts = SlashBladeIntegrationSlashArts.METEORITE_SWORD.getId();
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        this.setupBlade(stack);
        return stack;
    }

    private void setupBlade(ItemStack stack) {
        EnchantmentHelper.setEnchantments(
                Map.of(Enchantments.MOB_LOOTING, 10, Enchantments.BLOCK_FORTUNE, 100, Enchantments.POWER_ARROWS, 100, Enchantments.SHARPNESS, 100
                        , Enchantments.BANE_OF_ARTHROPODS, 100),
                stack
        );
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(state -> {
            state.setModel(new ResourceLocation(AvaritiaIntegration.MOD_ID, "models/item/stredgeuniverse.obj"));
            state.setTexture(new ResourceLocation(AvaritiaIntegration.MOD_ID, "models/item/stredgeuniverse.png"));
            state.setSlashArtsKey(this.slashArts);
            state.setDefaultBewitched(true);
            state.setMaxDamage(10000);
            state.setDamage(0);
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!worldIn.isClientSide)
            this.setupBlade(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack p_41458_) {
        return Component.translatable("item.avaritia_integration.stredgeuniverse");
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.avaritia_integration.stredgeuniverse.tooltip1"));
        list.add(Component.literal(""));
        list.add(Component.translatable("item.avaritia_integration.stredgeuniverse.tooltip2"));
        list.add(Component.literal(""));
        list.add(Component.translatable("item.avaritia_integration.stredgeuniverse.tooltip3"));
        list.add(Component.literal(""));
        list.add(Component.translatable("item.avaritia_integration.stredgeuniverse.tooltip4"));
        list.add(Component.literal(""));
        list.add(Component.translatable("item.avaritia_integration.stredgeuniverse.tooltip5"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, Player playerIn, Entity entity) {
        if (entity instanceof LivingEntity living) {
            SwordUtil.killEntity(living, playerIn);
        }
        return super.onLeftClickEntity(itemstack, playerIn, entity);
    }
}
