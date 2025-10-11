package committee.nova.mods.avaritia_integration.module.ae2.item;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.storage.cells.StorageCell;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.AEConfig;
import appeng.core.localization.PlayerMessages;
import appeng.items.contents.CellConfig;
import appeng.items.storage.StorageCellTooltipComponent;
import appeng.util.ConfigInventory;
import appeng.util.InteractionUtil;
import appeng.util.Platform;
import committee.nova.mods.avaritia_integration.module.ae2.localization.AEUniversalTooltips;
import committee.nova.mods.avaritia_integration.module.ae2.me.IAEUniversalCell;
import committee.nova.mods.avaritia_integration.module.ae2.me.biginteger.IAEBigIntegerCell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 用于承载物品到存储系统的桥接物品
 * @author Frostbite
 */
public class AEBigIntegerCellItem extends Item implements IAEBigIntegerCell, ICellWorkbenchItem
{
    private final @Nullable ItemLike coreItem;
    private final @Nullable ItemLike housingItem;
    private final double idleDrain;

    public AEBigIntegerCellItem(Properties pProperties, @Nullable Item coreItem, @Nullable Item housingItem, double idleDrain)
    {
        super(pProperties);
        this.idleDrain = idleDrain;
        this.coreItem = coreItem;
        this.housingItem = housingItem;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack,
                                @Nullable Level pLevel,
                                @NotNull List<Component> lines,
                                @NotNull TooltipFlag pIsAdvanced)
    {
        if (Platform.isClient())
        {
            BigInteger used = IAEBigIntegerCell.getUsedBytes(stack);
            lines.add(AEUniversalTooltips.bytesUsed(used, -1));
            long typesUsed = IAEBigIntegerCell.getUsedTypes(stack);
            lines.add(AEUniversalTooltips.typesUsed(typesUsed, -1));
        }
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack)
    {
        final boolean showUpg = AEConfig.instance().isTooltipShowCellUpgrades();
        final boolean showCnt = AEConfig.instance().isTooltipShowCellContent();

        // 升级图标
        List<ItemStack> upgrades = Collections.emptyList();
        if (showUpg) {
            List<ItemStack> tmp = new ArrayList<>();
            getUpgrades(stack).forEach(tmp::add);
            upgrades = tmp;
        }

        // 内容预览：从 IAEUniversalCell 约定的 NBT 中读取展示用 GenericStack 列表
        // 为了兼容 AE2 的组件，按需裁剪数量，并设置 hasMore
        List<GenericStack> content = Collections.emptyList();
        boolean hasMore = false;
        if (showCnt) {
            List<GenericStack> show = IAEUniversalCell.getTooltipShowStacks(stack);
            if (!show.isEmpty()) {
                // AE2 通常展示不超过 5 个条目；超过则裁剪并设置 hasMore = true
                final int limit = 5;
                if (show.size() > limit) {
                    content = new ArrayList<>(show.subList(0, limit));
                    hasMore = true;
                } else {
                    content = new ArrayList<>(show);
                }
            }
        }

        // 显示进度条：true（进度由组件内部根据存储状态/配色绘制）
        return Optional.of(new StorageCellTooltipComponent(upgrades, content, hasMore, true));
    }

    @Override
    public double getIdleDrain()
    {
        return idleDrain;
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack is)
    {
        return UpgradeInventories.forItem(is, 2);
    }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is)
    {
        // 通用盘：允许所有 AEKey 作为过滤对象（白名单/黑名单配置用）
        return CellConfig.create(key -> true, is);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack is)
    {
        final CompoundTag tag = is.getOrCreateTag();
        final String fz = tag.getString("FuzzyMode");
        if (fz.isEmpty()) return FuzzyMode.IGNORE_ALL;
        try
        {
            return FuzzyMode.valueOf(fz);
        }
        catch(IllegalArgumentException ex)
        {
            return FuzzyMode.IGNORE_ALL;
        }
    }

    @Override
    public void setFuzzyMode(ItemStack is, FuzzyMode fzMode)
    {
        is.getOrCreateTag().putString("FuzzyMode", fzMode.name());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        this.disassembleDrive(player.getItemInHand(hand), level, player);
        return new InteractionResultHolder<>(InteractionResult.sidedSuccess(level.isClientSide()),
                player.getItemInHand(hand));
    }

    @Override
    public @NotNull InteractionResult onItemUseFirst(@NotNull ItemStack stack, UseOnContext context)
    {
        return this.disassembleDrive(stack, context.getLevel(), context.getPlayer())
                ? InteractionResult.sidedSuccess(context.getLevel().isClientSide())
                : InteractionResult.PASS;
    }

    private boolean disassembleDrive(ItemStack stack, Level level, Player player)
    {
        if (InteractionUtil.isInAlternateUseMode(player)) {
            if (level.isClientSide()) {
                return false;
            }

            Inventory playerInventory = player.getInventory();
            StorageCell inv = StorageCells.getCellInventory(stack, null);
            if (inv != null && playerInventory.getSelected() == stack) {
                KeyCounter list = inv.getAvailableStacks();
                if (list.isEmpty()) {
                    if(this.coreItem == null || this.housingItem == null) return false;

                    playerInventory.setItem(playerInventory.selected, ItemStack.EMPTY);
                    playerInventory.placeItemBackInInventory(new ItemStack(this.coreItem));

                    for(ItemStack upgrade : this.getUpgrades(stack)) {
                        playerInventory.placeItemBackInInventory(upgrade);
                    }

                    playerInventory.placeItemBackInInventory(new ItemStack(this.housingItem));
                    return true;
                }

                player.displayClientMessage(PlayerMessages.OnlyEmptyCellsCanBeDisassembled.text(), true);
            }
        }

        return false;
    }
}