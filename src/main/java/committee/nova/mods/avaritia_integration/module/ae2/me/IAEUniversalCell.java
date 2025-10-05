package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.CellState;
import appeng.api.upgrades.IUpgradeableItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 定义一些信息获取接口以及一些为ItemStack快速设置/获取信息的方法
 * <p>
 * 是否无限容量、总字节数、待机功耗等信息
 * @author Frostbite
 */
public interface IAEUniversalCell extends IUpgradeableItem
{
    String CELL_STATE_TAG_NAME = "ae_universal_cell_state";
    String CELL_BYTES_USAGE_TAG_NAME = "ae_universal_cell_bytes_usage";
    String CELL_TYPES_USAGE_TAG_NAME = "ae_universal_cell_types_usage";
    String CELL_SHOW_TOOLTIP_STACKS_TAG_NAME = "ae_universal_cell_show_tooltip_stacks";

    /** 约定：返回值小于等于0则视为不限制类型总数 */
    int getTotalBytes();

    /** 约定：返回值小于等于0则视为不限制类型总数 */
    int getTotalTypes();

    double getIdleDrain();

    // 以下辅助方法均只操作用于让客户端显示的数据，其修改的任何数据对服务端无实际意义。
    // 修改时，为了保证数据准确无误，务必从服务端给出正确的上下文后内部计算
    // 不应该内部互相调用，除非内部调用时的最终数据来源为服务端
    static int getUsedBytes(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        if (tag.contains(CELL_BYTES_USAGE_TAG_NAME)) return tag.getInt(CELL_BYTES_USAGE_TAG_NAME);
        return 0;
    }

    static void setUsedBytes(ItemStack stack, int usedBytes)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(CELL_BYTES_USAGE_TAG_NAME, Math.max(0, usedBytes));
    }

    static int getUsedTypes(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if(tag == null) return 0;
        if(tag.contains(CELL_TYPES_USAGE_TAG_NAME)) return tag.getInt(CELL_TYPES_USAGE_TAG_NAME);
        return 0;
    }

    static void setUsedTypes(ItemStack stack, int usedTypes)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(CELL_TYPES_USAGE_TAG_NAME, Math.max(0, usedTypes));
    }

    static CellState getCellState(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return CellState.EMPTY;
        if (tag.contains(CELL_STATE_TAG_NAME)) return CellState.valueOf(tag.getString(CELL_STATE_TAG_NAME));
        return CellState.EMPTY;
    }

    static void setCellState(ItemStack stack, IAEUniversalCell cellType, int usedBytes, int usedTypes)
    {
        final int totalBytes = cellType.getTotalBytes(); // <=0 视为无限
        final int totalTypes = cellType.getTotalTypes(); // <=0 视为无限

        final CellState state;
        if (usedBytes <= 0 && usedTypes <= 0)
        {
            state = CellState.EMPTY;
        }
        else if (totalBytes > 0 && usedBytes >= totalBytes)
        {
            state = CellState.FULL;
        }
        else if (totalTypes > 0 && usedTypes >= totalTypes)
        {
            state = CellState.TYPES_FULL;
        }
        else
        {
            state = CellState.NOT_EMPTY;
        }

        stack.getOrCreateTag().putString(CELL_STATE_TAG_NAME, state.name());
    }

    static List<GenericStack> getTooltipShowStacks(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return List.of();

        // 期望是一个 ListTag<CompoundTag>，每个元素是 GenericStack.writeTag(...) 的结果
        ListTag list = tag.getList(CELL_SHOW_TOOLTIP_STACKS_TAG_NAME, Tag.TAG_COMPOUND);
        if (list.isEmpty()) return List.of();

        List<GenericStack> out = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++)
        {
            CompoundTag entry = list.getCompound(i);
            GenericStack genericStack = GenericStack.readTag(entry);
            if (genericStack != null) out.add(genericStack); // 坏条目静默跳过
        }
        return Collections.unmodifiableList(out);
    }

    static void setTooltipShowStacks(ItemStack stack, List<GenericStack> showStacks)
    {
        if (showStacks == null || showStacks.isEmpty())
        {
            CompoundTag tag = stack.getTag();
            if (tag != null) tag.remove(CELL_SHOW_TOOLTIP_STACKS_TAG_NAME);
            return;
        }

        ListTag list = new ListTag();
        for (GenericStack gs : showStacks)
        {
            if (gs == null) continue;
            list.add(GenericStack.writeTag(gs));
        }
        stack.getOrCreateTag().put(CELL_SHOW_TOOLTIP_STACKS_TAG_NAME, list);
    }
}