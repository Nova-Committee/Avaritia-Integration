package committee.nova.mods.avaritia_integration.module.mekanism.common.inventory.slot;

import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityItemToItemMIFactory;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@NothingNullByDefault
public class MIFactoryInputInventorySlot extends InputInventorySlot {

    public static MIFactoryInputInventorySlot create(TileEntityItemToItemMIFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IContentsListener listener, int x, int y) {
        Objects.requireNonNull(factory, "Factory cannot be null");
        Objects.requireNonNull(outputSlot, "Output slot cannot be null");
        return new MIFactoryInputInventorySlot(factory, process, outputSlot, listener, x, y);
    }

    private MIFactoryInputInventorySlot(TileEntityItemToItemMIFactory<?> factory, int process, IInventorySlot outputSlot, @Nullable IContentsListener listener, int x, int y) {
        super(stack -> factory.inputProducesOutput(process, stack, outputSlot, false), factory::isValidInputItem, listener, x, y);
    }

    //Increase access level of setStackUnchecked
    @Override
    public void setStackUnchecked(ItemStack stack) {
        super.setStackUnchecked(stack);
    }

    @Override
    public int getLimit(ItemStack stack) {
        return 65536;
    }
}