package committee.nova.mods.avaritia_integration.module.botania.item;

import committee.nova.mods.avaritia_integration.module.botania.entity.AlphaSparkEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.xplat.XplatAbstractions;

/**
 * @author cnlimiter
 */
public class AlphaSparkItem extends Item {
    public AlphaSparkItem(Item.Properties builder) {
        super(builder);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand())
                ? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide)
                : InteractionResult.PASS;
    }

    public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack) {
        var attach = XplatAbstractions.INSTANCE.findSparkAttachable(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), Direction.UP);
        if (attach != null) {
            if (attach.canAttachSpark(stack) && SparkAttachable.getAttachedSpark(world, pos) == null) {
                if (!world.isClientSide) {
                    stack.shrink(1);
                    AlphaSparkEntity spark = new AlphaSparkEntity(world);
                    spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
                    world.addFreshEntity(spark);
                    attach.attachSpark(spark);
                }
                return true;
            }
        }
        return false;
    }
}