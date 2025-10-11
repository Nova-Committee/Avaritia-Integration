package committee.nova.mods.avaritia_integration.module.ae2.me.biginteger;

import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.config.IncludeExclude;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.core.definitions.AEItems;
import appeng.util.ConfigInventory;
import appeng.util.prioritylist.IPartitionList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * BigInteger 版本的AEUniversalCellInventory内部存储。
 * <p>
 * 由于AE2以及绝大部分正常模组的api都不会使用BigInteger。故，此仓库计划仅用于创造元件，有关容量检查以及与容量相关升级卡的部分均被移除。
 *
 * @author Frostbite
 */
public class AEBigIntegerCellInventory implements StorageCell
{

    /** 对应的 SavedData（用于 setDirty 通知存盘） */
    private final @NotNull AEBigIntegerCellData cellData;

    /** 原始存储引用（AEKey -> amount(BigInteger)） */
    private final @NotNull Object2ObjectMap<AEKey, BigInteger> storage;

    /** 对应的物品堆（用于更新客户端 NBT 用于 tooltip/states） */
    private final @NotNull ItemStack itemStack;

    /** 元件类型（提供总字节/总类型/待机功耗等固定信息） */
    private final @NotNull IAEBigIntegerCell cellType;

    /** AE容器的保存回调：用于统一的 persist 时机 */
    private final @Nullable ISaveProvider saveContainer;

    // 运行时缓存 ----------------------------------------------------------------------

    /** 当前“已用字节”（BigInteger），按 Σ桶内 ceil(Σamount/amountPerByte) 计算 */
    private BigInteger usedBytesCached;

    /** 是否通知持久化 */
    private boolean isPersisted = false;

    /**
     * apb 桶累计：key=amountPerByte（>0），value=该桶内所有 Key 的数量总和（BigInteger）。
     * 用于 O(1) 计算“额外再塞多少单位会增加几个字节”
     */
    private final Long2ObjectOpenHashMap<BigInteger> bucketSums = new Long2ObjectOpenHashMap<>();

    public AEBigIntegerCellInventory(@NotNull AEBigIntegerCellData cellData,
                                     @NotNull ItemStack itemStack,
                                     @NotNull IAEBigIntegerCell cellType,
                                     @Nullable ISaveProvider saveProvider)
    {
        this.cellData = cellData;
        this.storage = cellData.getOriginalStorage();
        this.itemStack = itemStack;
        this.cellType = cellType;
        this.saveContainer = saveProvider;

        this.bucketSums.defaultReturnValue(BigInteger.ZERO);

        // 首次全量统计：填充 bucketSums、usedBytesCached
        for (Object2ObjectMap.Entry<AEKey, BigInteger> e : storage.object2ObjectEntrySet())
        {
            BigInteger v = nonNegative(e.getValue());
            if (v.signum() <= 0) continue;
            long apb = Math.max(1, e.getKey().getType().getAmountPerByte());
            bucketSums.put(apb, bucketSums.get(apb).add(v));
        }

        BigInteger bytesForValues = BigInteger.ZERO;
        for (Long2ObjectMap.Entry<BigInteger> b : bucketSums.long2ObjectEntrySet())
        {
            long apb = b.getLongKey();
            BigInteger sum = b.getValue();
            bytesForValues = bytesForValues.add(ceilDiv(sum, apb));
        }
        this.usedBytesCached = bytesForValues;

        // 初始化后把统计状态写进ItemStack给客户端显示用
        updateItemTooltipState();
    }

    // StorageCell 接口 ----------------------------------------------------------------

    /** 获取状态灯 */
    @Override
    public CellState getStatus()
    {
        if (storage.isEmpty()) return CellState.EMPTY;
        else return CellState.NOT_EMPTY;
    }

    /** 待机功耗 */
    @Override
    public double getIdleDrain()
    {
        return cellType.getIdleDrain();
    }

    /** 允许被放入其他存储元件内 */
    @Override
    public boolean canFitInsideCell()
    {
        return true;
    }

    /** 由驱动器等物品的统一监听，以减少频繁 tooltip 更新的额外开销 */
    @Override
    public void persist()
    {
        if (isPersisted) return;

        updateItemTooltipState();
        isPersisted = true;
    }

    /** 存入实现（BigInteger） */
    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        if (amount <= 0) return 0;

        // 分区/模糊/黑白名单 与 递归盘保护
        if (!matchesPartitionAndUpgrades(what)) return 0;
        if (!canNestStorageCells(what)) return 0;

        final long apb = Math.max(1, what.getType().getAmountPerByte());
        final BigInteger current = nonNegative(storage.get(what));

        if (mode == Actionable.MODULATE)
        {
            // ---- 增量更新缓存：桶累计、已用字节 ----
            final BigInteger oldBucket = bucketSums.get(apb);
            final BigInteger newBucket = oldBucket.add(BigInteger.valueOf(amount));

            // 值字节的增量 = ceil(new/apb) - ceil(old/apb)
            final BigInteger deltaValueBytes = ceilDiv(newBucket, apb).subtract(ceilDiv(oldBucket, apb));

            // 应用“值字节”增量
            usedBytesCached = usedBytesCached.add(deltaValueBytes);

            // 写回桶累计与具体 Key 的存量
            bucketSums.put(apb, newBucket);
            storage.put(what, current.add(BigInteger.valueOf(amount)));

            // 客户端状态 + 标脏
            markChanged();
        }
        return amount;
    }

    /** 取出实现（BigInteger） */
    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        if (amount <= 0) return 0;

        final BigInteger current = nonNegative(storage.get(what));
        if (current.signum() <= 0) return 0;

        final long currentAsLongCap = clampToLong(current);
        final long taken = Math.min(amount, currentAsLongCap);
        if (taken <= 0) return 0;

        if (mode == Actionable.MODULATE)
        {
            final long apb = Math.max(1, what.getType().getAmountPerByte());
            final BigInteger oldBucket = bucketSums.get(apb);
            BigInteger newBucket = oldBucket.subtract(BigInteger.valueOf(taken));
            if (newBucket.signum() < 0) newBucket = BigInteger.ZERO;

            // 值字节的增量 = ceil(new/apb) - ceil(old/apb)
            final BigInteger deltaValueBytes = ceilDiv(newBucket, apb).subtract(ceilDiv(oldBucket, apb));
            usedBytesCached = usedBytesCached.add(deltaValueBytes);

            BigInteger next = current.subtract(BigInteger.valueOf(taken));
            if (next.signum() > 0)
            {
                storage.put(what, next);
            }
            else
            {
                storage.remove(what);
            }

            // 更新桶
            if (newBucket.signum() > 0) bucketSums.put(apb, newBucket);
            else bucketSums.remove(apb);

            // 客户端状态 + 标脏
            markChanged();
        }
        return taken;
    }

    @Override
    public void getAvailableStacks(KeyCounter out)
    {
        for (Object2ObjectMap.Entry<AEKey, BigInteger> entry : storage.object2ObjectEntrySet())
        {
            BigInteger value = nonNegative(entry.getValue());
            if (value.signum() <= 0) continue;

            // 该 key 在 out 中已存在的数量
            long existing = out.get(entry.getKey());

            // 还能再加多少：避免 (Long.MAX_VALUE - existing) 的长整型上溢
            long headroom = (existing <= 0) ? Long.MAX_VALUE : (Long.MAX_VALUE - existing);
            if (headroom <= 0) continue;

            long add = clampToLong(value);

            if (add > headroom) add = headroom;

            if (add > 0) out.add(entry.getKey(), add);
        }
    }

    @Override
    public Component getDescription()
    {
        return this.itemStack.getHoverName();
    }

    // 内部辅助工具 --------------------------------------------------------------------

    /** 递归盘保护：若 what 是“另一个存储盘”且该盘声明不能嵌入，则拒收。 */
    private boolean canNestStorageCells(AEKey what)
    {
        if (what instanceof AEItemKey itemKey)
        {
            ItemStack s = itemKey.toStack();
            StorageCell nested = StorageCells.getCellInventory(s, null);
            return nested == null || nested.canFitInsideCell();
        }
        return true;
    }

    /** 分区/模糊/白黑名单匹配 */
    private boolean matchesPartitionAndUpgrades(AEKey what)
    {
        // 升级槽
        final IUpgradeInventory upgrades = cellType.getUpgrades(itemStack);
        final boolean hasInverter = upgrades.isInstalled(AEItems.INVERTER_CARD);
        final boolean hasFuzzy = upgrades.isInstalled(AEItems.FUZZY_CARD);

        // 分区配置
        ConfigInventory config = null;
        FuzzyMode fuzzyMode = FuzzyMode.IGNORE_ALL;
        if (cellType instanceof ICellWorkbenchItem cellWorkbenchItem)
        {
            config = cellWorkbenchItem.getConfigInventory(itemStack);
            if (hasFuzzy) fuzzyMode = cellWorkbenchItem.getFuzzyMode(itemStack);
        }
        if (config == null || config.keySet().isEmpty())
        {
            return true; // 未配置视为不过滤
        }

        // 构建分区列表
        IPartitionList.Builder builder = IPartitionList.builder();
        if (hasFuzzy) builder.fuzzyMode(fuzzyMode);
        builder.addAll(config.keySet());
        IPartitionList list = builder.build();

        IncludeExclude mode = hasInverter ? IncludeExclude.BLACKLIST : IncludeExclude.WHITELIST;

        return list.matchesFilter(what, mode);
    }

    /**
     * 更新物品 NBT（字节/类型 & 状态 + 预览堆栈前五条）以供客户端后续使用，并立即 setDirty。
     */
    private void markChanged()
    {
        // 始终在此标脏，以防某些容器实现/ae潜在的不正确persist调用导致未保存
        cellData.setDirty();

        isPersisted = false;
        if (saveContainer != null)
            saveContainer.saveChanges();
        else
            persist();
    }

    /** 把“已用字节/类型 & 状态 + 预览堆栈前五条”写到物品 NBT（仅供客户端 tooltip 用） */
    private void updateItemTooltipState()
    {
        BigInteger used = usedBytesCached.signum() > 0 ? usedBytesCached : BigInteger.ZERO;

        IAEBigIntegerCell.setUsedBytes(itemStack, used);
        IAEBigIntegerCell.setUsedTypes(itemStack, storage.size());
        IAEBigIntegerCell.setCellState(itemStack, getStatus());

        // 取迭代到的前 5 个 kv，对应数量>0 的条目，构造成 GenericStack 列表
        List<GenericStack> show = new ArrayList<>(5);
        int count = 0;
        for (Object2ObjectMap.Entry<AEKey, BigInteger> e : storage.object2ObjectEntrySet())
        {
            BigInteger v = nonNegative(e.getValue());
            if (v.signum() <= 0) continue;
            show.add(new GenericStack(e.getKey(), clampToLong(v)));
            if (++count >= 5) break;
        }
        IAEBigIntegerCell.setTooltipShowStacks(itemStack, show);
    }

    // 简单算数工具（BigInteger 版本） ---------------------------------------------------

    /** BigInteger 向上整除：ceil(a / bLong) */
    private static BigInteger ceilDiv(BigInteger a, long bLong)
    {
        if (bLong <= 0) throw new IllegalArgumentException("div by non-positive");
        if (a.signum() <= 0) return BigInteger.ZERO;
        BigInteger b = BigInteger.valueOf(bLong);
        return a.add(b.subtract(BigInteger.ONE)).divide(b);
    }

    /** BigInteger 向上整除：ceil(a / bBI) */
    private static BigInteger ceilDiv(BigInteger a, BigInteger b)
    {
        if (b.signum() <= 0) throw new IllegalArgumentException("div by non-positive");
        if (a.signum() <= 0) return BigInteger.ZERO;
        return a.add(b.subtract(BigInteger.ONE)).divide(b);
    }

    /** 将 BigInteger 钳到 long（下界 0，上界 Long.MAX_VALUE） */
    private static long clampToLong(BigInteger v)
    {
        if (v.signum() <= 0) return 0L;
        if (v.bitLength() > 63) return Long.MAX_VALUE;
        long r = v.longValue();
        return (r < 0) ? Long.MAX_VALUE : r;
    }

    /** 将 null 或 负数 归一为 ZERO */
    private static BigInteger nonNegative(BigInteger v)
    {
        if (v == null || v.signum() <= 0) return BigInteger.ZERO;
        return v;
    }

    /** BigInteger 最小值 */
    private static BigInteger minBI(BigInteger a, BigInteger b)
    {
        return a.compareTo(b) <= 0 ? a : b;
    }

    /** BigInteger 最大值 */
    @SuppressWarnings("unused")
    private static BigInteger maxBI(BigInteger a, BigInteger b)
    {
        return a.compareTo(b) >= 0 ? a : b;
    }
}