package committee.nova.mods.avaritia_integration.module.thermal_expansion;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.thermal_expansion.registry.ThermalExpansionIntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = ThermalExpansionModule.MOD_ID, target = @ModMeta(ThermalExpansionModule.MOD_ID))
public final class ThermalExpansionModule implements Module {
    public static final String MOD_ID = "thermal_expansion";

    @Override
    public void init(IEventBus registryBus) {
        ThermalExpansionIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(ThermalExpansionIntegrationItems.CREATIVE_AUGMENT_BASE.get());
    }
}
