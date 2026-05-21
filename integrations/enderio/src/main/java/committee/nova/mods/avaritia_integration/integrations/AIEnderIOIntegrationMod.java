package committee.nova.mods.avaritia_integration.integrations;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.enderio.EnderIOModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIEnderIOIntegrationMod.MOD_ID)
public final class AIEnderIOIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_enderio";

    public AIEnderIOIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, EnderIOModule::new);
    }
}
