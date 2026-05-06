package committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat;

import net.minecraft.world.item.ItemStack;

public interface BeltCrusherControllerBlockEntity {
    ItemStack insertFromBelt(ItemStack stack, boolean simulate);
}
