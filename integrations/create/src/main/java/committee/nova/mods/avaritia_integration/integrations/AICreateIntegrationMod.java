package committee.nova.mods.avaritia_integration.integrations;

import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.integrations.create.CreateModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AICreateIntegrationMod.MOD_ID)
public final class AICreateIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_create";

    public AICreateIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationRuntime.load(MOD_ID, bus, CreateModule::new);
    }
}
