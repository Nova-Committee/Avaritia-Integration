package committee.nova.mods.avaritia_integration.integrations.botania.botania.item;

import committee.nova.mods.avaritia_integration.integrations.botania.botania.entity.AlphaSparkEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.mana.spark.ManaSparkAttachable;
import vazkii.botania.api.mana.spark.ManaSparkHelper;

/**
 * @author cnlimiter
 */
public class AlphaSparkItem extends Item {

    public AlphaSparkItem(Properties builder) {
        super(builder);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        return attachSpark(ctx.getLevel(), ctx.getClickedPos(), ctx.getItemInHand()) ?
                InteractionResult.sidedSuccess(ctx.getLevel().isClientSide) : InteractionResult.PASS;
    }

    public static boolean attachSpark(Level world, BlockPos pos, ItemStack stack) {
        var attach = ManaSparkAttachable.LOOKUP.find(world, pos);
        if (attach != null && attach.canAttachSpark(stack) && ManaSparkHelper.getAttachedSpark(world, pos) == null) {
            if (!world.isClientSide) {
                stack.shrink(1);
                AlphaSparkEntity spark = new AlphaSparkEntity(world);
                spark.setPos(pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5);
                world.addFreshEntity(spark);
                attach.attachSpark(spark);
            }
            return true;
        }
        return false;
    }
}
