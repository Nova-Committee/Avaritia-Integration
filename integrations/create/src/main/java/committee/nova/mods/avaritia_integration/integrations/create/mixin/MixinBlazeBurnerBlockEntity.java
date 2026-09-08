package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationItems;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeBurnerBlockEntity.class)
public abstract class MixinBlazeBurnerBlockEntity {

    @Shadow
    protected int remainingBurnTime;

    @Shadow
    protected BlazeBurnerBlockEntity.FuelType activeFuel;

    @Shadow
    protected abstract void playSound();

    @Inject(method = "tryUpdateFuel", at = @At("HEAD"), cancellable = true, remap = false)
    private void avaritia$starCakeFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate,
            CallbackInfoReturnable<Boolean> cir) {
        if (!itemStack.is(CreateIntegrationItems.STAR_CAKE.get())) {
            return;
        }
        if (simulate) {
            cir.setReturnValue(true);
            return;
        }
        BlazeBurnerBlockEntity self = (BlazeBurnerBlockEntity) (Object) this;
        remainingBurnTime = Integer.MAX_VALUE;
        activeFuel = BlazeBurnerBlockEntity.FuelType.SPECIAL;
        Level level = self.getLevel();
        if (level != null && level.isClientSide) {
            self.spawnParticleBurst(true);
            cir.setReturnValue(true);
            return;
        }
        BlazeBurnerBlock.HeatLevel prev = self.getHeatLevelFromBlock();
        playSound();
        self.updateBlockState();
        if (level != null && prev != self.getHeatLevelFromBlock()) {
            level.playSound(null, self.getBlockPos(), SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS,
                    0.125f + level.random.nextFloat() * 0.125f, 1.15f - level.random.nextFloat() * 0.25f);
        }
        cir.setReturnValue(true);
    }
}
