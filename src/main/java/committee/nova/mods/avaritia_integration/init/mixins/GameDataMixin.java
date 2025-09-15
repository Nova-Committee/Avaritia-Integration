package committee.nova.mods.avaritia_integration.init.mixins;

import committee.nova.mods.avaritia_integration.api.module.XModule;
import net.minecraftforge.registries.GameData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author: cnlimiter
 */
@Mixin(value = GameData.class, remap = false)
public class GameDataMixin {

    @Inject(at = @At("TAIL"), method = "unfreezeData")
    private static void kiwi$unfreezeData(CallbackInfo ci) {
        XModule.preInit();
    }

}
