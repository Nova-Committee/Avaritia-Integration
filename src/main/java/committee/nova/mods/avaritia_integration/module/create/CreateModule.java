package committee.nova.mods.avaritia_integration.module.create;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.create.registry.CreateIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = CreateModule.MOD_ID, target = @ModMeta(CreateModule.MOD_ID))
public final class CreateModule implements Module {
    public static final String MOD_ID = "create";

    @Override
    public void init(IEventBus registryBus) {
        CreateIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(CreateIntegrationItems.CREATIVE_MECHANISM.get());
        output.accept(CreateIntegrationItems.CREATIVE_COMPOUND.get());
    }
}
