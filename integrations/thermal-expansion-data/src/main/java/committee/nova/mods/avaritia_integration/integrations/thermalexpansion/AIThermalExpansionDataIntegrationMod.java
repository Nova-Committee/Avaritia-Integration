package committee.nova.mods.avaritia_integration.integrations.thermalexpansion;

import committee.nova.mods.avaritia_integration.api.load.DependencyRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationLoadApi;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.List;

@Mod(AIThermalExpansionDataIntegrationMod.MOD_ID)
public final class AIThermalExpansionDataIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_thermal_expansion_data";

    public AIThermalExpansionDataIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration Thermal Expansion Data");
        IntegrationLoadApi.registerDefaultRule(MOD_ID,
                new IntegrationRule(List.of(new DependencyRule("thermal_expansion", "", ""))));
        IntegrationLoadApi.shouldLoad(MOD_ID);
    }
}
