package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/** 决定如何从ItemStack构建一个 {@link AEUniversalCellInventory} */
public class AEUniversalCellHandler implements ICellHandler
{
    @Override
    public boolean isCell(ItemStack itemStack)
    {
        return false;
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack itemStack, @Nullable ISaveProvider iSaveProvider)
    {
        return null;
    }
}
