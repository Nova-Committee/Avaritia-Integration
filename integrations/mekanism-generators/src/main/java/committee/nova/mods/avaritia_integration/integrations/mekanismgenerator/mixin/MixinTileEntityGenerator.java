package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.mixin;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.capabilities.energy.BigIntegerEnergyContainer;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekBigEnergy;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.InfinityAdvancedSolarGeneratorBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.blockentity.InfinitySolarGeneratorBlockEntity;

import net.minecraft.core.Direction;

import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.generators.common.tile.TileEntityGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(value = TileEntityGenerator.class, remap = false)
public abstract class MixinTileEntityGenerator {

    @Shadow
    @Mutable
    private BasicEnergyContainer energyContainer;

    @Shadow
    protected abstract RelativeSide[] getEnergySides();

    @Inject(method = "getInitialEnergyContainers", at = @At("HEAD"), cancellable = true)
    private void avaritia_integration$bigIntegerInfinityBuffer(IContentsListener listener,
                                                               CallbackInfoReturnable<IEnergyContainerHolder> cir) {
        Object self = this;
        if (!(self instanceof InfinitySolarGeneratorBlockEntity) &&
                !(self instanceof InfinityAdvancedSolarGeneratorBlockEntity)) {
            return;
        }
        Supplier<Direction> facing = ((mekanism.common.tile.base.TileEntityMekanism) self).facingSupplier;
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(facing);
        builder.addContainer(
                energyContainer = BigIntegerEnergyContainer.output(MekBigEnergy.INFINITY_CAPACITY, listener),
                getEnergySides());
        cir.setReturnValue(builder.build());
    }
}
