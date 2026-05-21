package committee.nova.mods.avaritia_integration.module.enderio.item;

import com.enderio.enderio.foundation.block.entity.MachineInstallable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InfinityCapacitorItem extends Item {

    public InfinityCapacitorItem(Properties properties) {
        super(properties);
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity var6 = level.getBlockEntity(pos);
        if (var6 instanceof MachineInstallable equippable) {
            return equippable.tryItemInstall(stack, context);
        } else {
            return super.onItemUseFirst(stack, context);
        }
    }

}
