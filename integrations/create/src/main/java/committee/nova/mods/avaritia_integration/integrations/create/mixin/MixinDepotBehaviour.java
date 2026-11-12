package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot.ExtremeDepotBlockEntity;

import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(DepotBehaviour.class)
public abstract class MixinDepotBehaviour {

    @Shadow
    Supplier<Integer> maxStackSize;

    @Shadow
    public abstract void enableMerging();

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void avaritia$extremeDepotStack(SmartBlockEntity be, CallbackInfo ci) {
        if (committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi
                .shouldLoad("avaritia_integration_create") && be instanceof ExtremeDepotBlockEntity) {
            maxStackSize = () -> 1024;
            enableMerging();
        }
    }
}
