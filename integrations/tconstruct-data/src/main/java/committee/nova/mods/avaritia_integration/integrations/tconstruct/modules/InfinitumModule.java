package committee.nova.mods.avaritia_integration.integrations.tconstruct.modules;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public enum InfinitumModule implements ModifierModule, InventoryTickModifierHook {

    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider
            .<InfinitumModule>defaultHooks(ModifierHooks.INVENTORY_TICK);
    public static final RecordLoadable<InfinitumModule> LOADER = new SingletonLoader<>(INSTANCE);

    public @NotNull RecordLoadable<InfinitumModule> getLoader() {
        return LOADER;
    }

    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder,
                                int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!world.isClientSide && holder.tickCount % 20 == 0 && holder.getUseItem() != stack && tool.getDamage() > 0) {
            ToolDamageUtil.repair(tool, 100);
        }
    }
}
