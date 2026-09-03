package committee.nova.mods.avaritia_integration.integrations.tconstruct;

import committee.nova.mods.avaritia_integration.api.load.DependencyRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.List;

@Mod(AITConstructDataIntegrationMod.MOD_ID)
public final class AITConstructDataIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_tconstruct_data";

    public AITConstructDataIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration TConstruct Data");
        IntegrationLoadApi.registerDefaultRule(MOD_ID,
                new IntegrationRule(List.of(new DependencyRule("tconstruct", "", ""))));
        IntegrationLoadApi.shouldLoad(MOD_ID);
    }
}
