package committee.nova.mods.avaritia_integration.module.ae2.me.biginteger;

import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.CellState;
import appeng.api.upgrades.IUpgradeableItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 定义一些信息获取接口以及一些为ItemStack快速设置/获取信息的方法
 * <p>
 * 与IAEUniversalCell的主要区别为——给所需接口提供BigInteger的路径以及简化方法
 *
 * @author Frostbite
 */
public interface IAEBigIntegerCell extends IUpgradeableItem
{
    String CELL_STATE_TAG_NAME = "ae_universal_cell_state";
    String CELL_BYTES_USAGE_BIG_TAG_NAME = "ae_universal_cell_bytes_usage_big";
    String CELL_TYPES_USAGE_TAG_NAME = "ae_universal_cell_types_usage";
    String CELL_SHOW_TOOLTIP_STACKS_TAG_NAME = "ae_universal_cell_show_tooltip_stacks";

    double getIdleDrain();

    // === 以下辅助方法：由 Data Components 迁移为 NBT，行为保持一致 ===
    static BigInteger getUsedBytes(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return BigInteger.ZERO;
        if (tag.contains(CELL_BYTES_USAGE_BIG_TAG_NAME, Tag.TAG_BYTE_ARRAY))
        {
            byte[] arr = tag.getByteArray(CELL_BYTES_USAGE_BIG_TAG_NAME);
            if (arr.length == 0) return BigInteger.ZERO;
            return new BigInteger(arr);
        }
        return BigInteger.ZERO;
    }

    static void setUsedBytes(ItemStack stack, BigInteger usedBytes)
    {
        CompoundTag tag = stack.getOrCreateTag();
        if (usedBytes == null)
        {
            tag.remove(CELL_BYTES_USAGE_BIG_TAG_NAME);
            return;
        }
        tag.putByteArray(CELL_BYTES_USAGE_BIG_TAG_NAME, usedBytes.toByteArray());
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
        if (!tag.contains(CELL_STATE_TAG_NAME, Tag.TAG_STRING)) return CellState.EMPTY;
        try
        {
            return CellState.valueOf(tag.getString(CELL_STATE_TAG_NAME));
        }
        catch (IllegalArgumentException ex)
        {
            return CellState.EMPTY;
        }
    }

    static void setCellState(ItemStack stack, CellState newState)
    {
        stack.getOrCreateTag().putString(CELL_STATE_TAG_NAME, newState.name());
    }

    static List<GenericStack> getTooltipShowStacks(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return List.of();

        ListTag list = tag.getList(CELL_SHOW_TOOLTIP_STACKS_TAG_NAME, Tag.TAG_COMPOUND);
        if (list.isEmpty()) return List.of();

        List<GenericStack> out = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++)
        {
            CompoundTag entry = list.getCompound(i);
            GenericStack genericStack = GenericStack.readTag(entry);
            if (genericStack != null) out.add(genericStack);
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