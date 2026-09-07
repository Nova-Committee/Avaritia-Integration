package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.mixin;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.capabilities.energy.BigIntegerEnergyContainer;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekBigEnergy;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.math.BigInteger;

@Mixin(value = TileEntitySolarGenerator.class, remap = false)
public abstract class MixinTileEntitySolarGenerator {

    @Redirect(method = "onUpdateServer",
              at = @At(value = "INVOKE",
                       target = "Lmekanism/common/capabilities/energy/BasicEnergyContainer;insert(JLmekanism/api/Action;Lmekanism/api/AutomationType;)J"))
    private long avaritia_integration$insertInfinityProduction(BasicEnergyContainer container, long amount,
                                                               Action action,
                                                               AutomationType automationType) {
        if (container instanceof BigIntegerEnergyContainer big && automationType == AutomationType.INTERNAL) {
            BigInteger supply = MekBigEnergy.infinitySupplyTick();
            BigInteger leftover = big.insertBig(supply, action, automationType);
            return MekBigEnergy.longInsertRemainder(amount, supply, leftover);
        }
        return container.insert(amount, action, automationType);
    }
}
