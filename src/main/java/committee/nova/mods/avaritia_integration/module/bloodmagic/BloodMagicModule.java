package committee.nova.mods.avaritia_integration.module.bloodmagic;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.bloodmagic.registry.BloodMagicIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = BloodMagicModule.MOD_ID, target = @ModMeta(BloodMagicModule.MOD_ID))
public final class BloodMagicModule implements Module {
    public static final String MOD_ID = "bloodmagic";

    @Override
    public void init(IEventBus registryBus) {
        BloodMagicIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(BloodMagicIntegrationItems.BLOOD_ORB_OF_ARMOK.get());
    }
}
