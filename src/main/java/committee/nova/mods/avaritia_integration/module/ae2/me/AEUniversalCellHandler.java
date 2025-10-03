package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import committee.nova.mods.avaritia_integration.module.ae2.item.AEUniversalCellItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

/**
 * 决定如何从ItemStack构建一个 {@link AEUniversalCellInventory}
 * @author Frostbite
 */
public class AEUniversalCellHandler implements ICellHandler
{
    @Override
    public boolean isCell(ItemStack itemStack)
    {
        // 只允许堆叠为1的创建为元件，以防止极端情况下可能存在的刷物品bug
        return itemStack.getItem() instanceof AEUniversalCellItem && itemStack.getCount() == 1;
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack itemStack, @Nullable ISaveProvider iSaveProvider)
    {
        // 我们无视ISaveProvider，因为这种元件仅由SavedData统一保存
        if(ServerLifecycleHooks.getCurrentServer() == null) return null;
        if(!(itemStack.getItem() instanceof AEUniversalCellItem cellItem)) return null;
        if(itemStack.getCount() != 1) return null;

        AEUniversalCellData cellData = AEUniversalCellData.computeIfAbsentCellDataForItemStack(itemStack);
        if(cellData == null) return null;

        return new AEUniversalCellInventory(cellData, itemStack, cellItem);
    }
}
