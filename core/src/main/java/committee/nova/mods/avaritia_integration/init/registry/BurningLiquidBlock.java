package committee.nova.mods.avaritia_integration.init.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class BurningLiquidBlock extends LiquidBlock {
    /** Burn time in seconds. Lava uses 15 */
    private final int burnTime;
    /** Damage from being in the fluid, lava uses 4 */
    private final float damage;
    public BurningLiquidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties, int burnTime, float damage) {
        super(supplier.get(), properties);
        this.burnTime = burnTime;
        this.damage = damage;
    }

    @SuppressWarnings("deprecation")  // useless annotation on block methods
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune() && entity.getFluidTypeHeight(getFluidState(state).getFluidType()) > 0) {
            entity.setRemainingFireTicks(burnTime);
            if (entity.hurt(entity.damageSources().lava(), damage)) {
                entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + level.random.nextFloat() * 0.4F);
            }
        }
    }
}

