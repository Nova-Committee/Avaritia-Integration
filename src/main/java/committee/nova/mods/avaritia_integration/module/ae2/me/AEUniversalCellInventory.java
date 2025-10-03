package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 能存储多种不同资源的元件的内部存储
 */
public class AEUniversalCellInventory implements StorageCell
{
    /** storage一般传入来自AEUniversalCellData的原始存储的引用 */
    private final @NotNull Map<AEKey, Long> storage;
    /** 传入元件所对应的实际物品堆，用于设置nbt标签在客户端提供tooltip和内容预览 */
    private final @NotNull ItemStack itemStack;
    /** 元件类型，一般由Item类实现，在注册时将信息以单例形式写入，我们从这里读取一些固定的元件信息 */
    private final @NotNull IAEUniversalCell cellType;


    public AEUniversalCellInventory(@NotNull Map<AEKey, Long> storage, @NotNull ItemStack itemStack, @NotNull IAEUniversalCell cellType)
    {
        this.storage = storage;
        this.itemStack = itemStack;
        this.cellType = cellType;
    }

    @Override
    public CellState getStatus()
    {
        return null;
    }

    @Override
    public double getIdleDrain()
    {
        return 0;
    }

    @Override
    public boolean canFitInsideCell()
    {
        return StorageCell.super.canFitInsideCell();
    }

    @Override
    public void persist()
    {

    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source)
    {
        return StorageCell.super.isPreferredStorageFor(what, source);
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        return StorageCell.super.insert(what, amount, mode, source);
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        return StorageCell.super.extract(what, amount, mode, source);
    }

    @Override
    public void getAvailableStacks(KeyCounter out)
    {
        StorageCell.super.getAvailableStacks(out);
    }

    @Override
    public Component getDescription()
    {
        return null;
    }

    @Override
    public KeyCounter getAvailableStacks()
    {
        return StorageCell.super.getAvailableStacks();
    }
}
