package committee.nova.mods.avaritia_integration.module.ae2.me.biginteger;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

public class AEBigIntegerCellHandler implements ICellHandler
{
    public static final AEBigIntegerCellHandler INSTANCE = new AEBigIntegerCellHandler();

    private AEBigIntegerCellHandler() {}

    @Override
    public boolean isCell(ItemStack itemStack)
    {
        // 只允许堆叠为1的创建为元件，以防止极端情况下可能存在的刷物品bug
        return itemStack.getItem() instanceof IAEBigIntegerCell && itemStack.getCount() == 1;
    }

    @Override
    public @Nullable StorageCell getCellInventory(ItemStack itemStack, @Nullable ISaveProvider iSaveProvider)
    {
        if(ServerLifecycleHooks.getCurrentServer() == null) return null;
        if(!(itemStack.getItem() instanceof IAEBigIntegerCell cellItem)) return null;
        if(itemStack.getCount() != 1) return null;

        AEBigIntegerCellData cellData = AEBigIntegerCellData.computeIfAbsentCellDataForItemStack(itemStack);
        if(cellData == null) return null;

        return new AEBigIntegerCellInventory(cellData, itemStack, cellItem, iSaveProvider);
    }
}
