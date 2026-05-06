package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.logistics.chute.ChuteBlockEntity;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_fan.compat.ChuteFanCompatibility;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChuteBlockEntity.class)
public class ChuteBlockEntityMixin {
    @Inject(method = "calculatePull", at = @At("HEAD"), cancellable = true, remap = false)
    private void supportCompatibleFansAbove(CallbackInfoReturnable<Float> cir) {
        BlockEntity self = (BlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) {
            return;
        }
        BlockPos worldPosition = self.getBlockPos();
        BlockPos fanPos = worldPosition.above();
        if (ChuteFanCompatibility.isFanFacing(level, fanPos, Direction.DOWN)) {
            cir.setReturnValue(ChuteFanCompatibility.getFanSpeed(level, fanPos));
        }
    }

    @Inject(method = "calculatePush", at = @At("HEAD"), cancellable = true, remap = false)
    private void supportCompatibleFansBelow(int branchCount, CallbackInfoReturnable<Float> cir) {
        BlockEntity self = (BlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) {
            return;
        }
        BlockPos worldPosition = self.getBlockPos();
        BlockPos fanPos = worldPosition.below();
        if (ChuteFanCompatibility.isFanFacing(level, fanPos, Direction.UP)) {
            cir.setReturnValue(ChuteFanCompatibility.getFanSpeed(level, fanPos));
        }
    }
}
