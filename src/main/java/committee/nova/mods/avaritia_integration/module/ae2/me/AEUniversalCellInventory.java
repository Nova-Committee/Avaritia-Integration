package committee.nova.mods.avaritia_integration.module.ae2.me;

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
import appeng.api.storage.cells.StorageCell;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.core.definitions.AEItems;
import appeng.util.ConfigInventory;
import appeng.util.prioritylist.IPartitionList;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 能存储多种不同资源的元件的内部存储（通用盘）
 * <p>
 * usedBytes 仅由“值字节”组成：Σ ceil( sum(apb-bucket) / amountPerByte )。
 * <p>
 * insert/extract 走增量：维护 bucketSums、storedTypesCached、usedBytesCached。
 * <p>
 * 支持 AE2 升级卡：分区（白/黑）、模糊、均分、虚空、递归盘保护。
 * <p>
 * 每次变更后：立即刷新物品 NBT（tooltip 用）并标脏 SavedData。
 *
 * @author Frostbite
 */
public class AEUniversalCellInventory implements StorageCell
{

    /** 对应的 SavedData（用于 setDirty 通知存盘） */
    private final @NotNull AEUniversalCellData cellData;

    /** 来自 AEUniversalCellData 的原始存储引用 */
    private final @NotNull Object2LongMap<AEKey> storage;

    /** 对应的物品堆（用于更新客户端 NBT 用于 tooltip/states） */
    private final @NotNull ItemStack itemStack;

    /** 元件类型（由物品类实现，提供总字节/总类型/待机功耗等固定信息） */
    private final @NotNull IAEUniversalCell cellType;

    // 运行时缓存 ----------------------------------------------------------------------

    /** 有效总字节（<=0 视为无限 -> Long.MAX_VALUE） */
    private final long totalBytesEff;

    /** 有效“最多类型数”（<=0 视为无限 -> Long.MAX_VALUE） */
    private final long totalTypesEff;

    /** 当前“已用字节”，按 Σ桶内 ceil(Σamount/amountPerByte) 计算 */
    private long usedBytesCached;

    /** 当前“已用类型数”（value>0 的 AEKey 数量） */
    private long storedTypesCached;

    /**
     * apb 桶累计：key=amountPerByte（>0），value=该桶内所有 Key 的数量总和。
     * 用于 O(1) 计算“额外再塞多少单位会增加几个字节”
     */
    private final Long2LongOpenHashMap bucketSums = new Long2LongOpenHashMap();

    public AEUniversalCellInventory(@NotNull AEUniversalCellData cellData,
                                    @NotNull ItemStack itemStack,
                                    @NotNull IAEUniversalCell cellType)
    {
        this.cellData = cellData;
        this.storage = cellData.getOriginalStorage();
        this.itemStack = itemStack;
        this.cellType = cellType;

        this.bucketSums.defaultReturnValue(0L);

        // 统一把 无限 映射为 Long.MAX_VALUE，简化后续判断
        long totalBytes = cellType.getTotalBytes();
        this.totalBytesEff = (totalBytes <= 0) ? Long.MAX_VALUE : totalBytes;

        long totalTypes = cellType.getTotalTypes();
        this.totalTypesEff = (totalTypes <= 0) ? Long.MAX_VALUE : totalTypes;

        // 首次全量统计：填充 storedTypesCached、bucketSums、usedBytesCached
        long types = 0;
        for (Object2LongMap.Entry<AEKey> e : storage.object2LongEntrySet())
        {
            long v = e.getLongValue();
            if (v <= 0) continue;
            types++;
            long apb = Math.max(1, e.getKey().getType().getAmountPerByte());
            bucketSums.addTo(apb, v);
        }
        this.storedTypesCached = types;

        long bytesForValues = 0;
        for (Long2LongMap.Entry b : bucketSums.long2LongEntrySet())
        {
            long apb = b.getLongKey();
            long sum = b.getLongValue();
            bytesForValues = safeAdd(bytesForValues, ceilDiv(sum, apb));
        }
        this.usedBytesCached = bytesForValues;

        // 初始化后把统计状态写进 ItemStack 给客户端显示用
        updateItemTooltipState();
    }

    // StorageCell 接口 ----------------------------------------------------------------

    /** 获取状态灯 */
    @Override
    public CellState getStatus()
    {
        if (storedTypesCached == 0) return CellState.EMPTY;

        final long freeBytes = freeBytes();
        final boolean canOpenNewType = canHoldNewItemGeneric(freeBytes);
        if (canOpenNewType)
        {
            return CellState.NOT_EMPTY;
        }

        // 不能开新类型时，只要还能往现有类型里塞东西，就算 TYPES_FULL，否则 FULL。
        final boolean hasPartial = hasAnyBucketPartial() || freeBytes > 0;
        return hasPartial ? CellState.TYPES_FULL : CellState.FULL;
    }

    /** 待机功耗 */
    @Override
    public double getIdleDrain()
    {
        return cellType.getIdleDrain();
    }

    /** 允许被放入其他存储元件内（此元件对应物品仅存储UUID和几个预览物品） */
    @Override
    public boolean canFitInsideCell()
    {
        return true;
    }

    /** 忽略 persist，我们在 insert/extract 后立即 setDirty，随后由 SavedData 处理存盘 */
    @Override
    public void persist()
    {
        // no-op
    }

    /** 存入实现 */
    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        if (amount <= 0) return 0;

        // 分区/模糊/黑白名单 与 递归盘保护
        if (!matchesPartitionAndUpgrades(what)) return 0;
        if (!canNestStorageCells(what)) return 0;

        // 取当前 apb 与现存量
        final long amountPerByte = Math.max(1, what.getType().getAmountPerByte());
        final long current = storage.getLong(what);

        final long freeBytes = freeBytes();
        final boolean openingNewType = (current <= 0);

        // 先算“仅向现有类型堆叠”的可塞单位（针对当前 apb）
        long allowedUnits = remainingUnitsIntoExistingFor(amountPerByte, freeBytes);

        // 新开类型：需满足“有类型名额”
        if (openingNewType)
        {
            if (storedTypesCached >= totalTypesEff)
            {
                return handleOverflowVoidOnInsert(what, amount, /*inserted*/ 0);
            }
            // 若 freeBytes==0 但 apb 桶存在碎片，allowedUnits 仍可能>0（允许借碎片完成首字节）
            if (allowedUnits <= 0)
            {
                return handleOverflowVoidOnInsert(what, amount, /*inserted*/ 0);
            }
        }

        // 均分卡：限制“该类型”最大单位上限
        long maxPerTypeCap = computeEqualDistributionCap(amountPerByte);
        if (maxPerTypeCap != Long.MAX_VALUE)
        {
            long canGrowBy = Math.max(0, maxPerTypeCap - current);
            allowedUnits = Math.min(allowedUnits, canGrowBy);
            if (allowedUnits <= 0)
            {
                return handleOverflowVoidOnInsert(what, amount, /*inserted*/ 0);
            }
        }

        // 这次实际能塞多少
        final long toInsert = Math.min(amount, allowedUnits);
        if (toInsert <= 0) return 0;

        if (mode == Actionable.MODULATE)
        {
            // ---- 增量更新缓存：桶累计、已用字节、已用类型数 ----
            final long oldBucket = bucketSums.get(amountPerByte);
            final long newBucket = safeAdd(oldBucket, toInsert);

            // 值字节的增量 = ceil(new/apb) - ceil(old/apb)
            final long deltaValueBytes = safeSub(ceilDiv(newBucket, amountPerByte), ceilDiv(oldBucket, amountPerByte));

            // 打开新类型：仅 +1 个类型
            if (openingNewType)
            {
                storedTypesCached = safeAdd(storedTypesCached, 1);
            }

            // 应用“值字节”增量
            usedBytesCached = safeAdd(usedBytesCached, deltaValueBytes);

            // 写回桶累计与具体 Key 的存量
            bucketSums.put(amountPerByte, newBucket);
            storage.put(what, safeAdd(current, toInsert));

            // 客户端状态 + 标脏
            afterMutationUpdateClientAndSave();
        }
        return toInsert;
    }

    /** 取出实现 */
    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source)
    {
        if (amount <= 0) return 0;

        final long current = storage.getLong(what);
        if (current <= 0) return 0;

        final long taken = Math.min(amount, current);

        if (mode == Actionable.MODULATE)
        {
            final long amountPerByte = Math.max(1, what.getType().getAmountPerByte());
            final long oldBucket = bucketSums.get(amountPerByte);
            final long newBucket = Math.max(0, oldBucket - taken);

            // 值字节的增量 = ceil(new/apb) - ceil(old/apb)（可能为负）
            final long deltaValueBytes = safeSub(ceilDiv(newBucket, amountPerByte), ceilDiv(oldBucket, amountPerByte));

            usedBytesCached = safeAdd(usedBytesCached, deltaValueBytes);

            long next = current - taken;
            if (next > 0)
            {
                storage.put(what, next);
            }
            else
            {
                storage.removeLong(what);
                storedTypesCached = Math.max(0, storedTypesCached - 1);
            }

            // 更新桶
            if (newBucket > 0) bucketSums.put(amountPerByte, newBucket);
            else bucketSums.remove(amountPerByte);

            // 客户端状态 + 标脏
            afterMutationUpdateClientAndSave();
        }
        return taken;
    }

    @Override
    public void getAvailableStacks(KeyCounter out)
    {
        for (Object2LongMap.Entry<AEKey> entry : storage.object2LongEntrySet())
        {
            long value = entry.getLongValue();
            if (value > 0) out.add(entry.getKey(), value);
        }
    }

    @Override
    public Component getDescription()
    {
        return this.itemStack.getHoverName();
    }

    // 内部辅助工具 --------------------------------------------------------------------

    /** 当前剩余字节（无限时为 Long.MAX_VALUE）。 */
    private long freeBytes()
    {
        if (totalBytesEff == Long.MAX_VALUE) return Long.MAX_VALUE;
        long freeBytes = totalBytesEff - usedBytesCached;
        return Math.max(0, freeBytes);
    }

    /** 是否还能新开一种类型（仅看类型名额；若无剩余字节但存在任意 apb 桶碎片，也允许借碎片开启） */
    private boolean canHoldNewItemGeneric(long freeBytes)
    {
        if (storedTypesCached >= totalTypesEff) return false; // 没有类型名额
        if (freeBytes > 0) return true;                       // 还能增加值字节
        return hasAnyBucketPartial();                         // 或仍可用“桶碎片”（不增字节）开新类型
    }

    /** 是否存在任何“桶”未凑满 1 字节（sum % amountPerByte != 0） */
    private boolean hasAnyBucketPartial()
    {
        for (Long2LongMap.Entry bucket : bucketSums.long2LongEntrySet())
        {
            long apb = bucket.getLongKey(), sum = bucket.getLongValue();
            if (sum > 0 && (sum % apb) != 0) return true;
        }
        return false;
    }

    /**
     * 在“不新开类型”的前提下，能往“当前 apb 的桶”继续塞入多少单位：
     * 即：该桶补齐到下一个字节的“缺口单位数” + freeBytes * apb
     * 若 freeBytes 为无限，直接返回 Long.MAX_VALUE
     */
    private long remainingUnitsIntoExistingFor(long amountPerByte, long freeBytes)
    {
        long sum = bucketSums.get(amountPerByte);
        long pad = (sum == 0) ? 0 : ((amountPerByte - (sum % amountPerByte)) % amountPerByte);
        if (freeBytes == Long.MAX_VALUE) return Long.MAX_VALUE;
        long extra = safeMul(freeBytes, amountPerByte);
        return safeAdd(pad, extra);
    }

    /** 递归盘保护：若 what 是“另一个存储盘”且该盘声明不能嵌入，则拒收。 */
    private boolean canNestStorageCells(AEKey what
    ) {
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
     * 均分卡：计算“当前 what（apb）”的单位上限。无均分卡时返回 Long.MAX_VALUE。
     */
    private long computeEqualDistributionCap(long apb)
    {
        final IUpgradeInventory upgrades = cellType.getUpgrades(itemStack);
        if (!upgrades.isInstalled(AEItems.EQUAL_DISTRIBUTION_CARD)) return Long.MAX_VALUE;

        final boolean hasFuzzy = upgrades.isInstalled(AEItems.FUZZY_CARD);
        final boolean whitelist = !upgrades.isInstalled(AEItems.INVERTER_CARD);

        long estimatedTypes = Long.MAX_VALUE;
        ConfigInventory config = (cellType instanceof ICellWorkbenchItem cwi)
                ? cwi.getConfigInventory(itemStack)
                : null;

        // ae 原版逻辑：只有在“非模糊 + 白名单 + 配置非空”时，用配置条目数估算类型数
        if (!hasFuzzy && whitelist && config != null && !config.keySet().isEmpty())
        {
            estimatedTypes = config.keySet().size();
        }

        estimatedTypes = Math.min(estimatedTypes, totalTypesEff);
        if (estimatedTypes <= 0) return 0L;
        if (totalBytesEff == Long.MAX_VALUE) return Long.MAX_VALUE; // 无限字节 => 不限额

        long netBytes = totalBytesEff;
        long units = safeMul(netBytes, apb);
        return ceilDiv(units, estimatedTypes); // 向上取整
    }

    /**
     * 虚空卡处理（与 ae 原版一致）：
     * - 若“未分区”且“无法再开新类型”，则：已存在该类型 => 全吞（返回 amount）；否则仅返回 inserted（通常 0）
     * - 其它情况，只要装了虚空卡 => 全吞（返回 amount）
     * 未装虚空卡 => 返回 inserted
     */
    private long handleOverflowVoidOnInsert(AEKey what, long amount, long inserted)
    {
        final IUpgradeInventory upgrades = cellType.getUpgrades(itemStack);
        if (!upgrades.isInstalled(AEItems.VOID_CARD)) return inserted;

        boolean unpartitioned = true;
        if (cellType instanceof ICellWorkbenchItem cellWorkbenchItem)
        {
            ConfigInventory configInventory = cellWorkbenchItem.getConfigInventory(itemStack);
            unpartitioned = (configInventory == null || configInventory.keySet().isEmpty());
        }

        final long freeBytes = freeBytes();
        final boolean canOpen = canHoldNewItemGeneric(freeBytes);

        if (unpartitioned && !canOpen)
        {
            boolean exists = storage.getLong(what) > 0;
            return exists ? amount : inserted;
        }
        return amount;
    }

    /**
     * 更新物品 NBT（字节/类型 & 状态）以供客户端后续使用，并立即 setDirty。
     * 注意：状态更新后，nbt 数据会在客户端下次读取时随 menu 同步
     */
    private void afterMutationUpdateClientAndSave()
    {
        updateItemTooltipState();
        cellData.setDirty();
    }

    /** 把“已用字节/类型 & 状态 + 预览堆栈前五条”写到物品 NBT（仅供客户端 tooltip 用） */
    private void updateItemTooltipState()
    {
        int usedBytesClamped = (int) Math.min(Integer.MAX_VALUE, Math.max(0, usedBytesCached));
        int usedTypesClamped = (int) Math.min(Integer.MAX_VALUE, Math.max(0, storedTypesCached));
        IAEUniversalCell.setUsedBytes(itemStack, usedBytesClamped);
        IAEUniversalCell.setUsedTypes(itemStack, usedTypesClamped);
        IAEUniversalCell.setCellState(itemStack, cellType, usedBytesClamped, usedTypesClamped);

        // 取迭代到的前 5 个 kv，对应数量>0 的条目，构造成 GenericStack 列表
        List<GenericStack> show = new ArrayList<>(5);
        int count = 0;
        for (Object2LongMap.Entry<AEKey> e : storage.object2LongEntrySet())
        {
            long v = e.getLongValue();
            if (v <= 0L) continue;
            show.add(new GenericStack(e.getKey(), v));
            if (++count >= 5) break;
        }
        IAEUniversalCell.setTooltipShowStacks(itemStack, show);
    }

    // 简单算数工具 --------------------------------------------------------------------

    /** 除法，然后向上取整 */
    private static long ceilDiv(long a, long b)
    {
        if (b <= 0) throw new IllegalArgumentException("div by non-positive");
        if (a <= 0) return 0;
        long q = a / b;
        long r = a % b;
        return r == 0 ? q : (q + 1);
    }

    /** 加法（带上溢钳制） */
    private static long safeAdd(long a, long b)
    {
        long r = a + b;
        if (((a ^ r) & (b ^ r)) < 0) return Long.MAX_VALUE;
        return r;
    }

    /** 减法（带下溢钳制） */
    private static long safeSub(long a, long b)
    {
        long r = a - b;
        if (((a ^ b) & (a ^ r)) < 0) return Long.MIN_VALUE;
        return r;
    }

    /** 乘法（带上溢钳制） */
    private static long safeMul(long a, long b)
    {
        if (a == 0 || b == 0) return 0;
        if (a > Long.MAX_VALUE / b) return Long.MAX_VALUE;
        return a * b;
    }
}