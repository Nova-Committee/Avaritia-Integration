package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.stacks.AEKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * uuid -> {@link net.minecraft.world.level.saveddata.SavedData} 数据的管理类
 * <p>每个元件单独对应一个文件： data/ae_universal_cell_data/<uuid>.dat
 * <p>单文件损坏只影响单元件，降低风险。
 */
public class AEUniversalCellData extends SavedData
{

    /** 主容器子标签 */
    public static final String INV_SAVED_TAG = "inventory";

    /** 读取成功的条目列表名（在 INV_SAVED_TAG 里） */
    private static final String ENTRIES_TAG = "entries";

    /** 读取失败的条目列表名（在 INV_SAVED_TAG 里） */
    private static final String ERROR_ENTRIES_TAG = "error_entries";

    /** 单条目里的 key 子标签名 */
    private static final String ENTRY_KEY_TAG = "key";

    /** 单条目里的 amount 子标签名 */
    private static final String ENTRY_AMOUNT_TAG = "amount";

    /** 在ItemStack中，用于元件仓库 UUID 的 nbt 子标签名 */
    public static final String UUID_TAG = "ae_universal_cell_uuid";

    /** 统一目录名（位于 world/data/ 下） */
    private static final String SAVED_FOLDER_NAME = "ae_universal_cell_data";

    /** 原始仓库存放在此处，后续 AEUniversalCellInventory 使用此仓库的引用来构建 */
    private final Map<AEKey, Long> storage;

    /**
     * 上次反序列化失败而保留下来的“原始条目”队列。
     * - 每次 save() 都会把它写入到 INV_SAVED_TAG/ERROR_ENTRIES_TAG。
     * - 每次 load() 都会尝试“重读”；成功则并入 storage 并打印信息，失败则静默保留。
     */
    private final List<CompoundTag> pendingReadErrors;

    public AEUniversalCellData(@NotNull Map<AEKey, Long> storage)
    {
        this(storage, new ArrayList<>());
    }

    private AEUniversalCellData(@NotNull Map<AEKey, Long> storage,
                                @NotNull List<CompoundTag> pendingReadErrors)
    {
        this.storage = storage;
        this.pendingReadErrors = pendingReadErrors;
    }

    /** 获取原始存储数据 */
    public @NotNull Map<AEKey, Long> getOriginalStorage()
    {
        return storage;
    }

    /** 根据 UUID 获取对应的数据（仅当磁盘上已有对应文件时返回） */
    public static @Nullable AEUniversalCellData getCellDataByUUID(@NotNull UUID uuid)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return null;

        // 不是必须，但有助于在迁移存档后首次读写前保证目录存在
        ensureSaveDirExists(server);

        final String key = makeKey(uuid);
        return server.overworld().getDataStorage().get(AEUniversalCellData::load, key);
    }

    /**
     * 如果 ItemStack 存在 UUID 且对应数据文件存在，则加载；
     * 否则为其分配一个全新 UUID，创建并注册新的 SavedData，并把 UUID 写回该物品。
     */
    public static @Nullable AEUniversalCellData computeIfAbsentCellDataForItemStack(@NotNull ItemStack itemStack)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return null;

        ensureSaveDirExists(server);

        final var dataStorage = server.overworld().getDataStorage();
        final CompoundTag tag = itemStack.getOrCreateTag();

        // 先尝试读取已有 UUID 且对应文件存在
        if (tag.contains(UUID_TAG)) {
            try {
                UUID existing = UUID.fromString(tag.getString(UUID_TAG));
                AEUniversalCellData data = getCellDataByUUID(existing);
                if (data != null) {
                    return data;
                }
            } catch (IllegalArgumentException ignored) {
                // 非法 UUID 字符串，忽略，走创建流程
            }
        }

        // 分配一个全新的、与现有文件不冲突的 UUID
        UUID fresh;
        do {
            fresh = UUID.randomUUID();
        } while (getCellDataByUUID(fresh) != null); // 近乎不可能，但防御一下

        // 写回物品上的 UUID
        tag.putString(UUID_TAG, fresh.toString());

        // 创建并注册新的 SavedData（注册到 DataStorage 后由世界存档生命周期负责持久化）
        AEUniversalCellData newData = new AEUniversalCellData(new HashMap<>());
        dataStorage.set(makeKey(fresh), newData);
        newData.setDirty(); // 标脏以便尽快保存

        return newData;
    }

    /** 硬盘序列化 */
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag)
    {
        // 统一放在 INV_SAVED_TAG 里
        CompoundTag invTag = new CompoundTag();

        // 正常条目
        ListTag entriesList = new ListTag();
        for (Map.Entry<AEKey, Long> KAentry : storage.entrySet())
        {
            AEKey key = KAentry.getKey();
            Long amountObj = KAentry.getValue();
            long amount = amountObj == null ? 0L : amountObj;

            if (key == null)
            {
                // 理论上不应出现，防御性略过
                System.err.println("[AEUniversalCellData] Skip null key during serialization.");
                continue;
            }

            try
            {
                CompoundTag entry = new CompoundTag();
                entry.put(ENTRY_KEY_TAG, key.toTagGeneric()); // 通用写法，包含 #c
                entry.putLong(ENTRY_AMOUNT_TAG, amount);
                entriesList.add(entry);
            }
            catch(Throwable ex)
            {
                // 序列化失败：无法可靠得到要保存的信息 -> 打印并略过
                System.err.println("[AEUniversalCellData] Failed to serialize entry: key=" + key
                        + ", amount=" + amount + " ; cause=" + ex);
            }
        }
        invTag.put(ENTRIES_TAG, entriesList);

        // 读取失败的条目（从上次 load() 残留，或重试后仍失败），原样写回
        ListTag errorList = new ListTag();
        for (CompoundTag bad : pendingReadErrors)
        {
            // 这里直接写入一份 copy，避免外部引用导致的潜在修改
            errorList.add(bad.copy());
        }
        invTag.put(ERROR_ENTRIES_TAG, errorList);

        tag.put(INV_SAVED_TAG, invTag);
        return tag;
    }

    /** 从硬盘反序列化，用于 SavedData 的工厂方法 */
    public static AEUniversalCellData load(CompoundTag tag)
    {
        HashMap<AEKey, Long> storage = new HashMap<>();
        List<CompoundTag> errorQueue = new ArrayList<>();

        // 统一从 INV_SAVED_TAG 读取
        CompoundTag invTag = tag.getCompound(INV_SAVED_TAG);

        // 读取正常条目
        ListTag entries = invTag.getList(ENTRIES_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < entries.size(); i++)
        {
            CompoundTag entry = entries.getCompound(i);
            try
            {
                CompoundTag keyTag = entry.getCompound(ENTRY_KEY_TAG);
                AEKey key = AEKey.fromTagGeneric(keyTag);
                if(key == null)
                {
                    // 解析失败 -> 放入错误队列，打印
                    errorQueue.add(entry.copy());
                    System.err.println("[AEUniversalCellData] Failed to deserialize entry (null key). Entry=" + entry);
                    continue;
                }
                long amount = entry.getLong(ENTRY_AMOUNT_TAG);
                storage.merge(key, amount, Long::sum);
            }
            catch
            (Throwable ex)
            {
                // 解析失败 -> 放入错误队列，打印
                errorQueue.add(entry.copy());
                System.err.println("[AEUniversalCellData] Failed to deserialize entry: " + entry + " ; cause=" + ex);
            }
        }

        // 尝试重读历史错误条目（上次保存时写入的 ERROR_ENTRIES_TAG）
        ListTag oldErrors = invTag.getList(ERROR_ENTRIES_TAG, Tag.TAG_COMPOUND);
        for (int i = 0; i < oldErrors.size(); i++)
        {
            CompoundTag badEntry = oldErrors.getCompound(i);
            boolean recovered = false;
            try
            {
                CompoundTag keyTag = badEntry.getCompound(ENTRY_KEY_TAG);
                AEKey key = AEKey.fromTagGeneric(keyTag);
                if (key != null)
                {
                    long amount = badEntry.getLong(ENTRY_AMOUNT_TAG);
                    storage.merge(key, amount, Long::sum);
                    recovered = true;
                }
            }
            catch(Throwable ignored)
            {
                recovered = false;
            }

            if (recovered)
            {
                // 重读成功：打印信息，但不再保留到错误队列
                System.err.println("[AEUniversalCellData] Recovered previously failed entry: " + badEntry);
            }
            else
            {
                // 仍失败：静默保留，等待下次保存写回 下次加载再尝试
                errorQueue.add(badEntry.copy());
            }
        }
        return new AEUniversalCellData(storage, errorQueue);
    }

    // ---------------------------------- 辅助方法 ----------------------------------

    /** 生成 DataStorage 的路径（1.20.1 支持子路径，但不会自动建目录） */
    private static String makeKey(@NotNull UUID uuid)
    {
        return SAVED_FOLDER_NAME + "/" + uuid;
    }

    /** 确保 world/data/ae_universal_cell_data 目录存在 */
    private static void ensureSaveDirExists(@NotNull MinecraftServer server)
    {
        Path dir = server.getWorldPath(LevelResource.ROOT)
                .resolve("data")
                .resolve(SAVED_FOLDER_NAME);
        try
        {
            Files.createDirectories(dir); // 已存在则静默通过
        }
        catch(IOException e)
        {
            // 不中断，但留痕方便排查
            System.err.println("[AEUniversalCellData] Failed to create save directory: " + dir + " : " + e);
        }
    }
}