package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import committee.nova.mods.avaritia_integration.module.create.content.extreme_crusher.compat.BeltCrusherCompatibility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeltBlock.class)
public class BeltBlockMixin {
    @Inject(method = "isBlockCoveringBelt", at = @At("HEAD"), cancellable = true, remap = false)
    private static void allowCompatibleCrusherControllers(LevelAccessor world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (BeltCrusherCompatibility.isController(world.getBlockState(pos))) {
            cir.setReturnValue(false);
        }
    }
}
