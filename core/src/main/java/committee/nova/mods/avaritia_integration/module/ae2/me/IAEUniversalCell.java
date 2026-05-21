package committee.nova.mods.avaritia_integration.module.ae2.me;

import appeng.api.stacks.GenericStack;
import appeng.api.storage.cells.CellState;
import appeng.api.upgrades.IUpgradeableItem;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationDataComponents;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 定义一些信息获取接口以及一些为ItemStack快速设置/获取信息的方法
 * <p>
 * 是否无限容量、总字节数、待机功耗等信息
 *
 * @author Frostbite
 */
public interface IAEUniversalCell extends IUpgradeableItem {

    /**
     * 约定：返回值小于等于0则视为不限制类型总数
     */
    int getTotalBytes();

    /**
     * 约定：返回值小于等于0则视为不限制类型总数
     */
    int getTotalTypes();

    double getIdleDrain();

    // === 以下辅助方法：从 NBT 迁移为 Data Components，行为保持一致 ===
    static long getUsedBytes(ItemStack stack) {
        Long v = stack.get(AE2IntegrationDataComponents.CELL_BYTES_USAGE.get());
        return v == null ? 0 : v;
    }

    static void setUsedBytes(ItemStack stack, long usedBytes) {
        stack.set(AE2IntegrationDataComponents.CELL_BYTES_USAGE.get(), Math.max(0, usedBytes));
    }

    static int getUsedTypes(ItemStack stack) {
        Integer v = stack.get(AE2IntegrationDataComponents.CELL_TYPES_USAGE.get());
        return v == null ? 0 : v;
    }

    static void setUsedTypes(ItemStack stack, int usedTypes) {
        stack.set(AE2IntegrationDataComponents.CELL_TYPES_USAGE.get(), Math.max(0, usedTypes));
    }

    static CellState getCellState(ItemStack stack) {
        String s = stack.get(AE2IntegrationDataComponents.CELL_STATE.get());
        if (s == null) return CellState.EMPTY;
        try {
            return CellState.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return CellState.EMPTY;
        }
    }

    static void setCellState(ItemStack stack, IAEUniversalCell cellType, long usedBytes, int usedTypes) {
        final int totalBytes = cellType.getTotalBytes(); // <=0 视为无限
        final int totalTypes = cellType.getTotalTypes(); // <=0 视为无限

        final CellState state;
        if (usedBytes <= 0 && usedTypes <= 0) {
            state = CellState.EMPTY;
        } else if (totalBytes > 0 && usedBytes >= totalBytes) {
            state = CellState.FULL;
        } else if (totalTypes > 0 && usedTypes >= totalTypes) {
            state = CellState.TYPES_FULL;
        } else {
            state = CellState.NOT_EMPTY;
        }

        stack.set(AE2IntegrationDataComponents.CELL_STATE.get(), state.name());
    }

    static List<GenericStack> getTooltipShowStacks(ItemStack stack) {
        List<GenericStack> raw = stack.get(AE2IntegrationDataComponents.CELL_SHOW_TOOLTIP_STACKS.get());
        if (raw == null || raw.isEmpty()) return List.of();
        return Collections.unmodifiableList(new ArrayList<>(raw));
    }

    static void setTooltipShowStacks(ItemStack stack, List<GenericStack> showStacks) {
        if (showStacks == null || showStacks.isEmpty()) {
            stack.remove(AE2IntegrationDataComponents.CELL_SHOW_TOOLTIP_STACKS.get());
            return;
        }
        // 过滤掉可能的 null
        List<GenericStack> cleaned = new ArrayList<>(showStacks.size());
        for (GenericStack gs : showStacks) {
            if (gs != null) cleaned.add(gs);
        }
        if (cleaned.isEmpty()) {
            stack.remove(AE2IntegrationDataComponents.CELL_SHOW_TOOLTIP_STACKS.get());
        } else {
            stack.set(AE2IntegrationDataComponents.CELL_SHOW_TOOLTIP_STACKS.get(), cleaned);
        }
    }
}