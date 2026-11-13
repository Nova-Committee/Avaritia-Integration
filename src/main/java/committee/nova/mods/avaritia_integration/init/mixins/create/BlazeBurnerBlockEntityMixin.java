package committee.nova.mods.avaritia_integration.init.mixins.create;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import committee.nova.mods.avaritia_integration.module.create.registry.CreateIntegrationItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeBurnerBlockEntity.class)
public abstract class BlazeBurnerBlockEntityMixin {
    @Shadow protected int remainingBurnTime;
    @Shadow protected abstract void playSound();
    @Shadow protected BlazeBurnerBlockEntity.FuelType activeFuel;

    @Inject(method = "tryUpdateFuel", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/AllTags$AllItemTags;matches(Lnet/minecraft/world/item/ItemStack;)Z", ordinal = 0), cancellable = true, remap = false)
    private void applyStarFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        BlazeBurnerBlockEntity self = (BlazeBurnerBlockEntity) (Object) this;
        if (CreateIntegrationItems.STAR_CAKE.isIn(itemStack)) {
            if (simulate) cir.setReturnValue(true);

            this.remainingBurnTime = 36000;
            this.activeFuel = BlazeBurnerBlockEntity.FuelType.SPECIAL;

            if (self.getLevel().isClientSide) {
                self.spawnParticleBurst(self.getActiveFuel() == BlazeBurnerBlockEntity.FuelType.SPECIAL);
                cir.setReturnValue(true);
            }

            BlazeBurnerBlock.HeatLevel prev = self.getHeatLevelFromBlock();
            this.playSound();
            self.updateBlockState();

            if (prev != self.getHeatLevelFromBlock())
                self.getLevel().playSound(null, self.getBlockPos(), SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS,
                        .125f + self.getLevel().random.nextFloat() * .125f, 1.15f - self.getLevel().random.nextFloat() * .25f);

            cir.setReturnValue(true);
        }
    }
}
