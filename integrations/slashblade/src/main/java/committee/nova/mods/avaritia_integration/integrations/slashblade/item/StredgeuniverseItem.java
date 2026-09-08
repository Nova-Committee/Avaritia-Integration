package committee.nova.mods.avaritia_integration.integrations.slashblade.item;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.slashblade.registry.SlashBladeIntegrationSlashArts;
import committee.nova.mods.avaritia_integration.integrations.slashblade.util.SwordUtil;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import net.neoforged.neoforge.server.ServerLifecycleHooks;

import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.item.ItemTierSlashBlade;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StredgeuniverseItem extends ItemSlashBlade {

    private static final ResourceLocation MODEL = AvaritiaIntegration.rl("models/item/stredgeuniverse.obj");
    private static final ResourceLocation TEXTURE = AvaritiaIntegration.rl("models/item/stredgeuniverse.png");

    private final ResourceLocation slashArts;

    public StredgeuniverseItem() {
        super(new ItemTierSlashBlade(0, Float.POSITIVE_INFINITY), Integer.MAX_VALUE, -2.4f,
                new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
        this.slashArts = SlashBladeIntegrationSlashArts.METEORITE_SWORD.getId();
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            this.setupBlade(stack, server.registryAccess());
        } else {
            this.setupBladeState(stack);
        }
        return stack;
    }

    public void setupBlade(ItemStack stack, HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        mutable.set(enchantments.getOrThrow(Enchantments.LOOTING), 100);
        mutable.set(enchantments.getOrThrow(Enchantments.FORTUNE), 100);
        mutable.set(enchantments.getOrThrow(Enchantments.POWER), 100);
        mutable.set(enchantments.getOrThrow(Enchantments.SHARPNESS), 100);
        mutable.set(enchantments.getOrThrow(Enchantments.BANE_OF_ARTHROPODS), 100);
        EnchantmentHelper.setEnchantments(stack, mutable.toImmutable());
        this.setupBladeState(stack);
    }

    private void setupBladeState(ItemStack stack) {
        BladeStateAccess.of(stack).ifPresent(state -> {
            state.setModel(MODEL);
            state.setTexture(TEXTURE);
            state.setSlashArtsKey(this.slashArts);
            state.setDefaultBewitched(true);
            state.setMaxDamage(10000);
            state.setDamage(0);
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!worldIn.isClientSide) {
            this.setupBlade(stack, worldIn.registryAccess());
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable("item.avaritia_integration.stredgeuniverse");
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, list, flag);
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
